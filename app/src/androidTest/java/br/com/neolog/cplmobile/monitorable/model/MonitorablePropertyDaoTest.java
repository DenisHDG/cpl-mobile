package br.com.neolog.cplmobile.monitorable.model;

import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.CARRIER;
import static br.com.neolog.cplmobile.monitorable.repo.MonitorablePropertyType.VEHICLE;
import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( AndroidJUnit4.class )
public class MonitorablePropertyDaoTest
{
    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

    private static final int MONITORABLE_ID = 1;
    private MonitorablePropertyDao monitorablePropertyDao;
    private MonitorableDao monitorableDao;

    @Before
    public void setUp()
    {
        final AppDatabase db = Room.inMemoryDatabaseBuilder( InstrumentationRegistry.getContext(), AppDatabase.class )
            .allowMainThreadQueries()
            .build();
        monitorableDao = db.getMonitorableDao();
        monitorableDao.insert( new MonitorableBuilder( MONITORABLE_ID, "code", StandardMonitorableType.TRIP ).build() );

        monitorablePropertyDao = db.getMonitorablePropertyDao();
    }

    @Test
    public void shouldInsertPropertyForMonitorable()
    {
        final MonitorableProperty property = new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle" );
        monitorablePropertyDao.insert( property );
        assertThat( findByMonitorableId() ).containsExactly( property );
    }

    private List<MonitorableProperty> findByMonitorableId()
    {
        final LiveData<List<MonitorableProperty>> data = monitorablePropertyDao.findByMonitorableId( MONITORABLE_ID );
        data.observeForever( value -> {
        } );
        return data.getValue();
    }

    @Test( expected = SQLiteConstraintException.class )
    public void shouldNotInsertRepeatedPropertyForMonitorable()
    {
        monitorablePropertyDao.insert( new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle1" ) );
        monitorablePropertyDao.insert( new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle2" ) );
    }

    @Test( expected = SQLiteConstraintException.class )
    public void shouldNotInsertPropertyForNonExistentMonitorable()
    {
        monitorablePropertyDao.insert( new MonitorableProperty( - 1, VEHICLE, "vehicle" ) );
    }

    @Test
    public void shouldRemoveAllPropertiesFromMonitorable()
    {
        monitorablePropertyDao.insert( new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle" ) );
        monitorablePropertyDao.insert( new MonitorableProperty( MONITORABLE_ID, CARRIER, "carrier" ) );
        monitorablePropertyDao.deleteByMonitorableId( MONITORABLE_ID );
        assertThat( findByMonitorableId() ).isEmpty();
    }

    @Test
    public void shouldRemovePropertiesWhenMonitorableIsRemoved()
    {
        monitorablePropertyDao.insert( new MonitorableProperty( MONITORABLE_ID, VEHICLE, "vehicle" ) );
        monitorableDao.delete( MONITORABLE_ID );
        assertThat( findByMonitorableId() ).isEmpty();
    }
}