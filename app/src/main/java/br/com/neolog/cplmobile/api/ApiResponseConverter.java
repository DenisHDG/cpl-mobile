package br.com.neolog.cplmobile.api;

import java.util.Collections;

import javax.inject.Inject;

import br.com.neolog.cplmobile.exception.ExceptionMessageExtractor;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import retrofit2.Response;

class ApiResponseConverter
{
    private final ExceptionMessageExtractor exceptionMessageExtractor;

    @Inject
    ApiResponseConverter(
        final ExceptionMessageExtractor exceptionMessageExtractor )
    {
        this.exceptionMessageExtractor = exceptionMessageExtractor;
    }

    <R> ApiResponse<R> convert(
        final Response<R> response )
    {
        final int code = response.code();
        if( response.isSuccessful() ) {
            return new ApiResponse<>( code, response.body(), null );
        }
        return new ApiResponse<>( code, null, parseErrorMessage( response ) );
    }

    private <R> ExceptionMessages parseErrorMessage(
        final Response<R> response )
    {
        return exceptionMessageExtractor.convertErrorBody( response );
    }

    <R> ApiResponse<R> convert(
        final Throwable error )
    {
        return new ApiResponse<>( 500, null, ExceptionMessages.from( error.getMessage(), Collections.emptyList() ) );
    }
}
