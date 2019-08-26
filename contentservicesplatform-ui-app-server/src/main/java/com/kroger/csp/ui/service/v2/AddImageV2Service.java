package com.kroger.csp.ui.service.v2;/*
package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v2.AddAssetV2Response;
import com.kroger.imp.asset.model.add.response.AddResponsePayload;
import com.kroger.imp.datasource.model.add.DuplicateAsset;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddImageV2Service {

    @Autowired
    RestTemplate restTemplate;


    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic ";
    private static final String addUrl = "http://10.3.165.62:9091/imp/asset/v2/images/add";

    public AddImageUIResponse addImage(AddImageV2APIRequest request) throws Exception {

        AddImageUIResponse addImageUIResponse = new AddImageUIResponse();

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(addUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION, BASIC + "U2NtYWRtaW46UTVwWUFmYVgyM1ZJMmhyZDJ1");
            headers.add("Content-Type", "application/json");
            HttpEntity<AddImageV2APIRequest> httpEntityRequest = new HttpEntity<>(request, headers);

            ResponseEntity<AddAssetV2Response> responseEntity = restTemplate.postForEntity(builder.build().encode().toUri(), httpEntityRequest, AddAssetV2Response.class);

            AddAssetV2Response addAssetV2Response = new AddAssetV2Response();
            addAssetV2Response = responseEntity.getBody();
            addImageUIResponse.setReferenceId(addAssetV2Response.getHeader().getOriginalTransactionRef().getReferenceID());
            addImageUIResponse.setCreationDatetime(addAssetV2Response.getHeader().getOriginalTransactionRef().getCreationdatetime());
            addImageUIResponse.setAssetDetails(populateAssetDetailsList(addAssetV2Response.getAddAssetResponse().getResponsePayload()));

            return addImageUIResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return addImageUIResponse;
    }

    private List<AssetDetailsUIResponse> populateAssetDetailsList(List<AddResponsePayload> addResponsePayloadList) {
        List<AssetDetailsUIResponse> assetDetailsUIResponseList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(addResponsePayloadList)) {
            for (AddResponsePayload addResponsePayload : addResponsePayloadList) {

                //Populate AssetDetailsUIResponse
                AssetDetailsUIResponse assetDetailsUIResponse = new AssetDetailsUIResponse();

                assetDetailsUIResponse.setSequence(addResponsePayload.getSequence());
                assetDetailsUIResponse.setStatusMessage(addResponsePayload.getStatus());

                if (addResponsePayload.getErrorDetails() != null && StringUtils.isNotBlank(addResponsePayload.getErrorDetails().getErrorCode())) {

                    assetDetailsUIResponse.setStatusCode(addResponsePayload.getErrorDetails().getErrorCode());
                    assetDetailsUIResponse.setErrorDetails(addResponsePayload.getErrorDetails().getErrorDescription());

                } else if (addResponsePayload.getIngestionDetails() != null && addResponsePayload.getIngestionDetails().getDuplicateAssetList() != null) {

                    DuplicateAsset duplicateAsset = addResponsePayload.getIngestionDetails().getDuplicateAssetList().get(0);
                    assetDetailsUIResponse.setStatusCode("200");
                    assetDetailsUIResponse.setImageId(duplicateAsset.getImageID());
                    assetDetailsUIResponse.setImageUrl(duplicateAsset.getImageURL());

                } else if (addResponsePayload.getIngestionDetails() != null) {

                    assetDetailsUIResponse.setStatusCode("200");
                    assetDetailsUIResponse.setImageId(addResponsePayload.getIngestionDetails().getImageID());
                    assetDetailsUIResponse.setImageUrl(addResponsePayload.getIngestionDetails().getImageURL());
                }

                assetDetailsUIResponseList.add(assetDetailsUIResponse);
            }
        }
        return assetDetailsUIResponseList;
    }
}
*/
