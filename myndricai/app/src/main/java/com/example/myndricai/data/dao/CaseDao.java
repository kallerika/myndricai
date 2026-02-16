package com.example.myndricai.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myndricai.data.entity.CaseEntity;

import java.util.List;

@Dao
public interface CaseDao {

    @Query("SELECT * FROM cases ORDER BY id ASC")
    List<CaseEntity> getAll();

    @Query("SELECT * FROM cases WHERE id = :id LIMIT 1")
    CaseEntity getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CaseEntity e);

    @Update
    void update(CaseEntity e);

    @Query("UPDATE cases SET status = :status WHERE id = :id")
    void updateStatus(long id, int status);

    @Query("UPDATE cases SET status = 0")
    void resetAll();

    @Query("SELECT COUNT(*) FROM cases")
    int count();

    @Query("SELECT COUNT(*) FROM cases WHERE status = 2")
    int completedCount();

    @Query("UPDATE cases SET pin = :pin WHERE id = :id")
    void updatePin(long id, String pin);

}
