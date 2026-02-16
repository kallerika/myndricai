package com.example.myndricai.data.source.firebase;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public @Nullable FirebaseUser currentUser() {
        return auth.getCurrentUser();
    }

    public boolean isSignedIn() {
        return currentUser() != null;
    }

    public @Nullable String uid() {
        FirebaseUser u = currentUser();
        return u != null ? u.getUid() : null;
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }
}
