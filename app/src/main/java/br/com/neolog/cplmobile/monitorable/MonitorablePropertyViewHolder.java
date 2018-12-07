package br.com.neolog.cplmobile.monitorable;

import android.support.v7.widget.RecyclerView;

import br.com.neolog.cplmobile.databinding.MonitorablePropertiesBinding;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;

class MonitorablePropertyViewHolder
    extends
        RecyclerView.ViewHolder
{
    private final MonitorablePropertiesBinding databinding;

    public MonitorablePropertyViewHolder(
        final MonitorablePropertiesBinding databinding )
    {
        super( databinding.getRoot() );
        this.databinding = databinding;
    }

    public void setProperty(
        final MonitorableProperty property )
    {
        databinding.setMonitorableProperty( property );
    }
}
