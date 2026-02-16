package com.example.myndricai.common.result;

import androidx.annotation.Nullable;

public abstract class UiState<T> {

    private UiState() {}

    public static final class Idle<T> extends UiState<T> {}

    public static final class Loading<T> extends UiState<T> {}

    public static final class Content<T> extends UiState<T> {
        public final T data;
        public Content(T data) { this.data = data; }
    }

    public static final class Error<T> extends UiState<T> {
        public final String message;
        @Nullable public final Throwable throwable;

        public Error(String message, @Nullable Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
        }
    }
}
