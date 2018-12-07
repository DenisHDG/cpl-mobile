package br.com.neolog.cplmobile.monitorable.model;

import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.DOCUMENT;
import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.INVOICE;
import static br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType.TRIP;
import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( AndroidJUnit4.class )
public class MonitorableDaoTest
{

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MonitorableDao monitorableDao;

    @Before
    public void setUp()
    {
        final Context context = InstrumentationRegistry.getContext();
        monitorableDao = Room.inMemoryDatabaseBuilder( context, AppDatabase.class )
            .allowMainThreadQueries()
            .build()
            .getMonitorableDao();
    }

    @Test
    public void shouldInsertNewMonitorable()
    {
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        final Monitorable saved = monitorableDao.findOne( 1 );
        assertThat( saved ).isEqualTo( monitorable( 1, "code", TRIP ) );
    }

    @NonNull
    private Monitorable monitorable(
        final int id,
        final String code,
        final StandardMonitorableType type )
    {
        return new MonitorableBuilder( id, code, type ).build();
    }

    @Test
    public void shouldVerifyThatMonitorableExists()
    {
        assertThat( monitorableDao.exists( 1 ) ).isFalse();
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        assertThat( monitorableDao.exists( 1 ) ).isTrue();
    }

    @Test
    public void shouldDeleteMonitorables()
    {
        assertThat( monitorableDao.count() ).isEqualTo( 0 );
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        assertThat( monitorableDao.count() ).isEqualTo( 1 );
        monitorableDao.delete( 1 );
        assertThat( monitorableDao.count() ).isEqualTo( 0 );
    }

    @Test
    public void shouldUpdateMonitorables()
    {
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        monitorableDao.update( monitorable( 1, "code2", TRIP ) );
        final Monitorable saved = monitorableDao.findOne( 1 );
        assertThat( saved.getCode() ).isEqualTo( "code2" );
    }

    @Test( expected = SQLiteConstraintException.class )
    public void shouldNotAllowIdDuplicates()
    {
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        monitorableDao.insert( monitorable( 1, "code2", DOCUMENT ) );
    }

    @Test( expected = SQLiteConstraintException.class )
    public void shouldNotAllowCodeAndTypeDuplicates()
    {
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        monitorableDao.insert( monitorable( 2, "code", TRIP ) );
    }

    @Test
    public void shouldRemoveChildMonitorables()
    {
        monitorableDao.insert( monitorable( 1, "code", TRIP ) );
        monitorableDao.insert( new MonitorableBuilder( 2, "child", DOCUMENT ).setParentId( 1 ).build() );
        monitorableDao.insert( new MonitorableBuilder( 3, "child2", INVOICE ).setParentId( 2 ).build() );
        monitorableDao.delete( 1 );
        assertThat( findByIds() ).isEmpty();
    }

    private List<Monitorable> findByIds(
        final Integer... ids )
    {
        final LiveData<List<Monitorable>> liveData = monitorableDao.findByIds( Arrays.asList( ids ) );
        liveData.observeForever( obj -> {
        } );
        return liveData.getValue();
    }

}