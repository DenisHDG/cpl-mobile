package br.com.neolog.cplmobile.signal;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface SignalDao
{

    @Insert
    void insert(
        final Signal signals );

    @Query( "SELECT * FROM signal" )
    List<Signal> findAll();

    @Delete
    void delete(
        Signal signal );

    @Delete
    void delete(
        List<Signal> signals );
}
