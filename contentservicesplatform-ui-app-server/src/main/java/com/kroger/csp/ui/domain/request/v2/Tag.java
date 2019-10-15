package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    public String gtin;
    public String upc13;
    public String tagType;
    public String tagValue;

    @Override
    public String toString() {
        return "Tag [gtin=" + gtin + ", upc13=" + upc13 + ", tagType=" + tagType
                + ", tagValue=" + tagValue + "]";
    }
}