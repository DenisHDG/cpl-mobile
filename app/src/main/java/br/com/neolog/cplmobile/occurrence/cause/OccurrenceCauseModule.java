package br.com.neolog.cplmobile.occurrence.cause;

import javax.inject.Singleton;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class OccurrenceCauseModule
{
    @Singleton
    @Provides
    OccurrenceCauseApi occurrenceCauseApi(
        final Retrofit retrofit )
    {
        return retrofit.create( OccurrenceCauseApi.class );
    }

    @Singleton
    @Provides
    OccurrenceCauseDao occurrenceCauseDao(
        final AppDatabase database )
    {
        return database.getOccurrenceCauseDao();
    }

    @Singleton
    @Provides
    AllowedMonitorableTypeDao allowedMonitorableTypeDao(
        final AppDatabase database )
    {
        return database.getAllowedMonitorableTypeDao();
    }
}
