package br.com.neolog.cplmobile.occurrence.cause;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.neolog.cplmobile.occurrence.OccurrenceCauseAndCategory;

@Dao
public interface OccurrenceCauseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(
            OccurrenceCause occurrenceCause);

    @Query("SELECT * FROM occurrence_cause")
    List<OccurrenceCause> findAll();

    @Query("DELETE FROM occurrence_cause")
    void deleteAll();

    @Query("SELECT oc.* FROM occurrence_cause oc  INNER JOIN allowed_monitorable_type amt ON " +
            "           oc.id=amt.occurrence_cause_id WHERE " +
            "           amt.monitorable_type=:monitorableType")
    List<OccurrenceCause> findOccurrenceCauseByAllowedMonitorableType(
            String monitorableType);

    @Query("SELECT oct.id as 'cat_id', " +
            "oct.source_id as 'cat_source_id', " +
            "oct.name as 'cat_name', " +
            "oct.description as 'cat_description', " +
            "oct.quantity as 'cat_quantity', " +
            "oct.time_delta as 'cat_time_delta', " +
            "oct.value_delta as 'cat_value_delta', " +
            "oc.* " +
            "FROM occurrence_cause oc INNER JOIN occurrence_category oct ON " +
            " oc.category_id = oct.id " +
            "ORDER BY oc.cause_order, oct.name")
    LiveData<List<OccurrenceCauseAndCategory>> findOccurrenceCauseWithCategory();

    @Query("SELECT oct.id as 'cat_id', " +
            "oct.source_id as 'cat_source_id', " +
            "oct.name as 'cat_name', " +
            "oct.description as 'cat_description', " +
            "oct.quantity as 'cat_quantity', " +
            "oct.time_delta as 'cat_time_delta', " +
            "oct.value_delta as 'cat_value_delta', " +
            "oc.* " +
            "FROM occurrence_cause oc INNER JOIN occurrence_category oct ON " +
            " oc.category_id = oct.id WHERE lower(oc.name) LIKE lower(:name) " +
            "ORDER BY oc.cause_order, oct.name")
    LiveData<List<OccurrenceCauseAndCategory>> findOccurrenceCauseWithCategoryByCauseName(
            String name);

    @Query("SELECT * FROM occurrence_cause WHERE id=:id")
    OccurrenceCause findById(
            Integer id);
}
