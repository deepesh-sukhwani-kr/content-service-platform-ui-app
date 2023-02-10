package com.kroger.csp.ui.converter.v2;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.AssetDetailsUIRequest;
import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.csp.ui.domain.request.v2.Asset;
import com.kroger.csp.ui.domain.request.v2.AssetDetails;
import com.kroger.csp.ui.domain.request.v2.AssetInfo;
import com.kroger.csp.ui.domain.request.v2.Association;
import com.kroger.csp.ui.domain.request.v2.AttributeMap;
import com.kroger.csp.ui.domain.request.v2.Tag;
import com.kroger.imp.library.domain.TransactionRef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@RefreshScope
public class AddImageV2RequestConverter {

    @Value("${spring.profiles}")
    private String env;

    private   static String KROGER_CANONICAL_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final DateTimeFormatter krogerDtTimeFormatter = DateTimeFormatter.ofPattern(KROGER_CANONICAL_DATE_FORMAT);

    /**
     *
     * @param uiRequest
     * @return
     */
    public AddImageV2APIRequest populateAPIRequest(AddImageUIRequest uiRequest) {
        AddImageV2APIRequest request = new AddImageV2APIRequest();
        request.setTransactionRef(populateTransactionRef(uiRequest.getReferenceId()));
        request.setAddAssetRequest(populateAsset(uiRequest));
        return request;
    }

    /**
     *
     * @param referenceId
     * @return
     */
    private TransactionRef populateTransactionRef(String referenceId){
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(referenceId+"-"+env);
        transactionRef.setCreationdatetime(LocalDateTime.now().format(krogerDtTimeFormatter));
        transactionRef.setEvent("ADDASSET");
        transactionRef.setSource("UI");
        if("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("stage");
        else if("local".equalsIgnoreCase(env))
            transactionRef.setEnvironment("unittest");
        else
            transactionRef.setEnvironment(env);
        return transactionRef;
    }

    /**
     *
     * @param gtin
     * @return
     */
    private List<Tag> populateTags(String gtin){
        List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setGtin(gtin);
        tagList.add(tag);
        return tagList;
    }

    /**
     *
     * @param gtin
     * @return
     */
    private Association populateAssociation(String gtin){
        Association association = new Association();
        association.setImageType("ProductImage");
        association.setTags(populateTags(gtin));
        return association;
    }

    /**
     *
     * @param request
     * @return
     */
    private Asset populateAsset(AddImageUIRequest request){
        Asset asset = new Asset();
        asset.setAssociation(populateAssociation(request.getAssetIdentifier().getGtin()));
        asset.getAssociation().setImageType(request.getAssetDetails().get(0).getImageOrientationType());
        asset.setAssetDetails(populateAssetDetailsList(request.getAssetDetails()));
        return asset;
    }

    /**
     *
     * @param assetDetailsRequestList
     * @return
     */
    private List<AssetDetails> populateAssetDetailsList(List<AssetDetailsUIRequest> assetDetailsRequestList) {
        List<AssetDetails> assetDetailsList = new ArrayList<>();
        for(AssetDetailsUIRequest assetDetailsUIRequest : assetDetailsRequestList){
            assetDetailsList.add(populateAssetDetails(assetDetailsUIRequest));
        }
        return assetDetailsList;
    }

    /**
     *
     * @param request
     * @return
     */
    private AssetDetails populateAssetDetails(AssetDetailsUIRequest request){
        AssetDetails assetDetails = new AssetDetails();
        assetDetails.setSequence(request.getSequence());
        assetDetails.setFileName(request.getFileName());
        assetDetails.setAttributeMap(populateAttributeMap(request));
        assetDetails.setAssetInfo(populateAssetInfo(request));
        return assetDetails;
    }

    /**
     *
     * @param request
     * @return
     */
    private AttributeMap populateAttributeMap(AssetDetailsUIRequest request){
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setImpViewAngle(request.getViewAngle());
        attributeMap.setImpProvidedSize(request.getProvidedSize());
        attributeMap.setImpBackground(request.getBackground());
        attributeMap.setImpSource(request.getSource());
        attributeMap.setImpImageLastModifiedDt(request.getLastModifiedDate());
        String description = request.getDescription();
        if(description != null)
            description = description.trim();
        attributeMap.setImpDescription(description);
        attributeMap.setImpFileTypeExt(request.getFileExtension());
        return attributeMap;
    }

    /**
     *
     * @param request
     * @return
     */
    private AssetInfo populateAssetInfo(AssetDetailsUIRequest request){
        AssetInfo assetInfo = new AssetInfo();
        assetInfo.setAssetType(request.getAssetType());
        assetInfo.setAsset(request.getAsset());
        return assetInfo;
    }

}
