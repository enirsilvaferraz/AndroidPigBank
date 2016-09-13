package com.system.architecture.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class CardFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    public RecyclerView recyclerview;

    private List<CardAdapter.CardModel> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_card_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CardAdapter adapter = new CardAdapter(new CardViewHolder.OnClickListener() {
            @Override
            public void onContainerClicked(int action, CardAdapter.CardModel model) {
                performClick(action, model);
            }
        });
        adapter.addItens(getData());

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
    }

    public abstract void performClick(int action, CardAdapter.CardModel model);

    public abstract int getFragmentID();

    public List<CardAdapter.CardModel> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<CardAdapter.CardModel> data) {
        this.data = data;
    }

}
