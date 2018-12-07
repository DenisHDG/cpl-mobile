package br.com.neolog.cplmobile;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.Collection;

import javax.inject.Inject;

import com.github.clans.fab.FloatingActionMenu;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.neolog.cplmobile.databinding.ActivityMainBinding;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.event.HistoricActivity;
import br.com.neolog.cplmobile.exception.ExceptionHandler;
import br.com.neolog.cplmobile.exception.RemoteMessageHandler;
import br.com.neolog.cplmobile.location.LocationService;
import br.com.neolog.cplmobile.modal.ModalFactory;
import br.com.neolog.cplmobile.monitorable.MonitorableFragment;
import br.com.neolog.cplmobile.monitorable.MonitorableGrouperService;
import br.com.neolog.cplmobile.monitorable.MonitorableViewModel;
import br.com.neolog.cplmobile.monitorable.job.ResendMonitorableFinishJob;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableTypeConverter;
import br.com.neolog.cplmobile.occurrence.OccurrenceDelayDialog;
import br.com.neolog.cplmobile.occurrence.OccurrenceRepository;
import br.com.neolog.cplmobile.occurrence.OccurrenceService;
import br.com.neolog.cplmobile.occurrence.OccurrenceSpentDialog;
import br.com.neolog.cplmobile.occurrence.selection.OccurrenceCauseSelectionActivity;
import br.com.neolog.cplmobile.viewmodel.ViewModelFactory;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity
    extends
        AppCompatActivity
    implements
        Injectable,
        NavigationView.OnNavigationItemSelectedListener
{
    private final int REQUEST_CHECK_SETTINGS = 0x1;

    @Inject
    OccurrenceRepository occurrenceRepository;
    @Inject
    LocationService locationService;
    @Inject
    OccurrenceService occurrenceService;

    @BindView( R.id.toolbar )
    Toolbar toolbar;
    @BindView( R.id.main_activity_fragment )
    View monitorableFragment;
    @BindView( R.id.drawer_layout )
    DrawerLayout drawer;
    @BindView( R.id.nav_view )
    NavigationView navigationView;
    @BindView( R.id.activity_main_no_monitorable )
    View noMonitorableView;
    @BindView( R.id.activity_main_fab )
    FloatingActionMenu floatingActionButton;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    MonitorableGrouperService monitorableGrouperService;
    @Inject
    MonitorableTypeConverter monitorableTypeConverter;
    @Inject
    RemoteMessageHandler remoteMessageHandler;

    private ActivityMainBinding binding;

    private MonitorableViewModel viewModel;
    private Multimap<StandardMonitorableType,Monitorable> monitorables = HashMultimap.create();

    @Override
    protected void onCreate(
        final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        // Log de exceptions, precisa estar no onCreate das activities
        Thread.setDefaultUncaughtExceptionHandler( new ExceptionHandler( MainActivity.this ) );

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main );

        final String[] permissions = {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
        };

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            requestPermissions( permissions, REQUEST_CHECK_SETTINGS );
        }

        locationService.startTracking( this );

        ButterKnife.bind( this );
        setSupportActionBar( toolbar );

        configureDrawer();
        configureNavigationView();
        configureViewModel();
        showMonitorables( false );
    }

    private void configureDrawer()
    {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();
    }

    private void configureNavigationView()
    {
        navigationView.setNavigationItemSelectedListener( this );
    }

    private void configureViewModel()
    {
        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( MonitorableViewModel.class );
        viewModel.findCurrentMonitorable();
        viewModel.getLoading().observe( this, binding::setLoading );
        viewModel.getExceptionMessages().observe( this, this::showErrors );
        viewModel.getMonitorablesByType().observe( this, this::updateMonitorable );
    }

    private void showErrors(
        final ExceptionMessages exceptionMessages )
    {
        if( exceptionMessages != null ) {
            remoteMessageHandler.showModal( exceptionMessages, this );
        }
    }

    private void updateMonitorable(
        final Multimap<StandardMonitorableType,Monitorable> monitorables )
    {
        this.monitorables = monitorables;
        showMenusOfAvailableMonitorableTypes();
        showMonitorables( ! monitorables.isEmpty() );
        showMonitorableFragment( Iterables.getFirst( monitorables.keySet(), null ) );
        sendPendingOperations();
    }

    private void sendPendingOperations()
    {
        if( monitorables.isEmpty() ) {
            ResendMonitorableFinishJob.schedule( this );
        }
    }

    private void showMonitorables(
        final boolean show )
    {
        if( show ) {
            monitorableFragment.setVisibility( View.VISIBLE );
            floatingActionButton.setVisibility( View.VISIBLE );
            noMonitorableView.setVisibility( View.GONE );
        } else {
            monitorableFragment.setVisibility( View.GONE );
            floatingActionButton.setVisibility( View.GONE );
            noMonitorableView.setVisibility( View.VISIBLE );
        }
    }

    private void showMonitorableFragment(
        @Nullable final StandardMonitorableType type )
    {
        if( type == null ) {
            toolbar.setTitle( R.string.app_name );
            return;
        }

        setupTitle( type );
        final Collection<Monitorable> values = monitorables.get( type );
        final MonitorableFragment fragment = MonitorableFragment.create( values );
        getSupportFragmentManager()
            .beginTransaction()
            .replace( R.id.main_activity_fragment, fragment )
            .commit();
    }

    private void showMenusOfAvailableMonitorableTypes()
    {
        hideTypeItemsOnDrawerMenu();
        for( final StandardMonitorableType type : monitorables.keySet() ) {
            showItemOnDrawerMenu( type );
        }
    }

    private void hideTypeItemsOnDrawerMenu()
    {
        navigationView = findViewById( R.id.nav_view );
        final Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem( R.id.nav_trip ).setVisible( false );
        nav_Menu.findItem( R.id.nav_document ).setVisible( false );
        nav_Menu.findItem( R.id.nav_invoice ).setVisible( false );
    }

    private void showItemOnDrawerMenu(
        @NonNull final StandardMonitorableType type )
    {
        navigationView = findViewById( R.id.nav_view );
        final Menu nav_Menu = navigationView.getMenu();
        switch( type ) {
            case TRIP:
                nav_Menu.findItem( R.id.nav_trip ).setVisible( true );
                break;
            case DOCUMENT:
                nav_Menu.findItem( R.id.nav_document ).setVisible( true );
                break;
            case INVOICE:
                nav_Menu.findItem( R.id.nav_invoice ).setVisible( true );
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        if( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(
        final int requestCode,
        @NonNull final String[] permissions,
        @NonNull final int[] grantResults )
    {
        switch( requestCode ) {
            case REQUEST_CHECK_SETTINGS:
                locationService.startTracking( this );
                break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    public boolean onNavigationItemSelected(
        @NonNull final MenuItem item )
    {
        switch( item.getItemId() ) {
            case R.id.nav_trip:
                showMonitorableFragment( StandardMonitorableType.TRIP );
                break;
            case R.id.nav_document:
                showMonitorableFragment( StandardMonitorableType.DOCUMENT );
                break;
            case R.id.nav_invoice:
                showMonitorableFragment( StandardMonitorableType.INVOICE );
                break;
            case R.id.nav_historic:
                startActivity( new Intent( this, HistoricActivity.class ) );
                break;
            case R.id.nav_logout:
                break;
        }

        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @OnClick( R.id.floatingActionButton_occurrence )
    void occurrenceClicked()
    {
        startActivity( new Intent( this, OccurrenceCauseSelectionActivity.class ) );
    }

    @OnClick( R.id.floatingActionButton_spent )
    void oneClickOccurrenceSpentClicked()
    {
        final OccurrenceSpentDialog occurrenceSpentDialog = new OccurrenceSpentDialog( this, occurrenceService,
            viewModel.getMonitorablesIdentifierFromRoots() );
        occurrenceSpentDialog.show();
    }

    @OnClick( R.id.floatingActionButton_delay )
    void oneClickOccurrenceDelayClicked()
    {
        final OccurrenceDelayDialog occurrenceSpentDialog = new OccurrenceDelayDialog( this, occurrenceService,
            viewModel.getMonitorablesIdentifierFromRoots() );
        occurrenceSpentDialog.show();
    }

    @OnClick( R.id.floatingActionButton_finish )
    void finishMonitorable()
    {
        ModalFactory.with( this )
            .confirm( R.string.finish_monitorable_confirmation, () -> {
                viewModel.finishMonitorable();
                ModalFactory.with( this )
                    .success( R.string.finish_monitorable_success )
                    .show();
            } )
            .show();
    }

    private void setupTitle(
        final StandardMonitorableType type )
    {
        switch( type ) {
            case TRIP:
                toolbar.setTitle( R.string.trip );
                break;
            case DOCUMENT:
                toolbar.setTitle( R.string.document );
                break;
            case INVOICE:
                toolbar.setTitle( R.string.invoice );
                break;
        }
    }
}