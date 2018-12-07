package br.com.neolog.cplmobile.occurrence.selection;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.ActivityOccurrenceSelectionListBinding;
import br.com.neolog.cplmobile.occurrence.NewOccurrenceActivity;
import br.com.neolog.cplmobile.occurrence.OccurrenceCauseAndCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;

public class OccurrenceCauseSelectionAdapter
        extends
        RecyclerView.Adapter<OccurrenceCauseSelectionAdapter.InnerViewHolder> {

    private final List<OccurrenceCauseAndCategory> occurrenceCauseAndCategories = new ArrayList<>();
    private Context context;

    private final LayoutInflater inflater;

    OccurrenceCauseSelectionAdapter(
            final LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(
            @NonNull final ViewGroup viewGroup,
            final int viewType) {
        context = viewGroup.getContext();
        final ActivityOccurrenceSelectionListBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.activity_occurrence_selection_list,
                viewGroup,
                false);
        return new InnerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final InnerViewHolder viewHolder,
            final int position) {
        final OccurrenceCauseAndCategory occurrenceCauseAndCategory = occurrenceCauseAndCategories.get(position);
        final OccurrenceCause occurrenceCause = occurrenceCauseAndCategory.getOccurrenceCause();
        final OccurrenceCategory occurrenceCategory = occurrenceCauseAndCategory.getOccurrenceCategory();

        viewHolder.setOccurrenceCategory(occurrenceCategory);
        viewHolder.setOccurrenceCause(occurrenceCause);

        final Bundle bundle = new Bundle();
        bundle.putInt("causeId", occurrenceCause.getId());
        viewHolder.linearLayout.setOnClickListener(v -> {
            final Intent intent = new Intent(context, NewOccurrenceActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return occurrenceCauseAndCategories.size();
    }

    public void setData(
            @Nullable final List<OccurrenceCauseAndCategory> occurrenceCauseAndCategories) {
        this.occurrenceCauseAndCategories.clear();
        if (occurrenceCauseAndCategories != null) {
            this.occurrenceCauseAndCategories.addAll(occurrenceCauseAndCategories);
        }
        notifyDataSetChanged();
    }

    static class InnerViewHolder
            extends
            RecyclerView.ViewHolder {
        private final LinearLayout linearLayout;
        private final ActivityOccurrenceSelectionListBinding databinding;

        InnerViewHolder(
                @NonNull final ActivityOccurrenceSelectionListBinding databinding) {
            super(databinding.getRoot());
            this.databinding = databinding;

            linearLayout = itemView.findViewById(R.id.activity_report_occurrence_list_layout);
        }

        void setOccurrenceCategory(
                final OccurrenceCategory occurrenceCategory) {
            databinding.setOccurrenceCategory(occurrenceCategory);
        }

        void setOccurrenceCause(
                final OccurrenceCause occurrenceCause) {
            databinding.setOccurrenceCause(occurrenceCause);
        }
    }
}