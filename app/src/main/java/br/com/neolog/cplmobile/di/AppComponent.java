package br.com.neolog.cplmobile.di;

import java.util.Locale;

import javax.inject.Singleton;

import android.app.Application;

import br.com.neolog.cplmobile.CPLApplication;
import br.com.neolog.cplmobile.api.RetrofitModule;
import br.com.neolog.cplmobile.event.EventModule;
import br.com.neolog.cplmobile.event.HistoricActivityModule;
import br.com.neolog.cplmobile.exception.ExceptionModule;
import br.com.neolog.cplmobile.monitorable.MonitorableModule;
import br.com.neolog.cplmobile.monitorable.job.ResendMonitorableFinishAsyncTask;
import br.com.neolog.cplmobile.occurrence.NewOccurrenceActivityModule;
import br.com.neolog.cplmobile.occurrence.OccurrenceModule;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryModule;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseModule;
import br.com.neolog.cplmobile.occurrence.job.ResendOccurrenceAsyncTask;
import br.com.neolog.cplmobile.occurrence.selection.OccurrenceCauseSelectionActivityModule;
import br.com.neolog.cplmobile.persistence.PersistenceModule;
import br.com.neolog.cplmobile.signal.SignalModule;
import br.com.neolog.cplmobile.signal.job.RemoveOldSignalAsyncTask;
import br.com.neolog.cplmobile.signal.job.ResendSignalAsyncTask;
import br.com.neolog.cplmobile.transition.TransitionModule;
import br.com.neolog.cplmobile.viewmodel.ViewModelModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        EventModule.class,
        ExceptionModule.class,
        HistoricActivityModule.class,
        MainActivityModule.class,
        OccurrenceCauseSelectionActivityModule.class,
        NewOccurrenceActivityModule.class,
        MonitorableModule.class,
        RetrofitModule.class,
        SignalModule.class,
        ViewModelModule.class,
        TransitionModule.class,
        OccurrenceModule.class,
        PersistenceModule.class,
        OccurrenceCategoryModule.class,
        OccurrenceCauseModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(
                Application application);

        AppComponent build();
    }

    void inject(
            CPLApplication cplApp);

    void inject(
            ResendSignalAsyncTask resendSignalAsyncTask);

    void inject(
            RemoveOldSignalAsyncTask removeOldSignalAsyncTask);

    void inject(
            ResendOccurrenceAsyncTask resendOccurrenceAsyncTask);

    void inject(
        ResendMonitorableFinishAsyncTask resendMonitorableFinishAsyncTask );

    Locale locale();
}
