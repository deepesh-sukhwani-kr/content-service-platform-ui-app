package com.kroger.csp.ui.converter.v1;

import com.kroger.csp.ui.domain.request.AddImageUIRequest;
import com.kroger.csp.ui.domain.request.AssetDetailsUIRequest;
import com.kroger.csp.ui.domain.request.v1.AddImageAPIRequest;
import com.kroger.imp.assetmanagement.domain.Asset;
import com.kroger.imp.assetmanagement.domain.AssetDetails;
import com.kroger.imp.assetmanagement.domain.AttributeMap;
import com.kroger.imp.library.domain.TransactionRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.kroger.csp.ui.utils.ObjectUtils.ENV;
import static com.kroger.csp.ui.utils.ObjectUtils.createUIRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith (MockitoExtension.class)
public class AddImageRequestConverterTest {

    @InjectMocks
    private AddImageRequestConverter converter;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(converter, "env", ENV);
    }

    @Test
    public void shouldPopulateAPIRequestWhenValidUIRequest() throws Exception {
        AddImageUIRequest uiRequest = createUIRequest();

        AddImageAPIRequest apiRequestResult = converter.populateAPIRequest(uiRequest);

        assertThat(apiRequestResult).isNotNull();
        TransactionRef transactionRef = apiRequestResult.getTransactionRef();
        assertThat(transactionRef.getReferenceID()).isEqualTo(uiRequest.getReferenceId());
        assertThat(transactionRef.getReferenceID()).isEqualTo(uiRequest.getReferenceId());
        assertThat(transactionRef.getEvent()).isEqualTo("ADDASSET");
        assertThat(transactionRef.getSource()).isEqualTo("UI");
        assertThat(transactionRef.getSource()).isEqualTo("UI");
        assertThat(transactionRef.getEnvironment()).isEqualTo(ENV);

        Asset asset = apiRequestResult.getAsset();
        assertThat(asset.getImageType()).isEqualTo(uiRequest.getImageType());
        assertThat(asset.getImageType()).isEqualTo("ProductImages");
        assertThat(asset.getImageType()).isEqualTo(uiRequest.getAssetIdentifier().getGtin());

        AssetDetails assetDetails = asset.getAssetDetails().get(0);
        AssetDetailsUIRequest detailsUIRequest = uiRequest.getAssetDetails().get(0);
        assertThat(assetDetails.getSequence()).isEqualTo(detailsUIRequest.getSequence());
        assertThat(assetDetails.getFileName()).isEqualTo(detailsUIRequest.getFileName());

        AttributeMap attributeMap = assetDetails.getAttributeMap();
        assertThat(attributeMap.getIMP_VIEW_ANGLE()).isEqualTo(detailsUIRequest.getViewAngle());
        assertThat(attributeMap.getIMP_PROVIDED_SIZE()).isEqualTo(detailsUIRequest.getProvidedSize());
        assertThat(attributeMap.getIMP_BACKGROUND()).isEqualTo(detailsUIRequest.getBackground());
        assertThat(attributeMap.getIMP_SOURCE()).isEqualTo(detailsUIRequest.getSource());
        assertThat(attributeMap.getIMP_IMAGE_LAST_MODIFIED_DT()).isEqualTo(detailsUIRequest.getLastModifiedDate());
        assertThat(attributeMap.getIMP_DESCRIPTION()).isEqualTo(detailsUIRequest.getDescription());
        assertThat(attributeMap.getIMP_FILE_TYPE_EXT()).isEqualTo(detailsUIRequest.getFileExtension());
        assertThat(attributeMap.getIMP_COLOR_REP()).isEqualTo(detailsUIRequest.getColorProfile());
        assertThat(attributeMap.getIMP_UPC10()).isEqualTo(detailsUIRequest.getUpc10());
        assertThat(attributeMap.getIMP_UPC12()).isEqualTo(detailsUIRequest.getUpc12());
        assertThat(attributeMap.getIMP_UPC13()).isEqualTo(detailsUIRequest.getUpc13());

        assertThat(detailsUIRequest.getAsset()).isNotBlank();
    }

}
