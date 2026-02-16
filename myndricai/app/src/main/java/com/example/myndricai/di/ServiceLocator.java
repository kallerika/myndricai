package com.example.myndricai.di;

import android.content.Context;

import com.example.myndricai.data.repository.AuthRepository;
import com.example.myndricai.data.repository.CaseContentRepository;
import com.example.myndricai.data.repository.CasesRepository;
import com.example.myndricai.data.repository.UserRepository;
import com.example.myndricai.data.source.firebase.FirebaseAuthDataSource;
import com.example.myndricai.data.source.firebase.FirestoreDataSource;

public final class ServiceLocator {

    private static volatile ServiceLocator instance;

    private final Context appContext;

    private final FirebaseAuthDataSource authDataSource;
    private final FirestoreDataSource firestoreDataSource;

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final CasesRepository casesRepository;
    private final CaseContentRepository caseContentRepository;

    private ServiceLocator(Context appContext) {
        this.appContext = appContext.getApplicationContext();

        authDataSource = new FirebaseAuthDataSource();
        firestoreDataSource = new FirestoreDataSource();

        authRepository = new AuthRepository(authDataSource);
        userRepository = new UserRepository(firestoreDataSource, authDataSource);
        casesRepository = new CasesRepository(firestoreDataSource, authDataSource);
        caseContentRepository = new CaseContentRepository(firestoreDataSource);
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                if (instance == null) {
                    instance = new ServiceLocator(context);
                }
            }
        }
    }

    public static ServiceLocator get() {
        if (instance == null) {
            throw new IllegalStateException("ServiceLocator is not initialized. Call ServiceLocator.init() in Application.");
        }
        return instance;
    }

    public AuthRepository authRepository() {
        return authRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public CasesRepository casesRepository() {
        return casesRepository;
    }

    public CaseContentRepository caseContentRepository() {
        return caseContentRepository;
    }

    public Context appContext() {
        return appContext;
    }
}
