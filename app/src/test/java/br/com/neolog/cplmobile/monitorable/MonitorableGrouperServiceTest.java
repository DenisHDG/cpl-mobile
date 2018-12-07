package br.com.neolog.cplmobile.monitorable;

import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.DOCUMENT;
import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.TRIP;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableBuilder;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( MockitoJUnitRunner.class )
public class MonitorableGrouperServiceTest
{
    @InjectMocks
    private MonitorableGrouperService service;

    @Test
    public void shouldReturnEmptyCollectionWhenNullMonitorable()
    {
        final Multimap<StandardMonitorableType,Monitorable> group = service.groupMonitorablesByType( null );
        assertThat( group ).isEqualTo( ImmutableSetMultimap.builder().build() );
    }

    @Test
    public void shouldReturnEmptyCollectionWhenEmptyMonitorables()
    {
        final Multimap<StandardMonitorableType,Monitorable> group = service.groupMonitorablesByType( emptyList() );
        assertThat( group ).isEqualTo( ImmutableSetMultimap.builder().build() );
    }

    @Test
    public void shouldGroupRootMonitorable()
    {
        final Monitorable root = new MonitorableBuilder( 1, "root", TRIP ).build();
        final Multimap<StandardMonitorableType,Monitorable> group = service.groupMonitorablesByType( singletonList( root ) );
        assertThat( group ).isEqualTo( ImmutableSetMultimap.builder().put( TRIP, root ).build() );
    }

    @Test
    public void shouldGroupMonitorablesByType()
    {
        final Monitorable child1 = new MonitorableBuilder( 2, "doc1", DOCUMENT ).build();
        final Monitorable child2 = new MonitorableBuilder( 3, "doc2", DOCUMENT ).build();
        final Monitorable root = new MonitorableBuilder( 1, "trip", TRIP ).build();
        final Multimap<StandardMonitorableType,Monitorable> group = service.groupMonitorablesByType( ImmutableList.of(
            root,
            child1,
            child2 ) );
        assertThat( group ).isEqualTo( ImmutableSetMultimap.builder()
            .put( TRIP, root )
            .put( StandardMonitorableType.DOCUMENT, child1 )
            .put( StandardMonitorableType.DOCUMENT, child2 )
            .build() );
    }
}