package com.example.myndricai.domain.model;

public class CaseProgress {

    public static final String STATUS_NOT_STARTED = "not_started";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_COMPLETED = "completed";

    public String caseId;
    public String status;

    public long updatedAt;     // millis
    public Long completedAt;   // millis nullable

    public CaseProgress() {}

    public CaseProgress(String caseId, String status, long updatedAt, Long completedAt) {
        this.caseId = caseId;
        this.status = status;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
