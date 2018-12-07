package br.com.neolog.cplmobile.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.android.AndroidInjection;
import dagger.android.support.HasSupportFragmentInjector;

class AutoWireActivityLifecycleCallbacks
    implements
        Application.ActivityLifecycleCallbacks
{
    @Override
    public void onActivityCreated(
        final Activity activity,
        final Bundle savedInstanceState )
    {
        handleActivity( activity );
    }

    private static void handleActivity(
        final Activity activity )
    {
        if( activity instanceof Injectable || activity instanceof HasSupportFragmentInjector ) {
            AndroidInjection.inject( activity );
        }
        if( activity instanceof FragmentActivity ) {
            final FragmentManager supportFragmentManager = ( (FragmentActivity) activity ).getSupportFragmentManager();
            supportFragmentManager.registerFragmentLifecycleCallbacks( new AutoWireFragmentLifecycleCallbacks(), true );
        }
    }

    @Override
    public void onActivityStarted(
        final Activity activity )
    {

    }

    @Override
    public void onActivityResumed(
        final Activity activity )
    {

    }

    @Override
    public void onActivityPaused(
        final Activity activity )
    {

    }

    @Override
    public void onActivityStopped(
        final Activity activity )
    {

    }

    @Override
    public void onActivitySaveInstanceState(
        final Activity activity,
        final Bundle outState )
    {

    }

    @Override
    public void onActivityDestroyed(
        final Activity activity )
    {

    }

}
