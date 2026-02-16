package com.example.myndricai.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cases")
public class CaseEntity {
    @PrimaryKey
    public long id;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @NonNull
    @ColumnInfo(name = "subtitle")
    public String subtitle;

    /** 0=new, 1=in_progress, 2=completed */
    @ColumnInfo(name = "status")
    public int status;

    @NonNull
    @ColumnInfo(name = "pin")
    public String pin;

    @NonNull
    @ColumnInfo(name = "answer_keywords")
    public String answerKeywordsCsv;

    public CaseEntity(long id,
                      @NonNull String title,
                      @NonNull String subtitle,
                      int status,
                      @NonNull String pin,
                      @NonNull String answerKeywordsCsv) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.status = status;
        this.pin = pin;
        this.answerKeywordsCsv = answerKeywordsCsv;
    }
}
