package br.com.neolog.cplmobile.exception;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.modal.ModalFactory;
import br.com.neolog.exceptionmessage.ExceptionMessage;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RemoteMessageHandler
{
    private final ExceptionMessageExtractor exceptionMessageExtractor;

    @Inject
    RemoteMessageHandler(
        final ExceptionMessageExtractor exceptionMessageExtractor )
    {
        this.exceptionMessageExtractor = exceptionMessageExtractor;
    }

    public interface OnSuccessCallback<T>
    {
        void onSuccess(
            T t );
    }

    public <T> void handleErrors(
        final Call<T> call,
        final OnSuccessCallback<T> callback,
        final Context context )
    {
        call.enqueue( new Callback<T>() {
            @Override
            public void onResponse(
                @NonNull final Call<T> call,
                @NonNull final Response<T> response )
            {
                if( response.isSuccessful() ) {
                    callback.onSuccess( response.body() );
                } else {
                    showModal( response, context );
                }
            }

            @Override
            public void onFailure(
                @NonNull final Call<T> call,
                @NonNull final Throwable t )
            {
                showModal( t, context );
            }
        } );
    }

    public void handleRemoteMessage(
        final Call<RestRemoteMessage> remoteMessageCall,
        final OnSuccessCallback<RestRemoteMessage> callback,
        final Context context )
    {
        handleErrors( remoteMessageCall, message -> {
            showModal( Collections.singletonList( message ), context );
            callback.onSuccess( message );
        }, context );
    }

    public void handleRemoteMessages(
        final Call<List<RestRemoteMessage>> remoteMessageCall,
        final OnSuccessCallback<List<RestRemoteMessage>> callback,
        final Context context )
    {
        handleErrors( remoteMessageCall, messages -> {
            showModal( messages, context );
            callback.onSuccess( messages );
        }, context );
    }

    public void showModal(
        @NonNull final List<RestRemoteMessage> messages,
        final Context context )
    {
        final StringBuilder createMessage = new StringBuilder();
        int success = 0;
        int error = 0;

        for( final RestRemoteMessage recoveryMessage : messages ) {
            if( recoveryMessage.getResult() ) {
                success++;
                continue;
            }

            error++;
        }

        if( success != 0 ) {
            if( success == 1 ) {
                createMessage.append(success).append(" ").append(context.getResources().getString(R.string.operation_success)).append("\n");
            } else {
                createMessage.append(success).append(" ").append(context.getResources().getString(R.string.operations_success)).append("\n");
            }
        }

        if( error != 0 ) {
            if( error == 1 ) {
                createMessage.append(error).append(" ").append(context.getResources().getString(R.string.operation_error)).append("\n");
            } else {
                createMessage.append(error).append(" ").append(context.getResources().getString(R.string.operations_error)).append("\n");
            }
        }

        ModalFactory.with( context ).warning( createMessage.toString() ).show();
    }

    public void showModal(
        @NonNull final ExceptionMessages exceptionMessages,
        final Context context )
    {
        final StringBuilder stringMessage = new StringBuilder();
        for( final ExceptionMessage message : exceptionMessages.getMessages() ) {
            stringMessage.append( message.getKeyBundle() );
        }

        ModalFactory.with( context ).error( stringMessage.toString() ).show();
    }

    public void showModal(
        @NonNull final Throwable t,
        final Context context )
    {
        Timber.e( t );
        showModal( R.string.remote_exception_handler_internal_server_error, context );
    }

    public void showModal(
        @NonNull final Response<?> response,
        final Context context )
    {
        final HttpUrl url = response.raw().request().url();
        final int code = response.code();
        Timber.i( "Http status %d on %s", code, url );
        final ExceptionMessages messages = exceptionMessageExtractor.convertErrorBody( response );
        showModal( messages, context );
    }

    public void showModal(
        final int resId,
        final Context context )
    {
        ModalFactory.with( context ).error( resId ).show();
    }
}