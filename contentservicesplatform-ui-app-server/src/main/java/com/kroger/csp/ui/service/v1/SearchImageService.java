package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.v1.SearchImageAPIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
@Service
public class SearchImageService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${search.url}")
    private String searchUrl;

    @Value("${authorization}")
    private String authorizationValue;


    public static final String AUTHORIZATION = "Authorization";

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    public SearchResponse searchImage(SearchImageAPIRequest request) throws Exception {

        SearchImageAPIResponse searchImageAPIResponse = new SearchImageAPIResponse();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(searchUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Basic "+authorizationValue);
        headers.add("Content-Type", "application/json");
        HttpEntity<SearchImageAPIRequest> httpEntityRequest = new HttpEntity<>(request, headers);
        ResponseEntity<SearchImageAPIResponse> responseEntity = restTemplate.postForEntity(builder.build().encode().toUri(), httpEntityRequest, SearchImageAPIResponse.class);
        searchImageAPIResponse = responseEntity.getBody();
        return new SearchResponse(searchImageAPIResponse);
    }
}
