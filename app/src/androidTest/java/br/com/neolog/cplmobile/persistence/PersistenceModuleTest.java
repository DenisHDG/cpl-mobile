package br.com.neolog.cplmobile.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

@RunWith( AndroidJUnit4.class )
public class PersistenceModuleTest
{

    @Rule
    public final MigrationTestHelper migrationTestHelper = new MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        checkNotNull( AppDatabase.class.getCanonicalName() ),
        new FrameworkSQLiteOpenHelperFactory() );

    @Test
    public void shouldMigrateFullDatabase()
        throws IOException
    {
        final String dbName = "test_cpl_mobile";
        migrationTestHelper.createDatabase( dbName, 1 );
        migrationTestHelper.runMigrationsAndValidate( dbName, AppDatabase.VERSION, true, PersistenceModule.MIGRATIONS );
    }
}
