package com.kroger.csp.ui.domain.response;

import com.kroger.csp.ui.domain.response.v1.SearchAsset;
import com.kroger.imp.exception.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchImageUIResponse implements Serializable {

    private String referenceId;
    private String creationDatetime;
    private ErrorDetails errorDetails;
    private ArrayList<SearchAsset> imageList;

}
