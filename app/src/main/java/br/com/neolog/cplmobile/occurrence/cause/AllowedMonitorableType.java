package br.com.neolog.cplmobile.occurrence.cause;

import static com.google.common.base.MoreObjects.toStringHelper;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity( tableName = "allowed_monitorable_type" )
public class AllowedMonitorableType
{

    @PrimaryKey
    private Integer id;

    @ColumnInfo( name = "occurrence_cause_id" )
    private final Integer occurrenceCauseId;

    @ColumnInfo( name = "monitorable_type" )
    private final String monitorableType;

    public AllowedMonitorableType(
        final Integer occurrenceCauseId,
        final String monitorableType )
    {
        this.occurrenceCauseId = occurrenceCauseId;
        this.monitorableType = monitorableType;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(
        final Integer id )
    {
        this.id = id;
    }

    public Integer getOccurrenceCauseId()
    {
        return occurrenceCauseId;
    }

    public String getMonitorableType()
    {
        return monitorableType;
    }

    @NonNull
    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "id", id )
            .add( "occurrenceCauseId", occurrenceCauseId )
            .add( "monitorableType", monitorableType)
            .toString();
    }
}
