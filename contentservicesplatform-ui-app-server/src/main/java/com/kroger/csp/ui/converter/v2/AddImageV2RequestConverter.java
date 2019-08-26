package com.kroger.csp.ui.converter.v2;/*
package com.kroger.csp.ui.converter.v2;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.AssetDetailsUIRequest;
import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.imp.asset.model.add.request.Asset;
import com.kroger.imp.asset.model.add.request.AssetDetails;
import com.kroger.imp.asset.model.add.request.AssetInfo;
import com.kroger.imp.asset.model.add.request.AttributeMap;
import com.kroger.imp.datasource.model.add.Association;
import com.kroger.imp.datasource.model.add.Tag;
import com.kroger.imp.library.domain.TransactionRef;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddImageV2RequestConverter {

    private   static String KROGER_CANONICAL_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final DateTimeFormatter krogerDtTimeFormatter = DateTimeFormatter.ofPattern(KROGER_CANONICAL_DATE_FORMAT);

    public AddImageV2APIRequest populateAPIRequest(AddImageUIRequest addImageUIRequest) {
        AddImageV2APIRequest addImageV2APIRequest = new AddImageV2APIRequest();

        //Populate TransactionRef fields
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(addImageUIRequest.getReferenceId());
        transactionRef.setCreationdatetime(LocalDateTime.now().format(krogerDtTimeFormatter));
        transactionRef.setEvent("ADDASSET");
        transactionRef.setSource("UI");
        transactionRef.setEnvironment("vendortest");

        //Populate Tag
        Tag tag = new Tag();
        tag.setGtin(addImageUIRequest.getAssetIdentifier().getGtin());
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);

        //Populate Association
        Association association = new Association();
        association.setImageType("ProductImage");
        association.setTags(tagList);


        //Populate Asset Fields
        Asset asset = new Asset();
        asset.setAssociation(association);
        asset.setAssetDetails(populateAssetDetailsList(addImageUIRequest.getAssetDetails()));

        //Set Transaction Ref and Asset
        addImageV2APIRequest.setTransactionRef(transactionRef);
        addImageV2APIRequest.setAddAssetRequest(asset);

        return addImageV2APIRequest;
    }

    private List<AssetDetails> populateAssetDetailsList(List<AssetDetailsUIRequest> assetDetailsRequestList) {
        List<AssetDetails> assetDetailsList = new ArrayList<>();

        for(AssetDetailsUIRequest assetDetailsUIRequest : assetDetailsRequestList){

            AssetDetails assetDetails = new AssetDetails();
            assetDetails.setSequence(assetDetailsUIRequest.getSequence());
            assetDetails.setFileName(assetDetailsUIRequest.getFileName());

            //Populate Attribute Map
            AttributeMap attributeMap = new AttributeMap();
            attributeMap.setImpViewAngle(assetDetailsUIRequest.getViewAngle());
            attributeMap.setImpProvidedSize(assetDetailsUIRequest.getProvidedSize());
            attributeMap.setImpBackground(assetDetailsUIRequest.getBackground());
            attributeMap.setImpSource(assetDetailsUIRequest.getSource());
            attributeMap.setImpImageLastModifiedDt(assetDetailsUIRequest.getLastModifiedDate());
            attributeMap.setImpDescription(assetDetailsUIRequest.getDescription());
            attributeMap.setImpFileTypeExt(assetDetailsUIRequest.getFileExtension());

            //Populate AssetInfo
            AssetInfo assetInfo = new AssetInfo();
            assetInfo.setAssetType(assetDetailsUIRequest.getAssetType());
            assetInfo.setAsset(assetDetailsUIRequest.getAsset());

            //Set Attribute Map and Asset Info and add to AssetDetailsList
            assetDetails.setAttributeMap(attributeMap);
            assetDetails.setAssetInfo(assetInfo);
            assetDetailsList.add(assetDetails);
        }
        return assetDetailsList;
    }

}
*/
