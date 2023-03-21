package com.kroger.csp.ui.converter.v2;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.v2.AddImageV2APIRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.kroger.csp.ui.utils.ObjectUtils.ENV;
import static com.kroger.csp.ui.utils.ObjectUtils.createAddImageUIRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith (MockitoExtension.class)
public class AddImageV2RequestConverterTest {

    @InjectMocks
    private AddImageV2RequestConverter addImageV2RequestConverter;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(addImageV2RequestConverter, "env", ENV);
    }

    @Test
    public void shouldPopulateAPIRequest() {
        AddImageUIRequest uiRequest = createAddImageUIRequest();

        AddImageV2APIRequest apiRequest = addImageV2RequestConverter.populateAPIRequest(uiRequest);

        assertThat(apiRequest).isNotNull();
        assertThat(apiRequest.getTransactionRef().getReferenceID()).isEqualTo("ref123-test");
        assertThat(apiRequest.getTransactionRef().getCreationdatetime()).isNotNull();
        assertThat(apiRequest.getTransactionRef().getEvent()).isEqualTo("ADDASSET");
        assertThat(apiRequest.getTransactionRef().getSource()).isEqualTo("UI");
        assertThat(apiRequest.getTransactionRef().getEnvironment()).isEqualTo("test");
        assertThat(apiRequest.getAddAssetRequest()).isNotNull();
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails()).hasSize(1);
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails().get(0).getSequence()).isEqualTo("1");
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails().get(0).getFileName()).isEqualTo("test.png");
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails().get(0).getAssetInfo()).isNotNull();
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails().get(0).getAssetInfo().getAssetType()).isEqualTo(
                "Product");
        assertThat(apiRequest.getAddAssetRequest().getAssetDetails().get(0).getAssetInfo().getAsset()).isEqualTo(
                "product123");
        assertThat(apiRequest.getAddAssetRequest().getAssociation()).isNotNull();
        assertThat(apiRequest.getAddAssetRequest().getAssociation().getTags()).hasSize(1);
        assertThat(apiRequest.getAddAssetRequest().getAssociation().getTags().get(0).getGtin()).isEqualTo(
                "12345678901234");
        assertThat(apiRequest.getAddAssetRequest().getAssociation().getImageType()).isEqualTo("ProductImage");
    }
}