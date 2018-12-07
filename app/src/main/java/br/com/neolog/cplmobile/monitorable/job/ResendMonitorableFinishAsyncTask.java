package br.com.neolog.cplmobile.monitorable.job;

import javax.inject.Inject;

import android.os.AsyncTask;

import br.com.neolog.cplmobile.CPLApplication;
import br.com.neolog.cplmobile.di.DaggerAppComponent;
import br.com.neolog.cplmobile.monitorable.MonitorableService;

public class ResendMonitorableFinishAsyncTask
    extends
        AsyncTask<Void,Void,Void>
{
    @Inject
    MonitorableService monitorableService;

    ResendMonitorableFinishAsyncTask()
    {
        DaggerAppComponent.builder().application( CPLApplication.getInstance() ).build().inject( this );
    }

    @Override
    protected Void doInBackground(
        final Void... voids )
    {
        monitorableService.sendFinishedMonitorables();
        return null;
    }
}
