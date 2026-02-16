package com.example.myndricai.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public final class ThemePrefs {
    private static final String PREFS = "app_prefs";
    private static final String KEY_DARK = "dark_theme";

    private ThemePrefs() {}

    public static boolean isDark(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_DARK, false);
    }

    public static void setDark(Context ctx, boolean dark) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_DARK, dark).apply();
        apply(dark);
    }

    public static void applySaved(Context ctx) {
        apply(isDark(ctx));
    }

    private static void apply(boolean dark) {
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
