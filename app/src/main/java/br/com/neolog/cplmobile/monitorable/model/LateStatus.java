package br.com.neolog.cplmobile.monitorable.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.DrawableSupplier;
import br.com.neolog.cplmobile.databinding.StringResourceSupplier;

public enum LateStatus
    implements
        DrawableSupplier,
        StringResourceSupplier
{
    IN_TIME( R.drawable.icon_circulo_verde, R.string.late_status_in_time ),
    WARNING( R.drawable.icon_cirulo_orange, R.string.late_status_warning ),
    DELAYED( R.drawable.icon_circulo_red, R.string.late_status_delayed );

    private final int drawable;
    private final int stringResource;

    LateStatus(
        @DrawableRes final int drawable,
        @StringRes final int stringResource )
    {
        this.drawable = drawable;
        this.stringResource = stringResource;
    }

    @Override
    @DrawableRes
    public int get()
    {
        return drawable;
    }

    @Override
    @StringRes
    public int getStringResource()
    {
        return stringResource;
    }
}
