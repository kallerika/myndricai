package com.example.myndricai.data.source.firebase;

import com.example.myndricai.common.constants.FirestorePaths;
import com.example.myndricai.domain.model.CaseDefinition;
import com.example.myndricai.domain.model.CaseProgress;
import com.example.myndricai.domain.model.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDataSource {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // -------- users --------

    public DocumentReference userDoc(String uid) {
        return db.document(FirestorePaths.userDoc(uid));
    }

    public Task<DocumentSnapshot> getUser(String uid) {
        return userDoc(uid).get();
    }

    public DocumentReference userCaseDoc(String uid, String caseId) {
        return db.document(FirestorePaths.userCaseDoc(uid, caseId));
    }

    public Query userCasesQuery(String uid) {
        return db.collection(FirestorePaths.userCasesCollection(uid));
    }

    public Task<Void> createOrUpdateUser(String uid, UserProfile profile) {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("displayName", profile.displayName);
        data.put("createdAt", System.currentTimeMillis());
        return userDoc(uid).set(data);
    }

    public Task<Void> updateDisplayName(String uid, String displayName) {
        Map<String, Object> data = new HashMap<>();
        data.put("displayName", displayName);
        return userDoc(uid).update(data);
    }

    public Task<Void> upsertCaseProgress(String uid, CaseProgress progress) {
        Map<String, Object> data = new HashMap<>();
        data.put("caseId", progress.caseId);
        data.put("status", progress.status);
        data.put("updatedAt", progress.updatedAt);
        data.put("completedAt", progress.completedAt);
        return userCaseDoc(uid, progress.caseId).set(data);
    }

    // -------- cases (контент) --------

    public CollectionReference casesCollection() {
        return db.collection(FirestorePaths.casesCollection());
    }

    public DocumentReference caseDoc(String caseId) {
        return db.document(FirestorePaths.caseDoc(caseId));
    }

    public Task<Void> upsertCaseDefinition(CaseDefinition def) {
        Map<String, Object> data = new HashMap<>();
        data.put("caseId", def.caseId);
        data.put("title", def.title);
        data.put("objective", def.objective);
        data.put("requiredKeywords", def.requiredKeywords);
        data.put("updatedAt", System.currentTimeMillis());
        return caseDoc(def.caseId).set(data);
    }

    public FirebaseFirestore rawDb() {
        return db;
    }
}
