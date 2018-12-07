package br.com.neolog.cplmobile.transition;

import static br.com.neolog.cplmobile.monitorable.repo.MonitorableSignalTimeCalculator.calculateLateStatus;
import static br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus.IN_EXECUTION;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import br.com.neolog.cplmobile.monitorable.model.LateStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestLocality;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransitionLocation;

public class TransitionGroup
{
    private final int index;
    private final RestTransitionLocation location;
    private final List<MonitorableAndTransition> monitorableAndTransitions = new ArrayList<>();
    private transient TransitionGroupStatus status;

    TransitionGroup(
        final int index,
        final RestTransitionLocation location )
    {
        this.index = index;
        this.location = Preconditions.checkNotNull( location, "location is null" );
    }

    public int getIndex()
    {
        return index;
    }

    public String getAddress()
    {
        final RestLocality locality = location.getLocality();
        if( locality == null ) {
            return "";
        }
        return Joiner.on( ", " )
            .skipNulls()
            .join( locality.getStreet(), locality.getNumber(), locality.getComplement(), locality.getZipCode() );
    }

    public String getLocalityName()
    {
        final RestLocality locality = location.getLocality();
        if( locality == null ) {
            return "";
        }
        return locality.getName();
    }

    public DateTime getEstimatedArrival()
    {
        return monitorableAndTransitions.get( 0 ).getTransition().getEstimatedTimestamp();
    }

    public TransitionGroupStatus getStatus()
    {
        if( status != null ) {
            return status;
        }
        if( monitorableAndTransitions.isEmpty() ) {
            return status = TransitionGroupStatus.DONE;
        }
        int count = 0;
        for( final MonitorableAndTransition monitorableAndTransition : monitorableAndTransitions ) {
            if( ! IN_EXECUTION.equals( monitorableAndTransition.getTransition().getStatus() ) ) {
                count++;
            }
        }
        if( count == 0 ) {
            return status = TransitionGroupStatus.OPEN;
        }
        if( count < monitorableAndTransitions.size() ) {
            return status = TransitionGroupStatus.IN_PROGRESS;
        }
        return status = TransitionGroupStatus.DONE;
    }

    List<MonitorableAndTransition> getMonitorableAndTransitions()
    {
        return monitorableAndTransitions;
    }

    @Nullable
    RestTransition getNextTransition()
    {
        RestTransition nextTransition = null;
        for( final MonitorableAndTransition monitorableAndTransition : Lists.reverse( monitorableAndTransitions ) ) {
            final RestTransition transition = monitorableAndTransition.getTransition();
            if( ! IN_EXECUTION.equals( transition.getStatus() ) ) {
                return nextTransition;
            }
            nextTransition = transition;
        }
        return nextTransition;
    }

    public LateStatus getLateStatus()
    {
        final RestTransition nextTransition = getNextTransition();
        return calculateLateStatus( nextTransition );
    }

    void addTransition(
        final MonitorableAndTransition transition )
    {
        monitorableAndTransitions.add( transition );
        status = null;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final TransitionGroup that = (TransitionGroup) o;
        return Objects.equal( location, that.location ) &&
            Objects.equal( monitorableAndTransitions, that.monitorableAndTransitions );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( location, monitorableAndTransitions );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "location", location )
            .add( "monitorableAndTransitions", monitorableAndTransitions )
            .toString();
    }
}
