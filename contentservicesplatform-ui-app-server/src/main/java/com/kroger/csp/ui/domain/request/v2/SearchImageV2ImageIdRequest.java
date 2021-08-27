package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class SearchImageV2ImageIdRequest {
    private List<Filter> imageFilters;
}
