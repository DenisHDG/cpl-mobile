package br.com.neolog.cplmobile.monitorable.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.content.Context;

import br.com.neolog.cplmobile.R;
import br.com.neolog.monitoring.monitorable.model.api.CompletionStatus;

@RunWith( MockitoJUnitRunner.class )
public class CompletionStatusConverterTest
{
    @InjectMocks
    private CompletionStatusConverter converter;
    @Mock
    private Context context;

    @Test( expected = NullPointerException.class )
    public void shouldThrowNullPointerExceptionWhenNullStatus()
    {
        converter.convertToString( null );
    }

    @Test
    public void shouldConvertAllStatus()
    {
        when( context.getString( anyInt() ) ).thenReturn( "string" );
        for( final CompletionStatus completionStatus : CompletionStatus.values() ) {
            assertThat( converter.convertToString( completionStatus ) ).isNotNull();
        }
    }

    @Test
    public void shouldConvertCreatedToString()
    {
        when( context.getString( R.string.completion_status_created ) ).thenReturn( "string" );
        assertThat( converter.convertToString( CompletionStatus.CREATED ) ).isEqualTo( "string" );
    }

    @Test
    public void shouldConvertInExecutionToString()
    {
        when( context.getString( R.string.completion_status_in_execution ) ).thenReturn( "string" );
        assertThat( converter.convertToString( CompletionStatus.IN_EXECUTION ) ).isEqualTo( "string" );
    }

    @Test
    public void shouldConvertFinishedToString()
    {
        when( context.getString( R.string.completion_status_finished ) ).thenReturn( "string" );
        assertThat( converter.convertToString( CompletionStatus.FINISHED ) ).isEqualTo( "string" );
    }

    @Test
    public void shouldConvertCancelledToString()
    {
        when( context.getString( R.string.completion_status_cancelled ) ).thenReturn( "string" );
        assertThat( converter.convertToString( CompletionStatus.CANCELLED ) ).isEqualTo( "string" );
    }
}