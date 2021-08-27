package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kroger.csp.ui.domain.request.v2.Approval;
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
public class SearchAssociation {

    private String assetType;
    private String description;
    private boolean isAssociationVerified;
    private String associationDate;
    private List<SearchTag> tags;
    private List<String> labels;
    private List<Approval> approvals;
}
