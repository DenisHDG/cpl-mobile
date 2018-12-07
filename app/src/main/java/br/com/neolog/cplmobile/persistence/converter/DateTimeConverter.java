package br.com.neolog.cplmobile.persistence.converter;

import org.joda.time.DateTime;

import android.arch.persistence.room.TypeConverter;

public class DateTimeConverter
{

    @TypeConverter
    public static DateTime toDateTime(
        final Long timestamp )
    {
        return timestamp == null ? null : new DateTime( timestamp );
    }

    @TypeConverter
    public static Long toTimestamp(
        final DateTime date )
    {
        return date == null ? null : date.getMillis();
    }
}
