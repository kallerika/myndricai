package com.example.myndricai.data.repository;

import com.example.myndricai.data.source.firebase.FirestoreDataSource;
import com.example.myndricai.domain.model.CaseDefinition;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CaseContentRepository {

    private final FirestoreDataSource firestore;

    public CaseContentRepository(FirestoreDataSource firestore) {
        this.firestore = firestore;
    }

    /**
     * Пытаемся загрузить cases из Firestore.
     * Если коллекция пуста или ошибка — возвращаем локальные кейсы.
     */
    public Task<List<CaseDefinition>> loadCasesWithFallback() {
        return firestore.casesCollection()
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        return CaseCatalog.getAllLocalCases();
                    }
                    QuerySnapshot snap = task.getResult();
                    if (snap == null || snap.isEmpty()) {
                        return CaseCatalog.getAllLocalCases();
                    }

                    List<CaseDefinition> list = new ArrayList<>();
                    for (DocumentSnapshot d : snap.getDocuments()) {
                        CaseDefinition def = new CaseDefinition();
                        def.caseId = d.getString("caseId");
                        def.title = d.getString("title");
                        def.objective = d.getString("objective");
                        Object kws = d.get("requiredKeywords");
                        if (kws instanceof List) {
                            //noinspection unchecked
                            def.requiredKeywords = (List<String>) kws;
                        }
                        if (def.caseId != null && def.title != null) {
                            list.add(def);
                        }
                    }

                    // на всякий случай: если вдруг кривые документы
                    if (list.isEmpty()) return CaseCatalog.getAllLocalCases();
                    return list;
                });
    }

    /**
     * Заполняет Firestore локальными кейсами (чтобы можно было показать базу).
     */
    public Task<Void> seedCasesToFirestore() {
        List<CaseDefinition> local = CaseCatalog.getAllLocalCases();
        List<Task<Void>> writes = new ArrayList<>();
        for (CaseDefinition d : local) {
            writes.add(firestore.upsertCaseDefinition(d));
        }
        return Tasks.whenAll(writes);
    }
}
