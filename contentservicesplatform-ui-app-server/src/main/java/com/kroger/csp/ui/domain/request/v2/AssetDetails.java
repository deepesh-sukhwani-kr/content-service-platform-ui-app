package com.kroger.csp.ui.domain.request.v2;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Dhriti Ghosh
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class AssetDetails {
    public String sequence;
    public AttributeMap attributeMap;
    public String fileName;
    public AssetInfo assetInfo;

    public AssetDetails() {
    }

    public String toString() {
        return "AssetDetails [sequence=" + this.sequence + ", fileName=" + this.fileName + ", attributeMap=" + this.attributeMap + ", assetInfo=" + this.assetInfo + "]";
    }

    public String getSequence() {
        return this.sequence;
    }

    public AttributeMap getAttributeMap() {
        return this.attributeMap;
    }

    public String getFileName() {
        return this.fileName;
    }

    public AssetInfo getAssetInfo() {
        return this.assetInfo;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setAttributeMap(AttributeMap attributeMap) {
        this.attributeMap = attributeMap;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setAssetInfo(AssetInfo assetInfo) {
        this.assetInfo = assetInfo;
    }
}

