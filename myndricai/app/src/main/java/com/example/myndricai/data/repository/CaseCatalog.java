package com.example.myndricai.data.repository;

import com.example.myndricai.domain.model.CaseDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CaseCatalog {
    private CaseCatalog() {}

    public static final String CASE_001 = "case_001";
    public static final String CASE_002 = "case_002";
    public static final String CASE_003 = "case_003";

    public static List<CaseDefinition> getAllLocalCases() {
        List<CaseDefinition> list = new ArrayList<>();

        list.add(new CaseDefinition(
                CASE_001,
                "Кейс 1: <Название>",
                "<Цель/описание кейса>",
                Arrays.asList("<ключ1>", "<ключ2>", "<ключ3>")
        ));

        list.add(new CaseDefinition(
                CASE_002,
                "Кейс 2: <Название>",
                "<Цель/описание кейса>",
                Arrays.asList("<ключ1>", "<ключ2>")
        ));

        list.add(new CaseDefinition(
                CASE_003,
                "Кейс 3: <Название>",
                "<Цель/описание кейса>",
                Arrays.asList("<ключ1>")
        ));

        return Collections.unmodifiableList(list);
    }

    public static CaseDefinition getById(String caseId) {
        for (CaseDefinition d : getAllLocalCases()) {
            if (d.caseId.equals(caseId)) return d;
        }
        return null;
    }
}
