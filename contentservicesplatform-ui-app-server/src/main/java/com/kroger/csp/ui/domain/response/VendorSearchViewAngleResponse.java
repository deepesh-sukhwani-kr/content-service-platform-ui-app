package com.kroger.csp.ui.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorSearchViewAngleResponse {

    private String viewAngle;
    private String base64;
    private String url;
    private String lastModifiedDate;
}
