package br.com.neolog.cplmobile.occurrence;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.google.common.base.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestImpact;

public class Impact
{

    @ColumnInfo( name = "time_delta" )
    private final Long timeDelta;
    @ColumnInfo( name = "value_delta" )
    private final Double valueDelta;
    @ColumnInfo( name = "quantity_delta" )
    private final Integer quantityDelta;

    public Impact(
        final Long timeDelta,
        final Double valueDelta,
        final Integer quantityDelta )
    {
        this.timeDelta = timeDelta;
        this.valueDelta = valueDelta;
        this.quantityDelta = quantityDelta;
    }

    public static Impact from(
        final RestImpact restImpact )
    {
        if( restImpact == null ) {
            return null;
        }
        return new Impact( restImpact.getTimeDelta(), restImpact.getValueDelta(), restImpact.getQuantityDelta() );
    }

    public Long getTimeDelta()
    {
        return timeDelta;
    }

    public Double getValueDelta()
    {
        return valueDelta;
    }

    public Integer getQuantityDelta()
    {
        return quantityDelta;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( timeDelta, valueDelta, quantityDelta );
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o )
            return true;
        if( ! ( o instanceof Impact ) )
            return false;
        final Impact that = (Impact) o;
        return Objects.equal( getTimeDelta(), that.getTimeDelta() ) &&
            Objects.equal( getValueDelta(), that.getValueDelta() ) &&
            Objects.equal( getQuantityDelta(), that.getQuantityDelta() );
    }

    @NonNull
    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "timeDelta", timeDelta )
            .add( "valueDelta", valueDelta )
            .add( "quantityDelta", quantityDelta )
            .toString();
    }
}
