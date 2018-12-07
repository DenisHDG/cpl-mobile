package br.com.neolog.cplmobile.transition;

import java.util.List;

import javax.inject.Inject;

import android.support.annotation.NonNull;

import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestFinalizeTransitionDTO;
import retrofit2.Call;

public class TransitionService
{
    private final TransitionApi transitionApi;

    @Inject
    public TransitionService( final TransitionApi transitionApi ) {
        this.transitionApi = transitionApi;
    }

    public Call<List<RestRemoteMessage>> finalize(@NonNull final List<RestFinalizeTransitionDTO> transitions) {
        return transitionApi.finalizeTransitions( transitions );
    }
}
