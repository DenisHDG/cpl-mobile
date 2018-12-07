package br.com.neolog.cplmobile.monitorable.model;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface MonitorableDao
{
    @Insert( onConflict = OnConflictStrategy.FAIL )
    void insert(
        Monitorable monitorable );

    @Insert( onConflict = OnConflictStrategy.FAIL )
    void insert(
        List<Monitorable> monitorable );

    @Update( onConflict = OnConflictStrategy.FAIL )
    void update(
        Monitorable monitorable );

    @Query( "SELECT * FROM monitorable WHERE id = :id" )
    Monitorable findOne(
        int id );

    @Query( "SELECT count(*) > 0 FROM monitorable WHERE id = :id" )
    boolean exists(
        int id );

    @Query( "SELECT count(*) FROM monitorable" )
    int count();

    @Query( "DELETE FROM monitorable WHERE id = :id" )
    void delete(
        int id );

    @Query( "SELECT * from monitorable ORDER BY root DESC, id DESC" )
    LiveData<List<Monitorable>> findAll();

    @Query( "SELECT * from monitorable where root = 1" )
    List<Monitorable> findAllRoots();

    @Query( "SELECT * from monitorable WHERE id in (:ids)" )
    LiveData<List<Monitorable>> findByIds(
        List<Integer> ids );
}
