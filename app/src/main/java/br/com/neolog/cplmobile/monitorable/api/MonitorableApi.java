package br.com.neolog.cplmobile.monitorable.api;

import java.util.List;

import android.arch.lifecycle.LiveData;

import br.com.neolog.cplmobile.api.ApiResponse;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MonitorableApi
{
    @GET( "monitoring/monitorable/{monitorableId}" )
    Call<RestMonitorable> findById(
        @Path( "monitorableId" ) int monitorableId );

    @POST( "monitoring/monitorable/findByIds" )
    LiveData<ApiResponse<List<RestMonitorable>>> findByIds(
        @Body List<Integer> monitorableIds );

    @GET( "monitoring-business-rules/monitorable/find/mobile/deviceId-and-providerId" )
    LiveData<ApiResponse<RestMonitorable>> findByDeviceIdAndProviderId(
        @Query( "deviceId" ) String deviceId,
        @Query( "providerId" ) String providerId );

    @POST( "monitoring-business-rules/monitorable/finishMonitorablesById" )
    Call<List<RestRemoteMessage>> finishMonitorables(
        @Body List<Integer> monitorableId );
}
