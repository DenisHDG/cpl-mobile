package br.com.neolog.cplmobile.formatter;

import static com.google.common.base.Objects.equal;

import javax.inject.Inject;

import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

public class ExternalizableEntityFormatter
{
    @Inject
    ExternalizableEntityFormatter()
    {
    }

    public String format(
        final RestExternalEntity entity )
    {
        if( entity == null ) {
            return "";
        }
        final String sourceId = entity.getSourceId();
        final String name = entity.getName();
        if( equal( name, sourceId ) ) {
            return name;
        }
        return name + " - " + sourceId;
    }
}
