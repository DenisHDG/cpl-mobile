package br.com.neolog.cplmobile.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.neolog.cplmobile.exception.ExceptionMessageExtractor;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

@RunWith( MockitoJUnitRunner.class )
public class ApiResponseConverterTest
{
    private static final MediaType JSON = MediaType.parse( "application/json" );

    @InjectMocks
    private ApiResponseConverter subject;
    @Mock
    private ExceptionMessageExtractor exceptionMessageExtractor;

    @Test
    public void shouldConvertSuccessResultToApiResponse()
    {
        final Object body = new Object();
        assertThat( subject.convert( Response.success( body ) ) )
            .isEqualTo( new ApiResponse<>( 200, body, null ) );
    }

    @Test
    public void shouldConvertErrorResultToApiResponse()
    {
        final Response<Object> response = Response.error( 500, ResponseBody.create( JSON, "bla" ) );
        final ExceptionMessages messages = ExceptionMessages.from( "error", Collections.emptyList() );
        when( exceptionMessageExtractor.convertErrorBody( response ) ).thenReturn( messages );
        assertThat( subject.convert( response ) ).isEqualTo( new ApiResponse<>( 500, null, messages ) );
    }

    @Test
    public void shouldConvertThrowableToApiResponse()
    {
        final ExceptionMessages messages = ExceptionMessages.from( "error", Collections.emptyList() );
        assertThat( subject.convert( new RuntimeException( "error" ) ) )
            .isEqualTo( new ApiResponse<>( 500, null, messages ) );
    }
}