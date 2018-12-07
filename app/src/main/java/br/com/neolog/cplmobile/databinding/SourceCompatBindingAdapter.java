package br.com.neolog.cplmobile.databinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SourceCompatBindingAdapter
{
    @BindingAdapter( "srcCompat" )
    public static void setSrcCompat(
        final ImageView view,
        final Drawable drawable )
    {
        view.setImageDrawable( drawable );
    }
}
