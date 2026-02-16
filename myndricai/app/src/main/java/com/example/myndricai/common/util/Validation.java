package com.example.myndricai.common.util;

import android.text.TextUtils;
import android.util.Patterns;

public final class Validation {
    private Validation() {}

    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) return "Введите email";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Некорректный email";
        return null;
    }

    public static String validatePassword(String password) {
        if (TextUtils.isEmpty(password)) return "Введите пароль";
        if (password.length() < 6) return "Пароль должен быть минимум 6 символов";
        return null;
    }

    public static String validateDisplayName(String name) {
        if (TextUtils.isEmpty(name)) return "Введите имя";
        if (name.trim().length() < 2) return "Имя слишком короткое";
        return null;
    }
}
