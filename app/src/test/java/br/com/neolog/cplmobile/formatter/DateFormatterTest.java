package br.com.neolog.cplmobile.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class DateFormatterTest
{
    private DateFormatter dateFormatter;

    @Before
    public void setUp()
    {
        dateFormatter = new DateFormatter( Locale.forLanguageTag( "pt-BR" ) );
    }

    @Test
    public void shouldReturnEmptyStringWhenNullDate()
    {
        final String result = dateFormatter.formatDateTime( null );
        assertThat( result ).isEmpty();
    }

    @Test
    public void shouldFormatDateAndTime()
    {
        final DateTime dateTime = DateTime.parse( "2018-10-30T10:00:00.000-03:00" );
        final String result = dateFormatter.formatDateTime( dateTime );
        assertThat( result ).isEqualTo( "30/10/18 10:00" );
    }
}