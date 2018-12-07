package br.com.neolog.cplmobile.occurrence;

import static com.google.common.base.MoreObjects.toStringHelper;

import android.arch.persistence.room.Embedded;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;

public class OccurrenceCauseAndCategory
{
    @Embedded
    private final OccurrenceCause occurrenceCause;
    @Embedded( prefix = "cat_" )
    private final OccurrenceCategory occurrenceCategory;

    public OccurrenceCauseAndCategory(
        final OccurrenceCause occurrenceCause,
        final OccurrenceCategory occurrenceCategory )
    {
        this.occurrenceCause = occurrenceCause;
        this.occurrenceCategory = occurrenceCategory;
    }

    public OccurrenceCategory getOccurrenceCategory()
    {
        return occurrenceCategory;
    }

    public OccurrenceCause getOccurrenceCause()
    {
        return occurrenceCause;
    }

    @NonNull
    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "occurrenceCategory", occurrenceCategory )
            .add( "occurrenceCause", occurrenceCause )
            .toString();
    }
}
