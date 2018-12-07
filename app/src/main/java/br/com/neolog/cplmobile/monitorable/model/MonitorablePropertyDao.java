package br.com.neolog.cplmobile.monitorable.model;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface MonitorablePropertyDao
{
    @Insert( onConflict = OnConflictStrategy.FAIL )
    void insert(
        MonitorableProperty property );

    @Query( "SELECT * FROM monitorable_property WHERE monitorable_id = :monitorableId" )
    LiveData<List<MonitorableProperty>> findByMonitorableId(
        int monitorableId );

    @Query( "DELETE FROM monitorable_property WHERE monitorable_id = :monitorableId" )
    void deleteByMonitorableId(
        int monitorableId );
}
