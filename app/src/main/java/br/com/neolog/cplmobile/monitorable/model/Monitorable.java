package br.com.neolog.cplmobile.monitorable.model;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@Entity(
    tableName = "monitorable",
    indices = {
        @Index( name = "monitorable_id_idx", unique = true, value = {
            "id"
        } ),
        @Index( name = "monitorable_parent_idx", value = {
            "parent_id"
        } ),
        @Index( name = "monitorable_uq_code_type", unique = true, value = {
            "code", "type"
        } )
    }, foreignKeys = {
        @ForeignKey(
            childColumns = "parent_id",
            entity = Monitorable.class,
            parentColumns = "id",
            onDelete = CASCADE )
    } )
public class Monitorable
{
    @PrimaryKey
    @NonNull
    private final Integer id;

    @ColumnInfo( name = "code" )
    @NonNull
    private final String code;

    @ColumnInfo( name = "type" )
    @NonNull
    private final StandardMonitorableType type;

    @ColumnInfo( name = "late_status" )
    @NonNull
    private final LateStatus lateStatus;

    @Nullable
    @ColumnInfo( name = "parent_id" )
    private final Integer parentId;

    @ColumnInfo( name = "root" )
    private final boolean root;

    Monitorable(
        @NonNull final Integer id,
        @NonNull final String code,
        @NonNull final StandardMonitorableType type,
        @NonNull final LateStatus lateStatus,
        @Nullable final Integer parentId,
        final boolean root )
    {
        this.id = checkNotNull( id, "id is null" );
        this.code = checkNotNull( code, "code is null" );
        this.type = checkNotNull( type, "type is null" );
        this.lateStatus = checkNotNull( lateStatus, "lateStatus is null" );
        this.parentId = parentId;
        this.root = root;
    }

    @NonNull
    public Integer getId()
    {
        return id;
    }

    @NonNull
    public String getCode()
    {
        return code;
    }

    @NonNull
    public StandardMonitorableType getType()
    {
        return type;
    }

    @NonNull
    public LateStatus getLateStatus()
    {
        return lateStatus;
    }

    @Nullable
    public Integer getParentId()
    {
        return parentId;
    }

    public boolean isRoot()
    {
        return root;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        final Monitorable that = (Monitorable) o;
        return Objects.equal( id, that.id ) &&
            Objects.equal( code, that.code ) &&
            type == that.type &&
            lateStatus == that.lateStatus &&
            Objects.equal( parentId, that.parentId ) &&
            Objects.equal( root, that.root );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(
            id,
            code,
            type,
            lateStatus,
            parentId,
            root );
    }

    @NonNull
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .omitNullValues()
            .add( "id", id )
            .add( "code", code )
            .add( "type", type )
            .add( "lateStatus", lateStatus )
            .add( "parentId", parentId )
            .add( "root", root )
            .toString();
    }
}
