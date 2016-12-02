package com.system.architecture.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by eferraz on 05/12/15.
 * Classe implementacao do manager abs
 */
public final class ManagerHelper {

    /**
     * Metodo padrao para execucoes em background.
     *
     * @param callback Retorno da execucao em background {@link LoaderResultInterface}
     */
    public <T> void execute(AppCompatActivity context, final LoaderResultInterface<T> callback) {

        final AsyncTaskLoader<LoaderResult<T>> asyncTaskLoader = new AsyncTaskLoader<LoaderResult<T>>(context) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                callback.onPreLoad();
                forceLoad();
            }

            @Override
            public LoaderResult<T> loadInBackground() {
                try {
                    return new LoaderResult<>(callback.executeAction());
                } catch (Throwable e) {
                    return new LoaderResult<>(e);
                }
            }
        };

        final LoaderManager.LoaderCallbacks<LoaderResult<T>> loaderCallbacks = new LoaderManager.LoaderCallbacks<LoaderResult<T>>() {

            @Override
            public Loader<LoaderResult<T>> onCreateLoader(int id, Bundle args) {
                return asyncTaskLoader;
            }

            @Override
            public void onLoadFinished(Loader<LoaderResult<T>> loader, LoaderResult<T> data) {
                callback.onComplete(data);
            }

            @Override
            public void onLoaderReset(Loader loader) {
            }
        };

        context.getSupportLoaderManager().restartLoader(callback.loaderId(), null, loaderCallbacks);
    }

    public static abstract class LoaderResultInterface<T> {

        public abstract T executeAction() throws Exception;

        public int loaderId() {
            return 1000;
        }

        public abstract void onComplete(LoaderResult<T> data);

        public void onPreLoad() {

        }
    }

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
}