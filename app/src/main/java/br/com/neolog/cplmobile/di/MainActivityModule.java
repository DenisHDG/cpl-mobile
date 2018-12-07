package br.com.neolog.cplmobile.di;

import br.com.neolog.cplmobile.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class MainActivityModule
{
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}