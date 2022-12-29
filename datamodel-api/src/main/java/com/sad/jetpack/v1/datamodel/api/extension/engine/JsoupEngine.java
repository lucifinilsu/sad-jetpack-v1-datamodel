package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelProductEngine;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class JsoupEngine<RP> implements IDataModelProductEngine<RP> {
    @Override
    public void onEngineExecute(IDataModelRequest request, IDataModelChainOutput<RP> chainOutput) throws Exception {
        SADTaskSchedulerClient.newInstance()
                .execute(new SADTaskRunnable<Connection.Response>("xxxx", new ISADTaskProccessListener<Connection.Response>() {
                    @Override
                    public void onSuccess(Connection.Response jsoupResponse) {
                        //Log.e("sad-jetpack-v1","---------------->请求完毕");

                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        if (chainOutput.exceptionListener()!=null){
                            chainOutput.exceptionListener().onDataObtainedException(request,throwable);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                }) {
                    @Override
                    public Connection.Response doInBackground() throws Exception {
                        Connection connection= Jsoup.connect(request.url())
                                .method(methodTransform(request.method()))
                                .timeout((int) request.timeout())
                                .followRedirects(true)
                                ;
                        ;
                        onResetJsoupConnection(request,connection);
                        Connection.Response response=connection.execute();
                        onHandleJsoupResponse(request,response,chainOutput);
                        return response;
                    }
                });
    }

    public abstract void onHandleJsoupResponse(IDataModelRequest request, Connection.Response jsoupResponse, IDataModelChainOutput<RP> chainOutput);
    public void onResetJsoupConnection(IDataModelRequest request, Connection connection) {
    }

    public Connection.Method methodTransform(IDataModelRequest.Method method){
        if (method == IDataModelRequest.Method.GET){
            return Connection.Method.GET;
        }
        else if (method == IDataModelRequest.Method.POST){
            return Connection.Method.POST;
        }
        else if (method == IDataModelRequest.Method.HEAD){
            return Connection.Method.HEAD;
        }
        return Connection.Method.GET;
    }
}
