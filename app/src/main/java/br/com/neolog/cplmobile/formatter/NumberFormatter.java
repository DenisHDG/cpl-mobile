package br.com.neolog.cplmobile.formatter;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

public class NumberFormatter
{
    private final Locale locale;

    @Inject
    NumberFormatter(
        final Locale locale )
    {
        this.locale = locale;
    }

    public String format(
        final Number number )
    {
        if( number == null ) {
            return "";
        }
        final NumberFormat format = NumberFormat.getNumberInstance( locale );
        format.setMaximumFractionDigits( 2 );
        format.setMinimumFractionDigits( 2 );
        return format.format( number );
    }
}
