package com.kroger.csp.ui.converter.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.imp.library.domain.TransactionRef;
import com.kroger.imp.search.domain.SearchFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Converter class to convert UI search request into CSP Search API request
 */
@Component
public class SearchImageRequestConverter {

    @Value("${spring.profiles}")
    private String env;

    /**
     * Convert UI search request into CSP Search API request
     * @param gtin
     * @param imageId
     * @param referenceId
     * @return CSP search Request
     */
    public SearchImageAPIRequest populateAPIRequest(String gtin, String imageId, String referenceId) {
        SearchImageAPIRequest request = new SearchImageAPIRequest();
        request.setTransactionRef(populateTransactionRef(referenceId));
        request.setSearchFilter(populateSearchFilterList(gtin, imageId));
        return request;
    }

    /**
     * Populate transaction ref object
     * @param referenceId
     * @return TransactionRef object
     */
    private TransactionRef populateTransactionRef(String referenceId) {
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(referenceId);
        transactionRef.setCreationdatetime(Instant.now().toString());
        transactionRef.setEvent("SEARCH");
        transactionRef.setSource("UI");
        if ("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("prod");
        else if("local".equalsIgnoreCase(env))
        transactionRef.setEnvironment("test");
        else
            transactionRef.setEnvironment(env);
        return transactionRef;
    }

    /**
     * Populate search filters
     * @param gtin
     * @param imageId
     * @return
     */
    private ArrayList<SearchFilter> populateSearchFilterList(String gtin, String imageId) {
        ArrayList<SearchFilter> searchFilterList = new ArrayList<>();
        if (StringUtils.isBlank(gtin) && StringUtils.isBlank(imageId)) {
            throw new IllegalArgumentException("ImageId and Gtin cannot be null");
        } else if (StringUtils.isNotBlank(gtin) && StringUtils.isNotBlank(imageId)) {
            SearchFilter filter = populateFilterGtin(gtin);
            filter.setOperand("AND");
            searchFilterList.add(filter);
            filter = populateFilterImageId(imageId);
            filter.setSequence(2);
            searchFilterList.add(filter);
        }else if(StringUtils.isNotBlank(gtin))
            searchFilterList.add(populateFilterGtin(gtin));
        else
            searchFilterList.add(populateFilterImageId(imageId));
        return searchFilterList;
    }

    /**
     * Populate GTIN filter
     * @param gtin
     * @return
     */
    private SearchFilter populateFilterGtin(String gtin) {
        SearchFilter filter = new SearchFilter();
        filter.setSequence(1);
        filter.setFieldName("IMP_GTIN");
        filter.setFieldValue(gtin);
        return filter;
    }

    /**
     * populate Image ID filter
     * @param imageId
     * @return
     */
    private SearchFilter populateFilterImageId(String imageId) {
        SearchFilter filter = new SearchFilter();
        filter.setSequence(1);
        filter.setFieldName("IMP_IMAGE_ID");
        filter.setFieldValue(imageId);
        return filter;
    }

}
