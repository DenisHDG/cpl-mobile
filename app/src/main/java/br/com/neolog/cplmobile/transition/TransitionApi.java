package br.com.neolog.cplmobile.transition;

import java.util.List;

import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestFinalizeTransitionDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TransitionApi {

    @POST( "monitoring-business-rules/transition/finalize" )
    Call<List<RestRemoteMessage>> finalizeTransitions(
        @Body List<RestFinalizeTransitionDTO> finalizedTransitions );
}
