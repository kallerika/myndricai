package com.example.myndricai.ui.cases;

import java.util.List;

public class CaseUiModel {
    public String caseId;
    public String title;
    public String objective;

    // "Не начат" / "В процессе" / "Завершен"
    public String statusText;

    // "15 фев 2026" или "-"
    public String lastDateText;

    // Для проверки ответа
    public List<String> requiredKeywords;

    // Служебное
    public String rawStatus;     // not_started / in_progress / completed
    public long updatedAtMillis; // 0 если нет

    public CaseUiModel() {}
}
