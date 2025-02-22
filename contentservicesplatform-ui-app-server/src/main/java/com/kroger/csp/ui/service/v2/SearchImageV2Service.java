package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.response.v2.ImageV2;
import com.kroger.csp.ui.domain.response.v2.SearchAssetV2Response;
import com.kroger.csp.ui.domain.response.v2.SearchAssociation;
import com.kroger.csp.ui.domain.response.v2.SearchResponsePayload;
import com.kroger.csp.ui.domain.response.v2.SearchTag;
import com.kroger.csp.ui.domain.response.v2.SearchV2Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    public SearchV2Response searchImages(SearchImageV2APIRequest request) {
        ResponseEntity<SearchAssetV2Response> responseEntity;
        SearchV2Response searchResponse = new SearchV2Response();
        try {
            responseEntity =
                    restTemplate.postForEntity(URI.create(searchUrl), buildRequest(request),
                            SearchAssetV2Response.class);
        }catch(Exception e){
            log.error("Error in V2 Csp Search - Service Call : ", e);
            throw e;
        }
        if(responseEntity!=null) {
            searchResponse = populateSearchImageUIResponse(responseEntity.getBody());
        }
        return searchResponse;
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

    private SearchV2Response populateSearchImageUIResponse(SearchAssetV2Response searchAssetV2Response){
        SearchV2Response searchResponse = new SearchV2Response();
        if(searchAssetV2Response.getSearchResponse()!=null && searchAssetV2Response.getSearchResponse().getSearchResult()!=null) {
            searchResponse.setImages(populateImages(searchAssetV2Response.getSearchResponse().getSearchResult().getImageList()));
        }
        return searchResponse;
    }

    private List<ImageV2> populateImages(List<SearchResponsePayload> imageResponseList){
        List<ImageV2> imageList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(imageResponseList)) {
            for (SearchResponsePayload imageResponse : imageResponseList) {
                ImageV2 image = new ImageV2();
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
                for (SearchAssociation association : imageResponse.getAssociations()) {
                    descriptions.add(association.getDescription());
                    image.setItemType(association.getAssetType());
                    String approvalStatus = "false";
                    if (association.isAssociationVerified()) {
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
