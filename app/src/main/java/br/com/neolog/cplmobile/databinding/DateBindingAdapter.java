package br.com.neolog.cplmobile.databinding;

import javax.inject.Inject;

import org.joda.time.DateTime;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import br.com.neolog.cplmobile.formatter.DateFormatter;

public class DateBindingAdapter
{
    private final DateFormatter dateFormatter;

    @Inject
    DateBindingAdapter(
        final DateFormatter dateFormatter )
    {
        this.dateFormatter = dateFormatter;
    }

    @BindingAdapter( "android:text" )
    public void setText(
        final TextView view,
        final DateTime dateTime )
    {
        view.setText( dateFormatter.formatDateTime( dateTime ) );
    }
}
