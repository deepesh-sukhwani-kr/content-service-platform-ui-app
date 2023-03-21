package com.kroger.csp.ui.converter.v1;

import com.kroger.csp.ui.domain.request.v1.SearchImageAPIRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.kroger.csp.ui.utils.ObjectUtils.ENV;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith (MockitoExtension.class)
public class SearchImageRequestConverterTest {

    @InjectMocks
    private SearchImageRequestConverter searchImageRequestConverter;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(searchImageRequestConverter, "env", ENV);
    }

    @Test
    public void shouldPopulateAPIRequestWhenGtinAndImageIdProvided() {
        String gtin = "1234567890123";
        String imageId = "abc123";
        String referenceId = "ref123";

        SearchImageAPIRequest result = searchImageRequestConverter.populateAPIRequest(gtin, imageId, referenceId);

        assertThat(result.getTransactionRef().getReferenceID()).isEqualTo(referenceId + "-test");
        assertThat(result.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(result.getTransactionRef().getEvent()).isEqualTo("SEARCH");
        assertThat(result.getTransactionRef().getSource()).isEqualTo("UI");
        assertThat(result.getTransactionRef().getEnvironment()).isEqualTo("test");

        assertThat(result.getSearchFilter().get(0).getFieldName()).isEqualTo("IMP_GTIN");
        assertThat(result.getSearchFilter().get(0).getFieldValue()).isEqualTo(gtin);
        assertThat(result.getSearchFilter().get(0).getSequence()).isEqualTo(1);
        assertThat(result.getSearchFilter().get(0).getOperand()).isEqualTo("AND");

        assertThat(result.getSearchFilter().get(1).getFieldName()).isEqualTo("IMP_IMAGE_ID");
        assertThat(result.getSearchFilter().get(1).getFieldValue()).isEqualTo(imageId);
        assertThat(result.getSearchFilter().get(1).getSequence()).isEqualTo(2);
    }

    @Test
    public void shouldPopulateAPIRequestWhenOnlyGtinProvided() {
        String gtin = "1234567890123";
        String referenceId = "ref123";

        SearchImageAPIRequest result = searchImageRequestConverter.populateAPIRequest(gtin, null, referenceId);

        assertThat(result.getTransactionRef().getReferenceID()).isEqualTo(referenceId + "-test");
        assertThat(result.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(result.getTransactionRef().getEvent()).isEqualTo("SEARCH");
        assertThat(result.getTransactionRef().getSource()).isEqualTo("UI");
        assertThat(result.getTransactionRef().getEnvironment()).isEqualTo("test");

        assertThat(result.getSearchFilter().get(0).getFieldName()).isEqualTo("IMP_GTIN");
        assertThat(result.getSearchFilter().get(0).getFieldValue()).isEqualTo(gtin);
        assertThat(result.getSearchFilter().get(0).getSequence()).isEqualTo(1);
    }

    @Test
    public void shouldPopulateAPIRequestWhenOnlyImageIdProvided() {
        String imageId = "abc123";
        String referenceId = "ref123";

        SearchImageAPIRequest result = searchImageRequestConverter.populateAPIRequest(null, imageId, referenceId);

        assertThat(result.getTransactionRef().getReferenceID()).isEqualTo(referenceId + "-test");
        assertThat(result.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(result.getTransactionRef().getEvent()).isEqualTo("SEARCH");
        assertThat(result.getTransactionRef().getSource()).isEqualTo("UI");
        assertThat(result.getTransactionRef().getEnvironment()).isEqualTo("test");

        assertThat(result.getSearchFilter().get(0).getFieldName()).isEqualTo("IMP_IMAGE_ID");
        assertThat(result.getSearchFilter().get(0).getFieldValue()).isEqualTo(imageId);
        assertThat(result.getSearchFilter().get(0).getSequence()).isEqualTo(1);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenBothGtinAndImageIdAreNull() {
        assertThatThrownBy(() -> searchImageRequestConverter.populateAPIRequest(null, null, "ref123")).isInstanceOf(
                IllegalArgumentException.class).hasMessage("ImageId and Gtin cannot be null");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenBothGtinAndImageIdAreBlank() {
        assertThatThrownBy(() -> searchImageRequestConverter.populateAPIRequest("", "", "ref123")).isInstanceOf(
                IllegalArgumentException.class).hasMessage("ImageId and Gtin cannot be null");
    }
}


