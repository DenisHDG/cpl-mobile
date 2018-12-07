package br.com.neolog.cplmobile.signal.job;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;

import com.google.common.collect.FluentIterable;

import android.os.AsyncTask;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.CPLApplication;
import br.com.neolog.cplmobile.di.DaggerAppComponent;
import br.com.neolog.cplmobile.signal.Signal;
import br.com.neolog.cplmobile.signal.SignalDao;

public class RemoveOldSignalAsyncTask
    extends
        AsyncTask<Void,Void,Void>
{
    @Inject
    SignalDao signalDao;
    @Inject
    AppExecutors appExecutors;

    RemoveOldSignalAsyncTask()
    {
        DaggerAppComponent.builder().application( CPLApplication.getInstance() ).build().inject( this );
    }

    @Override
    protected Void doInBackground(
        final Void... voids )
    {

        final List<Signal> signals = signalDao.findAll();
        final List<Signal> signalsToRemove = FluentIterable.from( signals )
            .filter( signal -> hasMoreThanOneDay( signal ) )
            .toList();

        appExecutors.diskIO().execute( () -> {
            signalDao.delete( signalsToRemove );
        } );
        return null;
    }

    private boolean hasMoreThanOneDay(
        final Signal signal )
    {
        final DateTime now = DateTime.now();
        return signal.getSignalTime().plusDays( 1 ).isBefore( now );
    }

}
