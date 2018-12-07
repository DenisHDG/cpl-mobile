package br.com.neolog.cplmobile.monitorable.repo;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import br.com.neolog.cplmobile.R;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

public class MonitorableTypeConverter
{
    @Inject
    MonitorableTypeConverter()
    {
    }

    public Integer convertToResource(
        final StandardMonitorableType type )
    {
        switch( checkNotNull( type, "type is null" ) ) {
            case TRIP:
                return R.string.monitorable_type_trip;
            case DOCUMENT:
                return R.string.monitorable_type_document;
            case INVOICE:
                return R.string.monitorable_type_invoice;
            default:
                throw new IllegalStateException( "Type not found: " + type );
        }
    }
}
