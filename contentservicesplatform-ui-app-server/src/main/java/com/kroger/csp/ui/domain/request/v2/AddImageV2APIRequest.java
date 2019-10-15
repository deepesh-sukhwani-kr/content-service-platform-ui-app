package com.kroger.csp.ui.domain.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kroger.imp.library.domain.TransactionRef;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class AddImageV2APIRequest {

    public TransactionRef transactionRef;

    public Asset addAssetRequest;

}
