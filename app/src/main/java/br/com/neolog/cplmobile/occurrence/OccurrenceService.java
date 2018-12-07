package br.com.neolog.cplmobile.occurrence;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.exception.RemoteMessageHandler;
import br.com.neolog.cplmobile.location.LocationService;
import br.com.neolog.cplmobile.modal.ModalFactory;
import br.com.neolog.cplmobile.occurrence.job.ResendOccurrenceJobService;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;
import br.com.neolog.monitoring.monitorable.model.rest.RestLatLong;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestImpact;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestMonitorableOccurrence;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OccurrenceService {

    private static final String SPENT_OCCURRENCE = "Gasto";
    private static final String DELAY_OCCURRENCE = "Atraso";

    private final OccurrenceApi occurrenceApi;
    private final OccurrenceDao occurrenceDao;
    private final LocationService locationService;
    private final RemoteMessageHandler remoteMessageHandler;
    private final AppExecutors appExecutors;

    @Inject
    public OccurrenceService(
            final OccurrenceApi occurrenceApi,
            final OccurrenceDao occurrenceDao,
            final LocationService locationService,
            final RemoteMessageHandler remoteMessageHandler,
            final AppExecutors appExecutors) {
        this.occurrenceApi = occurrenceApi;
        this.occurrenceDao = occurrenceDao;
        this.locationService = locationService;
        this.remoteMessageHandler = remoteMessageHandler;
        this.appExecutors = appExecutors;
    }

    void createOccurrence(
            final Context context,
            final List<RestMonitorableIdentifier> monitorables,
            final RestImpact impact,
            final RestExternalEntity category,
            final RestExternalEntity cause) {
        final Task<Location> lastLocation = locationService.getLastLocation(context);
        if (lastLocation == null) {
            remoteMessageHandler.showModal(R.string.error_on_obtain_lat_long, context);
            return;
        }
        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(
                    final Location location) {

                final RestOccurrence occurrence = createRestOccurrence(impact, category, cause, location);

                Timber.d("Creating occurrence: %s", occurrence);

                final String message = context.getString(R.string.occurrence_confirmation_message);
                confirmAction(monitorables, occurrence, message, context, () -> {
                });
            }
        });
    }

    void createOccurrenceSpent(
            final double valueSpent,
            final Context context,
            final List<RestMonitorableIdentifier> monitorableIdentifier,
            final Runnable runnable) {

        final Task<Location> lastLocation = locationService.getLastLocation(context);
        if (lastLocation == null) {
            remoteMessageHandler.showModal(R.string.error_on_obtain_lat_long, context);
            return;
        }
        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(
                    final Location location) {

                final RestImpact impact = new RestImpact(null, valueSpent, null);
                final RestExternalEntity category = new RestExternalEntity(SPENT_OCCURRENCE, SPENT_OCCURRENCE, SPENT_OCCURRENCE);
                final RestExternalEntity cause = new RestExternalEntity(SPENT_OCCURRENCE, SPENT_OCCURRENCE, SPENT_OCCURRENCE);
                final RestOccurrence occurrence = createRestOccurrence(impact, category, cause, location);

                Timber.d("Creating occurrence: %s", occurrence);
                final String message = String.format(context.getString(R.string.occurrence_confirmation_spent_message), valueSpent,
                        SPENT_OCCURRENCE);
                confirmAction(monitorableIdentifier, occurrence, message, context, runnable);
            }
        });

    }

    void createOccurrenceDelay(
            final long delayInMillis,
            final Context context,
            final List<RestMonitorableIdentifier> monitorableIdentifier,
            final Runnable runnable) {

        final Task<Location> lastLocation = locationService.getLastLocation(context);
        if (lastLocation == null) {
            remoteMessageHandler.showModal(R.string.error_on_obtain_lat_long, context);
            return;
        }
        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(
                    final Location location) {

                final RestImpact impact = new RestImpact(delayInMillis, null, null);
                final RestExternalEntity category = new RestExternalEntity(DELAY_OCCURRENCE, DELAY_OCCURRENCE, DELAY_OCCURRENCE);
                final RestExternalEntity cause = new RestExternalEntity(DELAY_OCCURRENCE, DELAY_OCCURRENCE, DELAY_OCCURRENCE);
                final RestOccurrence occurrence = createRestOccurrence(impact, category, cause, location);

                Timber.d("creating occurrence: %s", occurrence);
                final String durationMessage = getDurationMessage(delayInMillis);
                final String message = String.format(context.getString(R.string.occurrence_confirmation_delay_message), durationMessage,
                        DELAY_OCCURRENCE);
                confirmAction(monitorableIdentifier, occurrence, message, context, runnable);
            }
        });

    }

    private RestOccurrence createRestOccurrence(
            final RestImpact impact,
            final RestExternalEntity category,
            final RestExternalEntity cause,
            final Location location) {
        return new RestOccurrence(null,
                null,
                DateTime.now(),
                cause,
                category,
                impact,
                null,
                getWhere(location),
                Collections.emptySet(),
                Collections.emptyList(),
                Collections.emptySet(),
                Collections.emptySet());
    }

    private static RestLatLong getWhere(
            final Location location) {
        if (location == null) {
            return null;
        }
        return new RestLatLong(location.getLatitude(), location.getLongitude());
    }

    private void confirmAction(
            @NonNull final List<RestMonitorableIdentifier> monitorablesIdentifier,
            @NonNull final RestOccurrence occurrence,
            @NonNull final String message,
            @NonNull final Context context,
            @NonNull final Runnable runnable) {
        ModalFactory.with(context)
                .confirm(message, () -> {
                    runnable.run();
                    sendOccurrenceComment(monitorablesIdentifier, occurrence, context);
                })
                .show();
    }

    private void sendOccurrenceComment(
            final List<RestMonitorableIdentifier> monitorableIdentifier,
            final RestOccurrence restOccurrence,
            final Context context) {
        final RestMonitorableOccurrence restMonitorableOccurrence = new RestMonitorableOccurrence(monitorableIdentifier, restOccurrence);
        final Occurrence occurrence = Occurrence.from(restOccurrence);
        occurrenceApi.createOccurrenceComment(restMonitorableOccurrence, null).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(
                    @NonNull final Call<Integer> call,
                    @NonNull final Response<Integer> response) {
                if (response.isSuccessful()) {
                    final Integer remoteServerId = response.body();
                    occurrence.setRemoteServerId(remoteServerId);
                } else {
                    ResendOccurrenceJobService.schedule(context);
                }
                // TODO persistir comentários
                insertOccurrence(occurrence);
                ModalFactory.with(context)
                        .success(context.getString(R.string.occurrece_save_success_message))
                        .show();
            }

            @Override
            public void onFailure(
                    @NonNull final Call<Integer> call,
                    @NonNull final Throwable t) {
                insertOccurrence(occurrence);
                ResendOccurrenceJobService.schedule(context);
            }
        });
    }

    private void insertOccurrence(
            @NonNull final Occurrence occurrence) {
        appExecutors.diskIO().execute(() -> occurrenceDao.insert(occurrence));
    }

    private String getDurationMessage(
            final long delayInMillis) {
        final Duration duration = Duration.millis(delayInMillis);
        final StringBuilder builder = new StringBuilder();
        final long hours = duration.getStandardHours();
        if (hours > 0) {
            builder.append(hours);
            builder.append(" horas");
            final long minutes = duration.getStandardMinutes() - (hours * 60);
            if (minutes > 0) {
                builder.append(" e ");
                builder.append(minutes);
                builder.append(" minutos");
            }
            return builder.toString();
        }
        builder.append(duration.getStandardMinutes());
        builder.append(" minutos");
        return builder.toString();
    }
}
