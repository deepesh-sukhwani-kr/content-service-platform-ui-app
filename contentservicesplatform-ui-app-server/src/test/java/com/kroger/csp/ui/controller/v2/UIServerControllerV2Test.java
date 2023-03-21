package com.kroger.csp.ui.controller.v2;

import com.kroger.csp.ui.converter.v2.AddImageV2RequestConverter;
import com.kroger.csp.ui.converter.v2.SearchImageV2RequestConvertor;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.ErrorResponse;
import com.kroger.csp.ui.domain.response.v2.SearchV2Response;
import com.kroger.csp.ui.service.v2.AddImageV2Service;
import com.kroger.csp.ui.service.v2.SearchImageV2Service;
import com.kroger.csp.ui.util.CommonUtils;
import com.kroger.imp.exception.ErrorDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class UIServerControllerV2Test {

    @Mock
    private AddImageV2Service addImageV2Service;

    @Mock
    private AddImageV2RequestConverter addImageV2RequestConverter;

    @Mock
    private SearchImageV2Service searchImageV2Service;

    @Mock
    private SearchImageV2RequestConvertor searchImageV2RequestConverter;

    @Mock
    private CommonUtils utils;

    @InjectMocks
    private UIServerControllerV2 uiServerControllerV2;

    @Test
    public void shouldReturnAddImageUIResponseWhenAddImageV2IsCalled() {
        AddImageUIRequest uiRequest = mock(AddImageUIRequest.class);
        AddImageUIResponse expectedResponse = mock(AddImageUIResponse.class);

        when(addImageV2Service.addImage(any())).thenReturn(expectedResponse);

        AddImageUIResponse actualResponse = uiServerControllerV2.addImageV2(uiRequest);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInAddImageV2() {
        AddImageUIRequest uiRequest = mock(AddImageUIRequest.class);
        ErrorResponse errorResponse = mock(ErrorResponse.class);

        doThrow(new RuntimeException("Error")).when(addImageV2Service).addImage(any());
        when(utils.populateErrorResponse(any(Exception.class))).thenReturn(errorResponse);

        AddImageUIResponse actualResponse = uiServerControllerV2.addImageV2(uiRequest);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorResponse()).isEqualTo(errorResponse);
    }

    @Test
    public void shouldReturnSearchV2ResponseWhenSearchImageInCSPV2IsCalled() {
        String imageId = "123";
        String gtin = "11111";
        String referenceId = "12345";
        Authentication auth = mock(Authentication.class);
        SearchV2Response expectedResponse = mock(SearchV2Response.class);

        when(searchImageV2Service.searchImages(any())).thenReturn(expectedResponse);

        SearchV2Response actualResponse = uiServerControllerV2.searchImageInCSP(imageId, gtin, referenceId, auth);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnErrorWhenExceptionOccursInSearchImageInCSPV2() {
        String imageId = "123";
        String gtin = "11111";
        String referenceId = "12345";
        Authentication auth = mock(Authentication.class);
        ErrorDetails errorDetails = mock(ErrorDetails.class);

        doThrow(new RuntimeException("Error")).when(searchImageV2Service).searchImages(any());
        when(utils.populateErrorDetails(any(Exception.class))).thenReturn(errorDetails);

        SearchV2Response actualResponse = uiServerControllerV2.searchImageInCSP(imageId, gtin, referenceId, auth);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getError()).isEqualTo(errorDetails);
    }

}
