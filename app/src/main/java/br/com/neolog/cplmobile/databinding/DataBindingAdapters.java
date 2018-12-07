package br.com.neolog.cplmobile.databinding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

public class DataBindingAdapters
{
    @BindingAdapter( "layout_height" )
    public static void setLayoutHeight(
        final View view,
        final float height )
    {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams( layoutParams );
    }

    private DataBindingAdapters()
    {
        throw new AssertionError( "can't be instantiated" );
    }
}
