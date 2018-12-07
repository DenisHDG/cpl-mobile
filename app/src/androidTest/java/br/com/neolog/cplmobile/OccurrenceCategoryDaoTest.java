package br.com.neolog.cplmobile;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryDao;
import br.com.neolog.cplmobile.persistence.AppDatabase;

@RunWith( AndroidJUnit4.class )
public class OccurrenceCategoryDaoTest
{
    private OccurrenceCategoryDao occurrenceCategoryDao;
    private AppDatabase database;

    @Before
    public void createDatabase()
    {
        database = Room.inMemoryDatabaseBuilder( InstrumentationRegistry.getContext(), AppDatabase.class ).build();
        occurrenceCategoryDao = database.getOccurrenceCategoryDao();
    }

    @After
    public void closeDatabase()
    {
        database.close();
    }

    @Test
    public void shouldInsertOccurrenceCategory()
    {

        final OccurrenceCategory occurrenceCategory = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );

        occurrenceCategoryDao.insert( occurrenceCategory );

        final OccurrenceCategory createdOccurrenceCategory = getOnlyElement( occurrenceCategoryDao.findAll() );
        assertThat( createdOccurrenceCategory ).isNotNull();
        assertThat( createdOccurrenceCategory.getId() ).isEqualTo( occurrenceCategory.getId() );
        assertThat( createdOccurrenceCategory.getSourceId() ).isEqualTo( occurrenceCategory.getSourceId() );
        assertThat( createdOccurrenceCategory.getName() ).isEqualTo( occurrenceCategory.getName() );
        assertThat( createdOccurrenceCategory.getDescription() ).isEqualTo( occurrenceCategory.getDescription() );
        assertThat( createdOccurrenceCategory.isQuantity() ).isEqualTo( occurrenceCategory.isQuantity() );
        assertThat( createdOccurrenceCategory.isTimeDelta() ).isEqualTo( occurrenceCategory.isTimeDelta() );
        assertThat( createdOccurrenceCategory.isValueDelta() ).isEqualTo( occurrenceCategory.isValueDelta() );
    }

    @Test
    public void shouldReturnEmptyWhenFindAllIsCalledWithDatabaseEmpty()
    {
        final List<OccurrenceCategory> createdOccurrenceCategory = occurrenceCategoryDao.findAll();
        assertThat( createdOccurrenceCategory ).isNotNull();
        assertThat( createdOccurrenceCategory ).isEmpty();
    }

    @Test
    public void shouldReturnAllOccurrenceCategoriesWhenFindAllHasOccurrenceCategory()
    {
        final OccurrenceCategory occurrenceCategoryStop = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );
        final OccurrenceCategory occurrenceCategoryLost = new OccurrenceCategory(
            2,
            "Extravio",
            "Extravio",
            "Extravio",
            false,
            true,
            false );
        occurrenceCategoryDao.insert( occurrenceCategoryStop );
        occurrenceCategoryDao.insert( occurrenceCategoryLost );

        final List<OccurrenceCategory> createdOccurrenceCategories = occurrenceCategoryDao.findAll();
        assertThat( createdOccurrenceCategories ).isNotNull();
        assertThat( createdOccurrenceCategories ).hasSize( 2 );

        final OccurrenceCategory createdOccurrenceCategoryStop = createdOccurrenceCategories.get( 0 );
        assertThat( createdOccurrenceCategoryStop ).isNotNull();
        assertThat( createdOccurrenceCategoryStop.getId() ).isEqualTo( occurrenceCategoryStop.getId() );
        assertThat( createdOccurrenceCategoryStop.getSourceId() ).isEqualTo( occurrenceCategoryStop.getSourceId() );
        assertThat( createdOccurrenceCategoryStop.getName() ).isEqualTo( occurrenceCategoryStop.getName() );
        assertThat( createdOccurrenceCategoryStop.getDescription() ).isEqualTo( occurrenceCategoryStop.getDescription() );
        assertThat( createdOccurrenceCategoryStop.isQuantity() ).isEqualTo( occurrenceCategoryStop.isQuantity() );
        assertThat( createdOccurrenceCategoryStop.isTimeDelta() ).isEqualTo( occurrenceCategoryStop.isTimeDelta() );
        assertThat( createdOccurrenceCategoryStop.isValueDelta() ).isEqualTo( occurrenceCategoryStop.isValueDelta() );

        final OccurrenceCategory createdOccurrenceCategoryLost = createdOccurrenceCategories.get( 1 );
        assertThat( createdOccurrenceCategoryLost ).isNotNull();
        assertThat( createdOccurrenceCategoryLost.getId() ).isEqualTo( occurrenceCategoryLost.getId() );
        assertThat( createdOccurrenceCategoryLost.getSourceId() ).isEqualTo( occurrenceCategoryLost.getSourceId() );
        assertThat( createdOccurrenceCategoryLost.getName() ).isEqualTo( occurrenceCategoryLost.getName() );
        assertThat( createdOccurrenceCategoryLost.getDescription() ).isEqualTo( occurrenceCategoryLost.getDescription() );
        assertThat( createdOccurrenceCategoryLost.isQuantity() ).isEqualTo( occurrenceCategoryLost.isQuantity() );
        assertThat( createdOccurrenceCategoryLost.isTimeDelta() ).isEqualTo( occurrenceCategoryLost.isTimeDelta() );
        assertThat( createdOccurrenceCategoryLost.isValueDelta() ).isEqualTo( occurrenceCategoryLost.isValueDelta() );
    }

    @Test
    public void shouldDeleteAllOccurrenceCategories()
    {
        final OccurrenceCategory occurrenceCategoryStop = new OccurrenceCategory(
            1,
            "Parada",
            "Parada",
            "Parada",
            false,
            true,
            false );
        final OccurrenceCategory occurrenceCategoryLost = new OccurrenceCategory(
            2,
            "Extravio",
            "Extravio",
            "Extravio",
            false,
            true,
            false );
        occurrenceCategoryDao.insert( occurrenceCategoryStop );
        occurrenceCategoryDao.insert( occurrenceCategoryLost );

        final List<OccurrenceCategory> createdOccurrenceCategories = occurrenceCategoryDao.findAll();
        assertThat( createdOccurrenceCategories ).isNotNull();
        assertThat( createdOccurrenceCategories ).hasSize( 2 );
        occurrenceCategoryDao.deleteAll();

        final List<OccurrenceCategory> occurrenceCategoriesAfterDelete = occurrenceCategoryDao.findAll();
        assertThat( occurrenceCategoriesAfterDelete ).isEmpty();
    }

}
