package br.com.neolog.cplmobile.monitorable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableTypeConverter;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( MockitoJUnitRunner.class )
public class MonitorableTypeConverterTest
{

    @InjectMocks
    private MonitorableTypeConverter converter;

    @Test
    public void shouldConvertTripTypeToTripResource()
    {
        assertThat( converter.convertToResource( StandardMonitorableType.TRIP ) )
            .isEqualTo( R.string.monitorable_type_trip );
    }

    @Test
    public void shouldConvertDocumentTypeToDocumentResource()
    {
        assertThat( converter.convertToResource( StandardMonitorableType.DOCUMENT ) )
            .isEqualTo( R.string.monitorable_type_document );
    }

    @Test
    public void shouldConvertInvoiceTypeToInvoiceResource()
    {
        assertThat( converter.convertToResource( StandardMonitorableType.INVOICE ) )
            .isEqualTo( R.string.monitorable_type_invoice );
    }

    @Test( expected = NullPointerException.class )
    public void shouldThrowNullPointerExceptionWhenNullMonitorableType()
    {
        converter.convertToResource( null );
    }

    @Test
    public void shouldConvertAllMonitorableTypes()
    {
        for( final StandardMonitorableType type : StandardMonitorableType.values() ) {
            assertThat( converter.convertToResource( type ) ).isNotNull();
        }
    }
}