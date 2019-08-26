package com.kroger.csp.ui.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddImageUIRequest {

    private String referenceId;
    private String creationDatetime;
    private String imageType;
    private AssetIdentifierUIRequest assetIdentifier;
    private List<AssetDetailsUIRequest> assetDetails;

}
