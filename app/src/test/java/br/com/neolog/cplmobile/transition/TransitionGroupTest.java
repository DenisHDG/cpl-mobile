package br.com.neolog.cplmobile.transition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.monitorable.model.LateStatus;
import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransitionLocation;

@RunWith( MockitoJUnitRunner.class )
public class TransitionGroupTest
{
    @Mock
    private RestTransitionLocation location;
    @Mock
    private RestMonitorable monitorable;
    @Mock
    private RestTransition transition1;
    @Mock
    private RestTransition transition2;

    @Test
    public void shouldReturnDoneWhenThereIsNoTransitions()
    {
        final TransitionGroup group = createTransitionGroup();
        assertThat( group.getStatus() ).isEqualTo( TransitionGroupStatus.DONE );
    }

    @Test
    public void shouldReturnStatusOpenWhenAllTransitionsAreInExecution()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        when( transition2.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        final TransitionGroup group = createTransitionGroup( transition1, transition2 );
        assertThat( group.getStatus() ).isEqualTo( TransitionGroupStatus.OPEN );
    }

    @Test
    public void shouldReturnStatusInProgressWhenSomeTransitionsAreInExecution()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.FINALIZED );
        when( transition2.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        final TransitionGroup group = createTransitionGroup( transition1, transition2 );
        assertThat( group.getStatus() ).isEqualTo( TransitionGroupStatus.IN_PROGRESS );
    }

    @Test
    public void shouldReturnStatusDoneWhenNoTransitionsAreInExecution()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.FINALIZED );
        when( transition2.getStatus() ).thenReturn( TransitionStatus.CANCELLED );
        final TransitionGroup group = createTransitionGroup( transition1, transition2 );
        assertThat( group.getStatus() ).isEqualTo( TransitionGroupStatus.DONE );
    }

    @Test
    public void shouldReturnNullWhenLastTransitionIsNotInExecution()
    {
        when( transition2.getStatus() ).thenReturn( TransitionStatus.CANCELLED );
        final TransitionGroup group = createTransitionGroup( transition1, transition2 );
        assertThat( group.getNextTransition() ).isNull();
    }

    @Test
    public void shouldReturnNullWhenThereIsNoTransitions()
    {
        final TransitionGroup group = createTransitionGroup();
        assertThat( group.getNextTransition() ).isNull();
    }

    @Test
    public void shouldReturnFirstTransitionWhenAllTransitionsAreInExecution()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        when( transition2.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        final TransitionGroup group = createTransitionGroup( transition1, transition2 );
        assertThat( group.getNextTransition() ).isEqualTo( transition1 );
    }

    @Test
    public void shouldReturnLastTransitionWhenLastTransitionIsNextOne()
    {
        final RestTransition transition3 = mock( RestTransition.class, "transition3" );
        final RestTransition transition4 = mock( RestTransition.class, "transition4" );
        when( transition3.getStatus() ).thenReturn( TransitionStatus.FINALIZED );
        when( transition4.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        final TransitionGroup group = createTransitionGroup( transition1, transition2, transition3, transition4 );
        assertThat( group.getNextTransition() ).isEqualTo( transition4 );
    }

    @Test
    public void shouldReturnInTimeLateStatusWhenEmptyTransitions()
    {
        final TransitionGroup group = createTransitionGroup();
        assertThat( group.getLateStatus() ).isEqualTo( LateStatus.IN_TIME );
    }

    @Test
    public void shouldReturnInTimeLateStatusWhenCurrentTransitionIsAfterNow()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        when( transition1.getExpectedTimestamp() ).thenReturn( DateTime.now().plusHours( 1 ) );
        when( transition1.getEstimatedTimestamp() ).thenReturn( DateTime.now().plusHours( 1 ) );
        final TransitionGroup group = createTransitionGroup( transition1 );
        assertThat( group.getLateStatus() ).isEqualTo( LateStatus.IN_TIME );
    }

    @Test
    public void shouldReturnWarningLateStatusWhenCurrentTransitionIsExpectedToAfterNowButEstimatedIsBeforeNow()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        when( transition1.getExpectedTimestamp() ).thenReturn( DateTime.now().plusHours( 1 ) );
        when( transition1.getEstimatedTimestamp() ).thenReturn( DateTime.now().minusHours( 1 ) );
        final TransitionGroup group = createTransitionGroup( transition1 );
        assertThat( group.getLateStatus() ).isEqualTo( LateStatus.WARNING );
    }

    @Test
    public void shouldReturnDelayedLateStatusWhenCurrentTransitionIsBeforeNow()
    {
        when( transition1.getStatus() ).thenReturn( TransitionStatus.IN_EXECUTION );
        when( transition1.getExpectedTimestamp() ).thenReturn( DateTime.now().minusHours( 1 ) );
        when( transition1.getEstimatedTimestamp() ).thenReturn( DateTime.now().minusHours( 1 ) );
        final TransitionGroup group = createTransitionGroup( transition1 );
        assertThat( group.getLateStatus() ).isEqualTo( LateStatus.DELAYED );
    }

    @NonNull
    private TransitionGroup createTransitionGroup(
        final RestTransition... transitions )
    {
        final TransitionGroup group = new TransitionGroup( 1, location );
        for( final RestTransition transition : transitions ) {
            group.addTransition( new MonitorableAndTransition( monitorable, transition ) );
        }
        return group;
    }
}