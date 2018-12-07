package br.com.neolog.cplmobile.di;

import java.util.Locale;

import javax.inject.Singleton;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule
{
    @Singleton
    @Provides
    Context applicationContext(
        final Application app )
    {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    @SuppressWarnings( "deprecation" )
    Locale applicationLocale(
        final Context context )
    {
        final Configuration configuration = context.getResources().getConfiguration();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
            return configuration.getLocales().get( 0 );
        }
        return configuration.locale;
    }
}
