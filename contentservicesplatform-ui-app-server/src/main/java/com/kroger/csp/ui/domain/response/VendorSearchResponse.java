package com.kroger.csp.ui.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VendorSearchResponse {

    private String source;
    private String gtin;
    private List<VendorSearchViewAngleResponse> viewAngleList;
    private String description;
    private String imageType;

    public String toString(){
         return (this.source + "-" + this.gtin + "-" + this.viewAngleList + "-" + this.description + "-" + this.imageType);
    }

}
