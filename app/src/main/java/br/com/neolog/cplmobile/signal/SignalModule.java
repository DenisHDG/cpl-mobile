package br.com.neolog.cplmobile.signal;

import javax.inject.Singleton;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SignalModule
{

    @Singleton
    @Provides
    public SignalApi signalApi(
        final Retrofit retrofit )
    {
        return retrofit.create( SignalApi.class );
    }

    @Singleton
    @Provides
    public SignalDao signalDao(
        final AppDatabase database )
    {
        return database.getSignalDao();
    }

}