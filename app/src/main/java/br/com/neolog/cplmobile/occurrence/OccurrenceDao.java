package br.com.neolog.cplmobile.occurrence;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface OccurrenceDao
{
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    long insert(
        Occurrence occurrence );

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insert(
        List<Occurrence> occurrences );

    @Query( "SELECT * FROM occurrence" )
    LiveData<List<Occurrence>> findAll();

    @Query( "SELECT * FROM occurrence WHERE remote_server_id IS NULL" )
    List<Occurrence> findAllWhereRemoteServerIdIsNull();

    @Query( "SELECT * FROM occurrence WHERE remote_server_id IS NOT NULL" )
    List<Occurrence> findAllWhereRemoteServerIdIsNotNull();

    @Query( "UPDATE occurrence SET remote_server_id =:remoteServerId WHERE id=:id" )
    void updateRemoteServerId(
        Integer id,
        Integer remoteServerId );
}
