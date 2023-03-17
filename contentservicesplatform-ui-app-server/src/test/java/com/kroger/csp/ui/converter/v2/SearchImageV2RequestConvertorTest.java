package com.kroger.csp.ui.converter.v2;

import com.kroger.csp.ui.domain.request.v2.Filter;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2APIRequest;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2GTINRequest;
import com.kroger.csp.ui.domain.request.v2.SearchImageV2ImageIdRequest;
import com.kroger.csp.ui.utils.ObjectUtils;
import com.kroger.imp.library.domain.TransactionRef;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.kroger.csp.ui.utils.ObjectUtils.ENV;
import static com.kroger.csp.ui.utils.ObjectUtilsV2.createSearchImageV2APIRequest;
import static com.kroger.csp.ui.utils.ObjectUtilsV2.createSearchImageV2GTINRequest;
import static com.kroger.csp.ui.utils.ObjectUtilsV2.createSearchImageV2ImageIdRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class SearchImageV2RequestConvertorTest {

    @InjectMocks
    private SearchImageV2RequestConvertor searchImageV2RequestConvertor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(searchImageV2RequestConvertor, "env", ENV);
    }

    @Test
    public void shouldPopulateSearchImageV2APIRequestForGtin() {
        String gtin = "12345678901234";
        String imageId = null;
        String referenceId = "ref123";
        SearchImageV2GTINRequest searchImageV2GTINRequest = createSearchImageV2GTINRequest(gtin);

        TransactionRef transactionRef = ObjectUtils.createTransactionRef();
        SearchImageV2APIRequest expectedApiRequest =
                createSearchImageV2APIRequest(transactionRef, searchImageV2GTINRequest);

        SearchImageV2APIRequest response = searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat(response.getTransactionRef().getReferenceID()).isEqualTo("ref123-test");
        assertThat(response.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(response.getTransactionRef().getEvent()).isEqualTo("SEARCHASSET");
        assertThat(response.getTransactionRef().getSource()).isEqualTo("PIM");
        assertThat(response.getTransactionRef().getEnvironment()).isEqualTo("test");

        assertImageV2GTINRequest(expectedApiRequest.getSearchRequest(), response.getSearchRequest());

    }

    @Test
    public void shouldPopulateSearchImageV2APIRequestForImageId() {
        String gtin = null;
        String imageId = "123456";
        String referenceId = "ref123";
        SearchImageV2ImageIdRequest searchImageV2ImageIdRequest = createSearchImageV2ImageIdRequest(imageId);

        TransactionRef transactionRef = ObjectUtils.createTransactionRef();
        SearchImageV2APIRequest expectedApiRequest =
                createSearchImageV2APIRequest(transactionRef, searchImageV2ImageIdRequest);

        SearchImageV2APIRequest response = searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat(response.getTransactionRef().getReferenceID()).isEqualTo("ref123-test");
        assertThat(response.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(response.getTransactionRef().getEvent()).isEqualTo("SEARCHASSET");
        assertThat(response.getTransactionRef().getSource()).isEqualTo("PIM");
        assertThat(response.getTransactionRef().getEnvironment()).isEqualTo("test");
        assertV2ImageIdRequest(expectedApiRequest.getSearchRequest(), response.getSearchRequest());

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullGtinAndImageId() {
        String gtin = null;
        String imageId = null;
        String referenceId = "ref123";

        assertThatThrownBy(
                () -> searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId)).isInstanceOf(
                IllegalArgumentException.class).hasMessage("ImageId or Gtin cannot be null");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForBlankGtin() {
        String gtin = " ";
        String imageId = null;
        String referenceId = "ref123";

        assertThatThrownBy(
                () -> searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId)).isInstanceOf(
                IllegalArgumentException.class).hasMessage("ImageId or Gtin cannot be null");
    }

    @Test
    public void shouldSetTransactionRef() {
        String gtin = "12345678901234";
        String imageId = null;
        String referenceId = "ref123";

        SearchImageV2APIRequest response = searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat(response.getTransactionRef().getReferenceID()).isNotNull();
        assertThat(response.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(response.getTransactionRef().getEvent()).isNotNull();
        assertThat(response.getTransactionRef().getSource()).isNotNull();
        assertThat(response.getTransactionRef().getEnvironment()).isNotNull();
    }

    @Test
    public void shouldSetEnvironmentAsStageForStageProfile() {
        String gtin = "12345678901234";
        String imageId = null;
        String referenceId = "ref123";
        ReflectionTestUtils.setField(searchImageV2RequestConvertor, "env", "stage");

        SearchImageV2APIRequest actualApiRequest =
                searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat("stage").isEqualTo(actualApiRequest.getTransactionRef().getEnvironment());
    }

    @Test
    public void shouldSetEnvironmentAsTestForLocalProfile() {
        String gtin = "12345678901234";
        String imageId = null;
        String referenceId = "ref123";
        ReflectionTestUtils.setField(searchImageV2RequestConvertor, "env", "local");

        SearchImageV2APIRequest actualApiRequest =
                searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat("test").isEqualTo(actualApiRequest.getTransactionRef().getEnvironment());
    }

    @Test
    public void shouldSetEnvironmentAsConfiguredProfile() {
        String gtin = "12345678901234";
        String imageId = null;
        String referenceId = "ref123";
        ReflectionTestUtils.setField(searchImageV2RequestConvertor, "env", "prod");

        SearchImageV2APIRequest actualApiRequest =
                searchImageV2RequestConvertor.populateAPIRequest(gtin, imageId, referenceId);

        assertThat("prod").isEqualTo(actualApiRequest.getTransactionRef().getEnvironment());
    }

    public static void assertV2ImageIdRequest(Object expectedRequest, Object resultRequest) {
        SearchImageV2ImageIdRequest expectedSearchImageRequest = (SearchImageV2ImageIdRequest) expectedRequest;
        SearchImageV2ImageIdRequest resultSearchImageRequest = (SearchImageV2ImageIdRequest) resultRequest;
        List<Filter> expectedFilters = expectedSearchImageRequest.getImageFilters();
        List<Filter> actualFilters = resultSearchImageRequest.getImageFilters();

        assertThat(actualFilters).hasSameSizeAs(expectedFilters);

        for (int i = 0; i < expectedFilters.size(); i++) {
            Filter expectedFilter = expectedFilters.get(i);
            Filter actualFilter = actualFilters.get(i);

            assertThat(actualFilter.getField()).isEqualTo(expectedFilter.getField());
            assertThat(actualFilter.getValue()).isEqualTo(expectedFilter.getValue());
            assertThat(actualFilter.getSequence()).isEqualTo(expectedFilter.getSequence());
            assertThat(actualFilter.getOperand()).isEqualTo(expectedFilter.getOperand());
        }
    }

    private void assertImageV2GTINRequest(Object expectedRequest, Object resultRequest) {
        SearchImageV2GTINRequest expectedSearchImageRequest = (SearchImageV2GTINRequest) expectedRequest;
        SearchImageV2GTINRequest resultSearchImageRequest = (SearchImageV2GTINRequest) resultRequest;
        List<Filter> expectedFilters = expectedSearchImageRequest.getAssetFilters().getFilters();
        List<Filter> actualFilters = resultSearchImageRequest.getAssetFilters().getFilters();

        assertThat(actualFilters).hasSameSizeAs(expectedFilters);

        for (int i = 0; i < expectedFilters.size(); i++) {
            Filter expectedFilter = expectedFilters.get(i);
            Filter actualFilter = actualFilters.get(i);

            assertThat(actualFilter.getField()).isEqualTo(expectedFilter.getField());
            assertThat(actualFilter.getValue()).isEqualTo(expectedFilter.getValue());
            assertThat(actualFilter.getSequence()).isEqualTo(expectedFilter.getSequence());
            assertThat(actualFilter.getOperand()).isEqualTo(expectedFilter.getOperand());
        }
    }

}
