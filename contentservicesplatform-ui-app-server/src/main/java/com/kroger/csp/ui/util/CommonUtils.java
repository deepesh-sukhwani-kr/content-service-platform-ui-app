package com.kroger.csp.ui.util;

import com.kroger.csp.ui.domain.response.ErrorResponse;
import com.kroger.imp.exception.ErrorDetails;
import org.springframework.stereotype.Component;

/**
 * @author Dhriti Ghosh
 */
@Component
public class CommonUtils {
    public ErrorResponse populateErrorResponse(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode("3013");
        error.setStatusMessage("Error in processing request at CSP UI backend. " +
                "Please copy and send this message to CSP team for resolution. Message - " + ex.getMessage());
        return error;
    }

    public ErrorDetails populateErrorDetails(Exception ex){
        ErrorDetails error = new ErrorDetails();
        error.setErrorCode("3013");
        error.setErrorDescription("Error in processing request at CSP UI backend. " +
                "Please copy and send this message to CSP team for resolution. Message - " + ex.getMessage());
         return error;
    }
}
