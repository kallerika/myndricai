package com.example.myndricai.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myndricai.data.dao.CaseDao;
import com.example.myndricai.data.dao.UserDao;
import com.example.myndricai.data.entity.CaseEntity;
import com.example.myndricai.data.entity.UserEntity;

@Database(
        entities = {UserEntity.class, CaseEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CaseDao caseDao();

    public static AppDatabase get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "myndricai.db")
                            .allowMainThreadQueries() // for exam/demo; replace with background in production
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
