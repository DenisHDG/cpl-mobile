package br.com.neolog.cplmobile.databinding;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

@RunWith( MockitoJUnitRunner.class )
public class SourceCompatBindingAdapterTest
{
    @Mock
    private ImageView view;
    @Mock
    private Drawable drawable;

    @Test
    public void shouldSetDrawableOnImage()
    {
        SourceCompatBindingAdapter.setSrcCompat( view, drawable );
        verify( view ).setImageDrawable( drawable );
    }
}