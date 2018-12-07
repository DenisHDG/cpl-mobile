package br.com.neolog.cplmobile.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class NumberFormatterTest
{
    private NumberFormatter numberFormatter;

    @Before
    public void setUp()
    {
        numberFormatter = new NumberFormatter( Locale.forLanguageTag( "pt-BR" ) );
    }

    @Test
    public void shouldReturnEmptyStringWhenNullNumber()
    {
        final String result = numberFormatter.format( null );
        assertThat( result ).isEmpty();
    }

    @Test
    public void shouldFormatNumberWithTwoDecimalDigits()
    {
        final String result = numberFormatter.format( 1 );
        assertThat( result ).isEqualTo( "1,00" );
    }
}