package br.com.neolog.cplmobile.databinding;

import static br.com.neolog.cplmobile.databinding.StringResourceSupplierStatusBindingAdapter.setContentDescription;
import static br.com.neolog.cplmobile.databinding.StringResourceSupplierStatusBindingAdapter.setText;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

@RunWith( MockitoJUnitRunner.class )
public class StringResourceSupplierStatusBindingAdapterTest
{
    @Mock
    private TextView textView;
    @Mock
    private StringResourceSupplier supplier;
    @Mock
    private ImageView imageView;
    @Mock
    private Context context;
    @Mock
    private Resources resources;

    @Before
    public void setUp()
    {
        when( imageView.getContext() ).thenReturn( context );
        when( context.getResources() ).thenReturn( resources );
    }

    @Test
    public void shouldClearTextWhenSupplierIsNull()
    {
        setText( textView, null );
        verify( textView ).setText( "" );
    }

    @Test
    public void shouldSetTextWhenSupplierReturnsStringResource()
    {
        when( supplier.getStringResource() ).thenReturn( 1000 );
        setText( textView, supplier );
        verify( textView ).setText( 1000 );
    }

    @Test
    public void shouldClearContentDescriptionWhenSupplierIsNull()
    {
        setContentDescription( imageView, null );
        verify( imageView ).setContentDescription( "" );
    }

    @Test
    public void shouldFillContentDescriptionWhenSupplierReturnsValue()
    {
        when( supplier.getStringResource() ).thenReturn( 1 );
        when( resources.getString( 1 ) ).thenReturn( "description" );
        setContentDescription( imageView, supplier );
        verify( imageView ).setContentDescription( "description" );
    }
}