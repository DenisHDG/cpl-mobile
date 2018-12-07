package br.com.neolog.cplmobile.formatter;

import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

public class DateFormatter
{
    private final Locale locale;

    @Inject
    DateFormatter(
        final Locale locale )
    {
        this.locale = locale;
    }

    public String formatDateTime(
        final ReadableInstant instant )
    {
        if( instant == null ) {
            return "";
        }
        return DateTimeFormat.shortDateTime()
            .withLocale( locale )
            .print( instant );
    }
}
