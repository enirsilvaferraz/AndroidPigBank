package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.system.androidpigbank.R;
import com.system.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.recyclerv.CategoryAdapter;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.sqlite.business.CategoryBusiness;
import com.system.androidpigbank.controllers.vos.CategoryVO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.category_manager_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CategoryAdapter());

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<CategoryVO>>() {

            @Override
            public List<CategoryVO> executeAction() throws Exception {
                return new CategoryBusiness(CategoryListActivity.this).findAll();
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<List<CategoryVO>> data) {
                if (data.isSuccess()) {
                    ((CategoryAdapter) recyclerView.getAdapter()).addItens(data.getData());
                } else {
                    showMessage(data.getException());
                }
            }
        });
    }
}
