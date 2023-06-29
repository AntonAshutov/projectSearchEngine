package searchengine.services;

import searchengine.dto.statistics.SearchResponse;

public interface SearchingSevice {
    SearchResponse getSearchResponse(String query, String site, Integer offset, Integer limit);
}
