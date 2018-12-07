package br.com.neolog.cplmobile.transition;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestLocality;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransitionLocation;

@RunWith( MockitoJUnitRunner.class )
public class TransitionGrouperServiceTest
{
    @InjectMocks
    private TransitionGrouperService grouperService;

    @Mock
    private RestMonitorable monitorable;

    @Test
    public void shouldReturnEmptyListWhenNullTransitions()
    {
        assertThat( grouperService.groupTransitions( null ) ).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenEmptyTransitions()
    {
        whenMonitorableHasTransitions();
        assertThat( grouperService.groupTransitions( ImmutableList.of() ) ).isEmpty();
    }

    @Test
    public void shouldReturnSingleGroupWhenSingleTransition()
    {
        whenMonitorableHasTransitions( transition( 1, 1 ) );
        assertThat( execute() )
            .containsExactly( group( 0, monitorableAndTransition( monitorable, 1, 1 ) ) );
    }

    @Test
    public void shouldReturnDifferentGroupsWhenTransitionsHaveDifferentLocalities()
    {
        whenMonitorableHasTransitions( transition( 1, 1 ), transition( 2, 2 ) );
        assertThat( execute() )
            .containsExactly(
                group( 0, monitorableAndTransition( monitorable, 1, 1 ) ),
                group( 1, monitorableAndTransition( monitorable, 2, 2 ) ) );
    }

    @Test
    public void shouldReturnSameGroupWhenTransitionsHaveSameLocalities()
    {
        whenMonitorableHasTransitions( transition( 1, 1 ), transition( 2, 1 ) );
        assertThat( execute() )
            .containsExactly(
                group(
                    0,
                    monitorableAndTransition( monitorable, 1, 1 ),
                    monitorableAndTransition( monitorable, 2, 1 ) ) );
    }

    @Test
    public void shouldReturnSeparateGroupsWhenTransitionsOfTheSameLocationHaveAnotherLocationInTheMiddle()
    {
        whenMonitorableHasTransitions(
            transition( 1, 1 ),
            transition( 2, 2 ),
            transition( 3, 1 ) );

        assertThat( execute() )
            .containsExactly(
                group( 0, monitorableAndTransition( monitorable, 1, 1 ) ),
                group( 1, monitorableAndTransition( monitorable, 2, 2 ) ),
                group( 2, monitorableAndTransition( monitorable, 3, 1 ) ) );
    }

    @Test
    public void shouldGroupWhenMonitorableHasTransitionsInChildren()
    {
        final RestMonitorable child = mock( RestMonitorable.class );
        final ImmutableSet<RestMonitorable> children = ImmutableSet.of( child );

        when( monitorable.getChildren() ).thenReturn( children );
        whenTransitions( monitorable );
        whenTransitions( child, transition( 1, 1 ) );

        assertThat( execute() )
            .containsExactly( group( 0, monitorableAndTransition( child, 1, 1 ) ) );
    }

    @Test
    public void shouldGroupWhenTransitionsFromAllMonitorables()
    {
        final RestMonitorable secondMonitorable = mock( RestMonitorable.class );

        whenTransitions( monitorable, transition( 1, 1 ) );
        whenTransitions( secondMonitorable, transition( 2, 1 ) );

        assertThat( grouperService.groupTransitions( ImmutableList.of( monitorable, secondMonitorable ) ) )
            .containsExactly( group(
                0,
                monitorableAndTransition( monitorable, 1, 1 ),
                monitorableAndTransition( secondMonitorable, 2, 1 ) ) );
    }

    @Test
    public void shouldNotRepeatSameTransitions()
    {
        whenMonitorableHasTransitions( transition( 1, 1 ), transition( 1, 1 ) );
        assertThat( execute() )
            .containsExactly( group( 0, monitorableAndTransition( monitorable, 1, 1 ) ) );
    }

    private void whenMonitorableHasTransitions(
        final RestTransition... transitions )
    {
        whenTransitions( monitorable, transitions );
    }

    private void whenTransitions(
        final RestMonitorable monitorable,
        final RestTransition... transitions )
    {
        when( monitorable.getTransitions() ).thenReturn( ImmutableList.copyOf( transitions ) );
    }

    private List<TransitionGroup> execute()
    {
        return grouperService.groupTransitions( singletonList( monitorable ) );
    }

    private TransitionGroup group(
        final int index,
        final MonitorableAndTransition monitorableAndTransition,
        final MonitorableAndTransition... monitorableAndTransitions )
    {
        final TransitionGroup transitionGroup = new TransitionGroup( index, monitorableAndTransition.getTransition().getLocation() );
        transitionGroup.addTransition( monitorableAndTransition );
        for( final MonitorableAndTransition otherTransition : monitorableAndTransitions ) {
            transitionGroup.addTransition( otherTransition );
        }
        return transitionGroup;
    }

    private RestTransition transition(
        final Integer transitionId,
        final Integer localityId )
    {
        final DateTime expected = DateTime.parse( "2018-10-31T13:15:00.000-03:00" );
        final RestTransitionLocation location = new RestTransitionLocation(
            0,
            new RestLocality(
                localityId,
                "sid" + localityId,
                "name" + localityId,
                "desc" + localityId,
                "street",
                "state",
                "city",
                "district",
                null,
                1.0,
                2.0,
                "complement",
                "number",
                "country",
                "zipCode" ),
            null,
            null );
        return new RestTransition(
            transitionId,
            "sid" + transitionId,
            "name" + transitionId,
            expected,
            expected.plusMinutes( 10 ),
            expected,
            null,
            Duration.ZERO,
            location,
            null,
            TransitionStatus.IN_EXECUTION,
            Collections.emptySet(),
            Collections.emptyList(),
            1,
            null );
    }

    private MonitorableAndTransition monitorableAndTransition(
        final RestMonitorable monitorable,
        final int transitionId,
        final int localityId )
    {
        return new MonitorableAndTransition( monitorable, transition( transitionId, localityId ) );
    }
}