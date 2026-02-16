package com.example.myndricai.app;

public final class AppConfig {
    private AppConfig() {}

    /**
     * Если true — можно из профиля/меню выполнить "заполнить базу" (seed),
     * чтобы в консоли Firebase появились документы cases.
     * В релизе можно выключить.
     */
    public static final boolean ENABLE_SEED_CASES = true;
}
