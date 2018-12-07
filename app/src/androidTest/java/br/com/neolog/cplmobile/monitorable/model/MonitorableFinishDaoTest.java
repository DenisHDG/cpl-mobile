package br.com.neolog.cplmobile.monitorable.model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.persistence.AppDatabase;

@RunWith( AndroidJUnit4.class )
public class MonitorableFinishDaoTest
{
    private static final int MONITORABLE_ID = 1;

    private MonitorableFinishDao monitorableFinishDao;

    @Before
    public void setUp()
    {
        final AppDatabase database = Room.inMemoryDatabaseBuilder( InstrumentationRegistry.getContext(), AppDatabase.class )
            .build();
        monitorableFinishDao = database.getMonitorableFinishDao();
        monitorableFinishDao.insert( new MonitorableFinish( MONITORABLE_ID ) );
    }

    @Test
    public void shouldInsertMonitorableToFinish()
    {
        assertThat( monitorableFinishDao.findAll() ).containsExactly( new MonitorableFinish( 1 ) );
    }

    @Test
    public void shouldRemoveMonitorableToFinish()
    {
        final MonitorableFinish alternativeMonitorable = new MonitorableFinish( 2 );
        monitorableFinishDao.insert( alternativeMonitorable );
        monitorableFinishDao.deleteByMonitorableId( MONITORABLE_ID );
        assertThat( monitorableFinishDao.findAll() ).containsExactly( alternativeMonitorable );
    }
}