package com.kroger.csp.ui.domain.request.v2;

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
public class Association {
    public String imageType;
    public String status;
    public String isRejected;
    public List<Tag> tags;
    public List<String> labels;

    @Override
    public String toString() {
        return "Association [imageType=" + imageType + ", status=" + status + ", isRejected=" + isRejected
                + ", tags=" + tags + ", labels=" + labels + "]";
    }

}
