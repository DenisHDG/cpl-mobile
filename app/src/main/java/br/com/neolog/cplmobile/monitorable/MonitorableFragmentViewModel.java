package br.com.neolog.cplmobile.monitorable;

import static com.google.common.collect.Iterables.getOnlyElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.api.Resource;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableRepository;
import br.com.neolog.cplmobile.transition.TransitionGroup;
import br.com.neolog.cplmobile.transition.TransitionGrouperService;
import br.com.neolog.exceptionmessage.ExceptionMessages;

public class MonitorableFragmentViewModel
    extends
        ViewModel
{
    private final MonitorableRepository monitorableRepository;
    private final TransitionGrouperService transitionGrouperService;

    private final MutableLiveData<List<Integer>> monitorableIds;
    private final LiveData<Resource<List<Monitorable>>> resourceMonitorables;
    private final LiveData<List<MonitorableProperty>> monitorableProperties;
    private final LiveData<List<TransitionGroup>> transitionGroups;
    private final LiveData<ExceptionMessages> exceptionMessages;
    private final LiveData<Monitorable> monitorable;

    @Inject
    MonitorableFragmentViewModel(
        final MonitorableRepository monitorableRepository,
        final TransitionGrouperService transitionGrouperService )
    {
        this.monitorableRepository = monitorableRepository;
        this.transitionGrouperService = transitionGrouperService;

        monitorableIds = new MutableLiveData<>();
        resourceMonitorables = Transformations.switchMap( monitorableIds, monitorableRepository::findAll );
        exceptionMessages = Transformations.map( resourceMonitorables, Resource::getMessage );
        monitorable = Transformations.map( resourceMonitorables, this::getOnlyMonitorable );
        monitorableProperties = Transformations.switchMap( monitorable, this::loadProperties );
        transitionGroups = Transformations.switchMap( monitorableIds, this::loadTransitions );
    }

    private LiveData<List<TransitionGroup>> loadTransitions(
        final List<Integer> monitorableIds )
    {
        return Transformations.map(
            monitorableRepository.findRestMonitorablesById( monitorableIds ),
            resource -> transitionGrouperService.groupTransitions( resource.getBody() ) );
    }

    private Monitorable getOnlyMonitorable(
        final Resource<List<Monitorable>> resource )
    {
        final List<Monitorable> monitorables = resource.getData();
        if( monitorables == null || monitorables.size() != 1 ) {
            return null;
        }
        return getOnlyElement( monitorables );
    }

    private LiveData<List<MonitorableProperty>> loadProperties(
        final Monitorable monitorable )
    {
        if( monitorable == null ) {
            return new MutableLiveData<>();
        }
        return monitorableRepository.findMonitorableProperties( monitorable.getId() );
    }

    void findAll(
        final List<Integer> monitorableIds )
    {
        this.monitorableIds.setValue( monitorableIds );
    }

    public LiveData<Boolean> getLoading()
    {
        return Transformations.map( resourceMonitorables, resource -> resource.getStatus().equals( Resource.Status.LOADING ) );
    }

    LiveData<ExceptionMessages> getExceptionMessages() {
        return exceptionMessages;
    }

    public LiveData<Monitorable> getMonitorable()
    {
        return monitorable;
    }

    LiveData<List<MonitorableProperty>> getMonitorableProperties()
    {
        return monitorableProperties;
    }

    LiveData<List<TransitionGroup>> getTransitionGroups()
    {
        return transitionGroups;
    }

    void refresh()
    {
        // TODO não deve ser necessário quando a persistencia estiver funcionando
        findAll( new ArrayList<>( monitorableIds.getValue() ) );
    }
}
