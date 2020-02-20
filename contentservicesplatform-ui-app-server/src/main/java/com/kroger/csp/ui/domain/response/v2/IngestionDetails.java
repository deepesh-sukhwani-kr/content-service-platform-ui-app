
package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kroger.csp.ui.domain.request.v2.Association;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "imageID",
        "imageURL",
        "association",
        "duplicateAssetList"
})
@Getter
@Setter
@NoArgsConstructor
public class IngestionDetails {

    public String imageID;
    public String imageURL;
    public List<Association> associations;
    public List<DuplicateAsset> duplicateAssetList;

    @Override
    public String toString() {
        return "IngestionDetails [imageID=" + imageID + ", imageURL=" + imageURL +
                ", associations=" + associations + ", duplicateAssetList=" + duplicateAssetList + "]";
    }
}