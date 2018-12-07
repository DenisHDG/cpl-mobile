package br.com.neolog.cplmobile.location;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

import javax.inject.Inject;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;

import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.signal.SignalRepository;

public class LocationService
    implements
        Injectable
{

    private final static int REQUEST_CHECK_SETTINGS = 0x1;
    private final SignalRepository signalRepository;

    @Inject
    LocationService(
        final SignalRepository signalRepository )
    {
        this.signalRepository = signalRepository;
    }

    @Nullable
    public Task<Location> getLastLocation(
        final Context context )
    {
        if( checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return LocationServices.getFusedLocationProviderClient( context ).getLastLocation();
    }

    public void startTracking(
        final Activity activity )
    {
        final LocationRequest locationRequest = createLocationRequest();
        final Task<LocationSettingsResponse> locationSettingsResponseTask = configureLocationSettings( locationRequest, activity );
        locationSettingsResponseTask.addOnSuccessListener( activity, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            if( checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission( activity, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the
                // documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient( activity );
            fusedLocationClient.requestLocationUpdates( locationRequest, getLocationCallback(), null );
        } );

        locationSettingsResponseTask.addOnFailureListener( activity, e -> {
            if( e instanceof ResolvableApiException ) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    final ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult( activity, REQUEST_CHECK_SETTINGS );
                } catch( final IntentSender.SendIntentException sendEx ) {
                    // Ignore the error.
                }
            }
        } );
    }

    private LocationRequest createLocationRequest()
    {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval( 30000 );
        locationRequest.setFastestInterval( 15000 );
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        return locationRequest;
    }

    private Task<LocationSettingsResponse> configureLocationSettings(
        final LocationRequest locationRequest,
        final Activity activity )
    {

        final LocationSettingsRequest.Builder locationSettingsBuilder = new LocationSettingsRequest.Builder()
            .addLocationRequest( locationRequest );
        final SettingsClient client = LocationServices.getSettingsClient( activity );
        return client.checkLocationSettings( locationSettingsBuilder.build() );
    }

    private LocationCallback getLocationCallback()
    {
        return new LocationCallback() {
            @Override
            public void onLocationResult(
                final LocationResult locationResult )
            {
                if( locationResult == null ) {
                    return;
                }
                for( final Location location : locationResult.getLocations() ) {
                    signalRepository.createSignal( location );
                }
            }
        };
    }

}
