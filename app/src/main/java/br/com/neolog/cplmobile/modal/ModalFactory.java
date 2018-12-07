package br.com.neolog.cplmobile.modal;

import javax.validation.constraints.NotNull;

import android.content.Context;
import android.support.annotation.StringRes;

import br.com.neolog.cplmobile.R;

public class ModalFactory
{
    private final Context context;

    private ModalFactory(
        final Context context )
    {
        this.context = context;
    }

    public static ModalFactory with(
        final Context context )
    {
        return new ModalFactory( context );
    }

    public DialogFragmentModal success(
        final String message )
    {
        return DialogFragmentModal.getInstance( context )
            .setTitle( R.string.occurrece_save_success_title )
            .setMessage( message )
            .setPositiveButton( android.R.string.ok );
    }

    public DialogFragmentModal success(
        @StringRes final int message )
    {
        return success( context.getString( message ) );
    }

    public DialogFragmentModal warning(
        final String message )
    {
        return DialogFragmentModal.getInstance( context )
            .setTitle( R.string.warning )
            .setMessage( message )
            .setPositiveButton( android.R.string.ok );
    }

    public DialogFragmentModal error(
        final String message )
    {
        return DialogFragmentModal.getInstance( context )
            .setTitle( R.string.error )
            .setMessage( message )
            .setPositiveButton( android.R.string.ok );
    }

    public DialogFragmentModal error(
        @StringRes final int resId )
    {
        return error( context.getString( resId ) );
    }

    public DialogFragmentModal confirm(
        @StringRes final int message,
        @NotNull final ModalCallback onAccepted )
    {
        return confirm( context.getString( message ), onAccepted );
    }

    public DialogFragmentModal confirm(
        final String message,
        @NotNull final ModalCallback onAccepted )
    {
        return DialogFragmentModal.getInstance( context )
            .setTitle( R.string.confirmation )
            .setMessage( message )
            .setPositiveButton( R.string.yes, onAccepted )
            .setNegativeButton( R.string.no, ModalCallback.EMPTY );
    }
}
