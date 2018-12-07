package br.com.neolog.cplmobile.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import dagger.android.support.AndroidSupportInjection;

class AutoWireFragmentLifecycleCallbacks
    extends
        FragmentManager.FragmentLifecycleCallbacks
{
    @Override
    public void onFragmentCreated(
        @NonNull final FragmentManager fm,
        @NonNull final Fragment f,
        final Bundle savedInstanceState )
    {
        if( f instanceof Injectable ) {
            AndroidSupportInjection.inject( f );
        }
    }
}
