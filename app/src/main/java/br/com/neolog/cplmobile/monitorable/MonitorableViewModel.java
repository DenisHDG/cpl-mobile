package br.com.neolog.cplmobile.monitorable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.api.Resource;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableRepository;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;

public class MonitorableViewModel
    extends
        ViewModel
{
    private final MonitorableRepository monitorableRepository;

    private final MutableLiveData<List<Integer>> monitorableIds;
    private final LiveData<Resource<List<Monitorable>>> resourceMonitorables;
    private final LiveData<List<Monitorable>> monitorables;
    private final LiveData<Multimap<StandardMonitorableType,Monitorable>> monitorablesByType;
    private final LiveData<ExceptionMessages> exceptionMessages;

    @Inject
    MonitorableViewModel(
        final MonitorableRepository monitorableRepository,
        final MonitorableGrouperService monitorableGrouperService )
    {
        this.monitorableRepository = monitorableRepository;
        monitorableIds = new MutableLiveData<>();
        resourceMonitorables = Transformations.switchMap(
            monitorableIds,
            value -> value.isEmpty()
                ? monitorableRepository.findCurrentMonitorable()
                : monitorableRepository.findAll( value ) );
        monitorables = Transformations.map( resourceMonitorables, Resource::getData );
        monitorablesByType = Transformations.map( monitorables, monitorableGrouperService::groupMonitorablesByType );
        this.exceptionMessages = Transformations.map( resourceMonitorables, Resource::getMessage );
    }

    public void findCurrentMonitorable()
    {
        monitorableIds.setValue( new ArrayList<>() );
    }

    public LiveData<Boolean> getLoading()
    {
        return Transformations.map( resourceMonitorables, resource -> resource.getStatus().equals( Resource.Status.LOADING ) );
    }

    public LiveData<ExceptionMessages> getExceptionMessages()
    {
        return exceptionMessages;
    }

    public LiveData<Multimap<StandardMonitorableType,Monitorable>> getMonitorablesByType()
    {
        return monitorablesByType;
    }

    public List<RestMonitorableIdentifier> getMonitorablesIdentifierFromRoots()
    {
        final List<Monitorable> monitorables = getMonitorablesRoot( this.monitorables.getValue() );
        return Lists.transform( monitorables, input -> new RestMonitorableIdentifier( input.getCode(), input.getType() ) );
    }

    public void finishMonitorable()
    {
        final List<Monitorable> monitorables = this.monitorables.getValue();
        if( monitorables == null ) {
            return;
        }
        monitorableRepository.finishMonitorables( Lists.transform( monitorables, Monitorable::getId ) );
    }

    private List<Monitorable> getMonitorablesRoot(
        final List<Monitorable> monitorables )
    {
        if( monitorables == null ) {
            return Collections.emptyList();
        }
        return FluentIterable.from( monitorables ).filter( Monitorable::isRoot ).toList();
    }
}
