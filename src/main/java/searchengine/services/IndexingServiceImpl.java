package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.IndexingPageResponse;
import searchengine.dto.statistics.IndexingStartingResponse;
import searchengine.dto.statistics.IndexingStoppingResponse;
import searchengine.model.SiteM;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.scanner.LemmasAndIndexesOrganizer;
import searchengine.scanner.PageScanner;
import searchengine.scanner.SiteScanner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class IndexingServiceImpl implements IndexingService {
    PageScanner pageScanner;
    private SitesList sites;
    private ArrayList<SiteM> sitesM;

    @Autowired
    public IndexingServiceImpl(PageScanner pageScanner, SitesList sites, ArrayList<SiteM> sitesM, LemmasAndIndexesOrganizer lemmasAndIndexesOrganizer, SiteRepository siteRepository, PageRepository pageRepository) {
        this.pageScanner = pageScanner;
        this.sites = sites;
        this.sitesM = sitesM;
        this.lemmasAndIndexesOrganizer = lemmasAndIndexesOrganizer;
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }

    public IndexingServiceImpl() {
    }

    private LemmasAndIndexesOrganizer lemmasAndIndexesOrganizer;
    private boolean indexingInProcess = false;

    @Override
    public Boolean getIndexingStatus() {
        return indexingInProcess;
    }

    private SiteRepository siteRepository;
    private PageRepository pageRepository;

    @Override
    public IndexingStartingResponse getStartResponse() {
        IndexingStartingResponse response = new IndexingStartingResponse();
        response.setResult(!indexingInProcess);
        response.setError(indexingInProcess ? "Индексация уже запущена" : null);
        if (!indexingInProcess) {
            indexingInProcess = true;
            new Thread(this::startIndexing).start();

        }
        return response;
    }

    @Override
    public IndexingPageResponse getIndexingPageResponse(String url) {
        IndexingPageResponse response = new IndexingPageResponse();
        URL recievedURL;
        try {
            recievedURL = new URL(url);
        } catch (MalformedURLException e) {
            response.setError("некорректная ссылка");
            response.setResult(false);
            return response;
        }
        for (Site site : sites.getSites()) {
            URL siteURL;
            try {
                siteURL = new URL(site.getUrl());
            } catch (MalformedURLException e) {
                response.setError("некорректная ссылка в файле \"application.yaml\"");
                response.setResult(false);
                return response;
            }

            String host1 = siteURL.getHost().startsWith("www.") ? siteURL.getHost().substring(4) : siteURL.getHost();
            String host2 = recievedURL.getHost().startsWith("www.") ? recievedURL.getHost().substring(4) : recievedURL.getHost();

            if (host1.equals(host2)) {
                pageScanner.scanPage(recievedURL, site.getName());
                response.setResult(true);
                response.setError(null);
                return response;
            }
        }

        response.setResult(false);
        response.setError("Данная страница находится за пределами сайтов,\n" +
                "указанных в конфигурационном файле");
        return response;
    }

    @Override
    public IndexingStoppingResponse getStopResponse() {
        IndexingStoppingResponse response = new IndexingStoppingResponse();
        response.setResult(indexingInProcess);
        response.setError(!indexingInProcess ? "Индексация не запущена" : null);


        if (indexingInProcess) {
            indexingInProcess = false;
            SiteScanner.stopScanning();

        }

        return response;
    }

    private void startIndexing() {
        sitesM = new ArrayList<>();
        List<String> siteUrls = sites.getSites().stream()
                .map(searchengine.config.Site::getUrl)
                .toList();

        deleteSitesAndPages(siteUrls);
        createSites(sites);

        for (SiteM site : sitesM) {
            new Thread(() -> {
                try {
                    new SiteScanner(siteRepository, pageRepository, site, lemmasAndIndexesOrganizer).scan();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }


    private void deleteSitesAndPages(List<String> siteUrls) {
        for (String siteUrl : siteUrls) {
            Optional<SiteM> siteM = siteRepository.findByUrl(siteUrl);
            if (siteM.isEmpty()) {
                continue;
            }
            lemmasAndIndexesOrganizer.clearIndexesAndLemmasBySite(siteM.get());

        }

        pageRepository.deleteBySite_UrlIn(siteUrls);
        siteRepository.deleteByUrlIn(siteUrls);
    }

    private void createSites(SitesList list) {
        List<searchengine.config.Site> sites = list.getSites();
        for (searchengine.config.Site site : sites) {
            SiteM siteM = new SiteM();
            siteM.setUrl(site.getUrl());
            siteM.setName(site.getName());
            siteM.setStatus(SiteM.SiteStatus.FAILED);
            siteM.setLastError("Индектация не была запущена");
            siteM.setStatusTime(new Date(System.currentTimeMillis()));
            sitesM.add(siteM);
            siteRepository.save((siteM));
            siteRepository.flush();

        }
    }
}
