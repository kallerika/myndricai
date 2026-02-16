package com.example.myndricai.common.constants;

public final class FirestorePaths {
    private FirestorePaths() {}

    public static final String USERS = "users";
    public static final String CASES_SUBCOLLECTION = "cases";

    // Контент кейсов (общая коллекция)
    public static final String CASES = "cases";

    public static String userDoc(String uid) {
        return USERS + "/" + uid;
    }

    public static String userCasesCollection(String uid) {
        return userDoc(uid) + "/" + CASES_SUBCOLLECTION;
    }

    public static String userCaseDoc(String uid, String caseId) {
        return userCasesCollection(uid) + "/" + caseId;
    }

    public static String casesCollection() {
        return CASES;
    }

    public static String caseDoc(String caseId) {
        return CASES + "/" + caseId;
    }
}
