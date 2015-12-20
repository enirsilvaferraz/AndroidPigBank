package com.system.androidpigbank.controllers.managers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;

import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CategoryManager extends ManagerAbs {

    public CategoryManager(Context context) {
        super(context);
    }

    public Loader<LoaderResult<List<Category>>> getChartDataByMonth(final int month) {
        return new AsyncTaskLoader<LoaderResult<List<Category>>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public LoaderResult<List<Category>> loadInBackground() {

                try {
                    return new LoaderResult<>(new CategoryBusiness(getContext()).getChartDataByMonth(month));
                } catch (Throwable e) {
                    return new LoaderResult<>(e);
                }
            }
        };
    }

    public Loader<LoaderResult<List<Category>>> findAll() {
        return new AsyncTaskLoader<LoaderResult<List<Category>>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public LoaderResult<List<Category>> loadInBackground() {

                try {
                    return new LoaderResult<>(new CategoryBusiness(getContext()).findAll());
                } catch (Throwable e) {
                    return new LoaderResult<>(e);
                }
            }
        };
    }
}
