package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v1.AddImageAPIResponse;
import com.kroger.imp.assetmanagement.domain.ResponsePayload;
import com.kroger.imp.datasource.domain.DuplicateAsset;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Service
@RefreshScope
public class AddImageService {

    @Autowired
    RestTemplate restTemplate;
    @Value("${kroger.v1.add.url}")
    private String addUrl;
    @Value("${kroger.v1.authorization}")
    private String authorizationValue;
    public static final String AUTHORIZATION = "Authorization";

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public AddImageUIResponse addImage(AddImageAPIRequest request) {
        ResponseEntity<AddImageAPIResponse> responseEntity = restTemplate.postForEntity(URI.create(addUrl),
                populateHttpEntity(request), AddImageAPIResponse.class);
        return populateResponse(responseEntity.getBody());
    }

    /**
     * @param request
     * @return
     */
    private HttpEntity<AddImageAPIRequest> populateHttpEntity(AddImageAPIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Basic " + authorizationValue);
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(request, headers);
    }

    /**
     * @param body
     * @return
     */
    private AddImageUIResponse populateResponse(AddImageAPIResponse body) {
        AddImageUIResponse response = new AddImageUIResponse();
        response.setReferenceId(body.getHeader().getOriginalTransactionRef().getReferenceID());
        response.setCreationDatetime(body.getHeader().getOriginalTransactionRef().getCreationdatetime());
        response.setAssetDetails(populateAssetDetailsList(body.getBody().getResponsePayload()));
        return response;
    }

    /**
     * @param payloads
     * @return
     */
    private List<AssetDetailsUIResponse> populateAssetDetailsList(LinkedList<ResponsePayload> payloads) {
        List<AssetDetailsUIResponse> responseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(payloads)) {
            for (ResponsePayload payload : payloads) {
                responseList.add(populateAssetDetailsUIResponse(payload));
            }
        }
        return responseList;
    }

    /**
     * @param payload
     * @return
     */
    private AssetDetailsUIResponse populateAssetDetailsUIResponse(ResponsePayload payload) {
        AssetDetailsUIResponse response = new AssetDetailsUIResponse();
        response.setSequence(payload.getSequence());
        response.setStatusMessage(payload.getStatus());

        ResponsePayload.IngestionDetails ingestionDetails = payload.getIngestionDetails();
        if (ObjectUtils.allNotNull(payload.getErrorDetails())
                && StringUtils.isNotBlank(payload.getErrorDetails().getErrorCode())) {
            handleError(payload, response);
        } else if (ObjectUtils.allNotNull(ingestionDetails)
                && ObjectUtils.allNotNull(ingestionDetails.getDuplicateAssetList())
                && CollectionUtils.isNotEmpty(ingestionDetails.getDuplicateAssetList().getDuplicateAsset())) {
            handleDuplicate(ingestionDetails.getDuplicateAssetList().getDuplicateAsset().getFirst(), response);
        } else if (ObjectUtils.allNotNull(ingestionDetails)) {
            handleSuccess(ingestionDetails, response);
        }
        return response;
    }

    /**
     * @param payload
     * @param response
     */
    private void handleError(ResponsePayload payload, AssetDetailsUIResponse response) {
        response.setStatusCode(payload.getErrorDetails().getErrorCode());
        response.setErrorDetails(payload.getErrorDetails().getErrorDescription());
    }

    /**
     * @param duplicateAsset
     * @param response
     */
    private void handleDuplicate(DuplicateAsset duplicateAsset, AssetDetailsUIResponse response) {
        response.setStatusCode("200");
        response.setImageId(duplicateAsset.getImageID());
        response.setImageUrl(duplicateAsset.getImageURL());
    }

    /**
     * @param ingestionDetails
     * @param response
     */
    private void handleSuccess(ResponsePayload.IngestionDetails ingestionDetails, AssetDetailsUIResponse response) {
        response.setStatusCode("200");
        response.setImageId(ingestionDetails.getImageID());
        response.setImageUrl(ingestionDetails.getImageURL());
    }
}
