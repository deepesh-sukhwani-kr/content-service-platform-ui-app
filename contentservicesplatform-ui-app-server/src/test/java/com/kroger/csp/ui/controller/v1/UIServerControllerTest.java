package com.kroger.csp.ui.controller.v1;

import com.google.common.collect.ImmutableList;
import com.kroger.csp.ui.config.RBACConfiguration;
import com.kroger.csp.ui.converter.v1.AddImageRequestConverter;
import com.kroger.csp.ui.converter.v1.SearchImageRequestConverter;
import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.ErrorResponse;
import com.kroger.csp.ui.domain.response.v1.Image;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import com.kroger.csp.ui.service.v1.AddImageService;
import com.kroger.csp.ui.service.v1.SearchImageService;
import com.kroger.csp.ui.util.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class UIServerControllerTest {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
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
    public void shouldSearchImageWhenProvidedWithValidParametersAndRbacIsFalse() {
        Authentication auth = mock(Authentication.class);
        SearchImageAPIRequest apiRequest = mock(SearchImageAPIRequest.class);
        SearchResponse expectedResponse = mock(SearchResponse.class);

        when(searchImageRequestConverter.populateAPIRequest(anyString(), anyString(), anyString())).thenReturn(
                apiRequest);
        when(searchImageService.searchImage(apiRequest)).thenReturn(expectedResponse);
        when(rbacConfig.isCheckRbac()).thenReturn(false);

        SearchResponse actualResponse =
                uiServerController.searchImageInCSP(anyString(), anyString(), anyString(), auth);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldSearchImageWhenProvidedWithValidParametersAndRbacIsTrue() {
        Authentication auth = mock(Authentication.class);
        SearchImageAPIRequest apiRequest = mock(SearchImageAPIRequest.class);
        SearchResponse expectedResponse = createSearchResponse();

        when(searchImageRequestConverter.populateAPIRequest(anyString(), anyString(), anyString())).thenReturn(
                apiRequest);
        when(searchImageService.searchImage(apiRequest)).thenReturn(expectedResponse);
        when(rbacConfig.isCheckRbac()).thenReturn(true);
        when(rbacConfig.getExternalSources()).thenReturn(singletonList("source1"));
        when(rbacConfig.getKrogerExternalRoles()).thenReturn(Arrays.asList(ROLE_USER, ROLE_ADMIN));

        Collection<? extends GrantedAuthority> expectedAuthorities =
                Arrays.asList(new SimpleGrantedAuthority(ROLE_USER), new SimpleGrantedAuthority(ROLE_ADMIN));

        when(auth.getAuthorities()).thenAnswer(invocation -> expectedAuthorities);

        SearchResponse actualResponse =
                uiServerController.searchImageInCSP(anyString(), anyString(), anyString(), auth);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private static SearchResponse createSearchResponse() {
        Image image = new Image();
        image.setSource("source1");

        SearchResponse expectedResponse = new SearchResponse();
        expectedResponse.setImages(ImmutableList.of(image));
        return expectedResponse;
    }

}