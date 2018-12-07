package br.com.neolog.cplmobile.transition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import br.com.neolog.cplmobile.databinding.TransitionViewHolderBinding;

class TransitionViewHolder
    extends
        RecyclerView.ViewHolder
{
    final TransitionViewHolderBinding binding;

    TransitionViewHolder(
        @NonNull final TransitionViewHolderBinding binding )
    {
        super( binding.getRoot() );
        this.binding = binding;
    }
}
