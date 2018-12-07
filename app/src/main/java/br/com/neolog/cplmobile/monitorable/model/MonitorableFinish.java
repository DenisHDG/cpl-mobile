package br.com.neolog.cplmobile.monitorable.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity( tableName = "monitorable_finish" )
public class MonitorableFinish
{
    @PrimaryKey
    @ColumnInfo( name = "monitorable_id" )
    private final int monitorableId;

    public MonitorableFinish(
        final int monitorableId )
    {
        this.monitorableId = monitorableId;
    }

    public int getMonitorableId()
    {
        return monitorableId;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final MonitorableFinish that = (MonitorableFinish) o;
        return monitorableId == that.monitorableId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( monitorableId );
    }

    @NonNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .omitNullValues()
            .add( "monitorableId", monitorableId )
            .toString();
    }
}
