package br.com.neolog.cplmobile.monitorable.repo;

import static br.com.neolog.cplmobile.monitorable.model.LateStatus.DELAYED;
import static br.com.neolog.cplmobile.monitorable.model.LateStatus.IN_TIME;
import static br.com.neolog.cplmobile.monitorable.model.LateStatus.WARNING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableTrip;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableTripBuilder;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

@RunWith( MockitoJUnitRunner.class )
public class MonitorableSignalTimeCalculatorTest
{
    private static final DateTime CURRENT_NOW = DateTime.parse( "2018-10-29T22:00:00.000" );

    @InjectMocks
    private MonitorableSignalTimeCalculator calculator;

    @Mock
    private RestMonitorableStatus status;
    @Mock
    private RestTransition transition;

    @Before
    public void setUp()
    {
        DateTimeUtils.setCurrentMillisFixed( CURRENT_NOW.getMillis() );
        when( status.getCurrentTransition() ).thenReturn( transition );
    }

    @Test
    public void shouldReturnDelayedWhenExpectedAndEstimatedIsBeforeNow()
    {
        when( transition.getEstimatedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 10 ) );
        when( transition.getExpectedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 5 ) );
        assertThat( calculator.calculateLateStatus( status ) ).isEqualTo( DELAYED );
    }

    @Test
    public void shouldReturnWarningWhenExpectedIsAfterNowAndEstimatedIsBeforeNow()
    {
        when( transition.getEstimatedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 10 ) );
        when( transition.getExpectedTimestamp() ).thenReturn( CURRENT_NOW.plusMinutes( 5 ) );
        assertThat( calculator.calculateLateStatus( status ) ).isEqualTo( WARNING );
    }

    @Test
    public void shouldReturnInTimeWhenExpectedAndEstimatedIsAfterNow()
    {
        when( transition.getEstimatedTimestamp() ).thenReturn( CURRENT_NOW.plusMinutes( 10 ) );
        when( transition.getExpectedTimestamp() ).thenReturn( CURRENT_NOW.plusMinutes( 5 ) );
        assertThat( calculator.calculateLateStatus( status ) ).isEqualTo( IN_TIME );
    }

    @Test
    public void shouldReturnInTimeWhenMonitorableHasNoTransitions()
    {
        final RestMonitorableTrip monitorable = new RestMonitorableTripBuilder().build();
        assertThat( calculator.calculateLateStatus( monitorable ) ).isEqualTo( IN_TIME );
    }

    @Test
    public void shouldReturnLateStatusWhenMonitorableOneTransition()
    {
        final RestMonitorableTrip monitorable = new RestMonitorableTripBuilder()
            .transitions( Collections.singletonList( transition ) )
            .build();
        when( transition.getEstimatedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 10 ) );
        when( transition.getExpectedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 5 ) );
        assertThat( calculator.calculateLateStatus( monitorable ) ).isEqualTo( DELAYED );
    }

    @Test
    public void shouldReturnCurrentTransitionStatus()
    {
        final RestTransition finalizedTransition = mock( RestTransition.class );
        final RestMonitorableTrip monitorable = new RestMonitorableTripBuilder()
            .transitions( ImmutableList.of( finalizedTransition, transition ) )
            .build();
        when( finalizedTransition.getStatus() ).thenReturn( TransitionStatus.FINALIZED );
        when( transition.getEstimatedTimestamp() ).thenReturn( CURRENT_NOW.minusMinutes( 10 ) );
        when( transition.getExpectedTimestamp() ).thenReturn( CURRENT_NOW.plusMinutes( 5 ) );
        assertThat( calculator.calculateLateStatus( monitorable ) ).isEqualTo( WARNING );
    }
}