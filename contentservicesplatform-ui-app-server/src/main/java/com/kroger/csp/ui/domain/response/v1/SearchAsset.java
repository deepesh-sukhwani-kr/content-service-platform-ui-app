package com.kroger.csp.ui.domain.response.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.kroger.imp.datasource.domain.Asset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchAsset implements Serializable {
    private String gtin;
    private String itemType;
    private String encodedURL;
    private String approvalStatus;
    private HashMap<String, String> attributeMap;

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SearchAsset))
            return false;
        SearchAsset asset = (SearchAsset)obj;
        boolean isEqual = this.gtin.equalsIgnoreCase(asset.gtin) && this.approvalStatus.equalsIgnoreCase(asset.approvalStatus);
        if(this.attributeMap!=null)
            isEqual = isEqual && this.attributeMap.equals(asset.getAttributeMap());
        return isEqual;
    }
}
