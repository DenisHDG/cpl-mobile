package br.com.neolog.cplmobile.occurrence.job;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.CPLApplication;
import br.com.neolog.cplmobile.di.DaggerAppComponent;
import br.com.neolog.cplmobile.job.JobScheduler;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.occurrence.ExternalEntity;
import br.com.neolog.cplmobile.occurrence.Impact;
import br.com.neolog.cplmobile.occurrence.LatLong;
import br.com.neolog.cplmobile.occurrence.Occurrence;
import br.com.neolog.cplmobile.occurrence.OccurrenceApi;
import br.com.neolog.cplmobile.occurrence.OccurrenceDao;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;
import br.com.neolog.monitoring.monitorable.model.rest.RestLatLong;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestImpact;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestMonitorableOccurrence;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResendOccurrenceAsyncTask
    extends
        AsyncTask<Void,Void,Void>
{

    @Inject
    OccurrenceApi occurrenceApi;
    @Inject
    OccurrenceDao occurrenceDao;
    @Inject
    AppExecutors appExecutors;
    @Inject
    MonitorableDao monitorableDao;
    // TODO review warning
    @Inject
    Context context;

    ResendOccurrenceAsyncTask()
    {
        DaggerAppComponent.builder().application( CPLApplication.getInstance() ).build().inject( this );
    }

    @Override
    protected Void doInBackground(
        final Void... voids )
    {
        final List<Occurrence> occurrences = occurrenceDao.findAllWhereRemoteServerIdIsNull();
        for( final Occurrence occurrence : occurrences ) {
            final RestOccurrence restOccurrence = convertOccurrenceToRestOccurrence( occurrence );
            final List<RestMonitorableIdentifier> monitorableIdentifier = getMonitorables();
            final RestMonitorableOccurrence restMonitorableOccurrence = new RestMonitorableOccurrence( monitorableIdentifier,
                restOccurrence );
            // TODO reenviar comentários
            occurrenceApi.createOccurrenceComment( restMonitorableOccurrence, null ).enqueue( new Callback<Integer>() {
                @Override
                public void onResponse(
                    @NonNull final Call<Integer> call,
                    @NonNull final Response<Integer> response )
                {
                    if( response.isSuccessful() ) {
                        final Integer remoteServerId = response.body();
                        appExecutors.diskIO().execute( () -> occurrenceDao.updateRemoteServerId( occurrence.getId(), remoteServerId ) );
                    }
                }

                @Override
                public void onFailure(
                    @NonNull final Call<Integer> call,
                    @NonNull final Throwable t )
                {
                }
            } );
        }
        if( occurrenceDao.findAllWhereRemoteServerIdIsNull().isEmpty() ) {
            JobScheduler.cancel( context, ResendOccurrenceJobService.TAG );
        }
        return null;
    }

    private List<RestMonitorableIdentifier> getMonitorables()
    {
        return FluentIterable.from( monitorableDao.findAllRoots() )
            .transform( monitorable -> new RestMonitorableIdentifier( monitorable.getCode(), monitorable.getType() ) )
            .toList();
    }

    private RestOccurrence convertOccurrenceToRestOccurrence(
        @NonNull final Occurrence occurrence )
    {
        return new RestOccurrence(
            occurrence.getId(),
            occurrence.getSourceId(),
            occurrence.getTimestamp(),
            convertExternalEntityToRestExternalEntity( occurrence.getCause() ),
            convertExternalEntityToRestExternalEntity( occurrence.getCategory() ),
            convertImpactToRestImpact( occurrence.getImpact() ),
            null,
            convertLatLongToRestLatLong( occurrence.getWhere() ),
            Collections.emptySet(),
            Collections.emptyList(),
            Collections.emptySet(),
            Collections.emptySet() );
    }

    @Nullable
    private RestExternalEntity convertExternalEntityToRestExternalEntity(
        final ExternalEntity externalEntity )
    {
        if( externalEntity == null ) {
            return null;
        }
        return new RestExternalEntity( externalEntity.getId(), externalEntity.getSourceId(), externalEntity.getName(), externalEntity
            .getDescription() );
    }

    @Nullable
    private RestImpact convertImpactToRestImpact(
        final Impact impact )
    {
        if( impact == null ) {
            return null;
        }
        return new RestImpact( impact.getTimeDelta(), impact.getValueDelta(), impact.getQuantityDelta() );
    }

    @Nullable
    private RestLatLong convertLatLongToRestLatLong(
        final LatLong latLong )
    {
        if( latLong == null ) {
            return null;
        }
        return new RestLatLong( latLong.getLatitude(), latLong.getLongitude() );
    }
}