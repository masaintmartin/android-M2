package com.masaintmartin.memos.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;

    public Memo(){ }

    public Memo(int i, String s) {
        this.title = s;
    }

    public void setId(int id) {  this.id = id; }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}
