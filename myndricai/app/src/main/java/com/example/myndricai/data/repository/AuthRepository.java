package com.example.myndricai.data.repository;

import androidx.annotation.Nullable;

import com.example.myndricai.data.source.firebase.FirebaseAuthDataSource;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    private final FirebaseAuthDataSource auth;

    public AuthRepository(FirebaseAuthDataSource auth) {
        this.auth = auth;
    }

    public boolean isSignedIn() {
        return auth.isSignedIn();
    }

    public @Nullable FirebaseUser currentUser() {
        return auth.currentUser();
    }

    public @Nullable String uid() {
        return auth.uid();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signIn(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return auth.register(email, password);
    }

    public void signOut() {
        auth.signOut();
    }
}
