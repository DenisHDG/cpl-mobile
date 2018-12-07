package br.com.neolog.cplmobile.api;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Provider;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RetrofitConverters
{
    private final Provider<Retrofit> retrofitProvider;

    @Inject
    RetrofitConverters(
        final Provider<Retrofit> retrofitProvider )
    {
        this.retrofitProvider = retrofitProvider;
    }

    public <T> Converter<ResponseBody,T> responseBodyConverter(
        final Class<T> clazz )
    {
        return retrofitProvider.get().responseBodyConverter( clazz, new Annotation[ 0 ] );
    }
}
