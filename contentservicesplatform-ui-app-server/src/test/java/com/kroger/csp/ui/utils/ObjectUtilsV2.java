package com.kroger.csp.ui.utils;

import com.kroger.csp.ui.domain.request.v2.AssetFilters;
import com.kroger.csp.ui.domain.request.v2.Filter;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2GTINRequest;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2ImageIdRequest;
import com.kroger.csp.ui.domain.response.v2.SearchAssetResultV2;
import com.kroger.csp.ui.domain.response.v2.SearchAssetV2Body;
import com.kroger.csp.ui.domain.response.v2.SearchAssetV2Response;
import com.kroger.csp.ui.domain.response.v2.SearchAssociation;
import com.kroger.csp.ui.domain.response.v2.SearchResponsePayload;
import com.kroger.csp.ui.domain.response.v2.SearchTag;
import com.kroger.imp.library.domain.TransactionRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class ObjectUtilsV2 {
    public static SearchImageV2APIRequest createSearchImageV2APIRequest(TransactionRef transactionRef,
            Object searchRequest) {
        SearchImageV2APIRequest expectedApiRequest = new SearchImageV2APIRequest();

        expectedApiRequest.setSearchRequest(searchRequest);
        expectedApiRequest.setTransactionRef(transactionRef);
        return expectedApiRequest;
    }

    public static SearchImageV2GTINRequest createSearchImageV2GTINRequest(String gtin) {
        SearchImageV2GTINRequest searchImageV2GTINRequest = new SearchImageV2GTINRequest();
        AssetFilters assetFilters = new AssetFilters();
        assetFilters.setFilters(Collections.singletonList(createFilterGtin(gtin)));

        searchImageV2GTINRequest.setAssetFilters(assetFilters);
        return searchImageV2GTINRequest;
    }

    public static SearchImageV2ImageIdRequest createSearchImageV2ImageIdRequest(String imageId) {
        SearchImageV2ImageIdRequest searchImageV2GTINRequest = new SearchImageV2ImageIdRequest();
        searchImageV2GTINRequest.setImageFilters(Collections.singletonList(createFilterImgId(imageId)));
        return searchImageV2GTINRequest;
    }

    public static Filter createFilterImgId(String imageId) {
        Filter filter = new Filter();
        filter.setSequence(1);
        filter.setField("imageId");
        filter.setValue(imageId);
        filter.setOperand("AND");
        return filter;
    }

    public static Filter createFilterGtin(String gtin) {
        Filter filter = new Filter();
        filter.setSequence(1);
        filter.setField("gtin");
        filter.setValue(gtin);
        filter.setOperand("AND");
        return filter;
    }

    public static SearchAssetV2Response createSearchAssetV2Response() {
        SearchAssociation searchAssociation = createSearchAssociation();

        SearchResponsePayload searchResponsePayload = createSearchResponsePayload();

        List<SearchTag> searchTagList = createSearchTags();

        searchAssociation.setTags(searchTagList);
        searchResponsePayload.setAssociations(singletonList(searchAssociation));

        SearchAssetResultV2 searchAssetResultV2 = new SearchAssetResultV2();
        searchAssetResultV2.setImageList(singletonList(searchResponsePayload));

        SearchAssetV2Body searchAssetV2Body = new SearchAssetV2Body();
        searchAssetV2Body.setSearchResult(searchAssetResultV2);

        SearchAssetV2Response searchAssetV2Response = new SearchAssetV2Response();
        searchAssetV2Response.setSearchResponse(searchAssetV2Body);

        return searchAssetV2Response;
    }

    public static List<SearchTag> createSearchTags() {
        List<SearchTag> searchTagList = new ArrayList<>();
        SearchTag searchTag = new SearchTag();
        searchTag.setTagType("GTIN");
        searchTag.setTagValue("123456789012");
        searchTagList.add(searchTag);
        return searchTagList;
    }

    public static SearchResponsePayload createSearchResponsePayload() {
        SearchResponsePayload searchResponsePayload = new SearchResponsePayload();
        searchResponsePayload.setImageId("123");
        searchResponsePayload.setImageUrl("http://example.com/image.jpg");
        searchResponsePayload.setImageBackground("white");
        searchResponsePayload.setImageColorProfile("RGB");
        searchResponsePayload.setImageFileFormat("JPEG");
        searchResponsePayload.setImageHeight("100");
        searchResponsePayload.setImageWidth("200");
        searchResponsePayload.setImageLastModifiedDate("2023-03-17");
        searchResponsePayload.setImageProvidedSize("100KB");
        searchResponsePayload.setImageResolutionDpi("72");
        searchResponsePayload.setImageSource("example");
        searchResponsePayload.setImageViewAngle("front");
        return searchResponsePayload;
    }

    public static SearchAssociation createSearchAssociation() {
        SearchAssociation searchAssociation = new SearchAssociation();
        searchAssociation.setAssetType("Product");
        searchAssociation.setDescription("Test product");
        searchAssociation.setAssociationVerified(true);
        return searchAssociation;
    }
}
