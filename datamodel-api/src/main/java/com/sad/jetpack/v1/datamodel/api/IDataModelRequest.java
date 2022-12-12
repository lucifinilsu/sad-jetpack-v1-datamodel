package com.sad.jetpack.v1.datamodel.api;

import java.util.Map;

public interface IDataModelRequest<B> {

    String tag();

    String url();

    B body();

    Map<String,String> headers();

    long timeout();

    Method method();

    Creator<B> toCreator();

    interface Creator<B>{

        Creator<B> url(String url);

        Creator<B> body(B body);

        Creator<B> headers(Map<String,String> headers);

        Creator<B> addHeader(String key, String value);

        Creator<B> timeout(long timeout);

        Creator<B> method(Method method);

        Creator<B> tag(String tag);

        IDataModelRequest<B> create();
    }

    enum Method{
        GET(false), POST(true), PUT(true), DELETE(false), PATCH(true), HEAD(false), OPTIONS(false), TRACE(false);

        private final boolean hasBody;

        Method(boolean hasBody) {
            this.hasBody = hasBody;
        }

        /**
         * Check if this HTTP method has/needs a request body
         * @return if body needed
         */
        public final boolean hasBody() {
            return hasBody;
        }
    }

}
