package br.com.neolog.cplmobile.monitorable;

import javax.inject.Inject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;

public class MonitorableGrouperService
{
    @Inject
    public MonitorableGrouperService()
    {
    }

    Multimap<StandardMonitorableType,Monitorable> groupMonitorablesByType(
        final Iterable<Monitorable> monitorables )
    {
        final Multimap<StandardMonitorableType,Monitorable> result = HashMultimap.create();
        if( monitorables == null ) {
            return result;
        }
        for( final Monitorable monitorable : monitorables ) {
            result.put( monitorable.getType(), monitorable );
        }
        return result;
    }

}
