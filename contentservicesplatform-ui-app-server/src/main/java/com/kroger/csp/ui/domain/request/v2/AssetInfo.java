
package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AssetInfo {
    public String assetType;
    public String asset;

    @Override
    public String toString() {
        return "AssetInfo [assetType=" + assetType + ", asset=" + asset + "]";
    }
}
