package br.com.neolog.cplmobile.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.exception.ExceptionHandler;
import br.com.neolog.cplmobile.viewmodel.ViewModelFactory;

public class HistoricActivity
    extends
        AppCompatActivity
    implements
        Injectable
{
    @Inject
    ViewModelFactory viewModelFactory;

    private HistoricAdapter adapter;

    private List<Event> events;
    private List<Event> occurrenceEvents;
    private List<Event> transitionEvents;

    @Override
    protected void onCreate(
        final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_historic );
        Thread.setDefaultUncaughtExceptionHandler( new ExceptionHandler( HistoricActivity.this ) );
        configureView();
    }

    private void configureView()
    {
        adapter = new HistoricAdapter( getLayoutInflater() );
        final RecyclerView recyclerView = findViewById( R.id.activity_historic_recycler_view );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setAdapter( adapter );
        adapter.setData( events );

        final EventViewModel viewModel = ViewModelProviders.of( this, viewModelFactory ).get( EventViewModel.class );
        viewModel.findOccurrences().observe( this, occurrences -> {
            if( occurrences != null ) {
                final List<Event> newOccurrencesEvents = new ArrayList<>( ( Lists.transform( occurrences, Event::from ) ) );
                occurrenceEvents = newOccurrencesEvents;
                // TODO depende de transições
                // newOccurrencesEvents.addAll(transitionEvents);
                sortByDate( newOccurrencesEvents );
                events = newOccurrencesEvents;
                adapter.setData( newOccurrencesEvents );
            }
        } );
    }

    private static void sortByDate(
        final List<Event> newOccurrencesEvents )
    {
        Collections.sort( newOccurrencesEvents, (
            e1,
            e2 ) -> {
            if( e1.getDate() == null || e2.getDate() == null )
                return 0;
            return e1.getDate().compareTo( e2.getDate() );
        } );
    }
}
