package com.kroger.csp.ui.utils;

import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.csp.ui.domain.response.v1.AddImageAPIResponse;
import com.kroger.imp.assetmanagement.domain.ResponsePayload;
import com.kroger.imp.assetmanagement.domain.ResponsePayload.IngestionDetails;
import com.kroger.imp.exception.ErrorDetails;
import com.kroger.imp.library.domain.ImpResponseHeader;
import com.kroger.imp.library.domain.TransactionRef;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.LinkedList;

public class RequestResponseObjectUtils {

    public static AddImageAPIResponse createSuccessAPIResponse() {
        ImpResponseHeader header = new ImpResponseHeader();
        TransactionRef originalTransactionRef = new TransactionRef();

        originalTransactionRef.setReferenceID("REF123");
        originalTransactionRef.setCreationdatetime("2023-03-16T12:34:56");
        header.setOriginalTransactionRef(originalTransactionRef);

        ResponsePayload responsePayload = new ResponsePayload();
        responsePayload.setSequence("1");
        responsePayload.setStatus("Success");

        IngestionDetails ingestionDetails = new IngestionDetails();
        ingestionDetails.setImageID("IMG123");
        ingestionDetails.setImageURL("http://test-url.com/image/IMG123");

        responsePayload.setIngestionDetails(ingestionDetails);
        LinkedList<ResponsePayload> responsePayloads = new LinkedList<>();
        responsePayloads.add(responsePayload);

        AddImageAPIResponse.AddImageResponseBody body = new AddImageAPIResponse.AddImageResponseBody(responsePayloads);

        return new AddImageAPIResponse(header, body);
    }

    public static AddImageAPIResponse createErrorAPIResponse() {
        ImpResponseHeader header = new ImpResponseHeader();
        TransactionRef originalTransactionRef = new TransactionRef();

        originalTransactionRef.setReferenceID("REF456");
        originalTransactionRef.setCreationdatetime("2023-03-16T13:14:15");
        header.setOriginalTransactionRef(originalTransactionRef);

        ResponsePayload responsePayload = new ResponsePayload();
        responsePayload.setSequence("1");
        responsePayload.setStatus("Error");

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode("400");
        errorDetails.setErrorDescription("Bad Request");

        responsePayload.setErrorDetails(errorDetails);
        LinkedList<ResponsePayload> responsePayloads = new LinkedList<>();
        responsePayloads.add(responsePayload);

        AddImageAPIResponse.AddImageResponseBody body = new AddImageAPIResponse.AddImageResponseBody(responsePayloads);

        return new AddImageAPIResponse(header, body);
    }

    public static HttpEntity<AddImageAPIRequest> createHttpEntity(AddImageAPIRequest request,
            String authorizationValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authorizationValue);
        headers.add("Content-Type", "application/json");
        return new HttpEntity<>(request, headers);
    }

}
