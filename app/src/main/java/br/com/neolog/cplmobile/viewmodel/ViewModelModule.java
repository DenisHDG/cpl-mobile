package br.com.neolog.cplmobile.viewmodel;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule
{
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(
        final ViewModelFactory factory );
}
