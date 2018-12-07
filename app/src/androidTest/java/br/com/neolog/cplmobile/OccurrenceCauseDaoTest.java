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
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableType;
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableTypeDao;
import br.com.neolog.cplmobile.occurrence.Impact;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseDao;
import br.com.neolog.cplmobile.persistence.AppDatabase;
import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

@RunWith( AndroidJUnit4.class )
public class OccurrenceCauseDaoTest
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
    public void shouldInsertOccurrenceCause()
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

        final Impact impact = new Impact( 10000L, null, null );
        final OccurrenceCause occurrenceCause = new OccurrenceCause(
            1,
            "Parada",
            "Parada",
            "Parada",
            impact,
            1,
            occurrenceCategory.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCause );

        final OccurrenceCause createdOccurrenceCause = getOnlyElement( occurrenceCauseDao.findAll() );
        assertThat( createdOccurrenceCause ).isNotNull();
        assertThat( createdOccurrenceCause.getId() ).isEqualTo( occurrenceCause.getId() );
        assertThat( createdOccurrenceCause.getSourceId() ).isEqualTo( occurrenceCause.getSourceId() );
        assertThat( createdOccurrenceCause.getName() ).isEqualTo( occurrenceCause.getName() );
        assertThat( createdOccurrenceCause.getDescription() ).isEqualTo( occurrenceCause.getDescription() );
        assertThat( createdOccurrenceCause.getImpact().getTimeDelta() ).isEqualTo( occurrenceCause.getImpact().getTimeDelta() );
        assertThat( createdOccurrenceCause.getCauseOrder() ).isEqualTo( occurrenceCause.getCauseOrder() );
        assertThat( createdOccurrenceCause.getCategoryId() ).isEqualTo( occurrenceCategory.getId() );
        assertThat( createdOccurrenceCause.isCommentRequired() ).isEqualTo( occurrenceCause.isCommentRequired() );
        assertThat( createdOccurrenceCause.isImageRequired() ).isEqualTo( occurrenceCause.isImageRequired() );
    }

    @Test
    public void shouldReturnEmptyWhenFindAllIsCalledWithDatabaseEmpty()
    {
        final List<OccurrenceCause> createdOccurrenceCause = occurrenceCauseDao.findAll();
        assertThat( createdOccurrenceCause ).isNotNull();
        assertThat( createdOccurrenceCause ).isEmpty();
    }

    @Test
    public void shouldReturnAllOccurrenceCausesWhenFindAllHasOccurrenceCause()
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

        final Impact impactTime = new Impact( null, null, 1 );
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

        final Impact impactValue = new Impact( null, 10d, null );
        final OccurrenceCause occurrenceCauseSupply = new OccurrenceCause(
            2,
            "Abastecimento",
            "Abastecimento",
            "Abastecimento",
            impactValue,
            1,
            occurrenceCategoryStop.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCauseCall );
        occurrenceCauseDao.insert( occurrenceCauseSupply );

        final List<OccurrenceCause> createdOccurrenceCauses = occurrenceCauseDao.findAll();
        assertThat( createdOccurrenceCauses ).isNotNull();
        assertThat( createdOccurrenceCauses ).hasSize( 2 );

        final OccurrenceCause createdOccurrenceCauseCall = createdOccurrenceCauses.get( 0 );
        assertThat( createdOccurrenceCauseCall ).isNotNull();
        assertThat( createdOccurrenceCauseCall.getId() ).isEqualTo( occurrenceCauseCall.getId() );
        assertThat( createdOccurrenceCauseCall.getSourceId() ).isEqualTo( occurrenceCauseCall.getSourceId() );
        assertThat( createdOccurrenceCauseCall.getName() ).isEqualTo( occurrenceCauseCall.getName() );
        assertThat( createdOccurrenceCauseCall.getDescription() ).isEqualTo( occurrenceCauseCall.getDescription() );
        assertThat( createdOccurrenceCauseCall.getImpact().getQuantityDelta() ).isEqualTo( occurrenceCauseCall.getImpact().getQuantityDelta() );
        assertThat( createdOccurrenceCauseCall.getCauseOrder() ).isEqualTo( occurrenceCauseCall.getCauseOrder() );
        assertThat( createdOccurrenceCauseCall.getCategoryId() ).isEqualTo( occurrenceCategoryStop.getId() );
        assertThat( createdOccurrenceCauseCall.isCommentRequired() ).isEqualTo( occurrenceCauseCall.isCommentRequired() );
        assertThat( createdOccurrenceCauseCall.isImageRequired() ).isEqualTo( occurrenceCauseCall.isImageRequired() );

        final OccurrenceCause createdOccurrenceCauseSupply = createdOccurrenceCauses.get( 1 );
        assertThat( createdOccurrenceCauseSupply ).isNotNull();
        assertThat( createdOccurrenceCauseSupply.getId() ).isEqualTo( occurrenceCauseSupply.getId() );
        assertThat( createdOccurrenceCauseSupply.getSourceId() ).isEqualTo( occurrenceCauseSupply.getSourceId() );
        assertThat( createdOccurrenceCauseSupply.getName() ).isEqualTo( occurrenceCauseSupply.getName() );
        assertThat( createdOccurrenceCauseSupply.getDescription() ).isEqualTo( occurrenceCauseSupply.getDescription() );
        assertThat( createdOccurrenceCauseSupply.getImpact().getValueDelta() ).isEqualTo( occurrenceCauseSupply.getImpact()
            .getValueDelta() );
        assertThat( createdOccurrenceCauseSupply.getCauseOrder() ).isEqualTo( occurrenceCauseSupply.getCauseOrder() );
        assertThat( createdOccurrenceCauseSupply.getCategoryId() ).isEqualTo( occurrenceCategoryStop.getId() );
        assertThat( createdOccurrenceCauseSupply.isCommentRequired() ).isEqualTo( occurrenceCauseSupply.isCommentRequired() );
        assertThat( createdOccurrenceCauseSupply.isImageRequired() ).isEqualTo( occurrenceCauseSupply.isImageRequired() );

    }

    @Test
    public void shouldDeleteAllOccurrenceCauses()
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

        final Impact impactValue = new Impact( null, 10d, null );
        final OccurrenceCause occurrenceCauseSupply = new OccurrenceCause(
            2,
            "Abastecimento",
            "Abastecimento",
            "Abastecimento",
            impactValue,
            1,
            occurrenceCategoryStop.getId(),
            false,
            false );

        occurrenceCauseDao.insert( occurrenceCauseCall );
        occurrenceCauseDao.insert( occurrenceCauseSupply );

        final List<OccurrenceCause> createdOccurrenceCauses = occurrenceCauseDao.findAll();
        assertThat( createdOccurrenceCauses ).isNotNull();
        assertThat( createdOccurrenceCauses ).hasSize( 2 );
        occurrenceCauseDao.deleteAll();

        final List<OccurrenceCause> occurrenceCausesAfterDelete = occurrenceCauseDao.findAll();
        assertThat( occurrenceCausesAfterDelete ).isEmpty();
    }

    @Test
    public void shouldFindOccurrenceCausesByAllowedMonitorableType()
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

        final List<OccurrenceCause> occurrenceCauseByAllowedMonitorableType = occurrenceCauseDao.findOccurrenceCauseByAllowedMonitorableType(
            StandardMonitorableType.TRIP.name() );

        assertThat(occurrenceCauseByAllowedMonitorableType).isNotNull();
        assertThat(occurrenceCauseByAllowedMonitorableType).hasSize(1);

        final OccurrenceCause createdOccurrenceCause = getOnlyElement( occurrenceCauseDao.findAll() );
        assertThat( createdOccurrenceCause ).isNotNull();
        assertThat( createdOccurrenceCause.getId() ).isEqualTo( occurrenceCauseCall.getId() );
        assertThat( createdOccurrenceCause.getSourceId() ).isEqualTo( occurrenceCauseCall.getSourceId() );
        assertThat( createdOccurrenceCause.getName() ).isEqualTo( occurrenceCauseCall.getName() );
        assertThat( createdOccurrenceCause.getDescription() ).isEqualTo( occurrenceCauseCall.getDescription() );
        assertThat( createdOccurrenceCause.getImpact().getTimeDelta() ).isEqualTo( occurrenceCauseCall.getImpact().getTimeDelta() );
        assertThat( createdOccurrenceCause.getCauseOrder() ).isEqualTo( occurrenceCauseCall.getCauseOrder() );
        assertThat( createdOccurrenceCause.getCategoryId() ).isEqualTo( occurrenceCauseCall.getId() );
        assertThat( createdOccurrenceCause.isCommentRequired() ).isEqualTo( occurrenceCauseCall.isCommentRequired() );
        assertThat( createdOccurrenceCause.isImageRequired() ).isEqualTo( occurrenceCauseCall.isImageRequired() );

    }

}
