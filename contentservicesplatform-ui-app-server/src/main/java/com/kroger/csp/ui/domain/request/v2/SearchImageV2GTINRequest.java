package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class SearchImageV2GTINRequest {
    private AssetFilters assetFilters;
}
