package br.com.neolog.cplmobile.databinding;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.widget.ImageView;

@RunWith( MockitoJUnitRunner.class )
public class DrawableSupplierStatusBindingAdapterTest
{
    @Mock
    private DrawableSupplier supplier;

    @Mock
    private ImageView imageView;

    @Test
    public void shouldClearImageWhenSupplierIsNull()
    {
        DrawableSupplierStatusBindingAdapter.setDrawableResource( imageView, null );
        verify( imageView ).setImageDrawable( null );
        verify( imageView, never() ).setImageResource( anyInt() );
    }

    @Test
    public void shouldDisplaySupplierImageWhenSupplierIsNotNull()
    {
        when( supplier.get() ).thenReturn( 10 );
        DrawableSupplierStatusBindingAdapter.setDrawableResource( imageView, supplier );
        verify( imageView ).setImageResource( 10 );
        verify( imageView, never() ).setImageDrawable( any() );
    }
}