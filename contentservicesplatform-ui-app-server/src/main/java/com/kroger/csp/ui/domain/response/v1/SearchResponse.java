package com.kroger.csp.ui.domain.response.v1;

import com.kroger.imp.exception.ErrorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dhriti Ghosh
 */
@Setter
@Getter
@NoArgsConstructor
public class SearchResponse {
    private List<Image> images;
    private ErrorDetails error;

    public SearchResponse(SearchImageAPIResponse apiResponse) {
        if (apiResponse != null && apiResponse.getBody() != null) {
            if (apiResponse.getBody().getErrorDetails() != null) {
                error = apiResponse.getBody().getErrorDetails();
            }
            if (apiResponse.getBody().getImageList() != null) {
                images = new ArrayList<>();
                for (SearchAsset asset : apiResponse.getBody().getImageList()) {
                    images.add(populateImage(asset));
                }
            }
        }
    }

    private Image populateImage(SearchAsset asset) {
        Image image = new Image();
        List<String> gtin = new ArrayList<>();
        gtin.add(asset.getGtin());
        image.setGtin(gtin);
        image.setItemType(asset.getItemType());
        image.setEncodedURL(asset.getEncodedURL());
        image.setApprovalStatus(asset.getApprovalStatus());
        if (asset.getAttributeMap() != null) {
            image.setImageId(asset.getAttributeMap().get("IMP_IMAGE_ID"));
            image.setFileType(asset.getAttributeMap().get("IMP_FILE_TYPE_EXT"));
            List<String> description = new ArrayList<>();
            description.add(asset.getAttributeMap().get("IMP_DESCRIPTION"));
            image.setDescription(description);
            image.setLastModifiedDate(asset.getAttributeMap().get("IMP_IMAGE_LAST_MODIFIED_DT"));
            image.setBackground(asset.getAttributeMap().get("IMP_BACKGROUND"));
            image.setHeight(asset.getAttributeMap().get("IMP_HEIGHT_PX"));
            image.setColorRep(asset.getAttributeMap().get("IMP_COLOR_REP"));
            image.setResDpi(asset.getAttributeMap().get("IMP_RES_DPI"));
            image.setViewAngle(asset.getAttributeMap().get("IMP_VIEW_ANGLE"));
            image.setUpc10(asset.getAttributeMap().get("IMP_UPC10"));
            image.setUpc12(asset.getAttributeMap().get("IMP_UPC12"));
            image.setUpc13(asset.getAttributeMap().get("IMP_UPC13"));
            image.setSource(asset.getAttributeMap().get("IMP_SOURCE"));
            image.setWidth(asset.getAttributeMap().get("IMP_WIDTH_PX"));
            image.setProvidedSize(asset.getAttributeMap().get("IMP_PROVIDED_SIZE"));
        }
        return image;
    }
}
