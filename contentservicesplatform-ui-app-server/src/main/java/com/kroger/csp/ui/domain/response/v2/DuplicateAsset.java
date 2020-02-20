package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kroger.csp.ui.domain.request.v2.Association;
import lombok.AllArgsConstructor;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateAsset {

    public String imageID;
    public String imageURL;
    public List<Association> associations;

    @Override
    public String toString() {
        return "DuplicateAsset [imageID=" + imageID + ", imageURL=" + imageURL + ", associations=" + associations + "]";
    }

}
