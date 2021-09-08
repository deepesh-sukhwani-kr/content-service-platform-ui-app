package com.kroger.csp.ui.converter.v2;

import com.kroger.csp.ui.domain.request.v2.*;
import com.kroger.imp.library.domain.TransactionRef;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RefreshScope
public class SearchImageV2RequestConvertor {

    @Value("${spring.profiles}")
    private String env;

    private final String AND ="AND";
    private final String GTIN = "gtin";
    private final String IMAGEID = "imageId";

    public SearchImageV2APIRequest populateAPIRequest(String gtin, String imageId, String referenceId){
        SearchImageV2APIRequest searchImageV2APIRequest = new SearchImageV2APIRequest();
        List<Filter> searchFilterList = new ArrayList<>();
        if (StringUtils.isBlank(gtin) && StringUtils.isBlank(imageId)) {
            throw new IllegalArgumentException("ImageId or Gtin cannot be null");
        } else if(StringUtils.isNotBlank(gtin)) {
            searchFilterList.add(populateFilterGtin(gtin));
            SearchImageV2GTINRequest searchRequest = new SearchImageV2GTINRequest();
            AssetFilters assetFilters = new AssetFilters();
            assetFilters.setFilters(searchFilterList);
            searchRequest.setAssetFilters(assetFilters);
            searchImageV2APIRequest.setSearchRequest(searchRequest);
        }else {
            searchFilterList.add(populateFilterImageId(imageId));
            SearchImageV2ImageIdRequest searchRequest = new SearchImageV2ImageIdRequest();
            searchRequest.setImageFilters(searchFilterList);
            searchImageV2APIRequest.setSearchRequest(searchRequest);
        }
        searchImageV2APIRequest.setTransactionRef(populateTransactionRef(referenceId));
        return searchImageV2APIRequest;
    }

    /**
     * Populate GTIN filter
     * @param gtin
     * @return
     */
    private Filter populateFilterGtin(String gtin) {
        Filter filter = new Filter();
        filter.setSequence(1);
        filter.setField(GTIN);
        filter.setValue(gtin);
        filter.setOperand(AND);
        return filter;
    }

    /**
     * populate Image ID filter
     * @param imageId
     * @return
     */
    private Filter populateFilterImageId(String imageId) {
        Filter filter = new Filter();
        filter.setSequence(1);
        filter.setField(IMAGEID);
        filter.setValue(imageId);
        filter.setOperand(AND);
        return filter;
    }

    /**
     * Populate transaction ref object
     * @param referenceId
     * @return TransactionRef object
     */
    private TransactionRef populateTransactionRef(String referenceId) {
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(referenceId+"-"+env);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String createdDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss").format(LocalDateTime.now());
        transactionRef.setCreationdatetime(createdDateTime);
        transactionRef.setEvent("SEARCHASSET");
        transactionRef.setSource("PIM");
        if ("stage".equalsIgnoreCase(env))
            transactionRef.setEnvironment("test");
        else if("local".equalsIgnoreCase(env))
            transactionRef.setEnvironment("test");
        else
            transactionRef.setEnvironment(env);
        return transactionRef;
    }

}
