package br.com.neolog.cplmobile.monitorable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.MonitorableFragmentBinding;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.exception.RemoteMessageHandler;
import br.com.neolog.cplmobile.location.LocationService;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.transition.TransitionGroup;
import br.com.neolog.cplmobile.transition.TransitionGroupAdapter;
import br.com.neolog.cplmobile.transition.TransitionService;
import br.com.neolog.cplmobile.transition.TransitionTouchCallback;
import br.com.neolog.cplmobile.viewmodel.ViewModelFactory;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonitorableFragment
    extends
        Fragment
    implements
        Injectable
{
    private static final String MONITORABLE_IDS = "MONITORABLE_IDS";

    @BindView( R.id.monitorable_fragment_properties )
    RecyclerView propertiesView;
    @BindView( R.id.monitorable_fragment_transitions )
    RecyclerView transitionsView;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    TransitionService transitionService;
    @Inject
    RemoteMessageHandler remoteMessageHandler;
    @Inject
    LocationService locationService;

    private MonitorableFragmentBinding databinding;
    private MonitorablePropertyAdapter propertiesAdapter;
    private TransitionGroupAdapter transitionsAdapter;

    private boolean collapsed = true;
    private MonitorableFragmentViewModel viewModel;

    public static MonitorableFragment create(
        final Collection<Monitorable> monitorables )
    {
        final MonitorableFragment result = new MonitorableFragment();
        final Bundle args = new Bundle();
        args.putIntegerArrayList( MONITORABLE_IDS, getIds( monitorables ) );
        result.setArguments( args );
        return result;
    }

    private static ArrayList<Integer> getIds(
        final Collection<Monitorable> monitorables )
    {
        final ArrayList<Integer> result = new ArrayList<>();
        for( final Monitorable monitorable : monitorables ) {
            result.add( monitorable.getId() );
        }
        return result;
    }

    public MonitorableFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull final LayoutInflater inflater,
        @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState )
    {
        databinding = DataBindingUtil.inflate( inflater, R.layout.monitorable_fragment, container, false );
        final View view = databinding.getRoot();
        ButterKnife.bind( this, view );
        databinding.setCollapsed( collapsed );
        configurePropertiesView();
        configureTransitionsView();
        return view;
    }

    private void configurePropertiesView()
    {
        propertiesAdapter = new MonitorablePropertyAdapter( getLayoutInflater() );
        propertiesView.setAdapter( propertiesAdapter );
    }

    private void configureTransitionsView()
    {
        transitionsAdapter = new TransitionGroupAdapter( getLayoutInflater() );
        transitionsView.setAdapter( transitionsAdapter );

        final ItemTouchHelper touchHelper = new ItemTouchHelper( new TransitionTouchCallback( getContext(), transitionsAdapter,
            transitionService, remoteMessageHandler, locationService, () -> viewModel.refresh() ) );
        touchHelper.attachToRecyclerView( transitionsView );
    }

    @Override
    public void onActivityCreated(
        @Nullable final Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( MonitorableFragmentViewModel.class );
        viewModel.getLoading().observe( this, databinding::setLoading );
        viewModel.getExceptionMessages().observe( this, this::showExceptionMessages );
        viewModel.getMonitorable().observe( this, databinding::setMonitorable );
        viewModel.getMonitorableProperties().observe( this, propertiesAdapter::setProperties );
        viewModel.getTransitionGroups().observe( this, this::updateTransitions );
        showMonitorables( viewModel );
    }

    private void showExceptionMessages(
        final ExceptionMessages exceptionMessages )
    {
        if( exceptionMessages != null ) {
            remoteMessageHandler.showModal( exceptionMessages, getContext() );
        }
    }

    private void updateTransitions(
        final List<TransitionGroup> transitionGroups )
    {
        transitionsAdapter.setTransitionGroups( transitionGroups );
        transitionsView.postDelayed( () -> transitionsView.scrollToPosition( findTransitionToFocus( transitionGroups ) ), 500 );
    }

    private int findTransitionToFocus(
        final List<TransitionGroup> transitionGroups )
    {
        int position = 0;
        for( int i = 0; i < transitionGroups.size(); i++ ) {
            final TransitionGroup transitionGroup = transitionGroups.get( i );
            switch( transitionGroup.getStatus() ) {
                case IN_PROGRESS:
                    position = i;
                    break;
                case DONE:
                    position = i + 1;
                    break;
            }
        }
        return position;
    }

    private void showMonitorables(
        final MonitorableFragmentViewModel viewModel )
    {
        final Bundle args = getArguments();
        if( args == null ) {
            return;
        }
        final List<Integer> monitorableIds = args.getIntegerArrayList( MONITORABLE_IDS );
        if( monitorableIds == null ) {
            return;
        }
        viewModel.findAll( monitorableIds );
    }

    @OnClick( R.id.monitorable_fragment_header )
    void collapseHeader()
    {
        collapsed = ! collapsed;
        databinding.setCollapsed( collapsed );
    }
}
