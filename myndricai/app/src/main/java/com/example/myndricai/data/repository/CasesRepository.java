package com.example.myndricai.data.repository;

import androidx.annotation.NonNull;

import com.example.myndricai.common.constants.FirestorePaths;
import com.example.myndricai.data.source.firebase.FirebaseAuthDataSource;
import com.example.myndricai.data.source.firebase.FirestoreDataSource;
import com.example.myndricai.domain.model.CaseProgress;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class CasesRepository {

    private final FirestoreDataSource firestore;
    private final FirebaseAuthDataSource auth;

    public CasesRepository(FirestoreDataSource firestore, FirebaseAuthDataSource auth) {
        this.firestore = firestore;
        this.auth = auth;
    }

    public Task<Void> markInProgress(String caseId) {
        String uid = auth.uid();
        if (uid == null) return Tasks.forException(new IllegalStateException("User is not signed in"));

        CaseProgress p = new CaseProgress(caseId, CaseProgress.STATUS_IN_PROGRESS, System.currentTimeMillis(), null);
        return firestore.upsertCaseProgress(uid, p);
    }

    public Task<Void> markCompleted(String caseId) {
        String uid = auth.uid();
        if (uid == null) return Tasks.forException(new IllegalStateException("User is not signed in"));

        long now = System.currentTimeMillis();
        CaseProgress p = new CaseProgress(caseId, CaseProgress.STATUS_COMPLETED, now, now);
        return firestore.upsertCaseProgress(uid, p);
    }

    public Task<List<CaseProgress>> loadProgress() {
        String uid = auth.uid();
        if (uid == null) return Tasks.forException(new IllegalStateException("User is not signed in"));

        return firestore.userCasesQuery(uid).get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException() != null ? task.getException() : new RuntimeException("Failed to load cases progress");
            }
            QuerySnapshot snap = task.getResult();
            List<CaseProgress> list = new ArrayList<>();
            if (snap != null) {
                for (DocumentSnapshot d : snap.getDocuments()) {
                    CaseProgress p = new CaseProgress();
                    p.caseId = d.getString("caseId");
                    p.status = d.getString("status");
                    Long updatedAt = d.getLong("updatedAt");
                    Long completedAt = d.getLong("completedAt");
                    p.updatedAt = updatedAt != null ? updatedAt : 0L;
                    p.completedAt = completedAt;
                    if (p.caseId != null) list.add(p);
                }
            }
            return list;
        });
    }

    public Task<Void> resetAllProgress() {
        String uid = auth.uid();
        if (uid == null) return Tasks.forException(new IllegalStateException("User is not signed in"));

        FirebaseFirestore db = firestore.rawDb();
        return db.collection(FirestorePaths.userCasesCollection(uid))
                .get()
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        return Tasks.forException(task.getException() != null ? task.getException() : new RuntimeException("Failed to query user cases"));
                    }
                    QuerySnapshot snap = task.getResult();
                    WriteBatch batch = db.batch();
                    if (snap != null) {
                        for (DocumentSnapshot d : snap.getDocuments()) {
                            batch.delete(d.getReference());
                        }
                    }
                    return batch.commit();
                });
    }

    @NonNull
    public static String statusToRu(String status) {
        if (CaseProgress.STATUS_COMPLETED.equals(status)) return "Завершен";
        if (CaseProgress.STATUS_IN_PROGRESS.equals(status)) return "В процессе";
        return "Не начат";
    }
}
