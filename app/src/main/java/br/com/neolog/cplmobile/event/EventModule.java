package br.com.neolog.cplmobile.event;

import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class EventModule
{
    @Binds
    @IntoMap
    @ViewModelKey( EventViewModel.class )
    abstract ViewModel bindEventViewModel(
            final EventViewModel monitorableViewModel );
}
