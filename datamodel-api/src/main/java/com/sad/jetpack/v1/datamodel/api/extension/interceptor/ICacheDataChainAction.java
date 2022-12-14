package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;

public interface ICacheDataChainAction<RP> {

    boolean onReProceedDataOrgSourceAction(IDataModelChainInput<RP> chainInput);

}
