package br.com.neolog.cplmobile.monitorable.repo;

import android.support.annotation.StringRes;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.databinding.StringResourceSupplier;

public enum MonitorablePropertyType
    implements
        StringResourceSupplier
{
    STATUS( R.string.monitorable_status ),
    CARRIER( R.string.monitorable_carrier ),
    PLATE( R.string.monitorable_plate ),
    VEHICLE( R.string.monitorable_vehicle ),
    ESTIMATED_END( R.string.monitorable_estimated_end ),
    EXPECTED_END( R.string.monitorable_expected_end ),
    REALIZED_DISTANCE( R.string.monitorable_realized_distance ),
    DISTANCE( R.string.monitorable_distance );

    private final int resource;

    MonitorablePropertyType(
        @StringRes final int resource )
    {
        this.resource = resource;
    }

    @Override
    @StringRes
    public int getStringResource()
    {
        return resource;
    }
}
