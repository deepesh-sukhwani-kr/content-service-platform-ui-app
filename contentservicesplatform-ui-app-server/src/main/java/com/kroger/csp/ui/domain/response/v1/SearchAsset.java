package com.kroger.csp.ui.domain.response.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchAsset implements Serializable {
	private String gtin;
	private String itemType;
	private String encodedURL;
	private String approvalStatus;
	private HashMap<String, String> attributeMap;
	}
