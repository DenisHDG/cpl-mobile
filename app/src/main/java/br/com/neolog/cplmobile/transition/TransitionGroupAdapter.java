package br.com.neolog.cplmobile.transition;

import static br.com.neolog.cplmobile.transition.TransitionGroupAdapterItem.TRANSITION_GROUP_VIEW_TYPE;
import static br.com.neolog.cplmobile.transition.TransitionGroupAdapterItem.TRANSITION_VIEW_TYPE;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.TransitionGroupViewHolderBinding;
import br.com.neolog.cplmobile.databinding.TransitionViewHolderBinding;

public class TransitionGroupAdapter
        extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;

    private final List<TransitionGroupAdapterItem> items = new ArrayList<>();


    public TransitionGroupAdapter(
            final LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setTransitionGroups(
            final List<TransitionGroup> groups) {
        items.clear();
        for (final TransitionGroup group : groups) {
            items.add(new TransitionGroupAdapterItem(group, null, null));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull final ViewGroup viewGroup,
            final int viewType) {

        switch (viewType) {
            case TRANSITION_GROUP_VIEW_TYPE:
                return createTransitionGroupViewHolder(viewGroup);
            case TRANSITION_VIEW_TYPE:
                return createTransitionViewHolder(viewGroup);
            default:
                throw new IllegalStateException("unknown view type: " + viewType);
        }
    }

    @NonNull
    private TransitionGroupViewHolder createTransitionGroupViewHolder(
            @NonNull final ViewGroup viewGroup) {

        final TransitionGroupViewHolderBinding dataBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.transition_group_view_holder,
                viewGroup,
                false);
        return new TransitionGroupViewHolder(dataBinding, this::groupClicked);
    }

    private void groupClicked(

            final TransitionGroupViewHolder transitionGroupViewHolder,
            final TransitionGroup transitionGroup) {
        final int position = transitionGroupViewHolder.getAdapterPosition();
        final TransitionGroupAdapterItem item = items.get(position);
        if (item.isCollapsed()) {
            for (final MonitorableAndTransition monitorableAndTransition : Lists.reverse(transitionGroup
                    .getMonitorableAndTransitions())) {
                items.add(position + 1, new TransitionGroupAdapterItem(null, monitorableAndTransition.getMonitorable(),
                        monitorableAndTransition.getTransition()));
            }
            notifyItemRangeInserted(position + 1, transitionGroup.getMonitorableAndTransitions().size());
        } else {
            final int transitionsCount = transitionGroup.getMonitorableAndTransitions().size();
            for (int i = position + transitionsCount; i > position; i--) {
                items.remove(i);
            }
            notifyItemRangeRemoved(position + 1, transitionsCount);
        }
        item.toggleCollapsed();
        notifyItemChanged( position );
    }

    @NonNull
    private TransitionViewHolder createTransitionViewHolder(
            @NonNull final ViewGroup viewGroup) {
        final TransitionViewHolderBinding dataBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.transition_view_holder,
                viewGroup,
                false);
        return new TransitionViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final RecyclerView.ViewHolder transitionGroupViewHolder,
            final int index) {
        final int viewType = getItemViewType(index);
        switch (viewType) {
            case TRANSITION_GROUP_VIEW_TYPE:
                bindTransitionGroup((TransitionGroupViewHolder) transitionGroupViewHolder, index);
                break;
            case TRANSITION_VIEW_TYPE:
                bindTransition((TransitionViewHolder) transitionGroupViewHolder, index);
                break;
            default:
                throw new IllegalStateException("unknown view type: " + viewType);
        }

    }

    private void bindTransitionGroup(
            @NonNull final TransitionGroupViewHolder transitionGroupViewHolder,
            final int index) {
        final TransitionGroupAdapterItem item = items.get(index);
        transitionGroupViewHolder.setTransitionGroup(item.getGroup());
        transitionGroupViewHolder.setCollapsed( item.isCollapsed() );
    }

    private void bindTransition(
            @NonNull final TransitionViewHolder transitionViewHolder,
            final int index) {
        final TransitionGroupAdapterItem item = items.get(index);
        transitionViewHolder.binding.setMonitorable(item.getMonitorable());
        transitionViewHolder.binding.setTransition(item.getTransition());
    }

    @Override
    public int getItemViewType(
            final int position) {
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Nullable
    TransitionGroupAdapterItem getItem(
            final int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return items.get(position);
    }
}
