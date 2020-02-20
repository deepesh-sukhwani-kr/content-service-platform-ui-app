package com.kroger.csp.ui.domain.request.v2;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Dhriti Ghosh
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Asset {
    public Association association;
    public List<AssetDetails> assetDetails;

    public Asset() {
    }

    public String toString() {
        return "Asset [association=" + this.association + ", assetDetails=" + this.assetDetails + "]";
    }

    public Association getAssociation() {
        return this.association;
    }

    public List<AssetDetails> getAssetDetails() {
        return this.assetDetails;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public void setAssetDetails(List<AssetDetails> assetDetails) {
        this.assetDetails = assetDetails;
    }
}
