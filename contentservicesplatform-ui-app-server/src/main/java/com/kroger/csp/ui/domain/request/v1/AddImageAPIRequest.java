package com.kroger.csp.ui.domain.request.v1;

import com.kroger.imp.assetmanagement.domain.Asset;
import com.kroger.imp.library.domain.TransactionRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddImageAPIRequest implements Serializable{

	private TransactionRef transactionRef;
	
	private Asset asset;
}
