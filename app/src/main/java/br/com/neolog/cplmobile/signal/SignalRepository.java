package br.com.neolog.cplmobile.signal;

import javax.inject.Inject;

import org.joda.time.DateTime;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.device.DeviceService;
import br.com.neolog.cplmobile.signal.job.RemoveOldSignalJobService;
import br.com.neolog.cplmobile.signal.job.ResendSignalJobService;
import br.com.neolog.tracking.mobile.model.RestMobileSignal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignalRepository
{
    private final SignalDao dao;
    private final SignalApi api;
    private final DeviceService deviceService;
    private final AppExecutors appExecutors;
    private final Context context;

    @Inject
    public SignalRepository(
        final SignalDao dao,
        final SignalApi api,
        final DeviceService deviceService,
        final AppExecutors appExecutors,
        final Context context )
    {
        this.dao = dao;
        this.api = api;
        this.deviceService = deviceService;
        this.appExecutors = appExecutors;
        this.context = context;
    }

    public void createSignal(
        final Location location )
    {
        final DateTime signalTime = DateTime.now();
        final RestMobileSignal restMobileSignal = convertLocationToRestMobileSignal( location, signalTime );
        api.createSignal( restMobileSignal )
            .enqueue( new Callback<Long>() {
                @Override
                public void onResponse(
                    @NonNull final Call<Long> call,
                    @NonNull final Response<Long> response )
                {
                }

                @Override
                public void onFailure(
                    @NonNull final Call<Long> call,
                    @NonNull final Throwable t )
                {
                    appExecutors.diskIO().execute( () -> {
                        dao.insert( convertLocationToSignal( location, signalTime ) );
                    } );
                    ResendSignalJobService.schedule( context );
                    RemoveOldSignalJobService.schedule( context );
                }
            } );
    }

    private RestMobileSignal convertLocationToRestMobileSignal(
        final Location location,
        final DateTime signalTime )
    {
        return new RestMobileSignal( deviceService.getDeviceId(),
            location.getLatitude(),
            location.getLongitude(),
            null,
            signalTime,
            null );
    }

    private Signal convertLocationToSignal(
        final Location location,
        final DateTime signalTime )
    {
        return new Signal(
            location.getLatitude(),
            location.getLongitude(),
            signalTime );
    }
}
