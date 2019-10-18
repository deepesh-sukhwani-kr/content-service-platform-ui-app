package com.kroger.csp.ui.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AddImageUIResponse implements Serializable {
	
	private String referenceId;
	private String creationDatetime;
	private List<AssetDetailsUIResponse> assetDetails;
	//TODO: Handle UI server specific exceptions
}
