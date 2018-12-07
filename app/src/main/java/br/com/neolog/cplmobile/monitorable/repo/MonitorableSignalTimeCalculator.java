package br.com.neolog.cplmobile.monitorable.repo;

import static br.com.neolog.cplmobile.monitorable.model.LateStatus.DELAYED;
import static br.com.neolog.cplmobile.monitorable.model.LateStatus.IN_TIME;
import static br.com.neolog.cplmobile.monitorable.model.LateStatus.WARNING;

import java.util.Iterator;

import javax.inject.Inject;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.monitorable.model.LateStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableStatus;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

public class MonitorableSignalTimeCalculator
{

    @Inject
    MonitorableSignalTimeCalculator()
    {
    }

    @NonNull
    LateStatus calculateLateStatus(
        final RestMonitorableStatus status )
    {
        final RestTransition currentTransition = status.getCurrentTransition();
        return calculateLateStatus( currentTransition );
    }

    @NonNull
    public static LateStatus calculateLateStatus(
        @Nullable final RestTransition currentTransition )
    {
        if( currentTransition == null ) {
            return IN_TIME;
        }
        final DateTime estimatedTimestamp = currentTransition.getEstimatedTimestamp();
        final DateTime expectedTimestamp = currentTransition.getExpectedTimestamp();

        if( expectedTimestamp.isBeforeNow() && estimatedTimestamp.isBeforeNow() ) {
            return DELAYED;
        }
        if( expectedTimestamp.isAfterNow() && estimatedTimestamp.isBeforeNow() ) {
            return WARNING;
        }
        return IN_TIME;
    }

    @NonNull
    public LateStatus calculateLateStatus(
        @NonNull final RestMonitorable original )
    {
        if( original.getTransitions().isEmpty() ) {
            return IN_TIME;
        }
        final Iterator<RestTransition> transitionIterator = Lists.reverse( original.getTransitions() ).iterator();
        RestTransition currentTransition = transitionIterator.next();
        while( transitionIterator.hasNext() ) {
            final RestTransition transition = transitionIterator.next();
            if( transition.getStatus().isFinalized() ) {
                return calculateLateStatus( currentTransition );
            }
            currentTransition = transition;
        }
        return calculateLateStatus( currentTransition );
    }
}
