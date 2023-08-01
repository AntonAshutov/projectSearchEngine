package searchengine.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.SiteM;
import searchengine.services.IndexingService;
import searchengine.services.SearchingSevice;
import searchengine.services.StatisticsService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static searchengine.model.SiteM.SiteStatus.INDEXING;

@WebMvcTest(controllers = ApiController.class)
@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;
    @MockBean
    private IndexingService indexingService;
    @MockBean
    private SearchingSevice searchingSevice;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testStatistics() throws Exception {
        DetailedStatisticsItem item1 = formStatItem("www.test1.ru", "test site", INDEXING, 1_000_000, null, 2_000, 5_000);
        DetailedStatisticsItem item2 = formStatItem("www.test2.ru", "test site 2", INDEXING, 1_000_000, null, 2_000, 5_000);
        TotalStatistics totalStat = formTotalStat(2,4_000, 10_000, true);

        StatisticsData data = new StatisticsData();
        data.setTotal(totalStat);
        data.setDetailed(List.of(item1, item2));

        StatisticsResponse mockStatisticsResponse = new StatisticsResponse();
        mockStatisticsResponse.setResult(true);
        mockStatisticsResponse.setStatistics(data);

        when(statisticsService.getStatistics()).thenReturn(mockStatisticsResponse);

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statistics.total.sites").value(2))
                .andExpect(jsonPath("$.statistics.total.pages").value(4_000))
                .andExpect(jsonPath("$.statistics.total.lemmas").value(10_000))
                .andExpect(jsonPath("$.statistics.total.indexing").value(true));
    }

    private TotalStatistics formTotalStat(int sites, int pages, int lemmas, boolean indexing) {
        TotalStatistics item = new TotalStatistics();
        item.setSites(sites);
        item.setPages(pages);
        item.setLemmas(lemmas);
        item.setIndexing(indexing);
        return item;
    }

    private DetailedStatisticsItem formStatItem(String url, String siteName, SiteM.SiteStatus siteStatus, long statusTime, String error, int pages, int lemmas) {
        DetailedStatisticsItem item = new DetailedStatisticsItem();
        item.setUrl(url);
        item.setName(siteName);
        item.setStatus(siteStatus.toString());
        item.setStatusTime(statusTime);
        item.setError(error);
        item.setPages(pages);
        item.setLemmas(lemmas);
        return item;
    }
}
