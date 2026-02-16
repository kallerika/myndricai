package com.example.myndricai.domain.model;

import java.util.List;

/**
 * Контент кейса. На старте держим локально в коде (быстрее),
 * а в Firestore — только прогресс users/{uid}/cases/{caseId}.
 */
public class CaseDefinition {
    public String caseId;
    public String title;
    public String objective;
    public List<String> requiredKeywords;

    public CaseDefinition() {}

    public CaseDefinition(String caseId, String title, String objective, List<String> requiredKeywords) {
        this.caseId = caseId;
        this.title = title;
        this.objective = objective;
        this.requiredKeywords = requiredKeywords;
    }
}
