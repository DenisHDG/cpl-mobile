package br.com.neolog.cplmobile.monitorable.repo;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.api.ApiResponse;
import br.com.neolog.cplmobile.api.NetworkBoundResource;
import br.com.neolog.cplmobile.api.Resource;
import br.com.neolog.cplmobile.device.DeviceService;
import br.com.neolog.cplmobile.monitorable.MonitorableService;
import br.com.neolog.cplmobile.monitorable.api.MonitorableApi;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.cplmobile.monitorable.model.MonitorablePropertyDao;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;

public class MonitorableRepository
{
    private final MonitorableApi monitorableApi;
    private final MonitorableDao monitorableDao;
    private final MonitorablePropertyDao monitorablePropertyDao;
    private final MonitorableService monitorableService;
    private final DeviceService deviceService;
    private final AppExecutors appExecutors;

    @Inject
    MonitorableRepository(
        final MonitorableApi monitorableApi,
        final MonitorableDao monitorableDao,
        final MonitorablePropertyDao monitorablePropertyDao,
        final MonitorableService monitorableService,
        final AppExecutors appExecutors,
        final DeviceService deviceService )
    {
        this.monitorableApi = monitorableApi;
        this.monitorableDao = monitorableDao;
        this.monitorablePropertyDao = monitorablePropertyDao;
        this.monitorableService = monitorableService;
        this.appExecutors = appExecutors;
        this.deviceService = deviceService;
    }

    public LiveData<Resource<List<Monitorable>>> findCurrentMonitorable()
    {
        return new NetworkBoundResource<List<Monitorable>,RestMonitorable>( appExecutors ) {
            @NonNull
            @Override
            protected LiveData<List<Monitorable>> loadFromDb()
            {
                return monitorableDao.findAll();
            }

            @Override
            protected boolean shouldFetch(
                @Nullable final List<Monitorable> data )
            {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RestMonitorable>> createCall()
            {
                return monitorableApi.findByDeviceIdAndProviderId(
                    deviceService.getDeviceId(),
                    deviceService.getProviderId() );
            }

            @Override
            protected void saveCallResult(
                @NonNull final RestMonitorable monitorable )
            {
                monitorableService.save( monitorable );
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Monitorable>>> findAll(
        final List<Integer> ids )
    {
        return new NetworkBoundResource<List<Monitorable>,List<RestMonitorable>>( appExecutors ) {
            @NonNull
            @Override
            protected LiveData<List<Monitorable>> loadFromDb()
            {
                return monitorableDao.findByIds( ids );
            }

            @Override
            protected boolean shouldFetch(
                @Nullable final List<Monitorable> data )
            {
                return data == null || data.size() != ids.size();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<RestMonitorable>>> createCall()
            {
                return monitorableApi.findByIds( ids );
            }

            @Override
            protected void saveCallResult(
                @NonNull final List<RestMonitorable> monitorables )
            {
                monitorableService.save( monitorables );
            }
        }.asLiveData();
    }

    public void finishMonitorables(
        final List<Integer> id )
    {
        monitorableService.finishMonitorableIds( id );
    }

    public LiveData<List<MonitorableProperty>> findMonitorableProperties(
        final Integer id )
    {
        return monitorablePropertyDao.findByMonitorableId( id );
    }

    // FIXME usado para pegar as transições
    public LiveData<ApiResponse<List<RestMonitorable>>> findRestMonitorablesById(
        final List<Integer> ids )
    {
        return monitorableApi.findByIds( ids );
    }
}
