package com.kroger.csp.ui.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDetailsUIRequest {

    private String sequence;
    private String viewAngle;
    private String providedSize;
    private String background;
    private String source;
    private String description;
    private String lastModifiedDate;
    private String filePath;
    private String fileName;
    private String fileExtension;
    private String assetType;
    private String asset;
    private String colorProfile;
    private String upc10;
    private String upc12;
    private String upc13;
    private String imageOrientationType;
}
