package com.kroger.csp.ui.service;

import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import com.kroger.csp.ui.util.VendorUtil;
import com.kroger.imp.apm.SyndigoAPI;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.kroger.csp.ui.utils.SyndigoObjectUtils.VIEW_ANGLES;
import static com.kroger.csp.ui.utils.SyndigoObjectUtils.createVendorSearchViewAngleResponse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyndigoSearchServiceTest {

    public static final String SIZE = "200x200";
    public static final String BACK_GROUND = "white";
    public static final String IMAGE_TYPE = "jpg";
    @InjectMocks
    private SyndigoSearchService syndigoSearchService;

    @Mock
    private VendorUtil vendorUtil;

    @Mock
    private SyndigoAPI properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(syndigoSearchService, "viewAngles", VIEW_ANGLES);
        ReflectionTestUtils.setField(syndigoSearchService, "providedSize", SIZE);
        ReflectionTestUtils.setField(syndigoSearchService, "background", BACK_GROUND);
        ReflectionTestUtils.setField(syndigoSearchService, "imageType", IMAGE_TYPE);
    }

    @Test
    public void shouldReturnImageDetailsWhenGtinIsProvided() throws Exception {
        String gtin = "123456789012";
        List<VendorSearchViewAngleResponse> vendorSearchViewAngleResponse = createVendorSearchViewAngleResponse();

        when(vendorUtil.getViewAngleList(Mockito.anyMap())).thenReturn(vendorSearchViewAngleResponse);

        VendorSearchResponse response = syndigoSearchService.getImageDetailsByGtin(gtin);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getGtin()).isEqualTo(gtin);
        Assertions.assertThat(response.getImageType()).isEqualTo("jpg");
        Assertions.assertThat(response.getSource()).isEqualTo("SYNDIGO");
        Assertions.assertThat(response.getBackground()).isEqualTo(BACK_GROUND);
        Assertions.assertThat(response.getProvidedSize()).isEqualTo(SIZE);

        Assertions.assertThat(response.getViewAngleList()).hasSameSizeAs(vendorSearchViewAngleResponse);
        Assertions.assertThat(response.getViewAngleList().get(0))
                .isEqualToComparingFieldByField(vendorSearchViewAngleResponse.get(0));
    }

    @Test
    public void shouldReturnByteArrayWhenGetRawImageSucceeds() throws Exception {
        String url = "http://example.com/image.jpg";
        byte[] expectedByteArray = new byte[]{1, 2, 3, 4, 5};
        when(properties.downloadAsset(url)).thenReturn(expectedByteArray);

        byte[] result = syndigoSearchService.getRawImage(url);

        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(expectedByteArray);
        verify(properties, times(1)).downloadAsset(url);
    }

    @Test (expected = Exception.class)
    public void shouldThrowExceptionWhenGetRawImageFails() throws Exception {
        String url = "https://fakeurl.com/image.jpg";

        when(properties.downloadAsset(url)).thenThrow(new Exception("Error downloading asset"));

        syndigoSearchService.getRawImage(url);
    }
}
