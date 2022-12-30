package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpEngineForStringByStringBody extends OkhttpEngineForString {
    @Override
    public void onRestOkhttpRequest(IDataModelRequest request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== IDataModelRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body().toString(), MediaType.parse("application/json;charset=utf-8")));
        }
    }

    @Override
    public void onResetOkhttpClient(IDataModelRequest request, OkHttpClient.Builder builder) {
        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(new RetryInterceptor(2));
    }

    class RetryInterceptor implements Interceptor{
        // 最大重试次数
        private int maxRetry =2;
        public RetryInterceptor(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            /* 递归 2次下发请求，如果仍然失败 则返回 null ,但是 intercept must not return null.
             * 返回 null 会报 IllegalStateException 异常
             * */
            return retry(chain, 0);//这个递归真的很舒服
        }

        Response retry(Chain chain, int retryCent) throws IOException{
            Request request = chain.request();
            Response response = null;
            try {
//                System.out.println("第" + (retryCent + 1) + "次执行发http请求.");
                response = chain.proceed(request);
            } catch (IOException e) {
                if (maxRetry > retryCent) {
                    return retry(chain, retryCent + 1);
                }
                else {
                    throw e;
                }
            } finally {
                return response;
            }
        }
    }
}
