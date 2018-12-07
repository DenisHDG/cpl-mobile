package br.com.neolog.cplmobile.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StringResourceSupplierStatusBindingAdapter
{
    @BindingAdapter( "android:text" )
    public static void setText(
        final TextView textView,
        final StringResourceSupplier supplier )
    {
        if( supplier == null ) {
            textView.setText( "" );
            return;
        }
        textView.setText( supplier.getStringResource() );
    }

    @BindingAdapter( "android:contentDescription" )
    public static void setContentDescription(
        final ImageView image,
        final StringResourceSupplier supplier )
    {
        if( supplier == null ) {
            image.setContentDescription( "" );
            return;
        }
        image.setContentDescription( image.getContext().getResources().getString( supplier.getStringResource() ) );
    }
}
