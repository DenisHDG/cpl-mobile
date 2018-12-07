package br.com.neolog.cplmobile.databinding;

import android.databinding.BindingAdapter;
import android.view.View;

public class VisibilityBindingAdapter
{
    @BindingAdapter( "visible" )
    public static void setVisible(
        final View view,
        final boolean show )
    {
        view.setVisibility( show ? View.VISIBLE : View.GONE );
    }
}
