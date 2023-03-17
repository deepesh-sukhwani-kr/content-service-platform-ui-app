package com.kroger.csp.ui.controller.v1;

import com.kroger.csp.ui.config.RBACConfiguration;
import com.kroger.csp.ui.converter.v1.AddImageRequestConverter;
import com.kroger.csp.ui.converter.v1.SearchImageRequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.ErrorResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.service.v1.AddImageService;
import com.kroger.csp.ui.service.v1.SearchImageService;
import com.kroger.csp.ui.util.CommonUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UIServerControllerTest {

    @Mock
    private AddImageService addImageService;
    @Mock
    private AddImageRequestConverter addImageRequestConverter;
    @Mock
    private SearchImageRequestConverter searchImageRequestConverter;
    @Mock
    private SearchImageService searchImageService;
    @Mock
    private CommonUtils utils;
    @Mock
    private RBACConfiguration rbacConfig;

    @InjectMocks
    private UIServerController uiServerController;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldAddImageWhenProvidedWithValidRequest() throws Exception {
        AddImageUIRequest uiRequest = mock(AddImageUIRequest.class);
        AddImageAPIRequest apiRequest = mock(AddImageAPIRequest.class);
        AddImageUIResponse expectedResponse = mock(AddImageUIResponse.class);

        when(addImageRequestConverter.populateAPIRequest(uiRequest)).thenReturn(apiRequest);
        when(addImageService.addImage(apiRequest)).thenReturn(expectedResponse);

        AddImageUIResponse actualResponse = uiServerController.addImage(uiRequest);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }



    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInAddImage() throws Exception {
        AddImageUIRequest uiRequest = mock(AddImageUIRequest.class);
        AddImageAPIRequest apiRequest = mock(AddImageAPIRequest.class);
        ErrorResponse errorResponse = mock(ErrorResponse.class);

        when(addImageRequestConverter.populateAPIRequest(uiRequest)).thenReturn(apiRequest);
        doThrow(new RuntimeException("Error")).when(addImageService).addImage(apiRequest);
        when(utils.populateErrorResponse(any(Exception.class))).thenReturn(errorResponse);

        AddImageUIResponse actualResponse = uiServerController.addImage(uiRequest);
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getErrorResponse()).isEqualTo(errorResponse);
    }

    @Test
    public void shouldSearchImageWhenProvidedWithValidParameters() {
        Authentication auth = mock(Authentication.class);
        SearchImageAPIRequest apiRequest = mock(SearchImageAPIRequest.class);
        SearchResponse expectedResponse = mock(SearchResponse.class);

        when(searchImageRequestConverter.populateAPIRequest(anyString(), anyString(), anyString())).thenReturn(
                apiRequest);
        when(searchImageService.searchImage(apiRequest)).thenReturn(expectedResponse);
        when(rbacConfig.isCheckRbac()).thenReturn(false);

        SearchResponse actualResponse = uiServerController.searchImageInCSP(anyString(), anyString(), anyString(), auth);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}