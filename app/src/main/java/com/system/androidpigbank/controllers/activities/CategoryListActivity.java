package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.recyclerv.CategoryAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.helpers.constants.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;

import java.util.List;

public class CategoryListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category_manager_list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CategoryAdapter());

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<Category>>() {
            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(CategoryListActivity.this).findAll();
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                if (data.isSuccess()) {
                    ((CategoryAdapter) recyclerView.getAdapter()).addItens(data.getData());
                } else {
                    showMessage(data.getException());
                }
            }
        });
    }

    @Override
    public View getContainer() {
        return (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
    }
}
