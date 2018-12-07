package br.com.neolog.cplmobile.occurrence.cause;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface AllowedMonitorableTypeDao
{
    @Insert
    void insert(
        AllowedMonitorableType allowedMonitorableType );

    @Query( "DELETE FROM allowed_monitorable_type" )
    void deleteAll();

    @Query( "SELECT * FROM allowed_monitorable_type" )
    List<AllowedMonitorableType> findAll();

}