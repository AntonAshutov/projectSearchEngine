package searchengine.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.SiteM;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LemmasAndIndexesOrganizer {
    @Autowired
    LemmaRepository lemmaRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    IndexRepository indexRepository;
    private LemmaFinder lemmaFinder;

    {
        try {
            lemmaFinder = LemmaFinder.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handlePageContent(Page page, String pageContent){
        HashMap <String,Integer> lemmasFromPage = lemmaFinder.collectLemmasFromPageContent(pageContent);
        List<String> lemmas = lemmasFromPage.keySet().stream().toList();
        SiteM site = page.getSite();

        List<Lemma> lemmaMS = updateLemmasAdd(lemmas, site);

        updateIndexes(lemmaMS, lemmasFromPage, page);

    }

    private List<Lemma> updateLemmasAdd(List<String> lemmas, SiteM site) {
        ArrayList<Lemma> lemmaMS= new ArrayList<>();
        for (String lemma : lemmas) {
            Lemma existingLemma = lemmaRepository.findFirstByLemmaAndSiteId(lemma, site.getId());
            if (existingLemma != null) {
                int newFrequency = existingLemma.getFrequency() + 1;
                existingLemma.setFrequency(newFrequency);
                lemmaMS.add(existingLemma);
                lemmaRepository.save(existingLemma);
            } else {
                Lemma newLemma = new Lemma();
                newLemma.setSite(site);
                newLemma.setLemma(lemma);
                newLemma.setFrequency(1);
                lemmaRepository.save(newLemma);
                lemmaMS.add(newLemma);
            }
        }
        return lemmaMS;
    }
    private void updateIndexes(List<Lemma> lemmas, HashMap<String,Integer> indexes, Page page){
        for (Lemma lemma : lemmas){
            Index index = new Index();
            index.setLemma(lemma);
            index.setPage(page);
            index.setRank(indexes.get(lemma.getLemma()));
            indexRepository.save(index);
        }
    }
@Transactional
    public void deleteIndexesAndUpdateLemmas(Integer pageId){
        for (Index index : indexRepository.findByPageId(pageId)){
            Lemma lemma = index.getLemma();

            indexRepository.delete(index);
            indexRepository.flush();
            if (lemma.getFrequency() > 1){
                lemma.setFrequency(lemma.getFrequency() - 1);
                continue;
            }

            lemmaRepository.delete(lemma);
            lemmaRepository.flush();
        }

    }

    public void clearIndexesAndLemmasBySite(SiteM site){
        List<Integer> pageIds = pageRepository.findIdsBySiteId(site.getId());
        indexRepository.deleteAllByPageIdIn(pageIds);
        lemmaRepository.deleteAllBySiteId(site.getId());
    }
}
