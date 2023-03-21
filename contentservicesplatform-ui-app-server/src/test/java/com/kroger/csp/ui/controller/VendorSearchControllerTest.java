package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.domain.request.RawAssetRequest;
import com.kroger.csp.ui.domain.response.VendorSearchResponse;
import com.kroger.csp.ui.service.SyndigoSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class VendorSearchControllerTest {

    @Mock
    private SyndigoSearchService syndigoService;

    @InjectMocks
    private VendorSearchController vendorSearchController;

    @Test
    public void shouldReturnVendorSearchResponseWhenVendorSearchCalledWithValidParameters() throws Exception {
        String vendorSource = "SYNDIGO";
        String gtin = "1234567890123";

        VendorSearchResponse mockResponse = new VendorSearchResponse();
        mockResponse.setGtin(gtin);

        when(syndigoService.getImageDetailsByGtin(gtin)).thenReturn(mockResponse);

        VendorSearchResponse response = vendorSearchController.vendorSearch(vendorSource, gtin);

        assertThat(response).isEqualToComparingFieldByField(mockResponse);
    }

    @Test
    public void shouldReturnImageWhenGetImageCalledWithValidRequest() throws Exception {
        String vendor = "SYNDIGO";
        String url = "https://example.com/image.jpeg";

        RawAssetRequest request = new RawAssetRequest();
        request.setVendor(vendor);
        request.setUrl(url);

        byte[] mockImage = new byte[]{1, 2, 3, 4};

        when(syndigoService.getRawImage(url)).thenReturn(mockImage);

        ResponseEntity<byte[]> response = vendorSearchController.getImage(request);

        assertThat(response.getBody()).isEqualTo(mockImage);
    }

    @Test
    public void shouldReturnEmptyImageWhenGetImageCalledWithInvalidVendor() throws Exception {
        String vendor = "INVALID_VENDOR";
        String url = "https://example.com/image.jpeg";

        RawAssetRequest request = new RawAssetRequest();
        request.setVendor(vendor);
        request.setUrl(url);

        ResponseEntity<byte[]> response = vendorSearchController.getImage(request);

        assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldReturnEmptyVendorSearchResponseWhenVendorSearchThrowsException() throws Exception {
        String vendorSource = "SYNDIGO";
        String gtin = "1234567890123";

        when(syndigoService.getImageDetailsByGtin(gtin)).thenThrow(new RuntimeException("Error during vendor search"));

        VendorSearchResponse response = vendorSearchController.vendorSearch(vendorSource, gtin);

        assertThat(response).isEqualToComparingFieldByField(new VendorSearchResponse());
    }

    @Test
    public void shouldReturnEmptyImageWhenGetImageThrowsException() throws Exception {
        String vendor = "SYNDIGO";
        String url = "https://example.com/image.jpeg";

        RawAssetRequest request = new RawAssetRequest();
        request.setVendor(vendor);
        request.setUrl(url);

        when(syndigoService.getRawImage(url)).thenThrow(new RuntimeException("Error during image retrieval"));

        ResponseEntity<byte[]> response = vendorSearchController.getImage(request);

        assertThat(response.getBody()).isNull();
    }

}