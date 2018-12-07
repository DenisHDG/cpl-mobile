package br.com.neolog.cplmobile.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

class LiveDataCallAdapterFactory
    extends
        CallAdapter.Factory
{
    private final ApiResponseConverter apiResponseConverter;

    @Inject
    LiveDataCallAdapterFactory(
        final ApiResponseConverter apiResponseConverter )
    {
        this.apiResponseConverter = apiResponseConverter;
    }

    @Override
    public CallAdapter<?,?> get(
        @NonNull final Type returnType,
        @NonNull final Annotation[] annotations,
        @NonNull final Retrofit retrofit )
    {
        if( getRawType( returnType ) != LiveData.class ) {
            return null;
        }
        final Type observableType = getParameterUpperBound( 0, (ParameterizedType) returnType );
        final Class<?> rawObservableType = getRawType( observableType );
        if( rawObservableType != ApiResponse.class ) {
            throw new IllegalArgumentException( "type must be a resource" );
        }
        if( ! ( observableType instanceof ParameterizedType ) ) {
            throw new IllegalArgumentException( "resource must be parameterized" );
        }
        final Type bodyType = getParameterUpperBound( 0, (ParameterizedType) observableType );
        return new LiveDataCallAdapter<>( apiResponseConverter, bodyType );
    }
}
