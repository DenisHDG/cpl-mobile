package br.com.neolog.cplmobile.occurrence.selection;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import br.com.neolog.cplmobile.api.Resource;
import br.com.neolog.cplmobile.occurrence.OccurrenceCauseAndCategory;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseRepository;

public class OccurrenceCauseSelectionViewModel
    extends
        ViewModel
{

    private final MutableLiveData<String> causeName;
    private final LiveData<Resource<List<OccurrenceCauseAndCategory>>> occurrenceCauseAndCategory;

    @Inject
    OccurrenceCauseSelectionViewModel(
        final OccurrenceCauseRepository occurrenceCauseRepository )
    {
        this.causeName = new MutableLiveData<>();
        occurrenceCauseAndCategory = Transformations.switchMap( causeName, input -> {
            if( input == null || input.isEmpty() ) {
                return occurrenceCauseRepository.findAll();
            }
            return occurrenceCauseRepository.findByCauseName( input );
        } );
    }

    LiveData<Resource<List<OccurrenceCauseAndCategory>>> getOccurrenceCauseAndCategory()
    {
        return occurrenceCauseAndCategory;
    }

    void setCauseName(
        final String causeName )
    {
        this.causeName.setValue( causeName );
    }

}
