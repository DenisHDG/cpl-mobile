package br.com.neolog.cplmobile.monitorable.converter;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import android.content.Context;

import br.com.neolog.cplmobile.R;
import br.com.neolog.monitoring.monitorable.model.api.CompletionStatus;

public class CompletionStatusConverter
{
    private final Context context;

    @Inject
    CompletionStatusConverter(
        final Context context )
    {
        this.context = context;
    }

    public String convertToString(
        final CompletionStatus status )
    {
        switch( checkNotNull( status ) ) {
            case CREATED:
                return getString( R.string.completion_status_created );
            case IN_EXECUTION:
                return getString( R.string.completion_status_in_execution );
            case FINISHED:
                return getString( R.string.completion_status_finished );
            case CANCELLED:
                return getString( R.string.completion_status_cancelled );
            default:
                throw new IllegalStateException( "CompletionStatus not found: " + status );
        }
    }

    private String getString(
        final int resource )
    {
        return context.getString( resource );
    }
}
