package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.recyclerv.TransactionAdapter;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.CardActionBarView;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private static final int SPAN_COUNT = 1;
    private RecyclerView recyclerView;
    private List<Transaction> data;

    public TransactionListFragment() {
    }

    public static TransactionListFragment newInstance() {
        TransactionListFragment fragment = new TransactionListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_history_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TransactionAdapter adapter = new TransactionAdapter((AppCompatActivity) getActivity());
        adapter.addItens(getData());
        adapter.setOnCardBarClickListener(new CardActionBarView.OnClickListener() {
            @Override
            public void onEditClicked(EntityAbs model) {
                IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
            }

            @Override
            public void onDeleteClicked(final EntityAbs model) {

                ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<Transaction>() {
                    @Override
                    public Transaction executeAction() throws Exception {
                        return new TransactionBusiness(getActivity()).delete((Transaction) model);
                    }

                    @Override
                    public void onComplete(LoaderResult<Transaction> data) {
                        if (data.isSuccess()) {
                            adapter.removeItem(data.getData());
                        } else {
                            // TODO Tratar exception
                        }
                    }
                });

            }

            @Override
            public void onCopyClicked(final EntityAbs model) {

                ((Transaction)model).setDatePayment(((Transaction)model).getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(((Transaction)model).getDate());
                cal.add(Calendar.MONTH, 1);
                ((Transaction)model).setDate(cal.getTime());

                ((Transaction)model).setId(null);
                IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.transaction_history_recycleView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public List<Transaction> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}