package com.masaintmartin.memos.Models.Database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.masaintmartin.memos.Models.Database.DAO.MemoDAO;
import com.masaintmartin.memos.Models.Memo;

@Database(entities = {Memo.class}, version = 1)
public abstract class MemoDatabase extends RoomDatabase {
    // --- SINGLETON ---
    private static volatile MemoDatabase INSTANCE;

    // --- DAO ---
    public abstract MemoDAO memoDao();

    // --- INSTANCE ---
    public static MemoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MemoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MemoDatabase.class, "memoDatabase.db")
                            .allowMainThreadQueries()
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                for (int i = 0; i < 30; i++){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", i);
                    contentValues.put("title", String.format("memo numéro: %s", i+1));
                    db.insert("Memo", OnConflictStrategy.IGNORE, contentValues);
                }
            }
        };
    }
}
