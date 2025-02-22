package com.kroger.csp.ui.service.v1;

import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.response.AddImageUIResponse;
import com.kroger.csp.ui.domain.response.AssetDetailsUIResponse;
import com.kroger.csp.ui.domain.response.v1.AddImageAPIResponse;
import com.kroger.csp.ui.domain.response.v1.AddImageAPIResponse.AddImageResponseBody;
import com.kroger.imp.assetmanagement.domain.ResponsePayload;
import com.kroger.imp.assetmanagement.domain.ResponsePayload.IngestionDetails;
import com.kroger.imp.datasource.domain.DuplicateAsset;
import com.kroger.imp.datasource.domain.DuplicateAssetList;
import com.kroger.imp.exception.ErrorDetails;
import com.kroger.imp.library.domain.ImpResponseHeader;
import com.kroger.imp.library.domain.TransactionRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedList;

import static com.kroger.csp.ui.utils.RequestResponseObjectUtils.createHttpEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith (MockitoExtension.class)
public class AddImageServiceTest {

    public static final String TEST_AUTHORIZATION = "test-authorization";
    public static final String HTTP_TEST_URL_COM = "http://test-url.com";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AddImageService addImageService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(addImageService, "addUrl", HTTP_TEST_URL_COM);
        ReflectionTestUtils.setField(addImageService, "authorizationValue", TEST_AUTHORIZATION);
    }

    @Test
    public void shouldReturnSuccessResponseWhenValidRequest() {
        AddImageAPIRequest request = new AddImageAPIRequest();
        AddImageAPIResponse expectedResponse = createSuccessAPIResponse(createResponsePayloads(null));

        when(restTemplate.postForEntity(URI.create(HTTP_TEST_URL_COM), createHttpEntity(request, TEST_AUTHORIZATION),
                AddImageAPIResponse.class)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        AddImageUIResponse response = addImageService.addImage(request);

        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isEqualTo(
                expectedResponse.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isNotNull();
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetailsUIResponse = response.getAssetDetails().get(0);
        ResponsePayload responsePayload = expectedResponse.getBody().getResponsePayload().getFirst();
        IngestionDetails ingestionDetails = responsePayload.getIngestionDetails();

        assertThat(assetDetailsUIResponse.getStatusCode()).isEqualTo("200");
        assertThat(assetDetailsUIResponse.getImageId()).isEqualTo(ingestionDetails.getImageID());
        assertThat(assetDetailsUIResponse.getImageUrl()).isEqualTo(ingestionDetails.getImageURL());
    }

    @Test
    public void shouldReturnSuccessResponseWhenValidRequestWithDuplicates() {
        AddImageAPIRequest request = new AddImageAPIRequest();
        AddImageAPIResponse expectedResponse =
                createSuccessAPIResponse(createResponsePayloads(createDuplicateAssetList()));

        when(restTemplate.postForEntity(URI.create(HTTP_TEST_URL_COM), createHttpEntity(request, TEST_AUTHORIZATION),
                AddImageAPIResponse.class)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        AddImageUIResponse response = addImageService.addImage(request);

        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isEqualTo(
                expectedResponse.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isNotNull();
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetailsUIResponse = response.getAssetDetails().get(0);
        ResponsePayload responsePayload = expectedResponse.getBody().getResponsePayload().getFirst();
        IngestionDetails ingestionDetails = responsePayload.getIngestionDetails();

        assertThat(assetDetailsUIResponse.getStatusCode()).isEqualTo("200");
        assertThat(assetDetailsUIResponse.getImageId()).isEqualTo(ingestionDetails.getImageID());
        assertThat(assetDetailsUIResponse.getImageUrl()).isEqualTo(ingestionDetails.getImageURL());
    }

    @Test
    public void shouldReturnSuccessResponseWhenValidRequestWithEmptyResponsePayload() {
        AddImageAPIRequest request = new AddImageAPIRequest();
        AddImageAPIResponse expectedResponse = createSuccessAPIResponse(null);

        when(restTemplate.postForEntity(URI.create(HTTP_TEST_URL_COM), createHttpEntity(request, TEST_AUTHORIZATION),
                AddImageAPIResponse.class)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        AddImageUIResponse response = addImageService.addImage(request);

        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isEqualTo(
                expectedResponse.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isNotNull();

        assertThat(expectedResponse.getBody().getResponsePayload()).isNull();
    }

    private DuplicateAssetList createDuplicateAssetList() {
        DuplicateAssetList duplicateAssetList = new DuplicateAssetList();

        DuplicateAsset duplicateAsset1 = new DuplicateAsset("IMG123", "http://test-url.com/image/IMG123", "REJECTED");

        LinkedList<DuplicateAsset> duplicateAssetLinkedList = new LinkedList<>();
        duplicateAssetLinkedList.add(duplicateAsset1);

        duplicateAssetList.setDuplicateAsset(duplicateAssetLinkedList);

        return duplicateAssetList;
    }

    @Test
    public void shouldReturnErrorResponseWhenApiReturnsError() {
        AddImageAPIRequest request = new AddImageAPIRequest();
        AddImageAPIResponse expectedResponse = createErrorAPIResponse();

        when(restTemplate.postForEntity(URI.create(HTTP_TEST_URL_COM), createHttpEntity(request, TEST_AUTHORIZATION),
                AddImageAPIResponse.class)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        AddImageUIResponse response = addImageService.addImage(request);

        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isEqualTo(
                expectedResponse.getHeader().getOriginalTransactionRef().getReferenceID());
        assertThat(response.getCreationDatetime()).isNotNull();
        assertThat(response.getAssetDetails()).hasSize(1);

        AssetDetailsUIResponse assetDetailsUIResponse = response.getAssetDetails().get(0);
        assertThat(assetDetailsUIResponse.getStatusCode()).isEqualTo("400");
        assertThat(assetDetailsUIResponse.getErrorDetails()).isEqualTo("Bad Request");
    }

    private AddImageAPIResponse createSuccessAPIResponse(LinkedList<ResponsePayload> responsePayloads) {
        AddImageAPIResponse response = new AddImageAPIResponse();
        ImpResponseHeader header = new ImpResponseHeader();
        TransactionRef originalTransactionRef = new TransactionRef();

        originalTransactionRef.setReferenceID("123");
        originalTransactionRef.setCreationdatetime("2023-03-16T12:34:56");
        header.setOriginalTransactionRef(originalTransactionRef);
        response.setHeader(header);

        AddImageResponseBody body = new AddImageResponseBody();

        body.setResponsePayload(responsePayloads);

        response.setBody(body);

        return response;
    }

    private static LinkedList<ResponsePayload> createResponsePayloads(DuplicateAssetList duplicateAssetList) {
        ResponsePayload responsePayload = new ResponsePayload();
        responsePayload.setSequence("1");
        responsePayload.setStatus("Success");

        IngestionDetails ingestionDetails = new IngestionDetails();
        ingestionDetails.setImageID("IMG123");
        ingestionDetails.setImageURL("http://test-url.com/image/IMG123");
        ingestionDetails.setDuplicateAssetList(duplicateAssetList);

        responsePayload.setIngestionDetails(ingestionDetails);
        LinkedList<ResponsePayload> responsePayloads = new LinkedList<>();
        responsePayloads.add(responsePayload);
        return responsePayloads;
    }

    private AddImageAPIResponse createErrorAPIResponse() {
        AddImageAPIResponse response = new AddImageAPIResponse();
        ImpResponseHeader header = new ImpResponseHeader();
        TransactionRef originalTransactionRef = new TransactionRef();

        originalTransactionRef.setReferenceID("REF456");
        originalTransactionRef.setCreationdatetime("2023-03-16T13:14:15");
        header.setOriginalTransactionRef(originalTransactionRef);
        response.setHeader(header);

        AddImageResponseBody body = new AddImageResponseBody();
        ResponsePayload responsePayload = new ResponsePayload();
        responsePayload.setSequence("1");
        responsePayload.setStatus("Error");

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode("400");
        errorDetails.setErrorDescription("Bad Request");

        responsePayload.setErrorDetails(errorDetails);
        LinkedList<ResponsePayload> responsePayloads = new LinkedList<>();
        responsePayloads.add(responsePayload);
        body.setResponsePayload(responsePayloads);
        response.setBody(body);

        return response;
    }

}
