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

@Component
public class AddImageRequestConverter {

    @Value("${spring.profiles}")
    private String env;

    public AddImageAPIRequest populateAPIRequest(AddImageUIRequest addImageUIRequest) throws Exception {
        AddImageAPIRequest addImageAPIRequest = new AddImageAPIRequest();

        //Populate TransactionRef fields
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(addImageUIRequest.getReferenceId());
        transactionRef.setCreationdatetime(addImageUIRequest.getCreationDatetime());
        transactionRef.setEvent("ADDASSET");
        transactionRef.setSource("UI");
        if("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("prod");
        else
            transactionRef.setEnvironment(env);

        //Populate Asset Fields
        Asset asset = new Asset();
        asset.setImageType(addImageUIRequest.getImageType());
        asset.setItemType("ProductImages");
        asset.setGTIN(addImageUIRequest.getAssetIdentifier().getGtin());
        asset.setAssetDetails(populateAssetDetailsList(addImageUIRequest.getAssetDetails()));

        //Set Transaction Ref and Asset
        addImageAPIRequest.setTransactionRef(transactionRef);
        addImageAPIRequest.setAsset(asset);

        return addImageAPIRequest;
    }

    private List<AssetDetails> populateAssetDetailsList(List<AssetDetailsUIRequest> assetDetailsRequestList) throws Exception {
        List<AssetDetails> assetDetailsList = new ArrayList<>();

        for(AssetDetailsUIRequest assetDetailsUIRequest : assetDetailsRequestList){

            AssetDetails assetDetails = new AssetDetails();
            assetDetails.setSequence(assetDetailsUIRequest.getSequence());
            assetDetails.setFileName(assetDetailsUIRequest.getFileName());

            //Populate Attribute Map
            AttributeMap attributeMap = new AttributeMap();
            attributeMap.setIMP_VIEW_ANGLE(assetDetailsUIRequest.getViewAngle());
            attributeMap.setIMP_PROVIDED_SIZE(assetDetailsUIRequest.getProvidedSize());

            attributeMap.setIMP_BACKGROUND(assetDetailsUIRequest.getBackground());
            attributeMap.setIMP_SOURCE(assetDetailsUIRequest.getSource());
            attributeMap.setIMP_IMAGE_LAST_MODIFIED_DT(assetDetailsUIRequest.getLastModifiedDate());
            attributeMap.setIMP_DESCRIPTION(assetDetailsUIRequest.getDescription());
            attributeMap.setIMP_FILE_TYPE_EXT(assetDetailsUIRequest.getFileExtension());
            if(StringUtils.isNotBlank(assetDetailsUIRequest.getColorProfile()))
                attributeMap.setIMP_COLOR_REP(assetDetailsUIRequest.getColorProfile());
            else
                attributeMap.setIMP_COLOR_REP("RGB");
            attributeMap.setIMP_UPC10(assetDetailsUIRequest.getUpc10());
            attributeMap.setIMP_UPC12(assetDetailsUIRequest.getUpc12());
            attributeMap.setIMP_UPC13(assetDetailsUIRequest.getUpc13());
            //Populate AssetInfo
            AssetInfo assetInfo = new AssetInfo();
            assetInfo.setAssetType(assetDetailsUIRequest.getAssetType());

            if (StringUtils.isNotBlank(assetDetailsUIRequest.getFilePath())){
                Path path = Paths.get(assetDetailsUIRequest.getFilePath());
                Optional<Path> filePath = Files.find(path, 1, (path1, basicFileAttributes) -> path1.toFile().getName().contains(assetDetailsUIRequest.getFileName())).limit(1).findFirst();
                if (filePath.isPresent()) {
                     File imageFile = new File(filePath.get().toAbsolutePath().toString());
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        for (int n; (n = fis.read(buf)) != -1; ) {
                            output.write(buf, 0, n);
                        }
                        byte[] imgBytes = output.toByteArray();
                        String imgBytesString = Base64.getEncoder().encodeToString(imgBytes);
                        assetInfo.setAsset(imgBytesString);
                    }
                }

            } else{
                assetInfo.setAsset(assetDetailsUIRequest.getAsset());
            }

            //Set Attribute Map and Asset Info and add to AssetDetailsList
            assetDetails.setAttributeMap(attributeMap);
            assetDetails.setAssetInfo(assetInfo);
            assetDetailsList.add(assetDetails);
        }
        return assetDetailsList;
    }

}
