package br.com.neolog.cplmobile.databinding;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.widget.TextView;

import br.com.neolog.cplmobile.formatter.ExternalizableEntityFormatter;
import br.com.neolog.monitoring.monitorable.model.rest.RestExternalEntity;

@RunWith( MockitoJUnitRunner.class )
public class ExternalizableEntityBindingAdapterTest
{

    @InjectMocks
    private ExternalizableEntityBindingAdapter adapter;
    @Mock
    private ExternalizableEntityFormatter converter;

    @Mock
    private TextView textView;
    @Mock
    private RestExternalEntity entity;

    @Test
    public void shouldSetTextWithFormattedValue()
    {
        when( converter.format( entity ) ).thenReturn( "entity" );
        adapter.setText( textView, entity );
        verify( textView ).setText( "entity" );
    }
}