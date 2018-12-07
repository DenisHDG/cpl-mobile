package br.com.neolog.cplmobile.occurrence.cause;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.occurrence.Impact;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrenceCause;

@Entity( tableName = "occurrence_cause",
    foreignKeys = @ForeignKey( entity = OccurrenceCategory.class, parentColumns = "id", childColumns = "category_id", onDelete = CASCADE ),
    indices = @Index( value = "category_id", name = "category_id" ) )
public class OccurrenceCause
{

    @PrimaryKey
    private Integer id;

    @ColumnInfo( name = "source_id" )
    private final String sourceId;

    @ColumnInfo( name = "name" )
    private final String name;

    @ColumnInfo( name = "description" )
    private final String description;

    @Embedded
    private final Impact impact;

    @ColumnInfo( name = "cause_order" )
    private final int causeOrder;

    @ColumnInfo( name = "category_id" )
    private final Integer categoryId;

    @ColumnInfo( name = "comment_required" )
    private final boolean commentRequired;

    @ColumnInfo( name = "image_required" )
    private final boolean imageRequired;

    public OccurrenceCause(
        final Integer id,
        final String sourceId,
        final String name,
        final String description,
        final Impact impact,
        final int causeOrder,
        final Integer categoryId,
        final boolean commentRequired,
        final boolean imageRequired )
    {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.description = description;
        this.impact = impact;
        this.causeOrder = causeOrder;
        this.categoryId = categoryId;
        this.commentRequired = commentRequired;
        this.imageRequired = imageRequired;
    }

    public static OccurrenceCause from(
        final RestOccurrenceCause restOccurrenceCause )
    {
        if( restOccurrenceCause == null ) {
            return null;
        }
        return new OccurrenceCause(
            restOccurrenceCause.getId(),
            restOccurrenceCause.getSourceId(),
            restOccurrenceCause.getName(),
            restOccurrenceCause.getDescription(),
            Impact.from( restOccurrenceCause.getDefaultImpact() ),
            restOccurrenceCause.getCauseOrder(),
            restOccurrenceCause.getOccurrenceCategory().getId(),
            restOccurrenceCause.isCommentRequired(),
            restOccurrenceCause.isImageRequired() );
    }

    public static List<OccurrenceCause> from(
        final List<RestOccurrenceCause> restOccurrenceCauses )
    {
        if( restOccurrenceCauses == null || restOccurrenceCauses.isEmpty() ) {
            return Collections.emptyList();
        }
        return Lists.transform( restOccurrenceCauses, OccurrenceCause::from );
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

    public Impact getImpact()
    {
        return impact;
    }

    public int getCauseOrder()
    {
        return causeOrder;
    }

    public Integer getCategoryId()
    {
        return categoryId;
    }

    public boolean isCommentRequired()
    {
        return commentRequired;
    }

    public boolean isImageRequired()
    {
        return imageRequired;
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
            .add( "impact", impact )
            .add( "causeOrder", causeOrder )
            .add( "categoryId", categoryId )
            .add( "commentRequired", commentRequired )
            .add( "imageRequired", imageRequired )
            .toString();
    }
}
