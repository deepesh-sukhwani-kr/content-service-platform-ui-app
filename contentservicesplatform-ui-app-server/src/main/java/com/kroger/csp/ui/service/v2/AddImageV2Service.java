package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v2.AddAssetV2Response;
import com.kroger.csp.ui.domain.response.v2.AddResponsePayload;
import com.kroger.csp.ui.domain.response.v2.DuplicateAsset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to hit V2 CSP Add api and get back the response.
 */
@Service
@Slf4j
public class AddImageV2Service {

    @Autowired
    RestTemplate restTemplate;
    @Value("${kroger.v2.add.url:}")
    private String addUrl;
    @Value("${kroger.v2.authorization:}")
    private String auth;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic ";

    /**
     * Service method to hit V2 CSP add API
     * @param request Add request
     * @return Formatted response from CSP
     */
    public AddImageUIResponse addImage(AddImageV2APIRequest request) {
        ResponseEntity<AddAssetV2Response> responseEntity =
                restTemplate.postForEntity(URI.create(addUrl), buildRequest(request),
                        AddAssetV2Response.class);
        return populateAddImageUIResponse(responseEntity.getBody());
    }

    /**
     * To create HttpEntity using request payload to hit CSP V2 ADD
     * @param request Original UI request
     * @return HTTP Entity object
     */
    private HttpEntity<AddImageV2APIRequest> buildRequest(AddImageV2APIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, BASIC + auth);
        headers.add("Content-Type", "application/json");
        HttpEntity<AddImageV2APIRequest> httpEntityRequest = new HttpEntity<>(request, headers);
        return httpEntityRequest;
    }

    /**
     * Format and populate the CSP V2 ADD API server response
     * @param serverResponse CSP V2 ADD API server response
     * @return Formatted response to be used by UI
     */
    private AddImageUIResponse populateAddImageUIResponse(AddAssetV2Response serverResponse) {
        AddImageUIResponse response = new AddImageUIResponse();
        response.setReferenceId(serverResponse.getHeader().getOriginalTransactionRef().getReferenceID());
        response.setCreationDatetime(serverResponse.getHeader().getOriginalTransactionRef().getCreationdatetime());
        response.setAssetDetails(populateAssetDetailsList(serverResponse.getAddAssetResponse().getResponsePayload()));
        return response;
    }

    /**
     *
     * @param payloads
     * @return
     */
    private List<AssetDetailsUIResponse> populateAssetDetailsList(List<AddResponsePayload> payloads) {
        List<AssetDetailsUIResponse> responseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(payloads))
            for (AddResponsePayload payload : payloads) {
                if (payload.getErrorDetails() != null &&
                        StringUtils.isNotBlank(payload.getErrorDetails().getErrorCode()))
                    responseList.add(populateError(payload));
                else if (payload.getIngestionDetails() != null &&
                        payload.getIngestionDetails().getDuplicateAssetList() != null)
                    responseList.add(populateSuccessWithDuplicates(payload));
                else if (payload.getIngestionDetails() != null)
                    responseList.add(populateSuccess(payload));
            }
        return responseList;
    }

    /**
     * Populate response in case of success
     * @param serverResponse
     * @return
     */
    private AssetDetailsUIResponse populateSuccess(AddResponsePayload serverResponse) {
        AssetDetailsUIResponse response = new AssetDetailsUIResponse();
        response.setSequence(serverResponse.getSequence());
        response.setStatusMessage(serverResponse.getStatus());
        response.setStatusCode("200");
        response.setImageId(serverResponse.getIngestionDetails().getImageID());
        response.setImageUrl(serverResponse.getIngestionDetails().getImageURL());
        return response;
    }

    /**
     * Populate response in case of Failure with duplicates
     * @param serverResponse
     * @return
     */
    private AssetDetailsUIResponse populateSuccessWithDuplicates(AddResponsePayload serverResponse) {
        AssetDetailsUIResponse response = new AssetDetailsUIResponse();
        response.setSequence(serverResponse.getSequence());
        response.setStatusMessage(serverResponse.getStatus());
        DuplicateAsset duplicateAsset = serverResponse.getIngestionDetails().getDuplicateAssetList().get(0);
        response.setStatusCode("200");
        response.setImageId(duplicateAsset.getImageID());
        response.setImageUrl(duplicateAsset.getImageURL());
        return response;
    }

    /**
     * Populate response in case of Error
     * @param serverResponse
     * @return
     */
    private AssetDetailsUIResponse populateError(AddResponsePayload serverResponse) {
        AssetDetailsUIResponse response = new AssetDetailsUIResponse();
        response.setSequence(serverResponse.getSequence());
        response.setStatusMessage(serverResponse.getStatus());
        response.setStatusCode(serverResponse.getErrorDetails().getErrorCode());
        response.setErrorDetails(serverResponse.getErrorDetails().getErrorDescription());
        return response;
    }
}
