package br.com.neolog.cplmobile.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.exceptionmessage.ExceptionMessages;

public class Resource<T>
{
    public enum Status
    {
        SUCCESS,
        ERROR,
        LOADING;
    }

    @NonNull
    private final Status status;

    @Nullable
    private final ExceptionMessages message;

    @Nullable
    private final T data;

    public Resource(
        @NonNull final Status status,
        @Nullable final T data,
        @Nullable final ExceptionMessages message )
    {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(
        @Nullable final T data )
    {
        return new Resource<>( Status.SUCCESS, data, null );
    }

    public static <T> Resource<T> error(
        final ExceptionMessages msg,
        @Nullable final T data )
    {
        return new Resource<>( Status.ERROR, data, msg );
    }

    public static <T> Resource<T> loading(
        @Nullable final T data )
    {
        return new Resource<>( Status.LOADING, data, null );
    }

    @NonNull
    public Status getStatus()
    {
        return status;
    }

    @Nullable
    public ExceptionMessages getMessage()
    {
        return message;
    }

    @Nullable
    public T getData()
    {
        return data;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final Resource<?> resource = (Resource<?>) o;
        return status == resource.status &&
            Objects.equal( message, resource.message ) &&
            Objects.equal( data, resource.data );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( status, message, data );
    }

    @NonNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "status", status )
            .add( "message", message )
            .add( "data", data )
            .toString();
    }
}
