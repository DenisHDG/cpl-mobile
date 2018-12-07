package br.com.neolog.cplmobile.exception;

import static br.com.neolog.cplmobile.exception.ExceptionMessageExtractor.BAD_REQUEST;
import static br.com.neolog.cplmobile.exception.ExceptionMessageExtractor.FORBIDDEN;
import static br.com.neolog.cplmobile.exception.ExceptionMessageExtractor.NOT_FOUND;
import static br.com.neolog.cplmobile.exception.ExceptionMessageExtractor.TIMEOUT;
import static br.com.neolog.cplmobile.exception.ExceptionMessageExtractor.UNAUTHORIZED;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.api.RetrofitConverters;
import br.com.neolog.exceptionmessage.ExceptionMessages;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

@RunWith( MockitoJUnitRunner.class )
public class ExceptionMessageExtractorTest
{
    private static final MediaType JSON = MediaType.parse( "application/json" );
    private static final ResponseBody RESPONSE_BODY = ResponseBody.create( JSON, "blah" );
    private static final String ERROR_MESSAGE = "error";

    @InjectMocks
    private ExceptionMessageExtractor extractor;
    @Mock
    private Context context;
    @Mock
    private RetrofitConverters converters;
    @Mock
    private Converter<ResponseBody,ExceptionMessages> exceptionMessagesConverter;

    @Before
    public void setUp()
    {
        when( converters.responseBodyConverter( ExceptionMessages.class ) ).thenReturn( exceptionMessagesConverter );
    }

    @Test
    public void shouldConvertMessagesWhenBadRequestsHasMessages()
        throws IOException
    {
        final Response<?> response = whenErrorIs( BAD_REQUEST );
        final ExceptionMessages messages = ExceptionMessages.from( ERROR_MESSAGE, emptyList() );
        when( exceptionMessagesConverter.convert( response.errorBody() ) ).thenReturn( messages );
        verifyReturnsErrorMessage( response );
    }

    @Test
    public void shouldReturnInternalServerErrorWhenFailingToConvertBody()
        throws IOException
    {
        final Response<?> response = whenErrorIs( BAD_REQUEST );
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_internal_server_error );
        doThrow( IOException.class ).when( exceptionMessagesConverter ).convert( response.errorBody() );
        verifyReturnsErrorMessage( response );
    }

    @Test
    public void shouldReturnUnauthorizedWhenResponseIsUnauthorized()
    {
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_unauthorized );
        verifyReturnsErrorMessage( whenErrorIs( UNAUTHORIZED ) );
    }

    @Test
    public void shouldReturnForbiddenWhenResponseIsForbidden()
    {
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_forbbiden );
        verifyReturnsErrorMessage( whenErrorIs( FORBIDDEN ) );
    }

    @Test
    public void shouldReturnNotFoundWhenResponseIsNotFound()
    {
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_not_found );
        verifyReturnsErrorMessage( whenErrorIs( NOT_FOUND ) );
    }

    @Test
    public void shouldReturnTimeoutWhenResponseTimeout()
    {
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_timeout );
        verifyReturnsErrorMessage( whenErrorIs( TIMEOUT ) );
    }

    @Test
    public void shouldReturnInternalServerErrorWhenResponseInternalServerError()
    {
        whenBundleReturnsErrorMessage( R.string.remote_exception_handler_internal_server_error );
        verifyReturnsErrorMessage( whenErrorIs( 500 ) );
    }

    @NonNull
    private Response<Object> whenErrorIs(
        final int errorCode )
    {
        return Response.error( errorCode, RESPONSE_BODY );
    }

    private void whenBundleReturnsErrorMessage(
        final int bundle )
    {
        when( context.getString( bundle ) ).thenReturn( ERROR_MESSAGE );
    }

    private void verifyReturnsErrorMessage(
        final Response<?> response )
    {
        assertThat( extractor.convertErrorBody( response ) )
            .isEqualTo( ExceptionMessages.from( ERROR_MESSAGE, emptyList() ) );
    }
}