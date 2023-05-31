package com.kroger.csp.ui.controller;

import com.kroger.csp.ui.config.RBACConfiguration;
import com.kroger.csp.ui.domain.response.RBACResponse;
import com.kroger.csp.ui.domain.response.ViewAngleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class UtilControllerTest {

    @Mock
    private RBACConfiguration rbacConfiguration;

    @InjectMocks
    private UtilController utilController;

    @BeforeEach
    public void setUp() {
        mockViewAngles();
        mockServerEndpoints();
    }

    private void mockViewAngles() {
        setField(utilController, "viewAngles", new String[]{"Front", "Back", "Side"});
    }

    private void mockServerEndpoints() {
        setField(utilController, "add", "addEndpoint");
        setField(utilController, "search", "searchEndpoint");
        setField(utilController, "vendorSearch", "vendorSearchEndpoint");
        setField(utilController, "retrieval", "retrievalEndpoint");
    }

    @Test
    public void shouldReturnViewAnglesWhenGetViewAnglesCalled() {
        ViewAngleResponse response = utilController.getViewAngles();
        assertThat(response.getViewAngles()).containsExactly("Front", "Back", "Side");
    }

    @Test
    public void shouldReturnEndpointsWhenGetEndpointsCalled() {
        Map<String, String> endpoints = utilController.getEndpoints();
        assertThat(endpoints).containsEntry("add", "addEndpoint").containsEntry("search", "searchEndpoint")
                .containsEntry("vendorSearch", "vendorSearchEndpoint").containsEntry("retrieval", "retrievalEndpoint");
    }

    @Test
    public void shouldReturnRBACResponseWhenGetAddImageAuthorizedRolesCalled() {
        List<String> addRoles = asList("role1", "role2");
        boolean isCheckRbac = true;
        List<String> externalSources = asList("source1", "source2");
        List<String> krogerExternalRoles = asList("externalRole1", "externalRole2");
        List<String> searchRoles = asList("searchRole1", "searchRole2");
        List<String> vendorRoles = asList("vendorRole1", "vendorRole2");

        RBACResponse rbacResponse = new RBACResponse();
        rbacResponse.setAddRoles(addRoles);
        rbacResponse.setCheckRbac(isCheckRbac);
        rbacResponse.setExternalSources(externalSources);
        rbacResponse.setKrogerExternalRoles(krogerExternalRoles);
        rbacResponse.setSearchRoles(searchRoles);
        rbacResponse.setVendorAddRoles(vendorRoles);

        when(rbacConfiguration.getAddRoles()).thenReturn(addRoles);
        when(rbacConfiguration.isCheckRbac()).thenReturn(isCheckRbac);
        when(rbacConfiguration.getExternalSources()).thenReturn(externalSources);
        when(rbacConfiguration.getKrogerExternalRoles()).thenReturn(krogerExternalRoles);
        when(rbacConfiguration.getSearchRoles()).thenReturn(searchRoles);
        when(rbacConfiguration.getVendorAddRoles()).thenReturn(vendorRoles);

        RBACResponse response = utilController.getAddImageAuthorizedRoles();

        assertThat(response).isEqualToComparingFieldByField(rbacResponse);
    }

}
