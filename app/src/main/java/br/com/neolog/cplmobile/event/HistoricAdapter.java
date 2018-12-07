package br.com.neolog.cplmobile.event;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.ItemHistoricOccurrenceBinding;
import br.com.neolog.cplmobile.occurrence.comments.OccurrenceCommentsActivity;

public class HistoricAdapter
        extends
        RecyclerView.Adapter<HistoricViewHolder> {

    private final LayoutInflater inflater;
    private final List<Event> events = new ArrayList<>();
    private Context context;

    HistoricAdapter(
            @NonNull final LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public HistoricViewHolder onCreateViewHolder(
            @NonNull final ViewGroup viewGroup,
            final int i) {
        context = viewGroup.getContext();
        final ItemHistoricOccurrenceBinding databinding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_historic_occurrence,
                viewGroup,
                false);
        return new HistoricViewHolder(databinding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final HistoricViewHolder viewHolder,
            final int position) {
        final Event event = events.get(position);
        viewHolder.setEvent(event);
        viewHolder.imageView_iconMessage.setOnClickListener(view -> openActivityMessage());
    }

    private void openActivityMessage() {
//        final Bundle bundle = new Bundle();
//        bundle.putInt("causeId", occurrenceCause.getId());
        final Intent intent = new Intent(context, OccurrenceCommentsActivity.class);
//            intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setData(
            @Nullable final List<Event> events) {
        this.events.clear();
        if (events != null) {
            this.events.addAll(events);
        }
        notifyDataSetChanged();
    }
}