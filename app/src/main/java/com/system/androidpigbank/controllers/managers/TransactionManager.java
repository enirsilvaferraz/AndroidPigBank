package com.system.androidpigbank.controllers.managers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 *
 */
public class TransactionManager extends ManagerAbs {

    public TransactionManager(Context context) {
        super(context);
    }

    public Loader<LoaderResult<Transaction>> save(final Transaction transaction) {

        return new AsyncTaskLoader<LoaderResult<Transaction>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public LoaderResult<Transaction> loadInBackground() {

                try {
                    return new LoaderResult<>(new TransactionBusiness(getContext()).save(transaction));
                } catch (Throwable e) {
                    return new LoaderResult<>(e);
                }
            }
        };
    }

    public Loader<LoaderResult<List<Transaction>>> getTransactionByMonth(final int month) {
        return new AsyncTaskLoader<LoaderResult<List<Transaction>>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public LoaderResult<List<Transaction>> loadInBackground() {

                try {
                    return new LoaderResult<>(new TransactionBusiness(getContext()).getTransactionByMonth(month));
                } catch (Throwable e) {
                    return new LoaderResult<>(e);
                }
            }
        };
    }
}
