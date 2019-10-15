
package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kroger.imp.exception.ErrorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"sequence", "status", "errorDetails", "ingestionDetails"})
@Getter
@Setter
@NoArgsConstructor
public class AddResponsePayload {

    public String sequence;
    public String status;
    public ErrorDetails errorDetails;
    public IngestionDetails ingestionDetails;

    @Override
    public String toString() {
        return "AddResponsePayload [sequence=" + sequence + ", status=" + status +
                ", errorDetails=" + errorDetails + ", ingestionDetails=" + ingestionDetails + "]";
    }
}