package br.com.neolog.cplmobile.event;

import br.com.neolog.cplmobile.event.HistoricActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HistoricActivityModule
{
    @ContributesAndroidInjector
    abstract HistoricActivity contributeHistoricActivity();
}
