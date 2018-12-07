package br.com.neolog.cplmobile.occurrence.selection;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.event.HistoricActivity;
import br.com.neolog.cplmobile.exception.ExceptionHandler;
import br.com.neolog.cplmobile.viewmodel.ViewModelFactory;

public class OccurrenceCauseSelectionActivity
    extends
        AppCompatActivity
    implements
        Injectable
{
    private OccurrenceCauseSelectionAdapter adapter;
    private OccurrenceCauseSelectionViewModel viewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(
        @Nullable final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_occurrence_selection );
        final Toolbar toolbar = findViewById( R.id.activity_report_occurrence_toolbar );
        setSupportActionBar( toolbar );
        requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled( true );

        // Log de exceptions, precisa estar no onCreate das activities
        Thread.setDefaultUncaughtExceptionHandler( new ExceptionHandler( OccurrenceCauseSelectionActivity.this ) );

        configureView();
    }

    @Override
    public boolean onCreateOptionsMenu(
        final Menu menu )
    {
        getMenuInflater().inflate( R.menu.activty_menu_toolbar, menu );
        final SearchView searchView = (SearchView) menu.findItem( R.id.activity_menu_toolbar_search ).getActionView();
        final Context context = searchView.getContext();
        searchView.setQueryHint( context.getString( R.string.occurrence_cause_search ) );

        final EditText editSearch = searchView.findViewById( R.id.search_src_text );
        editSearch.setTextAppearance( getApplicationContext(), R.style.searchViewAppearance );
        editSearch.setHintTextColor( getResources().getColor( R.color.white ) );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(
                final String query )
            {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(
                final String query )
            {
                viewModel.setCauseName( "%" + query + "%" );
                return true;
            }
        } );
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    private void configureView()
    {
        adapter = new OccurrenceCauseSelectionAdapter( getLayoutInflater() );
        final RecyclerView recyclerView = findViewById( R.id.activity_report_occurrence_recycler_view );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setAdapter( adapter );
        viewModel = ViewModelProviders.of( this, viewModelFactory ).get(
            OccurrenceCauseSelectionViewModel.class );
        viewModel.setCauseName( null );
        viewModel.getOccurrenceCauseAndCategory().observe( this, value -> {
            adapter.setData( value.getData() );
        } );
    }

}