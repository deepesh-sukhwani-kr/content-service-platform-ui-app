package com.kroger.csp.ui.domain.response.v2;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kroger.imp.apm.model.ResultEntity;
import lombok.Data;

import java.util.List;

@Data
public class SyndigoImageSearchAPIResponse {
    @JsonProperty("Results")
    private List<ResultEntity> results;

}
