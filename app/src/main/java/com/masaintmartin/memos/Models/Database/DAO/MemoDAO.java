package com.masaintmartin.memos.Models.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.masaintmartin.memos.Models.Memo;

import java.util.List;

@Dao
public interface MemoDAO {

    @Query("SELECT * FROM Memo")
    List<Memo> getMemos();

    @Query("SELECT * FROM Memo WHERE id = :id")
    Memo getMemo(int id);

    @Query("SELECT * FROM Memo ORDER BY id DESC LIMIT 1")
    Memo getLastMemo();

    @Insert
    long insertMemo(Memo Memo);

    @Update
    int updateMemo(Memo Memo);

    @Query("DELETE FROM Memo WHERE id = :id")
    int deleteMemo(int id);
}
