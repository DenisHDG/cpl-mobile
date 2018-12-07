package br.com.neolog.cplmobile.transition;

import com.google.common.base.MoreObjects;

import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestTransition;

class TransitionGroupAdapterItem {
    static final int TRANSITION_GROUP_VIEW_TYPE = 1;
    static final int TRANSITION_VIEW_TYPE = 2;

    public boolean collapsed = true;
    private final TransitionGroup group;
    private final RestMonitorable monitorable;
    private final RestTransition transition;

    TransitionGroupAdapterItem(
            final TransitionGroup group,
            final RestMonitorable monitorable,
            final RestTransition transition) {
        this.group = group;
        this.monitorable = monitorable;
        this.transition = transition;
    }

    int getViewType() {
        return group != null ? TRANSITION_GROUP_VIEW_TYPE : TRANSITION_VIEW_TYPE;
    }

    boolean isCollapsed() {
        return collapsed;
    }

    void toggleCollapsed() {
        collapsed = !collapsed;
    }

    TransitionGroup getGroup() {
        return group;
    }

    public RestMonitorable getMonitorable() {
        return monitorable;
    }

    RestTransition getTransition() {
        return transition;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("group", group)
                .add("monitorable", monitorable)
                .add("transition", transition)
                .toString();
    }
}
