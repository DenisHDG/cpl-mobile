package br.com.neolog.cplmobile.signal;

import static com.google.common.base.MoreObjects.toStringHelper;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity( tableName = "signal" )
public class Signal
{

    @PrimaryKey( autoGenerate = true )
    private int id;

    @ColumnInfo( name = "latitude" )
    private Double latitude;

    @ColumnInfo( name = "longitude" )
    private Double longitude;

    @ColumnInfo( name = "signal_time" )
    private DateTime signalTime;

    public Signal(
        final Double latitude,
        final Double longitude,
        final DateTime signalTime )
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.signalTime = signalTime;
    }

    public int getId()
    {
        return id;
    }

    public void setId(
        int id )
    {
        this.id = id;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(
        final Double latitude )
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(
        final Double longitude )
    {
        this.longitude = longitude;
    }

    public DateTime getSignalTime()
    {
        return signalTime;
    }

    public void setSignalTime(
        final DateTime signalTime )
    {
        this.signalTime = signalTime;
    }

    @Override
    public boolean equals(
        final Object o )
    {
        if( this == o ) {
            return true;
        }
        if( ! ( o instanceof Signal ) ) {
            return false;
        }
        final Signal that = (Signal) o;
        return Objects.equal( getId(), that.getId() ) &&
            Objects.equal( getLatitude(), that.getLatitude() ) &&
            Objects.equal( getLongitude(), that.getLongitude() ) &&
            Objects.equal( getSignalTime(), that.getSignalTime() );
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(
            getId(),
            getLatitude(),
            getLongitude(),
            getSignalTime() );
    }

    @Override
    public String toString()
    {
        return toStringHelper( this )
            .add( "id", id )
            .add( "latitude", latitude )
            .add( "longitude", longitude )
            .add( "signalTime", signalTime )
            .omitNullValues()
            .toString();
    }
}
