package br.com.neolog.cplmobile.monitorable.converter;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableBuilder;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableSignalTimeCalculator;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;

public class RestMonitorableToMonitorableConverter
{
    private final MonitorableSignalTimeCalculator signalTimeCalculator;

    @Inject
    RestMonitorableToMonitorableConverter(
        final MonitorableSignalTimeCalculator signalTimeCalculator )
    {
        this.signalTimeCalculator = signalTimeCalculator;
    }

    public List<Monitorable> convert(
        final RestMonitorable monitorable )
    {
        return convertInner( monitorable, null );
    }

    private List<Monitorable> convertInner(
        final RestMonitorable monitorable,
        final Integer parentId )
    {
        final ImmutableList.Builder<Monitorable> result = ImmutableList.builder();
        result.add( convertSingle( monitorable, parentId ) );
        for( final RestMonitorable child : monitorable.getChildren() ) {
            result.addAll( convertInner( child, monitorable.getId() ) );
        }
        return result.build();
    }

    private Monitorable convertSingle(
        final RestMonitorable original,
        final Integer parentId )
    {
        return new MonitorableBuilder( original.getId(), original.getCode(), original.getType() )
            .setLateStatus( signalTimeCalculator.calculateLateStatus( original ) )
            .setParentId( parentId )
            .isRoot( original.isRoot() )
            .build();
    }
}
