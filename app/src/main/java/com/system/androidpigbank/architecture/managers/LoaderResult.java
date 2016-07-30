package com.system.androidpigbank.architecture.managers;

import android.support.annotation.NonNull;

/**
 * Created by andersonr on 18/11/15.
 */
public class LoaderResult<T> {

    private final T data;
    private final Throwable exception;

    public LoaderResult() {
        this.data = null;
        this.exception = null;
    }

    public LoaderResult(@NonNull T data) {
        this.data = data;
        this.exception = null;
    }

    public LoaderResult(@NonNull Throwable exception) {
        this.data = null;
        this.exception = exception;
    }

    public T getData() {
        return data;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }
}
