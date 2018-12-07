package br.com.neolog.cplmobile.occurrence.category;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrenceCategory;

@Entity( tableName = "occurrence_category" )
public class OccurrenceCategory
{
    @PrimaryKey
    private final Integer id;

    @ColumnInfo( name = "source_id" )
    private final String sourceId;

    @ColumnInfo( name = "name" )
    private final String name;

    @ColumnInfo( name = "description" )
    private final String description;

    @ColumnInfo( name = "quantity" )
    private final boolean quantity;

    @ColumnInfo( name = "time_delta" )
    private final boolean timeDelta;

    @ColumnInfo( name = "value_delta" )
    private final boolean valueDelta;

    public OccurrenceCategory(
        final Integer id,
        final String sourceId,
        final String name,
        final String description,
        final boolean quantity,
        final boolean timeDelta,
        final boolean valueDelta )
    {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.timeDelta = timeDelta;
        this.valueDelta = valueDelta;
    }

    public static OccurrenceCategory from(
        final RestOccurrenceCategory restOccurrenceCategory )
    {
        if( restOccurrenceCategory == null ) {
            return null;
        }
        return new OccurrenceCategory(
            restOccurrenceCategory.getId(),
            restOccurrenceCategory.getSourceId(),
            restOccurrenceCategory.getName(),
            restOccurrenceCategory.getDescription(),
            restOccurrenceCategory.isQuantity(),
            restOccurrenceCategory.isTimeDelta(),
            restOccurrenceCategory.isValueDelta() );
    }

    public static List<OccurrenceCategory> from(
        final List<RestOccurrenceCategory> restOccurrenceCategories )
    {
        if( restOccurrenceCategories == null || restOccurrenceCategories.isEmpty() ) {
            return Collections.emptyList();
        }

        return Lists.transform( restOccurrenceCategories, OccurrenceCategory::from );
    }

    public Integer getId()
    {
        return id;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isQuantity()
    {
        return quantity;
    }

    public boolean isTimeDelta()
    {
        return timeDelta;
    }

    public boolean isValueDelta()
    {
        return valueDelta;
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
            .add( "quantity", quantity )
            .add( "timeDelta", timeDelta )
            .add( "valueDelta", valueDelta )
            .omitNullValues()
            .toString();
    }

}
