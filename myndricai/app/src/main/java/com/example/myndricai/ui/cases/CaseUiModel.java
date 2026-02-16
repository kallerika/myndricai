package com.example.myndricai.ui.cases;

public class CaseUiModel {
    public final long id;
    public final String title;
    public final String subtitle;
    public final int status;

    public CaseUiModel(long id, String title, String subtitle, int status) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.status = status;
    }
}
