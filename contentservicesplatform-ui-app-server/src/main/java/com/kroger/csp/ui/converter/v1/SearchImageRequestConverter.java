package com.kroger.csp.ui.converter.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import com.kroger.imp.library.domain.TransactionRef;
import com.kroger.imp.search.domain.SearchFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Component
public class SearchImageRequestConverter {

    @Value("${spring.profiles}")
    private String env;

    public SearchImageAPIRequest populateAPIRequest(String gtin, String imageId, String referenceId) {
        SearchImageAPIRequest searchImageAPIRequest = new SearchImageAPIRequest();

        //Populate TransactionRef fields
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(referenceId);
        transactionRef.setCreationdatetime(Instant.now().toString());
        transactionRef.setEvent("SEARCH");
        transactionRef.setSource("UI");
        if("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("prod");
        else
            transactionRef.setEnvironment(env);

        //Set Transaction Ref and SearchFilter
        searchImageAPIRequest.setTransactionRef(transactionRef);
        searchImageAPIRequest.setSearchFilter(populateSearchFilterList(gtin, imageId));

        return searchImageAPIRequest;
    }

    private ArrayList<SearchFilter> populateSearchFilterList(String gtin, String imageId) {
        ArrayList<SearchFilter> searchFilterList = new ArrayList<>();
        SearchFilter searchFilterForGtin = new SearchFilter();
        SearchFilter searchFilterForImageId = new SearchFilter();

        if (StringUtils.isBlank(gtin) && StringUtils.isBlank(imageId)) {
            throw new IllegalArgumentException("ImageId and Gtin cannot be null");
        } else if (StringUtils.isNotBlank(gtin) && StringUtils.isNotBlank(imageId)) {
            searchFilterForGtin.setSequence(1);
            searchFilterForGtin.setFieldName("IMP_GTIN");
            searchFilterForGtin.setFieldValue(gtin);
            searchFilterForGtin.setOperand("AND");
            searchFilterList.add(searchFilterForGtin);

            searchFilterForImageId.setSequence(2);
            searchFilterForImageId.setFieldName("IMP_IMAGE_ID");
            searchFilterForImageId.setFieldValue(imageId);
            searchFilterList.add(searchFilterForImageId);

        } else if (StringUtils.isNotBlank(gtin)) {
            searchFilterForGtin.setSequence(1);
            searchFilterForGtin.setFieldName("IMP_GTIN");
            searchFilterForGtin.setFieldValue(gtin);
            searchFilterList.add(searchFilterForGtin);

        } else {
            searchFilterForImageId.setSequence(1);
            searchFilterForImageId.setFieldName("IMP_IMAGE_ID");
            searchFilterForImageId.setFieldValue(imageId);
            searchFilterList.add(searchFilterForImageId);
        }
        return searchFilterList;
    }

}
