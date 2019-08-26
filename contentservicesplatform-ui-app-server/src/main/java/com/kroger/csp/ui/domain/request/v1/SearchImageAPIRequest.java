package com.kroger.csp.ui.domain.request.v1;

import com.kroger.imp.library.domain.TransactionRef;
import com.kroger.imp.search.domain.SearchFilter;
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
public class SearchImageAPIRequest implements Serializable{

	private TransactionRef transactionRef;

	private ArrayList<SearchFilter> searchFilter;
}
