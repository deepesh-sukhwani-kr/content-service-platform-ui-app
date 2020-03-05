package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.v1.SearchAsset;
import com.kroger.csp.ui.domain.response.v1.SearchImageAPIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;

/**
 *
 */
@Service
@RefreshScope
public class SearchImageService {

    @Autowired
    RestTemplate restTemplate;
    @Value("${kroger.v1.search.url}")
    private String searchUrl;
    @Value("${kroger.v1.authorization}")
    private String authorizationValue;

    public static final String AUTHORIZATION = "Authorization";

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public SearchResponse searchImage(SearchImageAPIRequest request) {
        SearchImageAPIResponse response =
                restTemplate.postForEntity(URI.create(searchUrl), populateHttpEntity(request),
                        SearchImageAPIResponse.class).getBody();
        if (response.getBody().getImageList() != null && !response.getBody().getImageList().isEmpty()){
            response.getBody().setImageList(removeDuplicateAssets(response.getBody().getImageList()));
        }
        return new SearchResponse(response);
    }

    /**
     *
     * @param request
     * @return
     */
    private HttpEntity<SearchImageAPIRequest> populateHttpEntity(SearchImageAPIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Basic " + authorizationValue);
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(request, headers);
    }

    private ArrayList<SearchAsset> removeDuplicateAssets(ArrayList<SearchAsset> assets){
        ArrayList<SearchAsset> result = new ArrayList<>();
        assets.forEach(asset -> {
            if(!result.contains(asset))
                result.add(asset);
        });
        return result;
    }
}
