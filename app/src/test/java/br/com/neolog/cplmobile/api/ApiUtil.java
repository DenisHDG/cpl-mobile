package br.com.neolog.cplmobile.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.neolog.exceptionmessage.ExceptionMessages;

public class ApiUtil
{
    public static <T> LiveData<ApiResponse<T>> success(
        final T data )
    {
        return createCall( new ApiResponse<>( 200, data, null ) );
    }

    public static <T> LiveData<ApiResponse<T>> fail(
        final ExceptionMessages errors )
    {
        return createCall( new ApiResponse<>( 500, null, errors ) );
    }

    public static <T> LiveData<ApiResponse<T>> createCall(
        final ApiResponse<T> response )
    {
        final MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue( response );
        return data;
    }
}
