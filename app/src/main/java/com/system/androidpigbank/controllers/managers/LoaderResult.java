package com.system.androidpigbank.controllers.managers;

import android.support.annotation.NonNull;

import com.system.androidpigbank.models.entities.EntityAbs;

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
