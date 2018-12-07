package br.com.neolog.cplmobile.monitorable.model;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface MonitorableFinishDao
{
    @Insert( onConflict = OnConflictStrategy.FAIL )
    void insert(
        MonitorableFinish monitorable );

    @Query( "SELECT * FROM monitorable_finish" )
    List<MonitorableFinish> findAll();

    @Query( "DELETE FROM monitorable_finish WHERE monitorable_id = :monitorableId" )
    void deleteByMonitorableId(
        int monitorableId );
}
