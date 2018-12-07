package br.com.neolog.cplmobile.monitorable.converter;

import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.CARRIER;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.DISTANCE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.ESTIMATED_END;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.EXPECTED_END;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.PLATE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.REALIZED_DISTANCE;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.STATUS;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.VEHICLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.neolog.cplmobile.formatter.DateFormatter;
import br.com.neolog.cplmobile.formatter.ExternalizableEntityFormatter;
import br.com.neolog.cplmobile.formatter.NumberFormatter;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.monitoring.monitorable.model.api.CompletionStatus;
import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestCompletionData;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableInvoiceBuilder;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableTripBuilder;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

@RunWith( MockitoJUnitRunner.class )
public class MonitorablePropertiesConverterTest
{
    private static final int MONITORABLE_ID = 1;
    private static final int CHILD_ID = 2;

    @InjectMocks
    private MonitorablePropertiesConverter converter;
    @Mock
    private CompletionStatusConverter statusConverter;
    @Mock
    private ExternalizableEntityFormatter entityFormatter;
    @Mock
    private DateFormatter dateTimeFormatter;
    @Mock
    private NumberFormatter numberFormatter;

    private final RestMonitorable monitorable = new RestMonitorableTripBuilder()
        .id( MONITORABLE_ID )
        .code( "trip1" )
        .completion( RestCompletionData.fromStatus( CompletionStatus.IN_EXECUTION ) )
        .carrier( new RestExternalEntity( "carrier", "carrier", "carrier" ) )
        .truck( new RestExternalEntity( "plate", "plate", "plate" ) )
        .vehicle( new RestExternalEntity( "vehicle", "vehicle", "vehicle" ) )
        .value( 50.0 )
        .distance( 50.0 )
        .transitions( Collections.singletonList( new RestTransition(
            1,
            "end",
            "end",
            new DateTime( "2018-06-19T13:00:00.000-03:00" ),
            new DateTime( "2018-06-19T13:00:00.000-03:00" ),
            new DateTime( "2018-06-19T12:50:00.000-03:00" ),
            null,
            Duration.ZERO,
            null,
            null,
            TransitionStatus.IN_EXECUTION,
            Collections.emptySet(),
            Collections.emptyList(),
            0,
            null ) ) )
        .children( Collections.singleton(
            new RestMonitorableInvoiceBuilder()
                .id( CHILD_ID )
                .code( "invoice" )
                .completion( RestCompletionData.fromStatus( CompletionStatus.IN_EXECUTION ) )
                .distance( 30.0 )
                .build() ) )
        .build();

    @Before
    public void setUp()
    {
        when( statusConverter.convertToString( any() ) ).thenReturn( "inExecution" );
        when( entityFormatter.format( any() ) ).thenReturn( "" );
    }

    private List<MonitorableProperty> convert(
        final RestMonitorable monitorable )
    {
        return converter.convertProperties( monitorable );
    }

    @Test
    public void shouldConvertMonitorableStatusToString()
    {
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, STATUS, "inExecution" ) );
    }

    @Test
    public void shouldConvertCarrierToProperty()
    {
        when( entityFormatter.format( any() ) ).thenReturn( "carrier" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value )
            .contains( new MonitorableProperty( MONITORABLE_ID, CARRIER, "carrier" ) );
    }

    @Test
    public void shouldConvertTruckToProperty()
    {
        when( entityFormatter.format( any() ) ).thenReturn( "plate" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, PLATE, "plate" ) );
    }

    @Test
    public void shouldConvertVehicleToProperty()
    {
        when( entityFormatter.format( any() ) ).thenReturn( "vehicle" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle" ) );
    }

    @Test
    public void shouldConvertEstimatedFinalTime()
    {
        when( dateTimeFormatter.formatDateTime( new DateTime( "2018-06-19T12:50:00.000-03:00" ) ) )
            .thenReturn( "estimated_end" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, ESTIMATED_END, "estimated_end" ) );
    }

    @Test
    public void shouldConvertExpectedFinalTime()
    {
        when( dateTimeFormatter.formatDateTime( new DateTime( "2018-06-19T13:00:00.000-03:00" ) ) )
            .thenReturn( "expected_end" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, EXPECTED_END, "expected_end" ) );
    }

    @Test
    public void shouldConvertRealizedDistance()
    {
        when( numberFormatter.format( null ) ).thenReturn( "" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, REALIZED_DISTANCE, "" ) );
    }

    @Test
    public void shouldConvertPlannedDistance()
    {
        when( numberFormatter.format( 50.0 ) ).thenReturn( "50,00" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( MONITORABLE_ID, DISTANCE, "50,00" ) );
    }

    @Test
    public void shouldConvertChildrenProperties()
    {
        when( numberFormatter.format( 30.0 ) ).thenReturn( "30,00" );
        final List<MonitorableProperty> value = convert( monitorable );
        assertThat( value ).contains( new MonitorableProperty( CHILD_ID, DISTANCE, "30,00" ) );
    }
}