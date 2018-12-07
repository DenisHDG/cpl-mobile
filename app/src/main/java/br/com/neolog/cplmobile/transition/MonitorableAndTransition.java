package br.com.neolog.cplmobile.transition;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

public class MonitorableAndTransition
{
    private final RestMonitorable monitorable;
    private final RestTransition transition;

    MonitorableAndTransition(
        final RestMonitorable monitorable,
        final RestTransition transition )
    {
        this.monitorable = monitorable;
        this.transition = transition;
    }

    public RestMonitorable getMonitorable()
    {
        return monitorable;
    }

    public RestTransition getTransition()
    {
        return transition;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final MonitorableAndTransition that = (MonitorableAndTransition) o;
        return Objects.equal( monitorable.getId(), that.monitorable.getId() ) &&
            Objects.equal( transition.getId(), that.transition.getId() );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( monitorable.getId(), transition.getId() );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "monitorable", monitorable )
            .add( "transition", transition )
            .toString();
    }
}
