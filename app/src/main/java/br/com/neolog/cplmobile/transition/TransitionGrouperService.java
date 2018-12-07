package br.com.neolog.cplmobile.transition;

import static com.google.common.base.Objects.equal;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.firebase.perf.metrics.AddTrace;

import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransitionLocation;

public class TransitionGrouperService
{
    @Inject
    TransitionGrouperService()
    {
    }

    @AddTrace( name = "TransitionGrouperService.groupTransitions" )
    public List<TransitionGroup> groupTransitions(
        final Iterable<? extends RestMonitorable> monitorables )
    {
        final List<MonitorableAndTransition> transitions = getTransitions( monitorables );
        final ImmutableList.Builder<TransitionGroup> builder = ImmutableList.builder();
        RestTransitionLocation lastLocation = null;
        TransitionGroup lastTransitionGroup = null;
        int index = 0;
        for( final MonitorableAndTransition monitorableAndTransition : transitions ) {
            final RestTransition transition = monitorableAndTransition.getTransition();
            if( ! equal( lastLocation, transition.getLocation() ) ) {
                lastLocation = transition.getLocation();
                lastTransitionGroup = new TransitionGroup( index++, lastLocation );
                builder.add( lastTransitionGroup );
            }
            lastTransitionGroup.addTransition( monitorableAndTransition );
        }
        return builder.build();
    }

    private List<MonitorableAndTransition> getTransitions(
        final Iterable<? extends RestMonitorable> monitorables )
    {
        if( monitorables == null ) {
            return Collections.emptyList();
        }

        return FluentIterable.from( getTransitionsUnsorted( monitorables ) )
            .toSortedList( this::sortByEstimatedTimestamp );
    }

    private Iterable<MonitorableAndTransition> getTransitionsUnsorted(
        final Iterable<? extends RestMonitorable> monitorables )
    {
        final ImmutableSet.Builder<MonitorableAndTransition> transitions = ImmutableSet.builder();
        for( final RestMonitorable monitorable : monitorables ) {
            for( final RestTransition transition : monitorable.getTransitions() ) {
                transitions.add( new MonitorableAndTransition( monitorable, transition ) );
            }
            transitions.addAll( getTransitionsUnsorted( monitorable.getChildren() ) );
        }
        return transitions.build();
    }

    private int sortByEstimatedTimestamp(
        final MonitorableAndTransition left,
        final MonitorableAndTransition right )
    {
        return ComparisonChain.start()
            .compare( left.getTransition().getEstimatedTimestamp(), right.getTransition().getEstimatedTimestamp() )
            .result();
    }
}
