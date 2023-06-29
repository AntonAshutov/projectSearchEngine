package searchengine.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PageScanner {
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    IndexRepository indexRepository;
    @Autowired
    LemmaRepository lemmaRepository;
    @Autowired
    private LemmasAndIndexesOrganizer lemmasAndIndexesOrganizer;
    String siteName;
    Site site;
    Page page;
    @Autowired
    PageRepository pageRepository;

    public void scanPage(URL page, String siteName) {
        this.siteName = siteName;
        if (checkIfSiteExist(page)) {
            checkAndClearPage(page);
        } else {
            createSite(page.getProtocol() + "://" + page.getHost());
        }
        createPage(page.toString());
    }

    private Boolean checkIfSiteExist(URL page) {
        URL siteURL;
        for (Site site : siteRepository.findAll()) {
            try {
                siteURL = new URL(site.getUrl());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (siteURL.getHost().equals(page.getHost())) {
                this.site = site;
                return true;
            }
        }
        return false;
    }

    private void checkAndClearPage(URL page) {
        Optional<Page> optionalPage = pageRepository.findByPath(page.getPath());
        if (optionalPage.isEmpty()) {
            return;
        }
        lemmasAndIndexesOrganizer.deleteIndexesAndUpdateLemmas(optionalPage.get().getId());
        pageRepository.delete(optionalPage.get());
        pageRepository.flush();
    }

    private void createPage(String page) {
        URL pageURL;
        String pageContent;
        try {
            pageURL = new URL(page);
            pageContent = new Scanner(pageURL.openStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Page pageM = new Page();
        pageM.setPath(pageURL.getPath());
        pageM.setSite(site);
        pageM.setCode(200);
        pageM.setContent(pageContent);
        pageRepository.save(pageM);
        this.page = pageM;

        lemmasAndIndexesOrganizer.handlePageContent(pageM, pageContent);
    }

    private void createSite(String url) {
        Site site = new Site();
        site.setLastError("Индексация не проводилась");
        site.setStatus(Site.SiteStatus.FAILED);
        site.setUrl(url);
        site.setStatusTime(new Date(System.currentTimeMillis()));
        site.setName(siteName);
        siteRepository.save(site);
        this.site = site;
    }
}
