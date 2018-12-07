package br.com.neolog.cplmobile.api;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import br.com.neolog.jackson.enummodule.NeologEnumSerializerModule;
import br.com.neolog.monitoring.monitorable.model.api.serialization.MonitorablePolymorphicJsonTypeInfo;
import br.com.neolog.monitoring.monitorable.model.api.serialization.MonitorableSerializationModule;
import br.com.neolog.monitoring.monitorable.model.rest.RestMonitorable;
import dagger.Module;
import dagger.Provides;

@Module
public class ObjectMapperModule
{

    @Singleton
    @Provides
    public ObjectMapper objectMapper()
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules( new NeologEnumSerializerModule() );
        mapper.registerModule( new JodaModule() );
        mapper.registerModule( MonitorableSerializationModule.createCustomModule() );
        mapper.addMixIn( RestMonitorable.class, MonitorablePolymorphicJsonTypeInfo.class );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        return mapper;
    }
}
