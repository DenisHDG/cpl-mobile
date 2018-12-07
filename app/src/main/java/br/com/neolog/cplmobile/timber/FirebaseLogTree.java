package br.com.neolog.cplmobile.timber;

import com.crashlytics.android.Crashlytics;

import android.support.annotation.NonNull;

import timber.log.Timber;

public class FirebaseLogTree
    extends
        Timber.DebugTree
{

    @Override
    protected void log(
        final int priority,
        final String tag,
        final @NonNull String message,
        final Throwable t )
    {
        Crashlytics.log( priority, tag, message );
        if( t != null ) {
            Crashlytics.logException( t );
        }
    }
}