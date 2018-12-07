package br.com.neolog.cplmobile.transition;

import android.support.annotation.DrawableRes;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.DrawableSupplier;

public enum TransitionGroupStatus
    implements
        DrawableSupplier
{
    OPEN( R.drawable.empty ),
    IN_PROGRESS( R.drawable.ic_check_black_24dp ),
    DONE( R.drawable.ic_done_all_black_24dp );

    private final int icon;

    TransitionGroupStatus(
        @DrawableRes final int icon )
    {
        this.icon = icon;
    }

    @DrawableRes
    public int get()
    {
        return icon;
    }
}
