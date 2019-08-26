package com.kroger.csp.ui.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddImageUIResponse implements Serializable {
	
	private String referenceId;
	private String creationDatetime;
	private List<AssetDetailsUIResponse> assetDetails;
}
