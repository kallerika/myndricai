package com.example.myndricai.common.result;

import androidx.annotation.Nullable;

public abstract class Result<T> {

    private Result() {}

    public static final class Success<T> extends Result<T> {
        public final T data;
        public Success(T data) { this.data = data; }
    }

    public static final class Error<T> extends Result<T> {
        public final String message;
        @Nullable public final Throwable throwable;

        public Error(String message, @Nullable Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
        }
    }
}
