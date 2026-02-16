package com.example.myndricai.data.repository;

import androidx.annotation.Nullable;

import com.example.myndricai.data.source.firebase.FirebaseAuthDataSource;
import com.example.myndricai.data.source.firebase.FirestoreDataSource;
import com.example.myndricai.domain.model.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserRepository {

    private final FirestoreDataSource firestore;
    private final FirebaseAuthDataSource auth;

    public UserRepository(FirestoreDataSource firestore, FirebaseAuthDataSource auth) {
        this.firestore = firestore;
        this.auth = auth;
    }

    public @Nullable String uid() {
        return auth.uid();
    }

    public @Nullable FirebaseUser currentUser() {
        return auth.currentUser();
    }

    public Task<Void> createUserIfNeeded(String uid, String displayName) {
        return firestore.createOrUpdateUser(uid, new UserProfile(uid, displayName));
    }

    public Task<UserProfile> loadProfile() {
        String uid = auth.uid();
        if (uid == null) {
            return Tasks.forException(new IllegalStateException("User is not signed in"));
        }

        return firestore.getUser(uid).continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException() != null ? task.getException() : new RuntimeException("Failed to load profile");
            }
            DocumentSnapshot d = task.getResult();
            UserProfile p = new UserProfile();
            p.uid = uid;
            if (d != null && d.exists()) {
                p.displayName = d.getString("displayName");
            }
            return p;
        });
    }

    public Task<Void> updateDisplayName(String displayName) {
        String uid = auth.uid();
        if (uid == null) {
            return Tasks.forException(new IllegalStateException("User is not signed in"));
        }
        return firestore.updateDisplayName(uid, displayName);
    }
}
