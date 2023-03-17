package com.kroger.csp.ui.utils;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.AssetDetailsUIRequest;
import com.kroger.csp.ui.domain.request.AssetIdentifierUIRequest;
import com.kroger.csp.ui.domain.response.v1.SearchAsset;
import com.kroger.csp.ui.domain.response.v1.SearchImageAPIResponse;
import com.kroger.csp.ui.service.v1.SearchImageService;
import com.kroger.imp.assetmanagement.domain.Asset;
import com.kroger.imp.assetmanagement.domain.AssetDetails;
import com.kroger.imp.assetmanagement.domain.AssetInfo;
import com.kroger.imp.assetmanagement.domain.AttributeMap;
import com.kroger.imp.library.domain.TransactionRef;
import com.kroger.imp.search.domain.SearchFilter;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ObjectUtils {

    public static final String ENV = "test";
    public static final String REF_ID = "ref123";

    public static AddImageUIRequest createAddImageUIRequest() {
        AddImageUIRequest uiRequest = new AddImageUIRequest();
        uiRequest.setReferenceId(REF_ID);
        AssetDetailsUIRequest assetDetailsUIRequest = new AssetDetailsUIRequest();
        assetDetailsUIRequest.setImageOrientationType("ProductImage");
        assetDetailsUIRequest.setSequence("1");
        assetDetailsUIRequest.setFileName("test.png");
        assetDetailsUIRequest.setViewAngle("front");
        assetDetailsUIRequest.setProvidedSize("100x100");
        assetDetailsUIRequest.setBackground("white");
        assetDetailsUIRequest.setSource("UI");
        assetDetailsUIRequest.setLastModifiedDate("2022/01/01 12:00:00");
        assetDetailsUIRequest.setDescription("test description");
        assetDetailsUIRequest.setFileExtension("png");
        assetDetailsUIRequest.setAssetType("Product");
        assetDetailsUIRequest.setAsset("product123");
        uiRequest.setAssetDetails(Collections.singletonList(assetDetailsUIRequest));
        uiRequest.setAssetIdentifier(new AssetIdentifierUIRequest("12345678901234"));
        return uiRequest;
    }

    public static AddImageUIRequest createUIRequest() {
        AddImageUIRequest request = new AddImageUIRequest();
        request.setReferenceId(REF_ID);
        request.setCreationDatetime("2023-01-01T00:00:00Z");
        request.setImageType("ProductImages");
        request.setAssetIdentifier(createAssetIdentifier());
        request.setAssetDetails(createAssetDetailsUIRequestList());
        return request;
    }

    public static AssetIdentifierUIRequest createAssetIdentifier() {
        AssetIdentifierUIRequest identifier = new AssetIdentifierUIRequest();
        identifier.setGtin("ProductImages");
        return identifier;
    }

    public static List<AssetDetailsUIRequest> createAssetDetailsUIRequestList() {
        List<AssetDetailsUIRequest> list = new ArrayList<>();
        AssetDetailsUIRequest assetDetailsUIRequest = createAssetDetailsUIRequest();
        list.add(assetDetailsUIRequest);
        return list;
    }

    public static AssetDetailsUIRequest createAssetDetailsUIRequest() {
        AssetDetailsUIRequest request = new AssetDetailsUIRequest();
        request.setSequence("1");
        request.setFileName("testFileName");
        request.setAssetType("testAssetType");
        request.setAsset("testAsset");
        request.setViewAngle("testViewAngle");
        request.setProvidedSize("testProvidedSize");
        request.setBackground("testBackground");
        request.setSource("testSource");
        request.setLastModifiedDate("2023-01-01T00:00:00Z");
        request.setDescription("testDescription");
        request.setFileExtension("testFileExtension");
        request.setColorProfile("testColorProfile");
        request.setUpc10("testUpc10");
        request.setUpc12("testUpc12");
        request.setUpc13("testUpc13");
        return request;
    }

    public static TransactionRef createTransactionRef() {
        TransactionRef transactionRef = new TransactionRef();
        transactionRef.setReferenceID(REF_ID);
        transactionRef.setCreationdatetime("2023-01-01T00:00:00Z");
        transactionRef.setEvent("SEARCHASSET");
        transactionRef.setSource("UI");
        transactionRef.setEnvironment(ENV);
        return transactionRef;
    }

    public static Asset createAsset() {
        Asset asset = new Asset();
        asset.setImageType("testImageType");
        asset.setItemType("ProductImages");
        asset.setGTIN("testGtin");
        asset.setAssetDetails(createAssetDetailsList());
        return asset;
    }

    public static List<AssetDetails> createAssetDetailsList() {
        List<AssetDetails> assetDetailsList = new ArrayList<>();
        assetDetailsList.add(createAssetDetails());
        return assetDetailsList;
    }

    public static AssetDetails createAssetDetails() {
        AssetDetails assetDetails = new AssetDetails();
        assetDetails.setSequence("1");
        assetDetails.setFileName("testFileName");
        assetDetails.setAttributeMap(createAttributeMap());
        assetDetails.setAssetInfo(createExpectedAssetInfo());
        return assetDetails;
    }

    public static AttributeMap createAttributeMap() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setIMP_VIEW_ANGLE("testViewAngle");
        attributeMap.setIMP_PROVIDED_SIZE("testProvidedSize");
        attributeMap.setIMP_BACKGROUND("testBackground");
        attributeMap.setIMP_SOURCE("testSource");
        attributeMap.setIMP_IMAGE_LAST_MODIFIED_DT("2023-01-01T00:00:00Z");
        attributeMap.setIMP_DESCRIPTION("testDescription");
        attributeMap.setIMP_FILE_TYPE_EXT("testFileExtension");
        attributeMap.setIMP_COLOR_REP("testColorProfile");
        attributeMap.setIMP_UPC10("testUpc10");
        attributeMap.setIMP_UPC12("testUpc12");
        attributeMap.setIMP_UPC13("testUpc13");
        return attributeMap;
    }

    public static AssetInfo createExpectedAssetInfo() {
        AssetInfo assetInfo = new AssetInfo();
        assetInfo.setAssetType("testAssetType");
        assetInfo.setAsset("testAsset");
        return assetInfo;
    }

    public static HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SearchImageService.AUTHORIZATION, "Basic " + "abc123");
        headers.add("Content-Type", "application/json");
        return headers;
    }

    public static ArrayList<SearchFilter> createSearchFilters() {
        ArrayList<SearchFilter> searchFilters = new ArrayList<>();
        SearchFilter searchFilter1 = new SearchFilter();
        searchFilter1.setSequence(1);
        searchFilter1.setFieldName("field1");
        searchFilter1.setFieldValue("value1");
        searchFilter1.setOperand("=");
        searchFilters.add(searchFilter1);
        SearchFilter searchFilter2 = new SearchFilter();
        searchFilter2.setSequence(2);
        searchFilter2.setFieldName("field2");
        searchFilter2.setFieldValue("value2");
        searchFilter2.setOperand("=");
        searchFilters.add(searchFilter2);
        return searchFilters;
    }

    public static SearchImageAPIResponse createSearchResponse() {

        ArrayList<SearchAsset> imageList = new ArrayList<>();
        SearchAsset asset1 = createSearchAsset();
        HashMap<String, String> attributeMap1 = createAttributeMapForService();
        asset1.setAttributeMap(attributeMap1);
        imageList.add(asset1);

        SearchImageAPIResponse.SearchResponseBody body = new SearchImageAPIResponse.SearchResponseBody();
        body.setImageList(imageList);

        SearchImageAPIResponse apiResponse = new SearchImageAPIResponse();
        apiResponse.setBody(body);

        return apiResponse;
    }

    public static HashMap<String, String> createAttributeMapForService() {
        HashMap<String, String> attributeMap1 = new HashMap<>();
        attributeMap1.put("IMP_IMAGE_ID", "1");
        attributeMap1.put("IMP_FILE_TYPE_EXT", "jpg");
        attributeMap1.put("IMP_DESCRIPTION", "description1");
        attributeMap1.put("IMP_IMAGE_LAST_MODIFIED_DT", "2022-03-16T12:00:00Z");
        attributeMap1.put("IMP_BACKGROUND", "background1");
        attributeMap1.put("IMP_HEIGHT_PX", "100");
        attributeMap1.put("IMP_COLOR_REP", "color1");
        attributeMap1.put("IMP_RES_DPI", "200");
        attributeMap1.put("IMP_VIEW_ANGLE", "45");
        attributeMap1.put("IMP_UPC10", "1234567890");
        attributeMap1.put("IMP_UPC12", "123456789012");
        attributeMap1.put("IMP_UPC13", "1234567890123");
        attributeMap1.put("IMP_SOURCE", "source1");
        attributeMap1.put("IMP_WIDTH_PX", "200");
        attributeMap1.put("IMP_PROVIDED_SIZE", "300");
        attributeMap1.put("IMAGE_ORIENTATION_TYPE", "portrait");
        return attributeMap1;
    }

    public static SearchAsset createSearchAsset() {
        SearchAsset asset1 = new SearchAsset();
        asset1.setGtin("123456789");
        asset1.setItemType("type1");
        asset1.setEncodedURL("http://example.com/image1");
        asset1.setApprovalStatus("approved");
        return asset1;
    }

}
