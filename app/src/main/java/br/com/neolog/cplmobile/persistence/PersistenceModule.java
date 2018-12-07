package br.com.neolog.cplmobile.persistence;

import static br.com.neolog.cplmobile.persistence.AppDatabase.DATABASE_NAME;

import javax.inject.Singleton;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule
{
    @Singleton
    @Provides
    AppDatabase createDatabase(
        final Context appContext )
    {
        return Room.databaseBuilder( appContext, AppDatabase.class, DATABASE_NAME )
            .addMigrations( MIGRATIONS )
            .build();
    }

    private static final Migration MIGRATION_0_1 = new Migration( 0, 1 ) {

        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE IF NOT EXISTS 'signal' (" +
                "'id' INTEGER, " +
                "'latitude' REAL, " +
                "'longitude' REAL, " +
                "'signal_time' INTEGER, PRIMARY KEY('id'))" );
        }
    };

    private static final Migration MIGRATION_1_2 = new Migration( 1, 2 ) {

        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE IF NOT EXISTS 'occurrence_category' (" +
                "'id' INTEGER, " +
                "'source_id' TEXT, " +
                "'name' TEXT, " +
                "'description' TEXT, " +
                "'quantity' INTEGER NOT NULL, " +
                "'time_delta' INTEGER NOT NULL, " +
                "'value_delta' INTEGER NOT NULL,  PRIMARY KEY('id'))" );
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration( 2, 3 ) {

        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE IF NOT EXISTS 'occurrence_cause' (" +
                "'id' INTEGER, " +
                "'source_id' TEXT, " +
                "'name' TEXT, " +
                "'description' TEXT, " +
                "'time_delta' INTEGER, " +
                "'value_delta' REAL, " +
                "'quantity_delta' INTEGER, " +
                "'cause_order' INTEGER NOT NULL, " +
                "'category_id' INTEGER, " +
                "'comment_required' INTEGER NOT NULL, " +
                "'image_required' INTEGER NOT NULL, " +
                " PRIMARY KEY('id'), " +
                " CONSTRAINT occurrence_cause_cat_id FOREIGN KEY (category_id) REFERENCES occurrence_category(id) ON DELETE CASCADE)" );
            database.execSQL( "CREATE INDEX category_id ON occurrence_cause(category_id)" );

            database.execSQL( "CREATE TABLE IF NOT EXISTS 'allowed_monitorable_type' (" +
                "'id' INTEGER, " +
                "'occurrence_cause_id' INTEGER, " +
                "'monitorable_type' TEXT ,  PRIMARY KEY('id'))" );
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration( 3, 4 ) {

        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE IF NOT EXISTS 'occurrence' (" +
                "'id' INTEGER, " +
                "'remote_server_id' INTEGER, " +
                "'source_id' TEXT, " +
                "'timestamp' INTEGER, " +
                "'cause_id' INTEGER, " +
                "'cause_source_id' TEXT, " +
                "'cause_name' TEXT, " +
                "'cause_description' TEXT, " +
                "'category_id' INTEGER, " +
                "'category_source_id' TEXT, " +
                "'category_name' TEXT, " +
                "'category_description' TEXT, " +
                "'impact_time_delta' INTEGER, " +
                "'impact_value_delta' REAL, " +
                "'impact_quantity_delta' INTEGER, " +
                "'latitude' REAL, " +
                "'longitude' REAL,  PRIMARY KEY('id'), UNIQUE('remote_server_id'))" );
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration( 4, 5 ) {
        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE monitorable (" +
                " id INTEGER NOT NULL, " +
                " code TEXT NOT NULL," +
                " type TEXT NOT NULL," +
                " late_status TEXT NOT NULL," +
                " parent_id INTEGER, " +
                " root INTEGER NOT NULL, " +
                " CONSTRAINT monitorable_id PRIMARY KEY (id), " +
                " CONSTRAINT monitorable_fk_parent FOREIGN KEY (parent_id) REFERENCES monitorable(id) ON DELETE CASCADE" +
                " )" );
            database.execSQL( "CREATE UNIQUE INDEX monitorable_id_idx ON monitorable (id)" );
            database.execSQL( "CREATE INDEX monitorable_parent_idx ON monitorable (parent_id)" );
            database.execSQL( "CREATE UNIQUE INDEX monitorable_uq_code_type ON monitorable (code, type)" );

            database.execSQL( "CREATE TABLE monitorable_property (" +
                " monitorable_id INTEGER NOT NULL, " +
                " name TEXT NOT NULL," +
                " value TEXT," +
                " CONSTRAINT mon_prop_id PRIMARY KEY (monitorable_id, name), " +
                " CONSTRAINT mon_prop_mon_fk FOREIGN KEY (monitorable_id) REFERENCES monitorable(id) ON DELETE CASCADE" +
                " )" );
            database.execSQL( "CREATE INDEX mon_prop_id_idx ON monitorable_property (monitorable_id, name)" );
            database.execSQL( "CREATE INDEX mon_prop_mon_idx ON monitorable_property (monitorable_id)" );
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration( 5, 6 ) {
        @Override
        public void migrate(
            @NonNull final SupportSQLiteDatabase database )
        {
            database.execSQL( "CREATE TABLE monitorable_finish (" +
                " monitorable_id INTEGER NOT NULL, " +
                " CONSTRAINT monitorable_finish_pk PRIMARY KEY (monitorable_id)" +
                ")" );
        }
    };

    static final Migration[] MIGRATIONS = new Migration[] {
        MIGRATION_0_1,
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4,
        MIGRATION_4_5,
        MIGRATION_5_6
    };
}
