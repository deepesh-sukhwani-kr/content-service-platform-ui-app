package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.csp.ui.domain.response.v1.Image;
import com.kroger.csp.ui.domain.response.v1.SearchAsset;
import com.kroger.csp.ui.domain.response.v1.SearchImageAPIResponse;
import com.kroger.csp.ui.domain.response.v1.SearchResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.kroger.csp.ui.utils.ObjectUtils.createSearchFilters;
import static com.kroger.csp.ui.utils.ObjectUtils.createSearchResponse;
import static com.kroger.csp.ui.utils.ObjectUtils.createTransactionRef;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class SearchImageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SearchImageService searchImageService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(searchImageService, "searchUrl", "https://example.com/api/search");
        ReflectionTestUtils.setField(searchImageService, "authorizationValue", "abc123");
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchImageRequestIsValid() {
        // Given
        SearchImageAPIRequest request = new SearchImageAPIRequest();
        request.setTransactionRef(createTransactionRef());
        request.setSearchFilter(createSearchFilters());

        ResponseEntity<SearchImageAPIResponse> searchResponseEntity =
                new ResponseEntity<>(createSearchResponse(), HttpStatus.OK);
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any(Class.class))).thenReturn(
                searchResponseEntity);

        // When
        SearchResponse response = searchImageService.searchImage(request);

        // Then
        Assertions.assertThat(searchResponseEntity.getBody()).isNotNull();
        SearchAsset searchAsset = searchResponseEntity.getBody().getBody().getImageList().get(0);
        Image responseImage = response.getImages().get(0);
        Assertions.assertThat(searchAsset.getGtin()).isEqualTo(responseImage.getGtin());
        Assertions.assertThat(searchAsset.getItemType()).isEqualTo(responseImage.getItemType());
        Assertions.assertThat(searchAsset.getApprovalStatus()).isEqualTo(responseImage.getApprovalStatus());
        Assertions.assertThat(searchAsset.getEncodedURL()).isEqualTo(responseImage.getEncodedURL());
    }
}
