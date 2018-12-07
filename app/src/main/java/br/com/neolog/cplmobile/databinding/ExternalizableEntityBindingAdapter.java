package br.com.neolog.cplmobile.databinding;

import javax.inject.Inject;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import br.com.neolog.cplmobile.formatter.ExternalizableEntityFormatter;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

public class ExternalizableEntityBindingAdapter
{
    private final ExternalizableEntityFormatter converter;

    @Inject
    ExternalizableEntityBindingAdapter(
        final ExternalizableEntityFormatter converter )
    {
        this.converter = converter;
    }

    @BindingAdapter( "android:text" )
    public void setText(
        final TextView text,
        final RestExternalEntity entity )
    {
        text.setText( converter.format( entity ) );
    }
}
