package br.com.neolog.cplmobile.modal;

import java.util.List;

import javax.validation.constraints.NotNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.neolog.cplmobile.R;

public final class DialogFragmentModal
{
    private final AlertDialog.Builder builder;
    private final TextView titleView;
    private final TextView messageView;

    /**
     * used to get a new instance of class and access your members
     * 
     * @param context {@link Context} of application
     * @return return new instance of class
     */
    public static DialogFragmentModal getInstance(
        @NonNull final Context context )
    {
        return new DialogFragmentModal( context );
    }

    /**
     * constructor initializing variables
     * 
     * @param context {@link Context} of application
     */
    @SuppressLint( "InflateParams" )
    private DialogFragmentModal(
        final Context context )
    {
        final LayoutInflater layoutInflater = LayoutInflater.from( context );
        final View view = layoutInflater.inflate( R.layout.fragment_dialog_modal, null );
        builder = new AlertDialog.Builder( context ).setView( view ).setCancelable( false );

        titleView = view.findViewById( R.id.title_dialog_fragment_modal );
        messageView = view.findViewById( R.id.message_dialog_fragment_modal );
        messageView.setMovementMethod( new ScrollingMovementMethod() );
    }

    /**
     * used for setting text of title in screen
     *
     * @param title define text of title by any {@link CharSequence}
     * @return own class
     */
    public DialogFragmentModal setTitle(
        @NonNull final CharSequence title )
    {
        titleView.setText( title );
        return this;
    }

    /**
     * used for setting text of title in screen
     *
     * @param resId define text of title by ID of string contained in resources
     * @return own class
     */
    public DialogFragmentModal setTitle(
        @StringRes final int resId )
    {
        titleView.setText( resId );
        return this;
    }

    /**
     * used for setting text of message in screen
     *
     * @param message define text of message by any {@link CharSequence}
     * @return own class
     */
    public DialogFragmentModal setMessage(
        @NonNull final CharSequence message )
    {
        messageView.setText( message );
        return this;
    }

    /**
     * used for setting text of message in screen
     *
     * @param resId define text of message by ID of string contained in resources
     * @return own class
     */
    public DialogFragmentModal setMessage(
        @StringRes final int resId )
    {
        messageView.setText( resId );
        return this;
    }

    /**
     * used for setting text of message in screen
     *
     * @param messages define body of message by any {@link List} of
     *        {@link CharSequence}
     * @return own class
     */
    public DialogFragmentModal setMessage(
        @NonNull final List<CharSequence> messages )
    {
        final StringBuilder message = new StringBuilder();

        for( final CharSequence line : messages ) {
            message.append( line ).append( "\n" );
        }

        messageView.setText( message );
        return this;
    }

    /**
     * used for setting text of button label in screen and event in button click
     *
     * @param label define text of button label by any {@link CharSequence}
     * @return own class
     */
    public DialogFragmentModal setPositiveButton(
        @StringRes final int label )
    {
        return setPositiveButton( label, ModalCallback.EMPTY );
    }

    /**
     * used for setting text of button label in screen and event in button click
     *
     * @param label define text of button label by any {@link CharSequence}
     * @param onClickListener define execution flow in button click
     * @return own class
     */
    public DialogFragmentModal setPositiveButton(
        @StringRes final int label,
        @NotNull final ModalCallback onClickListener )
    {
        builder.setPositiveButton( label, (
            dialog,
            which ) -> {
            dialog.cancel();
            onClickListener.call();
        } );
        return this;
    }

    /**
     * used for setting text of button label in screen and event in button click
     *
     * @param resId define text of message by ID of string contained in resources
     * @return own class
     */
    public DialogFragmentModal setNegativeButton(
        @StringRes final int resId )
    {
        return setNegativeButton( resId, ModalCallback.EMPTY );
    }

    /**
     * used for setting text of button label in screen and event in button click
     *
     * @param resId define text of message by ID of string contained in resources
     * @param onClickListener define execution flow in button click
     * @return own class
     */
    public DialogFragmentModal setNegativeButton(
        @StringRes final int resId,
        @NotNull final ModalCallback onClickListener )
    {
        builder.setNegativeButton( resId, (
            dialog,
            which ) -> {
            dialog.cancel();
            onClickListener.call();
        } );
        return this;
    }

    /**
     * show {@link AlertDialog} custom in screen
     */
    public void show()
    {
        builder.create().show();
    }
}