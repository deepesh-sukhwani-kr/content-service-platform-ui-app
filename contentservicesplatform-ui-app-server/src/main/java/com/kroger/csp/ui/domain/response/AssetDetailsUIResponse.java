package com.kroger.csp.ui.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDetailsUIResponse {

    private String sequence;
    private String statusMessage;
    private String statusCode;
    private String errorDetails;
    private String imageId;
    private String imageUrl;

}
