package com.kroger.csp.ui.domain.response.v2;

import com.kroger.imp.exception.ErrorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * @author Sandeep
 */
@Setter
@Getter
@NoArgsConstructor
public class SearchV2Response {

    private List<ImageV2> images;
    private ErrorDetails error;
}
