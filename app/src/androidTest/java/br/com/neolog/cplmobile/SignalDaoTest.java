package br.com.neolog.cplmobile;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.persistence.AppDatabase;
import br.com.neolog.cplmobile.signal.Signal;
import br.com.neolog.cplmobile.signal.SignalDao;

@RunWith( AndroidJUnit4.class )
public class SignalDaoTest
{
    private SignalDao signalDao;
    private AppDatabase database;

    @Before
    public void createDatabase()
    {
        database = Room.inMemoryDatabaseBuilder( InstrumentationRegistry.getContext(), AppDatabase.class ).build();
        signalDao = database.getSignalDao();
    }

    @After
    public void closeDatabase()
    {
        database.close();
    }

    @Test
    public void shouldInsertSignal()
    {

        final DateTime signalTimestamp = DateTime.now();
        final Signal signal = new Signal( 23.6115217, - 46.6968023, signalTimestamp );
        signalDao.insert( signal );

        final Signal createdSignal = getOnlyElement( signalDao.findAll() );
        assertThat( createdSignal ).isNotNull();
        assertThat( createdSignal.getLatitude() ).isEqualTo( signal.getLatitude() );
        assertThat( createdSignal.getLongitude() ).isEqualTo( signal.getLongitude() );
        assertThat( createdSignal.getSignalTime() ).isEqualTo( signal.getSignalTime() );
    }

    @Test
    public void shouldReturnEmptyWhenFindAllIsCalledWithDatabaseEmpty()
    {
        final List<Signal> createdSignals = signalDao.findAll();
        assertThat( createdSignals ).isNotNull();
        assertThat( createdSignals ).isEmpty();
    }

    @Test
    public void shouldReturnAllSignalWhenFindAllHasSignal()
    {
        final DateTime signalTimestamp = DateTime.now();
        final Signal firstSignal = new Signal( 23.6115217, - 46.6968023, signalTimestamp );
        final Signal secondSignal = new Signal( - 23.6148815, - 46.6818888, signalTimestamp.plusSeconds( 10 ) );
        signalDao.insert( firstSignal );
        signalDao.insert( secondSignal );

        final List<Signal> createdSignals = signalDao.findAll();
        assertThat( createdSignals ).isNotNull();
        assertThat( createdSignals ).hasSize( 2 );
        final Signal firstCreatedSignal = createdSignals.get( 0 );
        assertThat( firstCreatedSignal.getLatitude() ).isEqualTo( firstSignal.getLatitude() );
        assertThat( firstCreatedSignal.getLongitude() ).isEqualTo( firstSignal.getLongitude() );
        assertThat( firstCreatedSignal.getSignalTime() ).isEqualTo( firstSignal.getSignalTime() );

        final Signal secondCreatedSignal = createdSignals.get( 1 );
        assertThat( secondCreatedSignal.getLatitude() ).isEqualTo( secondSignal.getLatitude() );
        assertThat( secondCreatedSignal.getLongitude() ).isEqualTo( secondSignal.getLongitude() );
        assertThat( secondCreatedSignal.getSignalTime() ).isEqualTo( secondSignal.getSignalTime() );
    }

    @Test
    public void shouldDeleteSignal()
    {
        final DateTime signalTimestamp = DateTime.now();
        final Signal signalToPersist = new Signal( 23.6115217, - 46.6968023, signalTimestamp );
        signalDao.insert( signalToPersist );

        final List<Signal> createdSignals = signalDao.findAll();
        assertThat( createdSignals ).isNotNull();
        assertThat( createdSignals ).hasSize( 1 );
        final Signal signal = getOnlyElement( createdSignals );
        signalDao.delete( signal );

        final List<Signal> signalAfterDelete = signalDao.findAll();
        assertThat( signalAfterDelete ).isEmpty();
    }

    @Test
    public void shouldDeleteSignalsList()
    {
        final DateTime signalTimestamp = DateTime.now();
        final Signal firstSignal = new Signal( 23.6115217, - 46.6968023, signalTimestamp );
        final Signal secondSignal = new Signal( - 23.6148815, - 46.6818888, signalTimestamp.plusSeconds( 10 ) );
        signalDao.insert( firstSignal );
        signalDao.insert( secondSignal );

        final List<Signal> createdSignals = signalDao.findAll();
        assertThat( createdSignals ).isNotNull();
        assertThat( createdSignals ).hasSize( 2 );
        signalDao.delete( createdSignals );

        final List<Signal> signalsAfterDelete = signalDao.findAll();
        assertThat( signalsAfterDelete ).isEmpty();
    }
}
