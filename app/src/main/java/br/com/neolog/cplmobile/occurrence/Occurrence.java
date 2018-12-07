package br.com.neolog.cplmobile.occurrence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrence;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity(tableName = "occurrence")
public class Occurrence {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "remote_server_id")
    private Integer remoteServerId;

    @ColumnInfo(name = "source_id")
    private String sourceId;

    @ColumnInfo(name = "timestamp")
    private DateTime timestamp;

    @Embedded(prefix = "cause_")
    private ExternalEntity cause;

    @Embedded(prefix = "category_")
    private ExternalEntity category;

    @Embedded(prefix = "impact_")
    private Impact impact;

    @Embedded
    private LatLong where;

    Occurrence()
    {
    }

    @Ignore
    public Occurrence(
            final Integer remoteServerId,
            final String sourceId,
            final DateTime timestamp,
            final ExternalEntity cause,
            final ExternalEntity category,
            final Impact impact,
            final LatLong where) {
        this.remoteServerId = remoteServerId;
        this.sourceId = sourceId;
        this.timestamp = checkNotNull(timestamp);
        this.cause = cause;
        this.category = category;
        this.impact = impact;
        this.where = where;
    }

    public static List<Occurrence> from(
            final List<? extends RestOccurrence> restOccurrences) {
        if (restOccurrences == null) {
            return Collections.emptyList();
        }
        final Builder<Occurrence> builder = ImmutableList.builder();
        for (final RestOccurrence restOccurrence : restOccurrences) {
            builder.add(Occurrence.from(restOccurrence));
        }
        return builder.build();
    }

    public static Occurrence from(
            final RestOccurrence restOccurrence) {
        if (restOccurrence == null) {
            return null;
        }
        return new Occurrence(
                restOccurrence.getId(),
                restOccurrence.getSourceId(),
                restOccurrence.getTimestamp(),
                ExternalEntity.from(restOccurrence.getCause()),
                ExternalEntity.from(restOccurrence.getCategory()),
                Impact.from(restOccurrence.getImpact()),
                LatLong.from(restOccurrence.getWhere()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(
            final Integer id) {
        this.id = id;
    }

    public Integer getRemoteServerId() {
        return remoteServerId;
    }

    public void setRemoteServerId(
            final Integer remoteServerId) {
        this.remoteServerId = remoteServerId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(
            final String sourceId) {
        this.sourceId = sourceId;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            final DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ExternalEntity getCause() {
        return cause;
    }

    public void setCause(
            final ExternalEntity cause) {
        this.cause = cause;
    }

    public ExternalEntity getCategory() {
        return category;
    }

    public void setCategory(
            final ExternalEntity category) {
        this.category = category;
    }

    public Impact getImpact() {
        return impact;
    }

    public void setImpact(
            final Impact impact) {
        this.impact = impact;
    }

    public LatLong getWhere() {
        return where;
    }

    public void setWhere(
            final LatLong where) {
        this.where = where;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("id", id)
                .add("sourceId", sourceId)
                .add("timestamp", timestamp)
                .add("cause", cause)
                .add("category", category)
                .add("impact", impact)
                .add("where", where)
                .omitNullValues()
                .toString();
    }
}
