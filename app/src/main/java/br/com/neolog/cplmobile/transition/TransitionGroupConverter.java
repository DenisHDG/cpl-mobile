package br.com.neolog.cplmobile.transition;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.neolog.monitoring.monitorable.model.api.transition.Transition;
import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestFinalizeTransitionDTO;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

public class TransitionGroupConverter {
    public static List<RestFinalizeTransitionDTO> convert(@NonNull final TransitionStatus transitionStatus, @NonNull final TransitionGroup transitionGroup, @NonNull final Location currentLocation) {
        final List<RestFinalizeTransitionDTO> transitions = new ArrayList<>();
        final DateTime now = DateTime.now();
        for( final MonitorableAndTransition monitorableAndTransition : transitionGroup.getMonitorableAndTransitions() ) {
            final RestTransition transition = monitorableAndTransition.getTransition();
            if( isNotFinalized( transition ) ) {
                transitions.add( RestFinalizeTransitionDTO.fromTransitionId( transition.getId(), currentLocation.getLatitude(), currentLocation
                        .getLongitude(), transitionStatus, now ) );
            }
        }
        return transitions;
    }

    public static List<RestFinalizeTransitionDTO> convert(@NonNull final TransitionStatus transitionStatus, @NonNull final RestTransition transition, @NonNull final Location location) {
        if( isNotFinalized( transition ) ) {
            return Lists.newArrayList(RestFinalizeTransitionDTO.fromTransitionId(transition.getId(), location.getLatitude(), location
                    .getLongitude(), transitionStatus, DateTime.now()));
        }
        return Collections.emptyList();
    }

    private static boolean isNotFinalized(@NonNull final Transition transition ) {
        return ! transition.getStatus().isFinalized();
    }
}
