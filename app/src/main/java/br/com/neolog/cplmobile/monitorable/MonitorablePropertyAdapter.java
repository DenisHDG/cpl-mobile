package br.com.neolog.cplmobile.monitorable;

import java.util.ArrayList;
import java.util.List;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.MonitorablePropertiesBinding;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;

public class MonitorablePropertyAdapter
    extends
        RecyclerView.Adapter<MonitorablePropertyViewHolder>
{
    private final List<MonitorableProperty> properties = new ArrayList<>();

    private final LayoutInflater inflater;

    MonitorablePropertyAdapter(
        final LayoutInflater inflater )
    {
        this.inflater = inflater;
    }

    void setProperties(
        final List<MonitorableProperty> properties )
    {
        this.properties.clear();
        this.properties.addAll( properties );
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonitorablePropertyViewHolder onCreateViewHolder(
        @NonNull final ViewGroup viewGroup,
        final int viewType )
    {
        final MonitorablePropertiesBinding databinding = DataBindingUtil.inflate(
            inflater,
            R.layout.monitorable_properties,
            viewGroup,
            false );
        return new MonitorablePropertyViewHolder( databinding );
    }

    @Override
    public void onBindViewHolder(
        @NonNull final MonitorablePropertyViewHolder viewHolder,
        final int position )
    {
        final MonitorableProperty property = properties.get( position );
        viewHolder.setProperty( property );
    }

    @Override
    public int getItemCount()
    {
        return properties.size();
    }
}
