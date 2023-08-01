package searchengine.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.IndexingPageResponse;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.scanner.LemmasAndIndexesOrganizer;
import searchengine.scanner.PageScanner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IndexingServiceImplTest {

    @MockBean
    private PageScanner pageScanner;

    @MockBean
    private SitesList sitesList;

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private PageRepository pageRepository;

    @MockBean
    private LemmasAndIndexesOrganizer lemmasAndIndexesOrganizer;

    @InjectMocks
    private IndexingServiceImpl indexingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создаем данные для тестового объекта SitesList
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
    public void testGetIndexingPageResponse_WithCorrectSiteUrl() throws MalformedURLException {
        String siteUrl = "https://example.com";
        String siteName = "Example Site";
        URL url = new URL(siteUrl);

        Site site = new Site();
        site.setUrl(siteUrl);
        site.setName(siteName);

        List<Site> siteList = new ArrayList<>();
        siteList.add(site);

        when(sitesList.getSites()).thenReturn(siteList);

        IndexingPageResponse response = indexingService.getIndexingPageResponse(siteUrl);

        assertTrue(response.isResult());
        assertNull(response.getError());

        verify(pageScanner, times(1)).scanPage(url, siteName);
    }

    @Test
    public void testGetIndexingPageResponse_WithIncorrectUrl() {
        String siteUrl = "https://example.com/invalid-url";

        IndexingPageResponse response = indexingService.getIndexingPageResponse(siteUrl);

        assertFalse(response.isResult());
        assertEquals("Данная страница находится за пределами сайтов,\n" +
                "указанных в конфигурационном файле", response.getError());

        verify(pageScanner, never()).scanPage(any(), any());
    }

    @Test
    public void testGetIndexingPageResponse_WithUrlNotInSitesList() {
        String siteUrl = "https://example.com";

        Site site = new Site();
        site.setUrl("https://another-site.com");

        List<Site> siteList = new ArrayList<>();
        siteList.add(site);

        when(sitesList.getSites()).thenReturn(siteList);

        IndexingPageResponse response = indexingService.getIndexingPageResponse(siteUrl);

        assertFalse(response.isResult());
        assertEquals("Данная страница находится за пределами сайтов,\nуказанных в конфигурационном файле", response.getError());

        verify(pageScanner, never()).scanPage(any(), any());
    }
}
