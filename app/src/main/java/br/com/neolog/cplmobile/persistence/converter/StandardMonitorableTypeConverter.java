package br.com.neolog.cplmobile.persistence.converter;

import android.arch.persistence.room.TypeConverter;

import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

public class StandardMonitorableTypeConverter
{

    @TypeConverter
    public static StandardMonitorableType toEnum(
        final String name )
    {
        return name == null ? null : StandardMonitorableType.valueOf( name );
    }

    @TypeConverter
    public static String toString(
        final StandardMonitorableType enumValue )
    {
        return enumValue == null ? null : enumValue.name();
    }
}
