package br.com.neolog.cplmobile.occurrence;

import java.util.List;

import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestMonitorableOccurrence;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrence;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface OccurrenceApi
{
    @Multipart
    @POST( "monitoring-business-rules/occurrence/create" )
    Call<Integer> createOccurrenceComment(
        @Part( "occurrence" ) RestMonitorableOccurrence occurrence,
        @Part MultipartBody.Part attachments );

    @GET( "monitoring/occurrence/{monitorableId}" )
    Call<List<RestOccurrence>> listForMonitorable(
        @Path( "monitorableId" ) int monitorableId );
}
