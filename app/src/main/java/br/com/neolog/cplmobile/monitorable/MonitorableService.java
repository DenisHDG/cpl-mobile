package br.com.neolog.cplmobile.monitorable;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.monitorable.api.MonitorableApi;
import br.com.neolog.cplmobile.monitorable.converter.MonitorablePropertiesConverter;
import br.com.neolog.cplmobile.monitorable.converter.RestMonitorableToMonitorableConverter;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableFinish;
import br.com.neolog.cplmobile.monitorable.model.MonitorableFinishDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.cplmobile.monitorable.model.MonitorablePropertyDao;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import retrofit2.Response;
import timber.log.Timber;

public class MonitorableService
{
    private final AppExecutors appExecutors;
    private final MonitorableApi monitorableApi;
    private final MonitorableDao monitorableDao;
    private final MonitorablePropertyDao monitorablePropertyDao;
    private final MonitorableFinishDao monitorableFinishDao;
    private final RestMonitorableToMonitorableConverter monitorableConverter;
    private final MonitorablePropertiesConverter propertiesConverter;

    @Inject
    MonitorableService(
        final AppExecutors appExecutors,
        final MonitorableApi monitorableApi,
        final MonitorableDao monitorableDao,
        final MonitorablePropertyDao monitorablePropertyDao,
        final MonitorableFinishDao monitorableFinishDao,
        final RestMonitorableToMonitorableConverter monitorableConverter,
        final MonitorablePropertiesConverter propertiesConverter )
    {
        this.appExecutors = appExecutors;
        this.monitorableApi = monitorableApi;
        this.monitorableDao = monitorableDao;
        this.monitorablePropertyDao = monitorablePropertyDao;
        this.monitorableFinishDao = monitorableFinishDao;
        this.monitorableConverter = monitorableConverter;
        this.propertiesConverter = propertiesConverter;
    }

    public void save(
        final RestMonitorable restMonitorable )
    {
        monitorableDao.insert( monitorableConverter.convert( restMonitorable ) );
        final List<MonitorableProperty> properties = propertiesConverter.convertProperties( restMonitorable );
        monitorablePropertyDao.deleteByMonitorableId( restMonitorable.getId() );
        for( final MonitorableProperty property : properties ) {
            monitorablePropertyDao.insert( property );
        }
    }

    public void save(
        final List<RestMonitorable> monitorables )
    {
        for( final RestMonitorable monitorable : monitorables ) {
            save( monitorable );
        }
    }

    public void finishMonitorableIds(
        final Iterable<Integer> monitorableIds )
    {
        appExecutors.diskIO().execute( () -> {
            for( final Integer id : monitorableIds ) {
                monitorableDao.delete( id );
                monitorableFinishDao.insert( new MonitorableFinish( id ) );
            }
        } );
    }

    public void sendFinishedMonitorables()
    {
        appExecutors.diskIO().execute( () -> {
            final List<MonitorableFinish> finishList = monitorableFinishDao.findAll();
            if( finishList.isEmpty() ) {
                return;
            }
            final List<Integer> monitorableIds = Lists.transform( finishList, MonitorableFinish::getMonitorableId );

            final Response<List<RestRemoteMessage>> response;
            try {
                response = monitorableApi.finishMonitorables( monitorableIds ).execute();
            } catch( final IOException e ) {
                // tenta novamente devido à erros de conexão
                Timber.w( e, "Connection error finalizing monitorables %s", monitorableIds );
                return;
            }
            if( ! response.isSuccessful() && response.errorBody() != null ) {
                try {
                    Timber.w( "Failed to finalize monitorables %s, with %s", monitorableIds, response.errorBody().string() );
                } catch( final IOException e ) {
                    Timber.w( e, "Failed to finalize monitorables %s, with exception", monitorableIds );
                }
            }
            for( final Integer monitorableId : monitorableIds ) {
                monitorableFinishDao.deleteByMonitorableId( monitorableId );
                monitorableDao.delete( monitorableId );
            }
        } );
    }
}
