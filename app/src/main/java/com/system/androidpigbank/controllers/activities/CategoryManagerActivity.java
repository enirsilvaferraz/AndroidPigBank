package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.persistences.DaoAbs;

public class CategoryManagerActivity extends BaseManagerActivity<Category> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected View getContainer() {
        return null;
    }

    @Override
    protected DaoAbs<Category> getBusinessInstance() {
        return new CategoryBusiness(this);
    }

    @Override
    protected void prepareToPersist() {

    }
}
