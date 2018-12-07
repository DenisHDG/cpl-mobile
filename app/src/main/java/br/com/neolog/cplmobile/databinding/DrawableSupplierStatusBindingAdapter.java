package br.com.neolog.cplmobile.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class DrawableSupplierStatusBindingAdapter
{
    @BindingAdapter( "android:src" )
    public static void setDrawableResource(
        final ImageView image,
        final DrawableSupplier supplier )
    {
        if( supplier == null ) {
            image.setImageDrawable( null );
            return;
        }
        image.setImageResource( supplier.get() );
    }
}
