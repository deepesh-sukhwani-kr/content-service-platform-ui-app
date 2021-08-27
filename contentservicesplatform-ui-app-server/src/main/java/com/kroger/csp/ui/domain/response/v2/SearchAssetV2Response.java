package com.kroger.csp.ui.domain.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kroger.imp.library.domain.ImpResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAssetV2Response {

    public ImpResponseHeader header;

    public SearchAssetV2Body searchResponse;
}
