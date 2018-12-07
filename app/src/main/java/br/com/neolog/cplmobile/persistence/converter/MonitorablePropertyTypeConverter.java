package br.com.neolog.cplmobile.persistence.converter;

import android.arch.persistence.room.TypeConverter;

import br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType;

public class MonitorablePropertyTypeConverter
{

    @TypeConverter
    public static MonitorablePropertyType toEnum(
        final String name )
    {
        return name == null ? null : MonitorablePropertyType.valueOf( name );
    }

    @TypeConverter
    public static String toString(
        final MonitorablePropertyType enumValue )
    {
        return enumValue == null ? null : enumValue.name();
    }
}
