package com.kroger.csp.ui.converter.v1;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.AssetDetailsUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.imp.assetmanagement.domain.Asset;
import com.kroger.imp.assetmanagement.domain.AssetDetails;
import com.kroger.imp.assetmanagement.domain.AssetInfo;
import com.kroger.imp.assetmanagement.domain.AttributeMap;
import com.kroger.imp.library.domain.TransactionRef;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * CSP Add image V1 API request converter class
 */
@Component
@RefreshScope
public class AddImageRequestConverter {

    @Value("${spring.profiles}")
    private String env;

    /**
     * To convert API request from UI request
     * @param uiRequest
     * @return CSP API request
     * @throws Exception
     */
    public AddImageAPIRequest populateAPIRequest(AddImageUIRequest uiRequest) throws Exception {
        AddImageAPIRequest request = new AddImageAPIRequest();
        request.setTransactionRef(populateTransactionRef(uiRequest));
        request.setAsset(populateAsset(uiRequest));
        return request;
    }

    /**
     * Populate Asset Details List
     * @param requestList
     * @return List of AssetDetails object
     * @throws Exception
     */
    private List<AssetDetails> populateAssetDetailsList(List<AssetDetailsUIRequest> requestList) throws Exception {
        List<AssetDetails> assetDetailsList = new ArrayList<>();
        for(AssetDetailsUIRequest request : requestList){
            assetDetailsList.add(populateAssetDetails(request));
        }
        return assetDetailsList;
    }

    /**
     * Populate Transaction ref
     * @param request
     * @return TransactionRef object
     */
    private TransactionRef populateTransactionRef(AddImageUIRequest request){
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(request.getReferenceId());
        transactionRef.setCreationdatetime(request.getCreationDatetime());
        transactionRef.setEvent("ADDASSET");
        transactionRef.setSource("UI");
        if("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("test");
        else if("local".equalsIgnoreCase(env))
            transactionRef.setEnvironment("test");
        else
            transactionRef.setEnvironment(env);
        return transactionRef;
    }

    /**
     * Populate individual AssetDetails objects
     * @param request
     * @return AssetDetails object
     * @throws Exception
     */
    private AssetDetails populateAssetDetails(AssetDetailsUIRequest request) throws Exception{
        AssetDetails assetDetails = new AssetDetails();
        assetDetails.setSequence(request.getSequence());
        assetDetails.setFileName(request.getFileName());
        assetDetails.setAttributeMap(populateAttributeMap(request));
        assetDetails.setAssetInfo(populateAssetInfo(request));
        return assetDetails;
    }

    /**
     * Populate Asset object
     * @param request
     * @return Asset object
     * @throws Exception
     */
    private Asset populateAsset(AddImageUIRequest request) throws Exception{
        Asset asset = new Asset();
        asset.setImageType(request.getImageType());
        asset.setItemType("ProductImages");
        asset.setGTIN(request.getAssetIdentifier().getGtin());
        asset.setAssetDetails(populateAssetDetailsList(request.getAssetDetails()));
        return asset;
    }

    /**
     * Puplate Attribute map
     * @param request
     * @return AttributeMap object
     */
    private AttributeMap populateAttributeMap(AssetDetailsUIRequest request){
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setIMP_VIEW_ANGLE(request.getViewAngle());
        attributeMap.setIMP_PROVIDED_SIZE(request.getProvidedSize());
        attributeMap.setIMP_BACKGROUND(request.getBackground());
        attributeMap.setIMP_SOURCE(request.getSource());
        attributeMap.setIMP_IMAGE_LAST_MODIFIED_DT(request.getLastModifiedDate());
        attributeMap.setIMP_DESCRIPTION(request.getDescription());
        attributeMap.setIMP_FILE_TYPE_EXT(request.getFileExtension());
        if(StringUtils.isNotBlank(request.getColorProfile()))
            attributeMap.setIMP_COLOR_REP(request.getColorProfile());
        else
            attributeMap.setIMP_COLOR_REP("RGB");
        attributeMap.setIMP_UPC10(request.getUpc10());
        attributeMap.setIMP_UPC12(request.getUpc12());
        attributeMap.setIMP_UPC13(request.getUpc13());
        //attributeMap.setImageOrientationType(request.getImageOrientationType());
        return attributeMap;
    }

    /**
     * Populate AssetInfo object
     * @param request
     * @return AssetInfo object
     * @throws Exception
     */
    private AssetInfo populateAssetInfo(AssetDetailsUIRequest request) throws Exception{
        AssetInfo assetInfo = new AssetInfo();
        assetInfo.setAssetType(request.getAssetType());
        if (StringUtils.isNotBlank(request.getFilePath())){
            Path path = Paths.get(request.getFilePath());
            Optional<Path> filePath = Files.find(path, 1,
                    (path1, basicFileAttributes) ->
                            path1.toFile().getName().contains(request.getFileName())).limit(1).findFirst();
            if (filePath.isPresent())
                assetInfo.setAsset(getAssetAsString(filePath));
        }else
            assetInfo.setAsset(request.getAsset());
        return assetInfo;
    }

    /**
     * Get Asset and Base64 encoded string
     * @param filePath
     * @return Base 64 encoded asset String
     * @throws Exception
     */
    private String getAssetAsString(Optional<Path> filePath) throws Exception{
        File imageFile = new File(filePath.get().toAbsolutePath().toString());
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int n; (n = fis.read(buf)) != -1; ) {
                output.write(buf, 0, n);
            }
            byte[] imgBytes = output.toByteArray();
            return Base64.getEncoder().encodeToString(imgBytes);
        }
    }

}
