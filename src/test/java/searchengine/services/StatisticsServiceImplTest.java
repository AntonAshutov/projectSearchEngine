package searchengine.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.SiteM;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StatisticsServiceImplTest {

    @MockBean
    private SitesList sitesList;

    @MockBean
    private IndexingService indexingService;

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private LemmaRepository lemmaRepository;

    @MockBean
    private PageRepository pageRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Site site1 = new Site();
        site1.setUrl("https://www.dombulgakova.ru/");
        site1.setName("Дом_Булгакова.ru");

        Site site2 = new Site();
        site2.setUrl("https://www.lenta.ru");
        site2.setName("Лента.ру");

        Site site3 = new Site();
        site3.setUrl("https://www.playback.ru");
        site3.setName("PlayBack.Ru");

        List<Site> siteList = new ArrayList<>();
        siteList.add(site1);
        siteList.add(site2);
        siteList.add(site3);

        when(sitesList.getSites()).thenReturn(siteList);
    }

    @Test
    public void testGetStatistics() {
        // Arrange
        Site site1 = new Site();
        site1.setUrl("https://www.example1.com");
        site1.setName("Example Site 1");

        Site site2 = new Site();
        site2.setUrl("https://www.example2.com");
        site2.setName("Example Site 2");

        List<Site> siteList = new ArrayList<>();
        siteList.add(site1);
        siteList.add(site2);

        when(sitesList.getSites()).thenReturn(siteList);

        when(indexingService.getIndexingStatus()).thenReturn(true); // Имитируем запущенную индексацию

        SiteM siteM1 = new SiteM();
        siteM1.setId(1); // Указываем id для siteM1
        siteM1.setUrl("https://www.example1.com");
        siteM1.setName("Example Site 1");
        siteM1.setStatus(SiteM.SiteStatus.INDEXING);
        siteM1.setStatusTime(new Date(System.currentTimeMillis()));
        siteM1.setLastError("No errors");

        SiteM siteM2 = new SiteM();
        siteM2.setId(2); // Указываем id для siteM2
        siteM2.setUrl("https://www.example2.com");
        siteM2.setName("Example Site 2");
        siteM2.setStatus(SiteM.SiteStatus.FAILED);
        siteM2.setStatusTime(new Date(System.currentTimeMillis()));
        siteM2.setLastError("An error occurred");

        when(siteRepository.findByUrl("https://www.example1.com")).thenReturn(Optional.of(siteM1));
        when(siteRepository.findByUrl("https://www.example2.com")).thenReturn(Optional.of(siteM2));

        when(lemmaRepository.countLemmasBySiteId(1)).thenReturn(100);
        when(lemmaRepository.countLemmasBySiteId(2)).thenReturn(50);

        when(pageRepository.countPagesBySiteId(1)).thenReturn(10);
        when(pageRepository.countPagesBySiteId(2)).thenReturn(5);

        StatisticsResponse response = statisticsService.getStatistics();

        assertEquals(true, response.isResult());

        assertEquals(2, response.getStatistics().getTotal().getSites());
        assertEquals(true, response.getStatistics().getTotal().isIndexing());

        assertEquals(2, response.getStatistics().getDetailed().size());
        assertEquals("Example Site 1", response.getStatistics().getDetailed().get(0).getName());
        assertEquals("https://www.example1.com", response.getStatistics().getDetailed().get(0).getUrl());
        assertEquals(10, response.getStatistics().getDetailed().get(0).getPages());
        assertEquals(100, response.getStatistics().getDetailed().get(0).getLemmas());
        assertEquals("INDEXING", response.getStatistics().getDetailed().get(0).getStatus());
        assertEquals("No errors", response.getStatistics().getDetailed().get(0).getError());
        assertEquals(siteM1.getStatusTime().getTime(), response.getStatistics().getDetailed().get(0).getStatusTime());

        assertEquals("Example Site 2", response.getStatistics().getDetailed().get(1).getName());
        assertEquals("https://www.example2.com", response.getStatistics().getDetailed().get(1).getUrl());
        assertEquals(5, response.getStatistics().getDetailed().get(1).getPages());
        assertEquals(50, response.getStatistics().getDetailed().get(1).getLemmas());
        assertEquals("FAILED", response.getStatistics().getDetailed().get(1).getStatus());
        assertEquals("An error occurred", response.getStatistics().getDetailed().get(1).getError());
        assertEquals(siteM2.getStatusTime().getTime(), response.getStatistics().getDetailed().get(1).getStatusTime());


    }
}
