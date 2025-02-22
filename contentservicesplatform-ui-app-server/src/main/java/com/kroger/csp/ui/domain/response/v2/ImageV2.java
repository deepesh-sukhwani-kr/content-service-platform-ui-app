package com.kroger.csp.ui.domain.response.v2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Sandeep
 */
@Setter
@Getter
@NoArgsConstructor
public class ImageV2 {

    private List<String> gtin;
    private String itemType;
    private String encodedURL;
    private String approvalStatus;
    private String imageId;
    private String fileType;
    private List<String> description;
    private String lastModifiedDate;
    private String background;
    private String height;
    private String colorRep;
    private String resDpi;
    private String viewAngle;
    private String upc10;
    private String upc12;
    private String upc13;
    private String source;
    private String width;
    private String providedSize;
}
