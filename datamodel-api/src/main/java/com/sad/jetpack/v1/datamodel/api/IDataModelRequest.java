package com.sad.jetpack.v1.datamodel.api;

import java.util.Map;

public interface IDataModelRequest {

    String tag();

    String url();

    <B> B body();

    Map<String,String> headers();

    long timeout();

    Method method();

    Creator toCreator();

    interface Creator{

        Creator url(String url);

        Creator body(Object body);

        Creator headers(Map<String,String> headers);

        Creator addHeader(String key, String value);

        Creator timeout(long timeout);

        Creator method(Method method);

        Creator tag(String tag);

        IDataModelRequest create();
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
