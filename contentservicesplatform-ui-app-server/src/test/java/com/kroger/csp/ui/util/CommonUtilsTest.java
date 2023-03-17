package com.kroger.csp.ui.util;

import com.kroger.csp.ui.domain.response.ErrorResponse;
import com.kroger.imp.exception.ErrorDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommonUtilsTest {

    @InjectMocks
    private CommonUtils commonUtils;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldPopulateErrorResponseWhenExceptionOccurs() {
        Exception ex = new Exception("Test exception");

        ErrorResponse errorResponse = commonUtils.populateErrorResponse(ex);

        assertThat(errorResponse.getStatusCode()).isEqualTo("3013");
        assertThat(errorResponse.getStatusMessage()).isEqualTo("Error in processing request at CSP UI backend. "
                + "Please copy and send this message to CSP team for resolution. Message - " + ex.getMessage());
    }

    @Test
    public void shouldPopulateErrorDetailsWhenExceptionOccurs() {
        Exception ex = new Exception("Test exception");

        ErrorDetails errorDetails = commonUtils.populateErrorDetails(ex);

        assertThat(errorDetails.getErrorCode()).isEqualTo("3013");
        assertThat(errorDetails.getErrorDescription()).isEqualTo("Error in processing request at CSP UI backend. "
                + "Please copy and send this message to CSP team for resolution. Message - " + ex.getMessage());
    }
}
