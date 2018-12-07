package br.com.neolog.cplmobile.exception;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Inject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.api.RetrofitConverters;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import timber.log.Timber;

public class ExceptionMessageExtractor
{
    @VisibleForTesting
    static final int BAD_REQUEST = 400;
    @VisibleForTesting
    static final int UNAUTHORIZED = 401;
    @VisibleForTesting
    static final int FORBIDDEN = 403;
    @VisibleForTesting
    static final int NOT_FOUND = 404;
    @VisibleForTesting
    static final int TIMEOUT = 408;

    private final Context context;
    private final RetrofitConverters retrofitConverters;

    @Inject
    ExceptionMessageExtractor(
        final Context context,
        final RetrofitConverters retrofitConverters )
    {
        this.context = context;
        this.retrofitConverters = retrofitConverters;
    }

    public <T> ExceptionMessages convertErrorBody(
        final Response<T> response )
    {
        switch( response.code() ) {
            case BAD_REQUEST:
                return convertFromBody( response );
            case UNAUTHORIZED:
                return message( R.string.remote_exception_handler_unauthorized );
            case FORBIDDEN:
                return message( R.string.remote_exception_handler_forbbiden );
            case NOT_FOUND:
                return message( R.string.remote_exception_handler_not_found );
            case TIMEOUT:
                return message( R.string.remote_exception_handler_timeout );
            default:
                return internalServerError();
        }
    }

    private <T> ExceptionMessages convertFromBody(
        final Response<T> response )
    {
        final Converter<ResponseBody,ExceptionMessages> converter = getConverter();
        final ResponseBody responseBody = response.errorBody();

        if( responseBody == null ) {
            return ExceptionMessages.from( response.message(), Collections.emptyList() );
        }

        try {
            return converter.convert( responseBody );
        } catch( final IOException e ) {
            Timber.w( e, "failed to convert response %s", responseBody );
            return internalServerError();
        }
    }

    @NonNull
    private ExceptionMessages internalServerError()
    {
        return message( R.string.remote_exception_handler_internal_server_error );
    }

    @NonNull
    private ExceptionMessages message(
        @StringRes final int resourceId )
    {
        return ExceptionMessages.from( context.getString( resourceId ), Collections.emptyList() );
    }

    private Converter<ResponseBody,ExceptionMessages> getConverter()
    {
        return retrofitConverters.responseBodyConverter( ExceptionMessages.class );
    }

}
