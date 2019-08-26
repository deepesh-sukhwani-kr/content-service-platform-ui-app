package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v1.AddImageAPIResponse;
import com.kroger.imp.assetmanagement.domain.ResponsePayload;
import com.kroger.imp.datasource.domain.DuplicateAsset;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AddImageService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${add.url}")
    private String addUrl;

    @Value("${authorization}")
    private String authorizationValue;


    public static final String AUTHORIZATION = "Authorization";

    public AddImageUIResponse addImage(AddImageAPIRequest request) throws Exception {

        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(addUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION, authorizationValue);
            headers.add("Content-Type", "application/json");
            HttpEntity<AddImageAPIRequest> httpEntityRequest = new HttpEntity<>(request, headers);

            ResponseEntity<AddImageAPIResponse> responseEntity = restTemplate.postForEntity(builder.build().encode().toUri(), httpEntityRequest, AddImageAPIResponse.class);

            AddImageAPIResponse addImageAPIResponse = new AddImageAPIResponse();
            addImageAPIResponse = responseEntity.getBody();
            addImageUIResponse.setReferenceId(addImageAPIResponse.getHeader().getOriginalTransactionRef().getReferenceID());
            addImageUIResponse.setCreationDatetime(addImageAPIResponse.getHeader().getOriginalTransactionRef().getCreationdatetime());
            addImageUIResponse.setAssetDetails(populateAssetDetailsList(addImageAPIResponse.getBody().getResponsePayload()));

            return addImageUIResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return addImageUIResponse;
    }

    private List<AssetDetailsUIResponse> populateAssetDetailsList(LinkedList<ResponsePayload> responsePayloadList) {
        List<AssetDetailsUIResponse> assetDetailsUIResponseList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(responsePayloadList)) {
            for (ResponsePayload responsePayload : responsePayloadList) {

                //Populate AssetDetailsUIResponse
                AssetDetailsUIResponse assetDetailsUIResponse = new AssetDetailsUIResponse();

                assetDetailsUIResponse.setSequence(responsePayload.getSequence());
                assetDetailsUIResponse.setStatusMessage(responsePayload.getStatus());

                if (responsePayload.getErrorDetails() != null && StringUtils.isNotBlank(responsePayload.getErrorDetails().getErrorCode())) {

                    assetDetailsUIResponse.setStatusCode(responsePayload.getErrorDetails().getErrorCode());
                    assetDetailsUIResponse.setErrorDetails(responsePayload.getErrorDetails().getErrorDescription());

                } else if (responsePayload.getIngestionDetails() != null && responsePayload.getIngestionDetails().getDuplicateAssetList() != null && CollectionUtils.isNotEmpty(responsePayload.getIngestionDetails().getDuplicateAssetList().getDuplicateAsset())) {

                    DuplicateAsset duplicateAsset = responsePayload.getIngestionDetails().getDuplicateAssetList().getDuplicateAsset().getFirst();
                    assetDetailsUIResponse.setStatusCode("200");
                    assetDetailsUIResponse.setImageId(duplicateAsset.getImageID());
                    assetDetailsUIResponse.setImageUrl(duplicateAsset.getImageURL());

                } else if (responsePayload.getIngestionDetails() != null) {

                    assetDetailsUIResponse.setStatusCode("200");
                    assetDetailsUIResponse.setImageId(responsePayload.getIngestionDetails().getImageID());
                    assetDetailsUIResponse.setImageUrl(responsePayload.getIngestionDetails().getImageURL());
                }

                assetDetailsUIResponseList.add(assetDetailsUIResponse);
            }
        }
        return assetDetailsUIResponseList;
    }
}
