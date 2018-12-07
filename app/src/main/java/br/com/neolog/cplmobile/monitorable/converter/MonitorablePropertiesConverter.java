package br.com.neolog.cplmobile.monitorable.converter;

import static br.com.neolog.cplmobile.monitorable.model.MonitorableProperty.from;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.CARRIER;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.DISTANCE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.ESTIMATED_END;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.EXPECTED_END;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.PLATE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.REALIZED_DISTANCE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.STATUS;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.VEHICLE;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import br.com.neolog.cplmobile.formatter.DateFormatter;
import br.com.neolog.cplmobile.formatter.ExternalizableEntityFormatter;
import br.com.neolog.cplmobile.formatter.NumberFormatter;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

public class MonitorablePropertiesConverter
{
    private final ExternalizableEntityFormatter entityFormatter;
    private final CompletionStatusConverter statusConverter;
    private final DateFormatter dateTimeFormatter;
    private final NumberFormatter numberFormatter;

    @Inject
    MonitorablePropertiesConverter(
        final ExternalizableEntityFormatter entityFormatter,
        final CompletionStatusConverter statusConverter,
        final DateFormatter dateTimeFormatter,
        final NumberFormatter numberFormatter )
    {
        this.entityFormatter = entityFormatter;
        this.statusConverter = statusConverter;
        this.dateTimeFormatter = dateTimeFormatter;
        this.numberFormatter = numberFormatter;
    }

    public List<MonitorableProperty> convertProperties(
        final RestMonitorable monitorable )
    {
        final ImmutableList.Builder<MonitorableProperty> builder = ImmutableList.builder();
        builder
            .add( convertStatus( monitorable ) )
            .addAll( convertBaseMonitorableProperties( monitorable ) )
            .addAll( convertMonitorableStatusProperties( monitorable ) );
        for( final RestMonitorable child : monitorable.getChildren() ) {
            builder.addAll( convertProperties( child ) );
        }
        return builder.build();
    }

    private MonitorableProperty convertStatus(
        final RestMonitorable monitorable )
    {
        return from( monitorable, STATUS, statusConverter.convertToString( monitorable.getCompletion().getStatus() ) );
    }

    private Iterable<MonitorableProperty> convertBaseMonitorableProperties(
        final RestMonitorable monitorable )
    {
        // TODO VERIFICAR SE ACASO OS VALORES VIEREM NULOS OU VAZIOS SE É PARA MOSTRAR
        // OU NÃO OS ITENS!
        return ImmutableList.<MonitorableProperty> builder()
            .add( from( monitorable, CARRIER, entityFormatter.format( monitorable.getCarrier() ) ) )
            .add( from( monitorable, VEHICLE, entityFormatter.format( monitorable.getVehicle() ) ) )
            .add( from( monitorable, PLATE, entityFormatter.format( monitorable.getTruck() ) ) )
            .build();
    }

    private Iterable<MonitorableProperty> convertMonitorableStatusProperties(
        final RestMonitorable monitorable )
    {
        return ImmutableList.<MonitorableProperty> builder()
            .add( from( monitorable, ESTIMATED_END, getLastTransitionEstimatedTime( monitorable ) ) )
            .add( from( monitorable, EXPECTED_END, getLastTransitionExpectedTime( monitorable ) ) )
            .add( from( monitorable, REALIZED_DISTANCE, numberFormatter.format( null ) ) )
            .add( from( monitorable, DISTANCE, numberFormatter.format( monitorable.getDistance() ) ) )
            .build();
    }

    private String getLastTransitionEstimatedTime(
        final RestMonitorable monitorable )
    {
        final RestTransition lastTransition = getLastTransition( monitorable );
        return dateTimeFormatter.formatDateTime( lastTransition == null ? null : lastTransition.getEstimatedTimestamp() );
    }

    private String getLastTransitionExpectedTime(
        final RestMonitorable monitorable )
    {
        final RestTransition lastTransition = getLastTransition( monitorable );
        return dateTimeFormatter.formatDateTime( lastTransition == null ? null : lastTransition.getExpectedTimestamp() );
    }

    private RestTransition getLastTransition(
        final RestMonitorable monitorable )
    {
        if( monitorable.getTransitions() == null || monitorable.getTransitions().isEmpty() ) {
            return null;
        }
        return Iterables.getLast( monitorable.getTransitions() );
    }
}
