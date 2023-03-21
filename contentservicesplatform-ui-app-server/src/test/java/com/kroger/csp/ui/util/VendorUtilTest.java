package com.kroger.csp.ui.util;

import com.kroger.csp.ui.domain.response.VendorSearchViewAngleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith (MockitoExtension.class)
public class VendorUtilTest {

    private static final String VIEW_ANGLE_FRONT = "front";
    private static final String VIEW_ANGLE_BACK = "back";
    private static final String VIEW_ANGLE_TOP = "top";
    private static final String VIEW_ANGLE_INVALID_LEFT = "left";
    private static final String URL_1 = "url1";
    private static final String URL_2 = "url2";
    private static final String URL_3 = "url3";
    private static final String URL_4 = "url4";

    @InjectMocks
    private VendorUtil vendorUtil;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(vendorUtil, "viewAngles",
                new String[]{VIEW_ANGLE_FRONT, VIEW_ANGLE_BACK, VIEW_ANGLE_TOP});
    }

    @Test
    public void shouldReturnTrueWhenViewAngleIsSupported() {
        assertThat(vendorUtil.isSupportedViewAngle(VIEW_ANGLE_FRONT)).isTrue();
        assertThat(vendorUtil.isSupportedViewAngle(VIEW_ANGLE_BACK)).isTrue();
        assertThat(vendorUtil.isSupportedViewAngle(VIEW_ANGLE_TOP)).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenViewAngleIsNotSupported() {
        assertThat(vendorUtil.isSupportedViewAngle(VIEW_ANGLE_INVALID_LEFT)).isFalse();
        assertThat(vendorUtil.isSupportedViewAngle("right")).isFalse();
        assertThat(vendorUtil.isSupportedViewAngle("bottom")).isFalse();
    }

    @Test
    public void shouldReturnViewAngleListWithSupportedViewAngles() {
        Map<String, String> searchResults = new HashMap<>();
        searchResults.put(VIEW_ANGLE_FRONT, URL_1);
        searchResults.put(VIEW_ANGLE_BACK, URL_2);
        searchResults.put(VIEW_ANGLE_INVALID_LEFT, URL_3);
        searchResults.put(VIEW_ANGLE_TOP, URL_4);

        List<VendorSearchViewAngleResponse> viewAngles = vendorUtil.getViewAngleList(searchResults);

        assertThat(viewAngles).hasSize(3);
        assertThat(viewAngles).anyMatch(
                viewAngle -> viewAngle.getViewAngle().equals(VIEW_ANGLE_FRONT) && viewAngle.getUrl().equals(URL_1));
        assertThat(viewAngles).anyMatch(
                viewAngle -> viewAngle.getViewAngle().equals(VIEW_ANGLE_BACK) && viewAngle.getUrl().equals(URL_2));
        assertThat(viewAngles).anyMatch(
                viewAngle -> viewAngle.getViewAngle().equals(VIEW_ANGLE_TOP) && viewAngle.getUrl().equals(URL_4));
    }
}
