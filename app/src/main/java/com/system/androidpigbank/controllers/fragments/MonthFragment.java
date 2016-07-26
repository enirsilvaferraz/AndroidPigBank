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

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.recyclerv.MonthAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.controllers.vos.Month;
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthFragment extends Fragment {

    private MonthAdapter.OnItemClicked onItemClicked;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mouth_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(new MonthAdapter());
        ((MonthAdapter)recyclerview.getAdapter()).setOnItemClicked(onItemClicked);

        final int year = getArguments().getInt("YEAR");

        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<Month>>() {
            @Override
            public List<Month> executeAction() throws Exception {
                return new TransactionBusiness(getActivity()).getMonthWithTransaction(year);
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_MONTH;
            }

            @Override
            public void onComplete(LoaderResult<List<Month>> data) {
                if (data.isSuccess()) {
                    ((MonthAdapter) recyclerview.getAdapter()).addItens(data.getData());
                } else {
                    ((BaseActivity) getActivity()).showMessage(data.getException());
                }
            }
        });
    }

    public static MonthFragment newInstance(int year) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("YEAR", year);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnItemClicked(MonthAdapter.OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }
}
