package br.com.neolog.cplmobile.occurrence.cause;

import android.arch.lifecycle.LiveData;

import java.util.List;

import br.com.neolog.cplmobile.api.ApiResponse;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestOccurrenceCause;
import retrofit2.Call;
import retrofit2.http.POST;

public interface OccurrenceCauseApi
{
    @POST( "monitoring/occurrence/causes/find" )
    LiveData<ApiResponse<List<RestOccurrenceCause>>> findCauses();
}
