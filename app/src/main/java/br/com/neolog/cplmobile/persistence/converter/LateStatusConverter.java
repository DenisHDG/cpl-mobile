package br.com.neolog.cplmobile.persistence.converter;

import android.arch.persistence.room.TypeConverter;

import br.com.neolog.cplmobile.monitorable.model.LateStatus;

public class LateStatusConverter
{

    @TypeConverter
    public static LateStatus toEnum(
        final String name )
    {
        return name == null ? null : LateStatus.valueOf( name );
    }

    @TypeConverter
    public static String toString(
        final LateStatus enumValue )
    {
        return enumValue == null ? null : enumValue.name();
    }
}
