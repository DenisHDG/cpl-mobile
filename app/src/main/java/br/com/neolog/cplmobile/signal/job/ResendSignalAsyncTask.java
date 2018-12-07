package br.com.neolog.cplmobile.signal.job;

import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.CPLApplication;
import br.com.neolog.cplmobile.device.DeviceService;
import br.com.neolog.cplmobile.di.DaggerAppComponent;
import br.com.neolog.cplmobile.job.JobScheduler;
import br.com.neolog.cplmobile.signal.Signal;
import br.com.neolog.cplmobile.signal.SignalApi;
import br.com.neolog.cplmobile.signal.SignalDao;
import br.com.neolog.tracking.mobile.model.RestMobileSignal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResendSignalAsyncTask
    extends
        AsyncTask<Void,Void,Void>
{
    @Inject
    SignalDao signalDao;
    @Inject
    DeviceService deviceService;
    @Inject
    SignalApi signalApi;
    // TODO review warning
    @Inject
    Context context;
    @Inject
    AppExecutors appExecutors;

    ResendSignalAsyncTask()
    {
        DaggerAppComponent.builder().application( CPLApplication.getInstance() ).build().inject( this );
    }

    @Override
    protected Void doInBackground(
        final Void... voids )
    {

        final List<Signal> signals = signalDao.findAll();
        for( final Signal signal : signals ) {
            final RestMobileSignal restMobileSignal = convertSignalToRestMobileSignal( signal );
            signalApi.createSignal( restMobileSignal ).enqueue( new Callback<Long>() {
                @Override
                public void onResponse(
                    @NonNull final Call<Long> call,
                    @NonNull final Response<Long> response )
                {
                    appExecutors.diskIO().execute( () -> {
                        signalDao.delete( signal );
                        final List<Signal> remainingSignals = signalDao.findAll();
                        if( remainingSignals.isEmpty() ) {
                            JobScheduler.cancel( context, ResendSignalJobService.TAG );
                            JobScheduler.cancel( context, RemoveOldSignalJobService.TAG );
                        }
                    } );
                }

                @Override
                public void onFailure(
                    @NonNull final Call<Long> call,
                    @NonNull final Throwable t )
                {
                }
            } );
        }

        return null;
    }

    private RestMobileSignal convertSignalToRestMobileSignal(
        final Signal signal )
    {
        return new RestMobileSignal( deviceService.getDeviceId(),
            signal.getLatitude(),
            signal.getLongitude(),
            null,
            signal.getSignalTime(),
            null );
    }

}
