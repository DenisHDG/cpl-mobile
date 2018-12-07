package br.com.neolog.cplmobile.occurrence;

import java.util.List;

import org.joda.time.Duration;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import br.com.neolog.cplmobile.R;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OccurrenceDelayDialog
    extends
        Dialog
{
    private final OccurrenceService occurrenceService;
    private final List<RestMonitorableIdentifier> monitorablesIdentifier;
    @BindView( R.id.occurrence_delay_layout_others )
    LinearLayout layoutOthers;
    @BindView( R.id.occurrence_delay_layout_options )
    LinearLayout layoutOptions;
    @BindView( R.id.occurrence_delay_hours )
    NumberPicker hoursPicker;
    @BindView( R.id.occurrence_delay_minutes )
    NumberPicker minutesPicker;

    public OccurrenceDelayDialog(
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
        setContentView( R.layout.occurrence_delay_layout );
        ButterKnife.bind( this );
    }

    @OnClick( R.id.occurrence_delay_image_30m )
    void onClickFirstValue()
    {
        occurrenceService.createOccurrenceDelay( convertMinutesToMillis( 30 ), getContext(), monitorablesIdentifier, getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_delay_image_1h )
    void onClickSecondValue()
    {
        occurrenceService.createOccurrenceDelay( convertMinutesToMillis( 60 ), getContext(), monitorablesIdentifier, getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_delay_image_2h )
    void onClickThirdValue()
    {
        occurrenceService.createOccurrenceDelay( convertMinutesToMillis( 120 ), getContext(), monitorablesIdentifier,
            getSuccessRunnable() );
    }

    @OnClick( R.id.occurrence_delay_image_other )
    void showOtherValue()
    {
        layoutOthers.setVisibility( View.VISIBLE );
        layoutOptions.setVisibility( View.GONE );

        hoursPicker.setMinValue( 0 );
        hoursPicker.setMaxValue( 23 );

        minutesPicker.setMinValue( 0 );
        minutesPicker.setMaxValue( 3 );
        minutesPicker.setDisplayedValues( getContext().getResources().getStringArray( R.array.minutes_options ) );
    }

    @OnClick( R.id.occurrence_delay_button_save )
    void saveOtherValue()
    {
        final String[] minutesOptions = getContext().getResources().getStringArray( R.array.minutes_options );
        final int index = minutesPicker.getValue();
        final Integer minutesSelected = Integer.valueOf( minutesOptions[ index ] );
        final int minutes = hoursPicker.getValue() * 60 + minutesSelected;
        occurrenceService.createOccurrenceDelay( convertMinutesToMillis( minutes ), getContext(), monitorablesIdentifier,
            getSuccessRunnable() );
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

    private long convertMinutesToMillis(
        final int minutes )
    {
        return Duration.standardMinutes( minutes ).getMillis();
    }

}
