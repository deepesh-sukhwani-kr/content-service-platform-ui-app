package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.response.v2.ImageV2;
import com.kroger.csp.ui.domain.response.v2.SearchAssetV2Response;
import com.kroger.csp.ui.domain.response.v2.SearchAssociation;
import com.kroger.csp.ui.domain.response.v2.SearchResponsePayload;
import com.kroger.csp.ui.domain.response.v2.SearchTag;
import com.kroger.csp.ui.domain.response.v2.SearchV2Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static com.kroger.csp.ui.utils.ObjectUtilsV2.createSearchAssetV2Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith (MockitoExtension.class)
public class SearchImageV2ServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SearchImageV2Service searchImageV2Service;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(searchImageV2Service, "searchUrl", "http://example.com/search");
        ReflectionTestUtils.setField(searchImageV2Service, "auth", "fakeAuth");
    }

    @Test
    public void shouldReturnSearchV2ResponseWhenSearchImagesCalled() {
        SearchImageV2APIRequest request = new SearchImageV2APIRequest();

        SearchAssetV2Response searchAssetV2Response = createSearchAssetV2Response();

        ResponseEntity<SearchAssetV2Response> responseEntity = ResponseEntity.ok(searchAssetV2Response);
        Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(HttpEntity.class),
                Mockito.eq(SearchAssetV2Response.class))).thenReturn(responseEntity);

        SearchV2Response searchV2Response = searchImageV2Service.searchImages(request);

        assertSearchResponse(searchAssetV2Response, searchV2Response);
    }

    @Test
    public void shouldThrowExceptionWhenSearchImagesCalledAndRestTemplateFails() {
        SearchImageV2APIRequest request = new SearchImageV2APIRequest();
        Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(HttpEntity.class),
                Mockito.eq(SearchAssetV2Response.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> searchImageV2Service.searchImages(request)).isInstanceOf(RuntimeException.class);
    }

    private void assertSearchResponse(SearchAssetV2Response searchAssetV2Response, SearchV2Response searchV2Response) {
        List<SearchResponsePayload> searchResponsePayloadList =
                searchAssetV2Response.getSearchResponse().getSearchResult().getImageList();
        List<ImageV2> imageV2List = searchV2Response.getImages();

        assertThat(imageV2List).hasSameSizeAs(searchResponsePayloadList);

        SearchResponsePayload payload = searchResponsePayloadList.get(0);
        ImageV2 imageV2 = imageV2List.get(0);

        assertThat(imageV2.getImageId()).isEqualTo(payload.getImageId());
        assertThat(imageV2.getEncodedURL()).isEqualTo(payload.getImageUrl());
        assertThat(imageV2.getBackground()).isEqualTo(payload.getImageBackground());
        assertThat(imageV2.getColorRep()).isEqualTo(payload.getImageColorProfile());
        assertThat(imageV2.getFileType()).isEqualTo(payload.getImageFileFormat());
        assertThat(imageV2.getHeight()).isEqualTo(payload.getImageHeight());
        assertThat(imageV2.getWidth()).isEqualTo(payload.getImageWidth());
        assertThat(imageV2.getLastModifiedDate()).isEqualTo(payload.getImageLastModifiedDate());
        assertThat(imageV2.getProvidedSize()).isEqualTo(payload.getImageProvidedSize());
        assertThat(imageV2.getResDpi()).isEqualTo(payload.getImageResolutionDpi());
        assertThat(imageV2.getViewAngle()).isEqualTo(payload.getImageViewAngle());
        assertThat(imageV2.getSource()).isEqualTo(payload.getImageSource());

        SearchAssociation searchAssociation = payload.getAssociations().get(0);
        assertThat(searchAssociation.getAssetType()).isEqualTo("Product");
        assertThat(searchAssociation.getDescription()).isEqualTo("Test product");

        SearchTag tag = searchAssociation.getTags().get(0);
        assertThat(tag.getTagType()).isEqualTo("GTIN");
        assertThat(tag.getTagValue()).isEqualTo("123456789012");

        assertThat(imageV2.getItemType()).isEqualTo(searchAssociation.getAssetType());
        assertThat(imageV2.getApprovalStatus()).isEqualTo(searchAssociation.isAssociationVerified() ? "true" : "false");
    }
}
