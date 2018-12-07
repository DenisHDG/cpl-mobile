package br.com.neolog.cplmobile.occurrence;

import javax.inject.Singleton;

import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.di.ViewModelKey;
import br.com.neolog.cplmobile.occurrence.selection.OccurrenceCauseSelectionViewModel;
import br.com.neolog.cplmobile.persistence.AppDatabase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import retrofit2.Retrofit;

@Module
public abstract class OccurrenceModule
{
    @Binds
    @IntoMap
    @ViewModelKey( OccurrenceCauseSelectionViewModel.class )
    abstract ViewModel bindOccurrenceCauseSelectionViewModel(
        final OccurrenceCauseSelectionViewModel occurrenceCauseSelectionViewModel );

    @Singleton
    @Provides
    static OccurrenceApi occurrenceApi(
        final Retrofit retrofit )
    {
        return retrofit.create( OccurrenceApi.class );
    }

    @Singleton
    @Provides
    static OccurrenceDao occurrenceDao(
        final AppDatabase database )
    {
        return database.getOccurrenceDao();
    }
}
