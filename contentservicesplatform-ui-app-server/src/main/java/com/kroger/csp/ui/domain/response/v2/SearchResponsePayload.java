package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SearchResponsePayload {
    private String imageId;
    private String imageUrl;
    private String imageBackground;
    private String imageColorProfile;
    private String imageFileFormat;
    private String imageHeight;
    private String imageWidth;
    private String imageLastModifiedDate;
    private String imageProvidedSize;
    private String imageResolutionDpi;
    private String imageSource;
    private String imageViewAngle;
    private List<SearchAssociation> associations;
}
