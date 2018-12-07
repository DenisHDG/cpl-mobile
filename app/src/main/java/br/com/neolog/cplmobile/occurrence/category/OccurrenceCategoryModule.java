package br.com.neolog.cplmobile.occurrence.category;

import javax.inject.Singleton;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class OccurrenceCategoryModule
{

    @Singleton
    @Provides
    OccurrenceCategoryDao occurrenceCategoryDao(
        final AppDatabase database )
    {
        return database.getOccurrenceCategoryDao();
    }
}
