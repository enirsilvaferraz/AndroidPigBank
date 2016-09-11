package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorySummaryFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    RecyclerView recyclerview;

    private List<Category> data;

    public static CategorySummaryFragment newInstance() {
        CategorySummaryFragment fragment = new CategorySummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_summary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CardAdapter adapter = new CardAdapter(getActivity(), true, new CardViewHolder.OnClickListener() {
            @Override
            public void onContainerClicked(CardAdapter.CardModel model) {
                if (model instanceof Transaction){
                    IntentRouter.hideDialog();
                    IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
                }
            }
        });
        adapter.addItens(getList());

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
    }

    @NonNull
    private List<CardAdapter.CardModel> getList() {

        List<Category> data = getData();
        List<CardAdapter.CardModel> itens = new ArrayList<>();

//        boolean alreadyAddedSecoundary = false;
        //this.itens.add(new TitleVO("Primary Categories"));

        for (Category category : data) {

//            if (!alreadyAddedSecoundary && !category.isPrimary()) {
//                TitleVO titleVO = new TitleVO("Secondary Categories");
//                titleVO.setCardStrategy(CardAdapter.CardModeItem.NO_STRATEGY);
//                itens.add(titleVO);
//                alreadyAddedSecoundary = true;
//            }

            if (category.getTransactionList().isEmpty()) {
                category.setCardStrategy(CardAdapter.CardModeItem.SINGLE);
                itens.add(category);
            } else {
                category.setCardStrategy(CardAdapter.CardModeItem.START);
                itens.add(category);
                itens.addAll(getTransactionByCategory(category));
            }
        }
        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(Category category) {

        String nomeCat = null;
        Double value = 0D;

        List<CardAdapter.CardModel> innerItens = new ArrayList<>();
        for (int i = 0; i < category.getTransactionList().size(); i++) {

            Transaction transaction = category.getTransactionList().get(i);
            String name = transaction.getCategorySecondary() != null ? transaction.getCategorySecondary().getName() : "";

            if (!innerItens.isEmpty() && !name.equals(nomeCat)) {
                TotalVO totalVO = new TotalVO(value);
                totalVO.setCardStrategy(CardAdapter.CardModeItem.MIDDLE);
                innerItens.add(totalVO);
                value = 0D;
            }

            nomeCat = name;

            transaction.setCardStrategy(CardAdapter.CardModeItem.MIDDLE);
            innerItens.add(transaction);

            value += transaction.getValue();
            if (i == category.getTransactionList().size() - 1) {
                TotalVO totalVO = new TotalVO(value);
                totalVO.setCardStrategy(CardAdapter.CardModeItem.END);
                innerItens.add(totalVO);
            }
        }

        return innerItens;
    }

    public List<Category> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
