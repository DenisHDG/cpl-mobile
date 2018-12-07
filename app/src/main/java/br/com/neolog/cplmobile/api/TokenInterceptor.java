package br.com.neolog.cplmobile.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor
        implements
        Interceptor {

    final private static String TOKEN = "cb2a3eb5d69a415ca912cecdcace5f9a";

    @Override
    public Response intercept(
            @NonNull final Interceptor.Chain chain)
            throws IOException {
        final Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.addHeader("user.token", TokenInterceptor.TOKEN);
        return chain.proceed(requestBuilder.build());
    }
}
