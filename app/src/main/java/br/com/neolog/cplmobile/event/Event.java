package br.com.neolog.cplmobile.event;

import org.joda.time.DateTime;

import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.occurrence.LatLong;
import br.com.neolog.cplmobile.occurrence.Occurrence;

public class Event
{
    private final EventType eventType;
    private final Integer id;
    private final String identifier;
    private final DateTime date;
    private final LatLong location;

    private Event(
        @NonNull final EventType eventType,
        @NonNull final Integer id,
        @NonNull final String identifier,
        @NonNull final DateTime date,
        @NonNull final LatLong location )
    {
        this.eventType = eventType;
        this.id = id;
        this.identifier = identifier;
        this.date = date;
        this.location = location;
    }

    public static Event from(
            @NonNull final Occurrence occurrence )
    {
        final String identifier = occurrence.getCategory().getName() + " - " + occurrence.getCause().getName();
        return new Event( EventType.OCCURRENCE, occurrence.getId(), identifier, occurrence.getTimestamp(), occurrence
                .getWhere() );
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public Integer getId()
    {
        return id;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public DateTime getDate()
    {
        return date;
    }

    public LatLong getLocation()
    {
        return location;
    }
}
