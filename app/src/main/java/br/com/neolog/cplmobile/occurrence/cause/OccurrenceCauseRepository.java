package br.com.neolog.cplmobile.occurrence.cause;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.api.ApiResponse;
import br.com.neolog.cplmobile.api.NetworkBoundResource;
import br.com.neolog.cplmobile.api.Resource;
import br.com.neolog.cplmobile.occurrence.OccurrenceCauseAndCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryDao;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrenceCause;

public class OccurrenceCauseRepository
{
    private final OccurrenceCauseApi occurrenceCauseApi;
    private final OccurrenceCauseDao occurrenceCauseDao;
    private final OccurrenceCategoryDao occurrenceCategoryDao;
    private final AllowedMonitorableTypeDao allowedMonitorableTypeDao;
    private final AppExecutors appExecutors;

    @Inject
    OccurrenceCauseRepository(
        final OccurrenceCauseApi occurrenceCauseApi,
        final OccurrenceCauseDao occurrenceCauseDao,
        final OccurrenceCategoryDao occurrenceCategoryDao,
        final AllowedMonitorableTypeDao allowedMonitorableTypeDao,
        final AppExecutors appExecutors )
    {
        this.occurrenceCauseApi = occurrenceCauseApi;
        this.occurrenceCauseDao = occurrenceCauseDao;
        this.occurrenceCategoryDao = occurrenceCategoryDao;
        this.allowedMonitorableTypeDao = allowedMonitorableTypeDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<OccurrenceCauseAndCategory>>> findAll()
    {
        return new NetworkBoundResource<List<OccurrenceCauseAndCategory>,List<RestOccurrenceCause>>( appExecutors ) {

            @Override
            protected void saveCallResult(
                @NonNull final List<RestOccurrenceCause> items )
            {
                allowedMonitorableTypeDao.deleteAll();
                occurrenceCauseDao.deleteAll();
                occurrenceCategoryDao.deleteAll();

                for( final RestOccurrenceCause restOccurrenceCause : items ) {

                    occurrenceCategoryDao.insert( OccurrenceCategory.from( restOccurrenceCause.getOccurrenceCategory() ) );
                    occurrenceCauseDao.insert( OccurrenceCause.from( restOccurrenceCause ) );

                    for( final String monitorableType : restOccurrenceCause.getAllowedMonitorableTypes() ) {
                        allowedMonitorableTypeDao.insert( getAllowedMonitorableType( restOccurrenceCause.getId(), monitorableType ) );
                    }
                }
            }

            @Override
            protected boolean shouldFetch(
                @Nullable final List<OccurrenceCauseAndCategory> data )
            {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<OccurrenceCauseAndCategory>> loadFromDb()
            {
                return occurrenceCauseDao.findOccurrenceCauseWithCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<RestOccurrenceCause>>> createCall()
            {
                return occurrenceCauseApi.findCauses();
            }

        }.asLiveData();

    }

    public LiveData<Resource<List<OccurrenceCauseAndCategory>>> findByCauseName(
        final String causeName )
    {
        return new NetworkBoundResource<List<OccurrenceCauseAndCategory>,List<RestOccurrenceCause>>( appExecutors ) {

            @Override
            protected void saveCallResult(
                @NonNull final List<RestOccurrenceCause> items )
            {
                allowedMonitorableTypeDao.deleteAll();
                occurrenceCauseDao.deleteAll();
                occurrenceCategoryDao.deleteAll();

                for( final RestOccurrenceCause restOccurrenceCause : items ) {

                    occurrenceCategoryDao.insert( OccurrenceCategory.from( restOccurrenceCause.getOccurrenceCategory() ) );
                    occurrenceCauseDao.insert( OccurrenceCause.from( restOccurrenceCause ) );

                    for( final String monitorableType : restOccurrenceCause.getAllowedMonitorableTypes() ) {
                        allowedMonitorableTypeDao.insert( getAllowedMonitorableType( restOccurrenceCause.getId(), monitorableType ) );
                    }
                }
            }

            @Override
            protected boolean shouldFetch(
                @Nullable final List<OccurrenceCauseAndCategory> data )
            {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<OccurrenceCauseAndCategory>> loadFromDb()
            {
                return occurrenceCauseDao.findOccurrenceCauseWithCategoryByCauseName( causeName );
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<RestOccurrenceCause>>> createCall()
            {
                return occurrenceCauseApi.findCauses();
            }

        }.asLiveData();
    }

    private AllowedMonitorableType getAllowedMonitorableType(
        final Integer restOccurrenceCauseId,
        final String monitorableType )
    {
        return new AllowedMonitorableType( restOccurrenceCauseId, monitorableType );
    }
}
