package com.example.myndricai;

import android.app.Application;

import com.example.myndricai.data.AppDatabase;
import com.example.myndricai.repo.CaseRepository;
import com.example.myndricai.repo.SessionRepository;
import com.example.myndricai.repo.UserRepository;

public class MyApp extends Application {

    private static MyApp instance;

    private AppDatabase db;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private CaseRepository caseRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = AppDatabase.get(this);
        sessionRepository = new SessionRepository(this);
        userRepository = new UserRepository(db.userDao());
        caseRepository = new CaseRepository(db.caseDao());

        // Seed minimal content (id=1) if database is empty.
        caseRepository.seedDefaultsIfNeeded();
    }

    public static MyApp get() {
        return instance;
    }

    public SessionRepository session() {
        return sessionRepository;
    }

    public UserRepository users() {
        return userRepository;
    }

    public CaseRepository cases() {
        return caseRepository;
    }
}
