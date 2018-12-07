package br.com.neolog.cplmobile.databinding;

import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.TRIP;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import android.widget.TextView;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.monitorable.repo.MonitorableTypeConverter;

@RunWith( MockitoJUnitRunner.class )
public class MonitorableTypeBindingAdapterTest
{
    @InjectMocks
    private MonitorableTypeBindingAdapter adapter;
    @Mock
    private MonitorableTypeConverter typeConverter;

    @Mock
    private TextView textView;

    @Test
    public void shouldSetTextWithConvertedValue()
    {
        when( typeConverter.convertToResource( TRIP ) ).thenReturn( R.string.monitorable_type_trip );
        adapter.setText( textView, TRIP );
        verify( textView ).setText( R.string.monitorable_type_trip );
    }
}