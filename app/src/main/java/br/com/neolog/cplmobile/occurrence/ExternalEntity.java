package br.com.neolog.cplmobile.occurrence;

import static com.google.common.base.MoreObjects.toStringHelper;

import javax.annotation.Nullable;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

public class ExternalEntity
{
    @ColumnInfo( name = "id" )
    private final Integer id;

    @ColumnInfo( name = "source_id" )
    private final String sourceId;

    @ColumnInfo( name = "name" )
    private final String name;

    @ColumnInfo( name = "description" )
    private final String description;

    public ExternalEntity(
        final Integer id,
        final String sourceId,
        final String name,
        final String description )
    {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.description = description;
    }

    public static ExternalEntity from(
        final RestExternalEntity other )
    {
        if( other == null ) {
            return null;
        }

        return new ExternalEntity(
            other.getId(),
            other.getSourceId(),
            other.getName(),
            other.getDescription() );
    }

    @Nullable
    public Integer getId()
    {
        return id;
    }

    @Nullable
    public String getSourceId()
    {
        return sourceId;
    }

    @Nullable
    public String getName()
    {
        return name;
    }

    @Nullable
    public String getDescription()
    {
        return description;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( id );
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( o == null ) {
            return false;
        }
        if( o == this ) {
            return true;
        }
        if( ! ( o instanceof ExternalEntity ) ) {
            return false;
        }
        final ExternalEntity other = (ExternalEntity) o;
        return Objects.equal( id, other.getId() )
            && Objects.equal( sourceId, other.getSourceId() )
            && Objects.equal( name, other.getName() )
            && Objects.equal( description, other.getDescription() );
    }

    @NonNull
    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "id", id )
            .add( "sourceId", sourceId )
            .add( "name", name )
            .add( "description", description )
            .omitNullValues()
            .toString();
    }
}
