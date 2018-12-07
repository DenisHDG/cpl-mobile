package br.com.neolog.cplmobile.event;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.ItemHistoricOccurrenceBinding;

public class HistoricViewHolder
        extends
        RecyclerView.ViewHolder {
    private final ItemHistoricOccurrenceBinding databinding;
    public ImageView imageView_iconMessage;


    public HistoricViewHolder(
            final ItemHistoricOccurrenceBinding databinding) {
        super(databinding.getRoot());
        this.databinding = databinding;
        imageView_iconMessage = itemView.findViewById(R.id.imageView_iconMessage);
    }

    public void setEvent(
            final Event event) {
        databinding.setEvent(event);
    }
}