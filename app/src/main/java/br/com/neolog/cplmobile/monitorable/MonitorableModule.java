package br.com.neolog.cplmobile.monitorable;

import javax.inject.Singleton;

import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.di.ViewModelKey;
import br.com.neolog.cplmobile.monitorable.api.MonitorableApi;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableFinishDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorablePropertyDao;
import br.com.neolog.cplmobile.persistence.AppDatabase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;
import retrofit2.Retrofit;

@Module
public abstract class MonitorableModule
{
    @Binds
    @IntoMap
    @ViewModelKey( MonitorableViewModel.class )
    abstract ViewModel bindMonitorableViewModel(
        final MonitorableViewModel monitorableViewModel );

    @Binds
    @IntoMap
    @ViewModelKey( MonitorableFragmentViewModel.class )
    abstract ViewModel bindMonitorableFragmentViewModel(
        final MonitorableFragmentViewModel monitorableViewModel );

    @ContributesAndroidInjector
    abstract MonitorableFragment contributeMonitorableFragment();

    @Singleton
    @Provides
    static MonitorableApi monitorableApi(
        final Retrofit retrofit )
    {
        return retrofit.create( MonitorableApi.class );
    }

    @Singleton
    @Provides
    static MonitorableDao monitorableDao(
        final AppDatabase appDatabase )
    {
        return appDatabase.getMonitorableDao();
    }

    @Singleton
    @Provides
    static MonitorableFinishDao monitorableFinishDao(
        final AppDatabase appDatabase )
    {
        return appDatabase.getMonitorableFinishDao();
    }

    @Singleton
    @Provides
    static MonitorablePropertyDao monitorablePropertyDao(
        final AppDatabase appDatabase )
    {
        return appDatabase.getMonitorablePropertyDao();
    }
}
