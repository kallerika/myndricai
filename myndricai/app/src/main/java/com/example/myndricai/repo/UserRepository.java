package com.example.myndricai.repo;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.myndricai.data.dao.UserDao;
import com.example.myndricai.data.entity.UserEntity;

public class UserRepository {

    private final UserDao dao;

    public UserRepository(UserDao dao) {
        this.dao = dao;
    }

    public long register(String name, String email, String password) throws IllegalArgumentException {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Заполните все поля");
        }
        UserEntity exists = dao.findByEmail(email.trim());
        if (exists != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        UserEntity u = new UserEntity(name.trim(), email.trim(), password);
        return dao.insert(u);
    }

    public long login(String email, String password) throws IllegalArgumentException {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Введите email и пароль");
        }
        UserEntity u = dao.findByEmail(email.trim());
        if (u == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        if (!password.equals(u.password)) {
            throw new IllegalArgumentException("Неверный пароль");
        }
        return u.id;
    }

    @Nullable
    public UserEntity getById(long id) {
        return dao.findById(id);
    }

    public void updateName(long userId, String newName) throws IllegalArgumentException {
        if (TextUtils.isEmpty(newName)) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        UserEntity u = dao.findById(userId);
        if (u == null) return;
        u.name = newName.trim();
        dao.update(u);
    }
}
