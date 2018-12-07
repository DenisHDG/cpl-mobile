package br.com.neolog.cplmobile.formatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.neolog.monitoring.monitorable.model.rest.RestDriver;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

@RunWith( MockitoJUnitRunner.class )
public class DriverFormatterTest
{
    @InjectMocks
    private DriverFormatter formatter;
    @Mock
    private ExternalizableEntityFormatter entityFormatter;
    @Mock
    private RestDriver driver;

    @Test
    public void shouldReturnEmptyStringForNullEntity()
    {
        assertThat( formatter.format( null ) ).isEmpty();
    }

    @Test
    public void shouldReturnSameFormatAsEntityFormatter()
    {
        when( driver.getSourceId() ).thenReturn( "sid" );
        when( driver.getName() ).thenReturn( "name" );
        when( driver.getDescription() ).thenReturn( "desc" );
        when( entityFormatter.format( new RestExternalEntity( "sid", "name", "desc" ) ) ).thenReturn( "format" );
        assertThat( formatter.format( driver ) ).isEqualTo( "format" );
    }
}