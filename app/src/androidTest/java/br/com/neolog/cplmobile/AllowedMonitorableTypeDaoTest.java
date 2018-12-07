package br.com.neolog.cplmobile;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryDao;
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableType;
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableTypeDao;
import br.com.neolog.cplmobile.occurrence.Impact;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseDao;
import br.com.neolog.cplmobile.persistence.AppDatabase;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( AndroidJUnit4.class )
public class AllowedMonitorableTypeDaoTest
{
    private OccurrenceCategoryDao occurrenceCategoryDao;
    private OccurrenceCauseDao occurrenceCauseDao;
    private AllowedMonitorableTypeDao allowedMonitorableTypeDao;
    private AppDatabase database;

    @Before
    public void createDatabase()
    {
        database = Room.inMemoryDatabaseBuilder( InstrumentationRegistry.getContext(), AppDatabase.class ).build();
        occurrenceCategoryDao = database.getOccurrenceCategoryDao();
        occurrenceCauseDao = database.getOccurrenceCauseDao();
        allowedMonitorableTypeDao = database.getAllowedMonitorableTypeDao();
    }

    @After
    public void closeDatabase()
    {
        database.close();
    }

    @Test
    public void shouldInsertAllowedMonitorableTypeDaoTest()
    {

        final OccurrenceCategory occurrenceCategoryStop = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );

        occurrenceCategoryDao.insert( occurrenceCategoryStop );

        final Impact impactTime = new Impact( 10000L, null, null );
        final OccurrenceCause occurrenceCauseCall = new OccurrenceCause(
            1,
            "Telefonar",
            "Telefonar",
            "Telefonar",
            impactTime,
            1,
            occurrenceCategoryStop.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCauseCall );

        allowedMonitorableTypeDao.insert( new AllowedMonitorableType( occurrenceCauseCall.getId(), StandardMonitorableType.TRIP.name() ) );
        final List<AllowedMonitorableType> allowedMonitorableTypes = allowedMonitorableTypeDao.findAll();
        assertThat( allowedMonitorableTypes ).isNotNull();
        assertThat( allowedMonitorableTypes ).hasSize( 1 );

        final AllowedMonitorableType allowedMonitorableType = Iterables.getOnlyElement( allowedMonitorableTypes );
        assertThat( allowedMonitorableType.getMonitorableType() ).isEqualTo( StandardMonitorableType.TRIP.name() );
        assertThat( allowedMonitorableType.getOccurrenceCauseId() ).isEqualTo( occurrenceCauseCall.getId() );

    }

    @Test
    public void shouldReturnEmptyWhenFindAllIsCalledWithDatabaseEmpty()
    {
        final List<AllowedMonitorableType> createdAllowedMonitorableType = allowedMonitorableTypeDao.findAll();
        assertThat( createdAllowedMonitorableType ).isNotNull();
        assertThat( createdAllowedMonitorableType ).isEmpty();
    }

    @Test
    public void shouldReturnAllAllowedMonitorableTypeWhenFindAllHasAllowedMonitorableType()
    {

        final OccurrenceCategory occurrenceCategoryStop = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );

        occurrenceCategoryDao.insert( occurrenceCategoryStop );

        final Impact impactTime = new Impact( 10000L, null, null );
        final OccurrenceCause occurrenceCauseCall = new OccurrenceCause(
            1,
            "Telefonar",
            "Telefonar",
            "Telefonar",
            impactTime,
            1,
            occurrenceCategoryStop.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCauseCall );

        allowedMonitorableTypeDao.insert( new AllowedMonitorableType( occurrenceCauseCall.getId(), StandardMonitorableType.TRIP.name() ) );
        allowedMonitorableTypeDao.insert( new AllowedMonitorableType( occurrenceCauseCall.getId(), StandardMonitorableType.DOCUMENT
            .name() ) );

        final List<AllowedMonitorableType> createdAllowedMonitorableTypes = allowedMonitorableTypeDao.findAll();
        assertThat( createdAllowedMonitorableTypes ).isNotNull();
        assertThat( createdAllowedMonitorableTypes ).hasSize( 2 );

        final AllowedMonitorableType allowedMonitorableTypeTrip = createdAllowedMonitorableTypes.get( 0 );
        assertThat( allowedMonitorableTypeTrip ).isNotNull();
        assertThat( allowedMonitorableTypeTrip.getMonitorableType() ).isEqualTo( StandardMonitorableType.TRIP.name() );
        assertThat( allowedMonitorableTypeTrip.getOccurrenceCauseId() ).isEqualTo( occurrenceCauseCall.getId() );

        final AllowedMonitorableType allowedMonitorableTypeDocument = createdAllowedMonitorableTypes.get( 1 );
        assertThat( allowedMonitorableTypeDocument ).isNotNull();
        assertThat( allowedMonitorableTypeDocument.getMonitorableType() ).isEqualTo( StandardMonitorableType.DOCUMENT.name() );
        assertThat( allowedMonitorableTypeDocument.getOccurrenceCauseId() ).isEqualTo( occurrenceCauseCall.getId() );

    }

    @Test
    public void shouldDeleteAllAllowedMonitorableTypes()
    {

        final OccurrenceCategory occurrenceCategoryStop = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );

        occurrenceCategoryDao.insert( occurrenceCategoryStop );

        final Impact impactTime = new Impact( 10000L, null, null );
        final OccurrenceCause occurrenceCauseCall = new OccurrenceCause(
            1,
            "Telefonar",
            "Telefonar",
            "Telefonar",
            impactTime,
            1,
            occurrenceCategoryStop.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCauseCall );

        allowedMonitorableTypeDao.insert( new AllowedMonitorableType( occurrenceCauseCall.getId(), StandardMonitorableType.TRIP.name() ) );
        allowedMonitorableTypeDao.insert( new AllowedMonitorableType( occurrenceCauseCall.getId(), StandardMonitorableType.DOCUMENT
            .name() ) );

        final List<AllowedMonitorableType> createdAllowedMonitorableTypes = allowedMonitorableTypeDao.findAll();
        assertThat( createdAllowedMonitorableTypes ).isNotNull();
        assertThat( createdAllowedMonitorableTypes ).hasSize( 2 );

        allowedMonitorableTypeDao.deleteAll();

        final List<AllowedMonitorableType> createdAllowedMonitorableTypesAfterDelete = allowedMonitorableTypeDao.findAll();
        assertThat( createdAllowedMonitorableTypesAfterDelete ).isEmpty();
    }

}
