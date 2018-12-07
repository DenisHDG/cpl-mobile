package br.com.neolog.cplmobile.occurrence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OccurrenceRepository
{

    private final OccurrenceApi occurrenceApi;
    private final OccurrenceDao occurrenceDao;
    private final AppExecutors appExecutors;

    @Inject
    OccurrenceRepository(
        final OccurrenceApi occurrenceApi,
        final OccurrenceDao occurrenceDao,
        final AppExecutors appExecutors )
    {
        this.occurrenceApi = occurrenceApi;
        this.occurrenceDao = occurrenceDao;
        this.appExecutors = appExecutors;
    }

    public void sync(
        final int monitorableId )
    {
        occurrenceApi.listForMonitorable( monitorableId ).enqueue( new Callback<List<RestOccurrence>>() {
            @Override
            public void onResponse(
                @NonNull final Call<List<RestOccurrence>> call,
                @NonNull final Response<List<RestOccurrence>> response )
            {
                if( ! response.isSuccessful() ) {
                    try {
                        Timber.w( "Error fetching Occurrence: %d %s", response.code(),
                            response.errorBody() != null ? response.errorBody().string() : "" );
                    } catch( final IOException e ) {
                        e.printStackTrace();
                    }
                    return;
                }
                final List<RestOccurrence> restOccurrence = response.body();
                final List<Occurrence> occurrences = Occurrence.from( restOccurrence );
                appExecutors.diskIO().execute( () -> {
                    final List<Integer> persistedRemoteIds = getPersitedRemoteIds( monitorableId );
                    final List<Occurrence> newOccurrencesToInsert = new ArrayList<>();
                    for( final Occurrence occurrence : occurrences ) {
                        if( ! persistedRemoteIds.contains( occurrence.getRemoteServerId() ) ) {
                            newOccurrencesToInsert.add( occurrence );
                        }
                    }
                    // TODO insert na tabela de relacionamento ocorrencia x monitoravel
                    occurrenceDao.insert( newOccurrencesToInsert );
                } );
            }

            @Override
            public void onFailure(
                @NonNull final Call<List<RestOccurrence>> call,
                @NonNull final Throwable t )
            {
                Timber.w( t, "Error fetching Occurrence" );
            }
        } );
    }

    @NonNull
    private List<Integer> getPersitedRemoteIds(
        @NonNull final Integer monitorableId )
    {
        // TODO buscar todas as ocorrências para monitorableId
        final List<Occurrence> persistedOccurrences = occurrenceDao.findAllWhereRemoteServerIdIsNotNull();
        final List<Integer> persistedIds = new ArrayList<>();
        for( final Occurrence occurrence : persistedOccurrences ) {
            persistedIds.add( occurrence.getRemoteServerId() );
        }
        return persistedIds;
    }
}
