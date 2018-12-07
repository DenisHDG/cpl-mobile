package br.com.neolog.cplmobile.exception;

import android.app.Activity;

import timber.log.Timber;

public class ExceptionHandler
    implements
        Thread.UncaughtExceptionHandler
{
    private final Activity activity;

    public ExceptionHandler(final Activity activity){
        this.activity = activity;
    }

    public void uncaughtException(
        final Thread thread,
        final Throwable exception )
    {
        Timber.e( exception );
        activity.finish();
    }
}