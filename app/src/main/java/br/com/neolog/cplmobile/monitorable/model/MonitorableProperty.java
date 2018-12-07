package br.com.neolog.cplmobile.monitorable.model;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;

@Entity(
    tableName = "monitorable_property",
    primaryKeys = {
        "monitorable_id", "name"
    },
    indices = {
        @Index( name = "mon_prop_id_idx", value = {
            "monitorable_id", "name"
        } ),
        @Index( name = "mon_prop_mon_idx", value = {
            "monitorable_id"
        } ),
    },
    foreignKeys = {
        @ForeignKey(
            childColumns = "monitorable_id",
            entity = Monitorable.class,
            parentColumns = "id",
            onDelete = CASCADE )
    } )
public class MonitorableProperty
{
    @ColumnInfo( name = "monitorable_id" )
    private final int monitorableId;
    @NonNull
    @ColumnInfo( name = "name" )
    private final MonitorablePropertyType propertyName;
    @Nullable
    @ColumnInfo( name = "value" )
    private final String propertyValue;

    public static MonitorableProperty from(
        @NonNull final RestMonitorable monitorable,
        @NonNull final MonitorablePropertyType propertyName,
        final String value )
    {
        return new MonitorableProperty( monitorable.getId(), propertyName, value );
    }

    public MonitorableProperty(
        final int monitorableId,
        @NonNull final MonitorablePropertyType propertyName,
        @Nullable final String propertyValue )
    {
        this.monitorableId = monitorableId;
        this.propertyName = checkNotNull( propertyName, "propertyName" );
        this.propertyValue = propertyValue;
    }

    public int getMonitorableId()
    {
        return monitorableId;
    }

    @NonNull
    public MonitorablePropertyType getPropertyName()
    {
        return propertyName;
    }

    @Nullable
    public String getPropertyValue()
    {
        return propertyValue;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final MonitorableProperty that = (MonitorableProperty) o;
        return Objects.equal( monitorableId, that.monitorableId ) &&
            Objects.equal( propertyName, that.propertyName ) &&
            Objects.equal( propertyValue, that.propertyValue );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( monitorableId, propertyName, propertyValue );
    }

    @NonNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "monitorableId", monitorableId )
            .add( "propertyName", propertyName )
            .add( "propertyValue", propertyValue )
            .toString();
    }
}
