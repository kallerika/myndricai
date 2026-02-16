package com.example.myndricai.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class KeywordMatcher {
    private KeywordMatcher() {}

    /**
     * Требование: "совпадает на 100%" -> в ответе должны присутствовать ВСЕ ключевые слова.
     * Сопоставление нечувствительно к регистру. Проверка "contains".
     */
    public static boolean isFullMatch(String userAnswer, List<String> requiredKeywords) {
        if (userAnswer == null) return false;
        String normalized = normalize(userAnswer);

        for (String kw : requiredKeywords) {
            if (kw == null) return false;
            String nkw = normalize(kw);
            if (!normalized.contains(nkw)) return false;
        }
        return true;
    }

    public static List<String> normalizeKeywords(List<String> keywords) {
        List<String> out = new ArrayList<>();
        if (keywords == null) return out;
        for (String k : keywords) {
            if (k != null) out.add(normalize(k));
        }
        return out;
    }

    private static String normalize(String s) {
        return s.toLowerCase(Locale.ROOT).trim();
    }
}
