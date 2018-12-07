package br.com.neolog.cplmobile.occurrence.selection;

import br.com.neolog.cplmobile.occurrence.selection.OccurrenceCauseSelectionActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OccurrenceCauseSelectionActivityModule
{
    @ContributesAndroidInjector
    abstract OccurrenceCauseSelectionActivity contributeOccurrenceCauseSelectionActivity();
}
