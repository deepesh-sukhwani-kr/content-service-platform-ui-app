package com.kroger.csp.ui.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorizedRolesResponse {
    private List<String> addImageAuthorizedRoles;
}
