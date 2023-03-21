package com.kroger.csp.ui.service.v2;

import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v2.AddAssetV2Body;
import com.kroger.csp.ui.domain.response.v2.AddAssetV2Response;
import com.kroger.csp.ui.domain.response.v2.AddResponsePayload;
import com.kroger.csp.ui.domain.response.v2.DuplicateAsset;
import com.kroger.csp.ui.domain.response.v2.IngestionDetails;
import com.kroger.imp.exception.ErrorDetails;
import com.kroger.imp.library.domain.ImpResponseHeader;
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
import java.util.Collections;
import java.util.UUID;

import static com.kroger.csp.ui.utils.ObjectUtils.createTransactionRef;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
public class AddImageV2ServiceTest {

    public static final String HTTPS_EXAMPLE_COM_API_SEARCH = "https://example.com/api/search";
    public static final String AUTH = "abc123";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AddImageV2Service addImageV2Service;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(addImageV2Service, "addUrl", HTTPS_EXAMPLE_COM_API_SEARCH);
        ReflectionTestUtils.setField(addImageV2Service, "auth", AUTH);
    }

    @Test
    public void shouldReturnSuccessResponse() {
        AddImageV2APIRequest request = new AddImageV2APIRequest();
        AddAssetV2Response addAssetV2Response = createAddAssetV2Response(HttpStatus.OK);
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any(Class.class))).thenReturn(
                (new ResponseEntity<>(addAssetV2Response, HttpStatus.OK)));

        AddImageUIResponse response = addImageV2Service.addImage(request);

        assertThat(response.getReferenceId()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getCreationdatetime());
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetails = response.getAssetDetails().get(0);
        assertThat(assetDetails.getSequence()).isEqualTo("1");
        assertThat(assetDetails.getStatusCode()).isEqualTo("200");
        assertThat(assetDetails.getStatusMessage()).isEqualTo("Success");
        assertThat(assetDetails.getImageId()).isEqualTo("123");
        assertThat(assetDetails.getImageUrl()).isEqualTo("http://example.com/image.png");
        assertThat(assetDetails.getErrorDetails()).isNull();
    }

    @Test
    public void shouldReturnErrorResponseWhenPayloadHasErrorCode() {
        AddImageV2APIRequest request = new AddImageV2APIRequest();
        AddAssetV2Response addAssetV2Response = createAddAssetV2ResponseWithError("500", "Internal Server Error");

        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any(Class.class))).thenReturn(
                (new ResponseEntity<>(addAssetV2Response, HttpStatus.OK)));

        AddImageUIResponse response = addImageV2Service.addImage(request);

        assertThat(response.getReferenceId()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getCreationdatetime());
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetails = response.getAssetDetails().get(0);
        assertThat(assetDetails.getSequence()).isEqualTo("1");
        assertThat(assetDetails.getStatusCode()).isEqualTo("500");
        assertThat(assetDetails.getStatusMessage()).isEqualTo("Error");
        assertThat(assetDetails.getImageId()).isNull();
        assertThat(assetDetails.getImageUrl()).isNull();
        assertThat(assetDetails.getErrorDetails()).isEqualTo("Internal Server Error");
    }

    @Test
    public void shouldReturnSuccessWithDuplicatesResponse() {
        // given
        AddImageV2APIRequest request = new AddImageV2APIRequest();
        AddAssetV2Response addAssetV2Response = createAddAssetV2ResponseWithDuplicates();

        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any(Class.class))).thenReturn(
                (new ResponseEntity<>(addAssetV2Response, HttpStatus.OK)));

        // when
        AddImageUIResponse response = addImageV2Service.addImage(request);

        // then
        assertThat(response.getReferenceId()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isEqualTo(
                addAssetV2Response.getHeader().getOriginalTransactionRef().getCreationdatetime());
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetails = response.getAssetDetails().get(0);
        assertThat(assetDetails.getSequence()).isEqualTo("1");
        assertThat(assetDetails.getStatusCode()).isEqualTo("200");
        assertThat(assetDetails.getStatusMessage()).isEqualTo("Success with Duplicates");
        assertThat(assetDetails.getImageId()).isEqualTo("123");
        assertThat(assetDetails.getImageUrl()).isEqualTo("http://example.com/image-123.png");
        assertThat(assetDetails.getErrorDetails()).isNull();
    }

    private AddAssetV2Response createAddAssetV2Response(HttpStatus httpStatus) {
        AddAssetV2Response addAssetV2Response = new AddAssetV2Response();

        ImpResponseHeader impResponseHeader = new ImpResponseHeader();
        impResponseHeader.setResponseStatus(httpStatus.toString());
        impResponseHeader.setResponseMessage(httpStatus.getReasonPhrase());
        impResponseHeader.setEnvironment("test");
        impResponseHeader.setSource("example.com");
        impResponseHeader.setSecurity("Secure");
        impResponseHeader.setSystemTransactionID(UUID.randomUUID().toString());
        impResponseHeader.setSystemCreationDateTime("2023-01-01T00:00:00Z");
        impResponseHeader.setOriginalTransactionRef(createTransactionRef());
        impResponseHeader.setEvent("event-123");

        addAssetV2Response.setHeader(impResponseHeader);

        AddAssetV2Body addAssetV2Body = new AddAssetV2Body();
        AddResponsePayload addResponsePayload = new AddResponsePayload();
        addResponsePayload.setSequence("1");
        addResponsePayload.setStatus("Success");
        IngestionDetails ingestionDetails = new IngestionDetails();
        ingestionDetails.setImageID("123");
        ingestionDetails.setImageURL("http://example.com/image.png");
        addResponsePayload.setIngestionDetails(ingestionDetails);
        addAssetV2Body.setResponsePayload(Collections.singletonList(addResponsePayload));
        addAssetV2Response.setAddAssetResponse(addAssetV2Body);

        return addAssetV2Response;
    }

    private AddAssetV2Response createAddAssetV2ResponseWithError(String errorCode, String errorDescription) {
        AddAssetV2Response addAssetV2Response = new AddAssetV2Response();

        ImpResponseHeader impResponseHeader = new ImpResponseHeader();
        impResponseHeader.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
        impResponseHeader.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        impResponseHeader.setEnvironment("test");
        impResponseHeader.setSource("example.com");
        impResponseHeader.setSecurity("Secure");
        impResponseHeader.setSystemTransactionID(UUID.randomUUID().toString());
        impResponseHeader.setSystemCreationDateTime("2023-01-01T00:00:00Z");
        impResponseHeader.setOriginalTransactionRef(createTransactionRef());
        impResponseHeader.setEvent("event-123");
        impResponseHeader.setOriginalTransactionRef(createTransactionRef());

        addAssetV2Response.setHeader(impResponseHeader);

        AddResponsePayload payload = new AddResponsePayload();
        payload.setSequence("1");
        payload.setStatus("Error");
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(errorCode);
        errorDetails.setErrorDescription(errorDescription);
        payload.setErrorDetails(errorDetails);

        AddAssetV2Body addAssetV2Body = new AddAssetV2Body();
        addAssetV2Body.setResponsePayload(Collections.singletonList(payload));
        addAssetV2Response.setAddAssetResponse(addAssetV2Body);

        return addAssetV2Response;
    }

    private AddAssetV2Response createAddAssetV2ResponseWithDuplicates() {
        AddAssetV2Response addAssetV2Response = new AddAssetV2Response();
        ImpResponseHeader impResponseHeader = new ImpResponseHeader();
        impResponseHeader.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
        impResponseHeader.setResponseMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        impResponseHeader.setEnvironment("test");
        impResponseHeader.setSource("example.com");
        impResponseHeader.setSecurity("Secure");
        impResponseHeader.setSystemTransactionID(UUID.randomUUID().toString());
        impResponseHeader.setSystemCreationDateTime("2023-01-01T00:00:00Z");
        impResponseHeader.setOriginalTransactionRef(createTransactionRef());
        impResponseHeader.setEvent("event-123");
        impResponseHeader.setOriginalTransactionRef(createTransactionRef());
        addAssetV2Response.setHeader(impResponseHeader);

        AddResponsePayload payload = new AddResponsePayload();
        payload.setSequence("1");
        payload.setStatus("Success with Duplicates");

        IngestionDetails ingestionDetails = new IngestionDetails();
        ingestionDetails.setImageID("123");
        ingestionDetails.setImageURL("http://example.com/image.png");

        DuplicateAsset duplicateAsset = new DuplicateAsset();
        duplicateAsset.setImageID("123");
        duplicateAsset.setImageURL("http://example.com/image-123.png");
        ingestionDetails.setDuplicateAssetList(Collections.singletonList(duplicateAsset));

        payload.setIngestionDetails(ingestionDetails);

        AddAssetV2Body addAssetV2Body = new AddAssetV2Body();
        addAssetV2Body.setResponsePayload(Collections.singletonList(payload));
        addAssetV2Response.setAddAssetResponse(addAssetV2Body);

        return addAssetV2Response;
    }
}

