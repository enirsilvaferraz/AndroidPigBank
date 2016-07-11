package com.system.androidpigbank.controllers.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.recyclerv.CategorySummaryAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.helpers.constants.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorySummaryFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_summary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CategorySummaryAdapter adapter = new CategorySummaryAdapter();

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<Category>>() {
            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(getActivity()).getChartDataByMonth(Calendar.getInstance().get(Calendar.MONTH));
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                if (data.isSuccess()) {
                    ((CategorySummaryAdapter) recyclerview.getAdapter()).addItens(data.getData());
                } else {
                    ((BaseActivity)getActivity()).showMessage(data.getException());
                }
            }
        });

//        Category c = new Category();
//        c.setName("Category 1");
//        c.setAmount(100F);
//
//        List<Category> list = new ArrayList<>();
//        list.add(c);
//
//        adapter.addItens(list);
    }
}
