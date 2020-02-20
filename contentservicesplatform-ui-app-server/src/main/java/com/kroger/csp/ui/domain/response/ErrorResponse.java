package com.kroger.csp.ui.domain.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dhriti Ghosh
 */
@Setter
@Getter
public class ErrorResponse {
    private String statusCode;
    private String statusMessage;
}
