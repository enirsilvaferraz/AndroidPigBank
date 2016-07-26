package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.system.androidpigbank.helpers.constant.*;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.recyclerv.CategorySummaryAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;

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

        final int month = getArguments().getInt("MONTH");
        final int year = getArguments().getInt("YEAR");

        update(month, year);
    }

    public static CategorySummaryFragment newInstance(int month, int year) {
        CategorySummaryFragment fragment = new CategorySummaryFragment();
        Bundle args = new Bundle();
        args.putInt("MONTH", month);
        args.putInt("YEAR", year);
        fragment.setArguments(args);
        return fragment;
    }

    public void update(final int month, final int year) {

        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<Category>>() {
            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(getActivity()).getChartDataByMonth(month, year);
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_CATEGORY;
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                if (data.isSuccess()) {
                    ((CategorySummaryAdapter) recyclerview.getAdapter()).addItens(data.getData());
                } else {
                    ((BaseActivity) getActivity()).showMessage(data.getException());
                }
            }
        });
    }
}
