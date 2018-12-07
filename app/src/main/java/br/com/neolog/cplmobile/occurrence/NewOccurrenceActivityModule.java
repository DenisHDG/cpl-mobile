package br.com.neolog.cplmobile.occurrence;

import br.com.neolog.cplmobile.occurrence.NewOccurrenceActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NewOccurrenceActivityModule {
    @ContributesAndroidInjector
    abstract NewOccurrenceActivity contributeNewOccurrenceActivity();
}
