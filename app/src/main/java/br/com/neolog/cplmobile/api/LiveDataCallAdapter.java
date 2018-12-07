package br.com.neolog.cplmobile.api;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R>
    implements
        CallAdapter<R,LiveData<ApiResponse<R>>>
{
    private final ApiResponseConverter apiResponseConverter;
    private final Type responseType;

    LiveDataCallAdapter(
        final ApiResponseConverter apiResponseConverter,
        final Type responseType )
    {
        this.apiResponseConverter = apiResponseConverter;
        this.responseType = responseType;
    }

    @Override
    public Type responseType()
    {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(
        @NonNull final Call<R> call )
    {
        return new LiveData<ApiResponse<R>>() {
            private final AtomicBoolean started = new AtomicBoolean( false );

            @Override
            protected void onActive()
            {
                super.onActive();
                if( started.compareAndSet( false, true ) ) {
                    call.enqueue( new Callback<R>() {
                        @Override
                        public void onResponse(
                            @NonNull final Call<R> call,
                            @NonNull final Response<R> response )
                        {
                            postValue( apiResponseConverter.convert( response ) );
                        }

                        @Override
                        public void onFailure(
                            @NonNull final Call<R> call,
                            @NonNull final Throwable throwable )
                        {
                            postValue( apiResponseConverter.convert( throwable ) );
                        }
                    } );
                }
            }
        };
    }
}
