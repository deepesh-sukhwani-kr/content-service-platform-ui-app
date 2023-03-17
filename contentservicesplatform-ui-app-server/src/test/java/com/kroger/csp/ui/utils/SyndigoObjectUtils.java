package com.kroger.csp.ui.utils;

import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;

import java.util.Arrays;
import java.util.List;

public class SyndigoObjectUtils {

    public static final String[] VIEW_ANGLES = {"Front", "Back"};

    public static List<VendorSearchViewAngleResponse> createVendorSearchViewAngleResponse() {
        VendorSearchViewAngleResponse vendorSearchViewAngleResponse1 =
                VendorSearchViewAngleResponse.builder().viewAngle(VIEW_ANGLES[0]).url("url1").base64("base64")
                        .lastModifiedDate("2023-03-16T13:14:15").build();

        VendorSearchViewAngleResponse vendorSearchViewAngleResponse2 =
                VendorSearchViewAngleResponse.builder().viewAngle(VIEW_ANGLES[1]).url("url2").base64("base64")
                        .lastModifiedDate("2023-03-16T13:14:15").build();

        return Arrays.asList(vendorSearchViewAngleResponse1, vendorSearchViewAngleResponse2);
    }
}
