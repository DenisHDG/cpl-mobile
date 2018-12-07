package br.com.neolog.cplmobile.occurrence;

import static com.google.common.base.MoreObjects.toStringHelper;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import br.com.neolog.monitoring.monitorable.model.rest.RestLatLong;

public class LatLong
{
    @ColumnInfo( name = "latitude" )
    private final Double latitude;

    @ColumnInfo( name = "longitude" )
    private final Double longitude;

    public LatLong(
        final Double latitude,
        final Double longitude )
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LatLong from(
        final RestLatLong other )
    {
        if( other == null ) {
            return null;
        }
        return new LatLong( other.getLatitude(), other.getLongitude() );
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode( latitude, longitude );
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
        if( ! ( o instanceof LatLong ) ) {
            return false;
        }

        final LatLong other = (LatLong) o;
        return Objects.equal( latitude, other.getLatitude() )
                && Objects.equal( longitude, other.getLongitude() );
    }

    @NonNull
    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "latitude", latitude )
            .add( "longitude", longitude )
            .toString();
    }
}
