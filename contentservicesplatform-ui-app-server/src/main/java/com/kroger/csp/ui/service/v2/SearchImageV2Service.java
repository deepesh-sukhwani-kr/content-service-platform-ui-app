package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.response.v1.Image;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.domain.response.v2.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RefreshScope
public class SearchImageV2Service {

    @Autowired
    RestTemplate restTemplate;
    @Value("${kroger.v2.search.url:}")
    private String searchUrl;
    @Value("${kroger.v2.authorization:}")
    private String auth;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic ";

    /**
     * Service method to hit V2 CSP search API
     * @param request Search request
     * @return Formatted response from CSP
     */
    public SearchResponse searchImages(SearchImageV2APIRequest request) {
        ResponseEntity<SearchAssetV2Response> responseEntity =
                restTemplate.postForEntity(URI.create(searchUrl), buildRequest(request),
                        SearchAssetV2Response.class);
        return populateSearchImageUIResponse(responseEntity.getBody());
    }

    /**
     * To create HttpEntity using request payload to hit CSP V2 ADD
     * @param request Original UI request
     * @return HTTP Entity object
     */
    private HttpEntity<SearchImageV2APIRequest> buildRequest(SearchImageV2APIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, BASIC + auth);
        headers.add("Content-Type", "application/json");
        HttpEntity<SearchImageV2APIRequest> httpEntityRequest = new HttpEntity<>(request, headers);
        return httpEntityRequest;
    }

    private SearchResponse populateSearchImageUIResponse(SearchAssetV2Response searchAssetV2Response){
        SearchResponse searchResponse = new SearchResponse();
        if(searchAssetV2Response.getSearchResponse()!=null && searchAssetV2Response.getSearchResponse().getSearchResult()!=null) {
            searchResponse.setImages(populateImages(searchAssetV2Response.getSearchResponse().getSearchResult().getImageList()));
        }
        return searchResponse;
    }

    private List<Image> populateImages(List<SearchResponsePayload> imageResponseList){
        List<Image> imageList = new ArrayList<>();
        for(SearchResponsePayload imageResponse: imageResponseList){
            Image image = new Image();
            image.setImageId(imageResponse.getImageId());
            image.setEncodedURL(imageResponse.getImageUrl());
            image.setBackground(imageResponse.getImageBackground());
            image.setColorRep(imageResponse.getImageColorProfile());
            image.setFileType(imageResponse.getImageFileFormat());
            image.setHeight(imageResponse.getImageHeight());
            image.setWidth(imageResponse.getImageWidth());
            image.setLastModifiedDate(imageResponse.getImageLastModifiedDate());
            image.setProvidedSize(imageResponse.getImageProvidedSize());
            image.setResDpi(imageResponse.getImageResolutionDpi());
            image.setViewAngle(imageResponse.getImageViewAngle());
            image.setSource(imageResponse.getImageSource());
            List<String> gtins = new ArrayList<>();
            List<String> descriptions = new ArrayList<>();
            for(SearchAssociation association: imageResponse.getAssociations()){
                descriptions.add(association.getDescription());
                image.setItemType(association.getAssetType());
                String approvalStatus = "false";
                if(association.isAssociationVerified()){
                    approvalStatus = "true";
                }
                image.setApprovalStatus(approvalStatus);
                gtins.add(getValue(association.getTags(), "GTIN"));
                image.setUpc10(getValue(association.getTags(), "UPC10"));
                image.setUpc12(getValue(association.getTags(), "UPC12"));
                image.setUpc13(getValue(association.getTags(), "UPC13"));
            }
            image.setGtin(gtins);
            image.setDescription(descriptions);
            imageList.add(image);
        }
        return imageList;
    }

    private String getValue(List<SearchTag> searchTagList, String key){
        Optional<SearchTag> tagOptional= searchTagList.stream().filter(x->x.getTagType().equals(key)).findAny();
        if(tagOptional.isPresent()){
            return tagOptional.get().getTagValue();
        }
        return null;
    }
}
