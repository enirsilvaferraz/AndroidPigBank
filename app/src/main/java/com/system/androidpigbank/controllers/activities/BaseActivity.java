package com.system.androidpigbank.controllers.activities;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.managers.LoaderResult;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class BaseActivity<T> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoaderResult<T>> {

}
