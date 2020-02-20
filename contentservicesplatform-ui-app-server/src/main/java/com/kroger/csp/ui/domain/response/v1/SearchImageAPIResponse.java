package com.kroger.csp.ui.domain.response.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.kroger.imp.exception.ErrorDetails;
import com.kroger.imp.library.domain.ImpResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchImageAPIResponse implements Serializable {

	private ImpResponseHeader responseHeader;
	private SearchResponseBody body;

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SearchResponseBody{

		private ArrayList<SearchAsset> imageList;

		private ErrorDetails errorDetails;

	}
	
}
