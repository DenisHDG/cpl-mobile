package br.com.neolog.cplmobile.transition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import br.com.neolog.cplmobile.databinding.TransitionGroupViewHolderBinding;

class TransitionGroupViewHolder
        extends
        RecyclerView.ViewHolder {
    private final TransitionGroupViewHolderBinding binding;

    interface OnClickListener {
        void groupClicked(
                TransitionGroupViewHolder viewHolder,
                TransitionGroup transitionGroup);
    }

    TransitionGroupViewHolder(
            @NonNull final TransitionGroupViewHolderBinding binding,
            final OnClickListener onClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(view -> onClickListener.groupClicked(this, binding.getTransitionGroup()));
        setCollapsed( true );
    }

    void setTransitionGroup(
            final TransitionGroup group) {
        binding.setTransitionGroup(group);
    }

    void setCollapsed(
        final boolean collapsed )
    {
        binding.setCollapsed( collapsed );
    }
}
