package br.com.neolog.cplmobile.di;

import br.com.neolog.cplmobile.CPLApplication;

public class AppInjector
{
    private AppInjector()
    {
        throw new AssertionError( "can't be instantiated" );
    }

    public static AppComponent init(
        final CPLApplication cplApplication )
    {
        final AppComponent component = DaggerAppComponent.builder().application( cplApplication ).build();
        component.inject( cplApplication );
        cplApplication.registerActivityLifecycleCallbacks( new AutoWireActivityLifecycleCallbacks() );
        return component;
    }
}
