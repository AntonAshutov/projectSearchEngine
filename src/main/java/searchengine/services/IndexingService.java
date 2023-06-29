package searchengine.services;

import searchengine.dto.statistics.IndexingPageResponse;
import searchengine.dto.statistics.IndexingStartingResponse;
import searchengine.dto.statistics.IndexingStoppingResponse;

public interface IndexingService {
    IndexingStartingResponse getStartResponse();
    IndexingStoppingResponse getStopResponse();
    IndexingPageResponse getIndexingPageResponse(String url);
    Boolean getIndexingStatus();
}
