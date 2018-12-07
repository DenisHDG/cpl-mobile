package br.com.neolog.cplmobile.occurrence;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import br.com.neolog.cplmobile.AppExecutors;
import br.com.neolog.cplmobile.PermissionManager;
import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.modal.DialogFragmentModal;
import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryDao;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseDao;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorableIdentifier;
import br.com.neolog.monitoring.monitorable.model.rest.occurrence.RestImpact;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewOccurrenceActivity
    extends
        AppCompatActivity
    implements
        Injectable
{

    private List<Monitorable> monitorables;
    private OccurrenceCause occurrenceCause;
    private OccurrenceCategory occurrenceCategory;

    private Bitmap image_camera, bitmap_camera;
    private Uri image_gallery;
    private Uri bitmap_gallery;
    private Uri imageUri;
    private boolean impact_financial_button = true, impact_quantity_button = true, impact_temporal_button = true;
    private boolean layoutImpactTemporal, layoutImpactFinancial, layoutImpactQuantity;
    private boolean state_impact_financial_button, state_impact_quantity_button, state_impact_temporal_button;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int TAKE_PICK_REQUEST = 1;
    @BindView( R.id.numberPicker_days )
    NumberPicker numberPicker_days;
    @BindView( R.id.numberPicker_hours )
    NumberPicker numberPicker_hours;
    @BindView( R.id.numberPicker_minutes )
    NumberPicker numberPicker_minutes;
    @BindView( R.id.textView_impactFinancial )
    TextView textView_impactFinancial;
    @BindView( R.id.textView_impactQuantity )
    TextView textView_impactQuantity;
    @BindView( R.id.textView_impactTemporal )
    TextView textView_impactTemporal;
    @BindView( R.id.textView_categoryOcurrence )
    TextView textView_categoryOcurrence;
    @BindView( R.id.textView_causeOcurrence )
    TextView textView_causeOcurrence;
    @BindView( R.id.switch_impactFinancial )
    SwitchCompat switch_impactFinancial;
    @BindView( R.id.switch_impactQuantity )
    SwitchCompat switch_impactQuantity;
    @BindView( R.id.switch_impactTemporal )
    SwitchCompat switch_impactTemporal;
    @BindView( R.id.imageView_choseFile )
    ImageView imageView_choseFile;
    @BindView( R.id.imageView_cancelChoseFile )
    ImageView imageView_cancelChoseFile;
    @BindView( R.id.imageView_takePicture )
    ImageView imageView_takePicture;
    @BindView( R.id.linearLayout_choseFile )
    LinearLayout linearLayout_choseFile;
    @BindView( R.id.linearLayout_takePicture )
    LinearLayout linearLayout_takePicture;
    @BindView( R.id.linearLayout_quantityImpact )
    LinearLayout linearLayout_quantityImpact;
    @BindView( R.id.linearLayout_financialImpact )
    LinearLayout linearLayout_financialImpact;
    @BindView( R.id.linearLayout_temporalImpact )
    LinearLayout linearLayout_temporalImpact;
    @BindView( R.id.textInputEditText_financeImpact )
    EditText financeImpact;
    @BindView( R.id.textInputEditText_quantityImpact )
    EditText quantityImpact;
    @Inject
    OccurrenceCategoryDao occurrenceCategoryDao;
    @Inject
    OccurrenceCauseDao occurrenceCauseDao;
    @Inject
    MonitorableDao monitorableDao;
    @Inject
    AppExecutors appExecutors;
    @Inject
    OccurrenceService occurrenceService;

    @Override
    protected void onCreate(
        final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_occurrence );
        ButterKnife.bind( this );

        final Bundle bundle = getIntent().getExtras();
        requireNonNull( bundle );
        final int causeId = bundle.getInt( "causeId" );

        appExecutors.diskIO().execute( () -> {
            occurrenceCause = occurrenceCauseDao.findById( causeId );
            requireNonNull( occurrenceCause );

            occurrenceCategory = occurrenceCategoryDao.findById( occurrenceCause.getCategoryId() );
            requireNonNull( occurrenceCategory );
            textView_causeOcurrence.setText( occurrenceCause.getName() );
            textView_categoryOcurrence.setText( occurrenceCategory.getName() );

            layoutImpactFinancial = occurrenceCategory.isValueDelta();
            layoutImpactQuantity = occurrenceCategory.isQuantity();
            layoutImpactTemporal = occurrenceCategory.isTimeDelta();
            // Deixa visivel apenas os campos que vem habilitado da API

            if( layoutImpactTemporal ) {
                linearLayout_temporalImpact.setVisibility( View.VISIBLE );
            }
            if( layoutImpactQuantity ) {
                linearLayout_quantityImpact.setVisibility( View.VISIBLE );
            }
            if( layoutImpactFinancial ) {
                linearLayout_financialImpact.setVisibility( View.VISIBLE );
            }

            monitorables = monitorableDao.findAllRoots();
        } );

        if( savedInstanceState != null ) {
            image_camera = savedInstanceState.getParcelable( "BitmapImage" );
            image_gallery = savedInstanceState.getParcelable( "BitmapGallery" );
            state_impact_financial_button = savedInstanceState.getBoolean( "ImpactFinancialButton" );
            state_impact_quantity_button = savedInstanceState.getBoolean( "ImpactQuantityButton" );
            state_impact_temporal_button = savedInstanceState.getBoolean( "ImpactTemporalButton" );
        }

        numberPicker_days.setMinValue( 0 );
        numberPicker_days.setMaxValue( 31 );

        numberPicker_hours.setMinValue( 0 );
        numberPicker_hours.setMaxValue( 23 );

        numberPicker_minutes.setMinValue( 0 );
        numberPicker_minutes.setMaxValue( 3 );
        numberPicker_minutes.setDisplayedValues( getResources().getStringArray( R.array.minutes_options ) );

    }

    @OnClick( R.id.switch_impactTemporal )
    void changeImpactTemporal()
    {
        if( switch_impactTemporal.isChecked() ) {
            impact_temporal_button = true; // guardando estado do botão
            textView_impactTemporal.setText( getResources().getText( R.string.occurrence_delay ) );
            textView_impactTemporal.setTextColor( getResources().getColor( R.color.red ) );
        } else {
            impact_temporal_button = false; // guardando estado do botão
            textView_impactTemporal.setText( getResources().getText( R.string.reversal ) );
            textView_impactTemporal.setTextColor( getResources().getColor( R.color.second_collor ) );
        }
    }

    @OnClick( R.id.switch_impactQuantity )
    void changeImpactQuantity()
    {
        if( switch_impactQuantity.isChecked() ) {
            impact_quantity_button = true; // guardando estado do botão
            textView_impactQuantity.setText( getResources().getText( R.string.occurrence_delay ) );
            textView_impactQuantity.setTextColor( getResources().getColor( R.color.red ) );
        } else {
            impact_quantity_button = false; // guardando estado do botão
            textView_impactQuantity.setText( getResources().getText( R.string.reversal ) );
            textView_impactQuantity.setTextColor( getResources().getColor( R.color.second_collor ) );
        }
    }

    @OnClick( R.id.switch_impactFinancial )
    void changeImpactFinancial()
    {
        if( switch_impactFinancial.isChecked() ) {
            impact_financial_button = true;
            textView_impactFinancial.setText( getResources().getText( R.string.occurrence_delay ) );
            textView_impactFinancial.setTextColor( getResources().getColor( R.color.red ) );
        } else {
            impact_financial_button = false;
            textView_impactFinancial.setText( getResources().getText( R.string.reversal ) );
            textView_impactFinancial.setTextColor( getResources().getColor( R.color.second_collor ) );
        }
    }

    @OnClick( R.id.button_photo )
    void button_photo()
    {

        final PermissionManager permissionManager = new PermissionManager();

        if( permissionManager.userHasPermission( NewOccurrenceActivity.this ) ) {
            takePicture();
        } else {
            permissionManager.requestPermission( NewOccurrenceActivity.this );
        }
    }

    @OnClick( R.id.button_uploadImageOccurrence )
    void button_uploadImageOccurrence()
    {

        final PermissionManager permissionManager = new PermissionManager();

        if( permissionManager.userHasPermission( NewOccurrenceActivity.this ) ) {
            changeFiles();
        } else {
            permissionManager.requestPermission( NewOccurrenceActivity.this );
        }
    }

    @Override
    protected void onActivityResult(
        final int requestCode,
        final int resultCode,
        final Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );
        if( requestCode == PICK_IMAGE_REQUEST &&
            resultCode == RESULT_OK &&
            data != null &&
            data.getData() != null ) {
            final Uri filePath = data.getData();
            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );

                imageView_choseFile.setImageBitmap( bitmap );
                bitmap_gallery = filePath;
                linearLayout_choseFile.setVisibility( View.VISIBLE );
            } catch( final IOException e ) {
                e.printStackTrace();
                imageView_choseFile.setVisibility( View.GONE );
            }
        } else {
            if( requestCode == TAKE_PICK_REQUEST &&
                data != null &&
                data.getExtras() != null ) {

                final Bundle extras = data.getExtras();
                if( extras.containsKey( "data" ) ) {
                    bitmap_camera = (Bitmap) extras.get( "data" );
                } else {
                    bitmap_camera = getBitmapFromUri();
                }
                imageView_takePicture.setImageBitmap( bitmap_camera );
            } else {
                DialogFragmentModal.getInstance( this )
                    .setMessage( R.string.image_not_found )
                    .setTitle( R.string.image_search )
                    .setPositiveButton( R.string.ok, null )
                    .show();
            }
        }
    }

    private Bitmap getBitmapFromUri()
    {

        getContentResolver().notifyChange( imageUri, null );
        final ContentResolver cr = getContentResolver();
        final Bitmap bitmap;

        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap( cr, imageUri );
            return bitmap;
        } catch( final Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    private void changeFiles()
    {
        final Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select Picture" ), PICK_IMAGE_REQUEST );
    }

    private void takePicture()
    {
        final Intent intentCamera = new Intent( "android.media.action.IMAGE_CAPTURE" );
        final File filePhoto = new File( Environment.getExternalStorageDirectory(), "Pic.jpg" );
        if( intentCamera.resolveActivity( getPackageManager() ) != null ) {

            imageUri = FileProvider.getUriForFile( NewOccurrenceActivity.this,
                getString( R.string.file_provider_authority ),
                filePhoto );

            intentCamera.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
            startActivityForResult( intentCamera, TAKE_PICK_REQUEST );

        }
    }

    @OnClick( R.id.imageView_cancelChoseFile )
    void cancelChoseFile()
    {
        if( imageView_choseFile != null ) {
            imageView_choseFile.setImageBitmap( null );
            bitmap_gallery = null;
            imageView_choseFile = null;
            linearLayout_choseFile.setVisibility( View.GONE );
        }
    }

    @OnClick( R.id.imageView_cancelTakePicture )
    void cancelPicture()
    {
        if( imageView_takePicture != null ) {
            imageView_takePicture.setImageBitmap( null );
            imageUri = null;
            bitmap_camera = null;
            linearLayout_takePicture.setVisibility( View.GONE );
        }
    }

    @Override
    protected void onRestoreInstanceState(
        final Bundle savedInstanceState )
    {
        image_camera = savedInstanceState.getParcelable( "BitmapImage" );
        image_gallery = savedInstanceState.getParcelable( "BitmapGallery" );
        state_impact_financial_button = savedInstanceState.getBoolean( "ImpactFinancialButton" );
        state_impact_quantity_button = savedInstanceState.getBoolean( "ImpactQuantityButton" );
        state_impact_temporal_button = savedInstanceState.getBoolean( "ImpactTemporalButton" );

        if( image_camera != null ) {
            imageView_takePicture.setImageBitmap( image_camera );
        }
        if( image_gallery != null ) {
            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), image_gallery );
                imageView_choseFile.setImageBitmap( bitmap );
            } catch( final IOException e ) {
                e.printStackTrace();
            }
        }
        if( state_impact_financial_button ) {
            switch_impactFinancial.isChecked();
            changeImpactFinancial();
        } else {
            switch_impactFinancial.setChecked( false );
            changeImpactFinancial();
        }
        if( state_impact_temporal_button ) {
            switch_impactTemporal.isChecked();
            changeImpactTemporal();
        } else {
            switch_impactTemporal.setChecked( false );
            changeImpactTemporal();
        }
        if( state_impact_quantity_button ) {
            switch_impactQuantity.isChecked();
            changeImpactQuantity();
        } else {
            switch_impactQuantity.setChecked( false );
            changeImpactQuantity();
        }
    }

    @Override
    public void onSaveInstanceState(
        final Bundle savedInstanceState )
    {
        super.onSaveInstanceState( savedInstanceState );
        savedInstanceState.putParcelable( "BitmapImage", bitmap_camera );
        savedInstanceState.putParcelable( "BitmapGallery", bitmap_gallery );
        savedInstanceState.putBoolean( "ImpactFinancialButton", impact_financial_button );
        savedInstanceState.putBoolean( "ImpactQuantityButton", impact_quantity_button );
        savedInstanceState.putBoolean( "ImpactTemporalButton", impact_temporal_button );
    }

    @OnClick( R.id.button_saveOccurrence )
    public void saveOccurrence()
    {
        occurrenceService.createOccurrence(
            NewOccurrenceActivity.this,
            getMonitorableIdentifiers(),
            getImpact(),
            getOccurrenceCategory(),
            getOccurrenceCause() );
    }

    private List<RestMonitorableIdentifier> getMonitorableIdentifiers()
    {
        return FluentIterable.from( monitorables )
            .transform( monitorable -> new RestMonitorableIdentifier( monitorable.getCode(), monitorable.getType() ) )
            .toList();
    }

    private RestImpact getImpact()
    {
        return new RestImpact( getImpactTimeDelta(), getImpactValueDelta(), getImpactQuantityDelta() );
    }

    private RestExternalEntity getOccurrenceCategory()
    {
        return new RestExternalEntity(
            occurrenceCategory.getId(),
            occurrenceCategory.getSourceId(),
            occurrenceCategory.getName(),
            occurrenceCategory.getDescription() );
    }

    private RestExternalEntity getOccurrenceCause()
    {
        return new RestExternalEntity(
            occurrenceCause.getId(),
            occurrenceCause.getSourceId(),
            occurrenceCause.getName(),
            occurrenceCause.getDescription() );
    }

    private Long getImpactTimeDelta()
    {
        final String[] minutesOptions = getResources().getStringArray( R.array.minutes_options );
        final int index = numberPicker_minutes.getValue();
        final Integer minutesSelected = Integer.valueOf( minutesOptions[ index ] );
        final int minutes = ( ( ( numberPicker_days.getValue() * 24 ) + numberPicker_hours.getValue() ) * 60 ) + minutesSelected;
        final Long millis = convertMinutesToMillis( minutes );
        if( millis != null && ! impact_temporal_button ) {
            return millis * - 1;
        }
        return millis;
    }

    private Long convertMinutesToMillis(
        final int minutes )
    {
        if( minutes == 0 ) {
            return null;
        }
        return Duration.standardMinutes( minutes ).getMillis();
    }

    private Double getImpactValueDelta()
    {
        final String valueDelta = financeImpact.getText().toString();
        if( Strings.isNullOrEmpty( valueDelta ) ) {
            return null;
        }
        if( ! impact_financial_button ) {
            return Double.valueOf( valueDelta ) * - 1;
        }
        return Double.valueOf( valueDelta );
    }

    private Integer getImpactQuantityDelta()
    {
        final String quantityText = quantityImpact.getText().toString();
        if( Strings.isNullOrEmpty( quantityText ) ) {
            return null;
        }
        final int quantity = Integer.parseInt( quantityText );
        if( ! impact_quantity_button ) {
            return quantity * - 1;
        }
        return quantity;
    }

}
