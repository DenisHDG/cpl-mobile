package br.com.neolog.cplmobile.databinding;

import javax.inject.Inject;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import br.com.neolog.cplmobile.monitorable.repo.MonitorableTypeConverter;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

public class MonitorableTypeBindingAdapter
{
    private final MonitorableTypeConverter converter;

    @Inject
    public MonitorableTypeBindingAdapter(
        final MonitorableTypeConverter converter )
    {
        this.converter = converter;
    }

    @BindingAdapter( "android:text" )
    public void setText(
        final TextView text,
        final StandardMonitorableType type )
    {
        text.setText( converter.convertToResource( type ) );
    }
}
