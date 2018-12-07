package br.com.neolog.cplmobile.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

@RunWith( MockitoJUnitRunner.class )
public class ExternalizableEntityFormatterTest
{
    @InjectMocks
    private ExternalizableEntityFormatter formatter;

    @Test
    public void shouldReturnEmptyStringForNullEntity()
    {
        assertThat( formatter.format( null ) ).isEmpty();
    }

    @Test
    public void shouldReturnEntityNameAndSourceIdWhenBothAreDifferent()
    {
        final RestExternalEntity entity = new RestExternalEntity( "sid", "name", "desc" );
        assertThat( formatter.format( entity ) ).isEqualTo( "name - sid" );
    }

    @Test
    public void shouldReturnEntityNameWhenNameAndSourceIdAreEquals()
    {
        final RestExternalEntity entity = new RestExternalEntity( "sid", "sid", "desc" );
        assertThat( formatter.format( entity ) ).isEqualTo( "sid" );
    }
}