package br.com.neolog.cplmobile.databinding;

import static br.com.neolog.cplmobile.databinding.VisibilityBindingAdapter.setVisible;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.view.View;

@RunWith( MockitoJUnitRunner.class )
public class VisibilityBindingAdapterTest
{
    @Mock
    private View view;

    @Test
    public void shouldShowViewWhenTrue()
    {
        setVisible( view, true );
        verify( view ).setVisibility( View.VISIBLE );
    }

    @Test
    public void shouldHideViewWhenFalse()
    {
        setVisible( view, false );
        verify( view ).setVisibility( View.GONE );
    }
}