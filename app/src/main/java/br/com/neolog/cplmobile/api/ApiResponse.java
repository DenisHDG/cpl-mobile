package br.com.neolog.cplmobile.api;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import br.com.neolog.exceptionmessage.ExceptionMessages;

public class ApiResponse<T>
{
    private final int code;
    @Nullable
    private final T body;
    @Nullable
    private final ExceptionMessages errorMessage;

    ApiResponse(
        final int code,
        @Nullable final T body,
        @Nullable final ExceptionMessages errorMessage )
    {
        this.code = code;
        this.body = body;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful()
    {
        return code >= 200 && code < 300;
    }

    public int getCode()
    {
        return code;
    }

    @Nullable
    public T getBody()
    {
        return body;
    }

    @Nullable
    public ExceptionMessages getErrorMessage()
    {
        return errorMessage;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final ApiResponse<?> that = (ApiResponse<?>) o;
        return code == that.code &&
            Objects.equal( body, that.body ) &&
            Objects.equal( errorMessage, that.errorMessage );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( code, body, errorMessage );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .omitNullValues()
            .add( "code", code )
            .add( "body", body )
            .add( "errorMessage", errorMessage )
            .toString();
    }
}
