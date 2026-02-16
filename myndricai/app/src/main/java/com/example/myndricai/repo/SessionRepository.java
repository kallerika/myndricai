package com.example.myndricai.repo;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class SessionRepository {
    private static final String PREFS = "session_prefs";
    private static final String KEY_UID = "uid";

    private final SharedPreferences sp;

    public SessionRepository(Context ctx) {
        sp = ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void setUserId(long uid) {
        sp.edit().putLong(KEY_UID, uid).apply();
    }

    public void clear() {
        sp.edit().remove(KEY_UID).apply();
    }

    public long getUserId() {
        return sp.getLong(KEY_UID, -1L);
    }

    public boolean isLoggedIn() {
        return getUserId() > 0;
    }

    @Nullable
    public Long getUserIdNullable() {
        long id = getUserId();
        return id > 0 ? id : null;
    }
}
