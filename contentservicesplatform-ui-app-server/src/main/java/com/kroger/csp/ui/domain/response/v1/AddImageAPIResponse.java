package com.kroger.csp.ui.domain.response.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.kroger.imp.assetmanagement.domain.ResponsePayload;
import com.kroger.imp.library.domain.ImpResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddImageAPIResponse implements Serializable {

	private ImpResponseHeader header;
	private AddImageResponseBody body;

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AddImageResponseBody{

		private LinkedList<ResponsePayload> responsePayload;

	}
	
}
