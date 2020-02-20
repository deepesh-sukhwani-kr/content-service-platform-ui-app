package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AttributeMap {

    @XmlElement(name = "IMP_VIEW_ANGLE")
    @JsonProperty("IMP_VIEW_ANGLE")
    private String impViewAngle;
    @XmlElement(name = "IMP_PROVIDED_SIZE")
    @JsonProperty("IMP_PROVIDED_SIZE")
    private String impProvidedSize;
    @XmlElement(name = "IMP_BACKGROUND")
    @JsonProperty("IMP_BACKGROUND")
    private String impBackground;
    @XmlElement(name = "IMP_DESCRIPTION")
    @JsonProperty("IMP_DESCRIPTION")
    private String impDescription;
    @XmlElement(name = "IMP_SOURCE")
    @JsonProperty("IMP_SOURCE")
    private String impSource;
    @XmlElement(name = "IMP_IMAGE_LAST_MODIFIED_DT")
    @JsonProperty("IMP_IMAGE_LAST_MODIFIED_DT")
    private String impImageLastModifiedDt;
    @XmlElement(name = "IMP_FILE_TYPE_EXT")
    @JsonProperty("IMP_FILE_TYPE_EXT")
    private String impFileTypeExt;

    @JsonIgnore
    private String impColorRep;

    @JsonIgnore
    private String impImageId;

    @JsonIgnore
    private String impResDpi;

    @JsonIgnore
    private String impHeightPx;

    @JsonIgnore
    private String impWidthPx;

    @JsonIgnore
    private String impGtin;

    @JsonIgnore
    private String impCapturedDt;

    @JsonIgnore
    public String getImpColorRep() {
        return impColorRep;
    }

    @JsonIgnore
    public void setImpColorRep(String impColorRep) {
        this.impColorRep = impColorRep;
    }

    @JsonIgnore
    public String getImpImageId() {
        return impImageId;
    }

    @JsonIgnore
    public void setImpImageId(String impImageId) {
        this.impImageId = impImageId;
    }

    @JsonIgnore
    public String getImpResDpi() {
        return impResDpi;
    }

    @JsonIgnore
    public void setImpResDpi(String impResDpi) {
        this.impResDpi = impResDpi;
    }

    @JsonIgnore
    public String getImpHeightPx() {
        return impHeightPx;
    }

    @JsonIgnore
    public void setImpHeightPx(String impHeightPx) {
        this.impHeightPx = impHeightPx;
    }

    @JsonIgnore
    public String getImpWidthPx() {
        return impWidthPx;
    }

    @JsonIgnore
    public void setImpWidthPx(String impWidthPx) {
        this.impWidthPx = impWidthPx;
    }

    @JsonIgnore
    public String getImpGtin() {
        return impGtin;
    }

    @JsonIgnore
    public void setImpGtin(String impGtin) {
        this.impGtin = impGtin;
    }

    @JsonIgnore
    public String getImpCapturedDt() {
        return impCapturedDt;
    }

    @JsonIgnore
    public void setImpCapturedDt(String impCapturedDt) {
        this.impCapturedDt = impCapturedDt;
    }

    @JsonIgnore
    public HashMap<String, String> getAttrMap() throws Exception {
        HashMap<String, String> attributeMap = new HashMap<>();
        if (this.getImpViewAngle() != null) {
            attributeMap.put("IMP_VIEW_ANGLE", this.getImpViewAngle());
        }
        if (this.getImpProvidedSize() != null) {
            attributeMap.put("IMP_PROVIDED_SIZE", this.getImpProvidedSize());
        }
        if (this.getImpBackground() != null) {
            attributeMap.put("IMP_BACKGROUND", this.getImpBackground());
        }
        if (this.getImpSource() != null) {
            attributeMap.put("IMP_SOURCE", this.getImpSource());
        }
        if (this.getImpImageLastModifiedDt() != null) {
            attributeMap.put("IMP_IMAGE_LAST_MODIFIED_DT", (this.impImageLastModifiedDt != null) ? this.impImageLastModifiedDt.toString() : null);
        }
        if (this.getImpDescription() != null) {
            attributeMap.put("IMP_DESCRIPTION", this.getImpDescription());
        }
        if (this.getImpGtin() != null) {
            attributeMap.put("IMP_UPC10", this.getImpGtin().substring(0, 10));
            attributeMap.put("IMP_UPC12", this.getImpGtin().substring(0, 12));
            attributeMap.put("IMP_UPC13", this.getImpGtin().substring(0, 13));
            attributeMap.put("IMP_GTIN", this.getImpGtin());
        }
        if (this.getImpFileTypeExt() != null) {
            attributeMap.put("IMP_FILE_TYPE_EXT", this.getImpFileTypeExt());
        }
        if (this.getImpColorRep() != null) {
            attributeMap.put("IMP_COLOR_REP", this.getImpColorRep());
        }
        if (this.getImpResDpi() != null) {
            attributeMap.put("IMP_RES_DPI", this.getImpResDpi());
        }
        if (this.getImpHeightPx() != null) {
            attributeMap.put("IMP_HEIGHT_PX", this.getImpHeightPx());
        }
        if (this.getImpWidthPx() != null) {
            attributeMap.put("IMP_WIDTH_PX", this.getImpWidthPx());
        }
        if (this.getImpImageId() != null) {
            attributeMap.put("IMP_IMAGE_ID", this.getImpImageId());
        }
        if (this.getImpCapturedDt() != null) {
            attributeMap.put("IMP_CAPTURED_DT", this.getImpCapturedDt());
        }
        return attributeMap;
    }

    @Override
    public String toString() {
        return "AttributeMap [IMP_VIEW_ANGLE=" + impViewAngle + ", IMP_PROVIDED_SIZE=" + impProvidedSize
                + ", IMP_BACKGROUND=" + impBackground + ", IMP_DESCRIPTION=" + impDescription + ", IMP_SOURCE="
                + impSource + ", IMP_IMAGE_LAST_MODIFIED_DT=" + impImageLastModifiedDt + ", IMP_FILE_TYPE_EXT=" + impFileTypeExt
                + ", IMP_COLOR_REP=" + impColorRep + ", IMP_IMAGE_ID=" + impImageId + ", IMP_RES_DPI=" + impResDpi
                + ", IMP_HEIGHT_PX=" + impHeightPx + ", IMP_WIDTH_PX=" + impWidthPx + ", IMP_GTIN=" + impGtin
                + ", IMP_CAPTURED_DT=" + impCapturedDt + "]";
    }

}
