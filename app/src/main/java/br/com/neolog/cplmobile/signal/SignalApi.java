package br.com.neolog.cplmobile.signal;

import br.com.neolog.tracking.mobile.model.RestMobileSignal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignalApi {

    @POST("tracking-mobile/signal")
    Call<Long> createSignal(
            @Body RestMobileSignal signal);

//    @GET("monitoring/comment/occurrence/{occurrenceId}")
//    Call<RestComment> getCommentsByOccurrence(@Path("occurrenceId") int id);
}
