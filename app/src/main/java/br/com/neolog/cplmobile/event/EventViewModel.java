package br.com.neolog.cplmobile.event;

import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.occurrence.Occurrence;
import br.com.neolog.cplmobile.occurrence.OccurrenceDao;

class EventViewModel
    extends
        ViewModel
{
    private final LiveData<List<Occurrence>> occurrences;

    @Inject
    EventViewModel(
        @NonNull final OccurrenceDao occurrenceDao )
    {
        this.occurrences = occurrenceDao.findAll();
    }

    LiveData<List<Occurrence>> findOccurrences()
    {
        return occurrences;
    }
}
