package br.com.neolog.cplmobile.occurrence.category;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OccurrenceCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(
            OccurrenceCategory occurrenceCategory);

    @Query("SELECT * FROM occurrence_category")
    List<OccurrenceCategory> findAll();

    @Query("DELETE FROM occurrence_category")
    void deleteAll();

    @Query("SELECT * FROM occurrence_category WHERE id=:id")
    OccurrenceCategory findById(
            Integer id);
}
