package br.com.neolog.cplmobile.occurrence;

import java.util.List;

import com.google.common.base.Strings;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import br.com.neolog.cplmobile.R;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OccurrenceSpentDialog
    extends
        Dialog
{

    private final OccurrenceService occurrenceService;
    private final List<RestMonitorableIdentifier> monitorablesIdentifier;
    @BindView( R.id.occurrence_spent_layout_others )
    LinearLayout layoutOthers;
    @BindView( R.id.occurrence_spent_layout_options )
    LinearLayout layoutOptions;
    @BindView( R.id.occurrence_spent_input_edit_value )
    TextInputEditText inputEditText;

    public OccurrenceSpentDialog(
        final Activity activity,
        final OccurrenceService occurrenceService,
        final List<RestMonitorableIdentifier> monitorablesIdentifier )
    {
        super( activity );
        this.occurrenceService = occurrenceService;
        this.monitorablesIdentifier = monitorablesIdentifier;
    }

    @Override
    protected void onCreate(
            final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.occurrence_spent_layout );
        ButterKnife.bind( this );
    }

    @OnClick( R.id.occurrence_spent_image_20 )
    void onClickFirstValue()
    {
        occurrenceService.createOccurrenceSpent( 20, getContext(), monitorablesIdentifier, getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_spent_image_50 )
    void onClickSecondValue()
    {
        occurrenceService.createOccurrenceSpent( 50, getContext(), monitorablesIdentifier, getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_spent_image_100 )
    void onClickThirdValue()
    {
        occurrenceService.createOccurrenceSpent( 100, getContext(), monitorablesIdentifier, getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_spent_image_other )
    void showOtherValue()
    {
        layoutOthers.setVisibility( View.VISIBLE );
        layoutOptions.setVisibility( View.GONE );
    }

    @OnClick( R.id.occurrence_spent_button_save )
    void saveOtherValue()
    {
        final String spentValueText = inputEditText.getText().toString();
        if( ! Strings.isNullOrEmpty( spentValueText ) ) {
            final Double spentValue = Double.valueOf( spentValueText );
            occurrenceService.createOccurrenceSpent( spentValue, getContext(), monitorablesIdentifier, getSuccessRunnable() );
        }

    }

    private Runnable getSuccessRunnable()
    {
        return new Runnable() {
            @Override
            public void run()

            {
                hide();
            }
        };
    }

}
