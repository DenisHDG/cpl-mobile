package br.com.neolog.cplmobile.formatter;

import javax.inject.Inject;

import br.com.neolog.monitoring.monitorable.model.api.Driver;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

public class DriverFormatter
{
    private final ExternalizableEntityFormatter entityFormatter;

    @Inject
    DriverFormatter(
        final ExternalizableEntityFormatter entityFormatter )
    {
        this.entityFormatter = entityFormatter;
    }

    public String format(
        final Driver driver )
    {
        if( driver == null ) {
            return "";
        }
        return entityFormatter.format( new RestExternalEntity( driver.getSourceId(), driver.getName(), driver.getDescription() ) );
    }
}
