package com.example.myndricai;

import android.app.Application;

import com.example.myndricai.data.AppDatabase;
import com.example.myndricai.repo.CaseRepository;
import com.example.myndricai.repo.SessionRepository;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.myndricai.repo.UserRepository;
import com.example.myndricai.common.ThemePrefs;


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

        // APPLY THEME EARLY
        AppCompatDelegate.setDefaultNightMode(
                sessionRepository.isDarkThemeEnabled()
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        userRepository = new UserRepository(db.userDao());
        caseRepository = new CaseRepository(db.caseDao());

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
