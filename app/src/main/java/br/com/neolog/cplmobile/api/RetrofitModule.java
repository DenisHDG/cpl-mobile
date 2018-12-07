package br.com.neolog.cplmobile.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

@Module(includes = ObjectMapperModule.class)
public class RetrofitModule {

    private static final String BASE_URL = "http://denisgoncalves.neolog.com.br:8080/cockpit-gateway/";

    @Singleton
    @Provides
    public Retrofit retrofit(
            final ObjectMapper objectMapper,
            final LiveDataCallAdapterFactory callAdapterFactory,
            final HttpLoggingInterceptor loggingInterceptor) {
        final TokenInterceptor tokenInterceptor = new TokenInterceptor();
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(callAdapterFactory)
                .build();
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }
}
