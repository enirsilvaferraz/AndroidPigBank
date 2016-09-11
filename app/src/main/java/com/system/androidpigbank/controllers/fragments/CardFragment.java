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
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.androidpigbank.models.sqlite.business.TransactionBusiness;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardFragment extends Fragment {

    @BindView(R.id.category_recyclerview)
    RecyclerView recyclerview;

    private List<CardAdapter.CardModel> data;

    public static CardFragment newInstance() {
        CardFragment fragment = new CardFragment();
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

        final CardAdapter adapter = new CardAdapter(true, new CardEvent());
        adapter.addItens(getData());

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
    }

    public List<CardAdapter.CardModel> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<CardAdapter.CardModel> data) {
        this.data = data;
    }

    /**
     *
     */
    private class CardEvent implements CardViewHolder.OnClickListener {

        ActionBarVO toolbar;

        @Override
        public void onContainerClicked(int action, final CardAdapter.CardModel model) {
            performClick(action, model);
        }

        private void performClick(int action, final CardAdapter.CardModel model) {
            final CardAdapter cardAdapter = (CardAdapter) recyclerview.getAdapter();

            if (model instanceof Transaction) {
                switch (action) {
                    case Constants.ACTION_VIEW:
                        if (toolbar == null) {
                            toolbar = new ActionBarVO(model, CardAdapter.CardModeItem.MIDDLE);
                            cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                        }
                        else {
                            boolean mustAdd = !toolbar.getCardReferency().equals(model);
                            removeToolbar(cardAdapter);
                            if (mustAdd){
                                toolbar = new ActionBarVO(model, CardAdapter.CardModeItem.MIDDLE);
                                cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                            }
                        }
                        break;

                    case Constants.ACTION_EDIT:
                        removeToolbar(cardAdapter);
                        IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
                        break;

                    case Constants.ACTION_COPY:
                        removeToolbar(cardAdapter);
                        ((Transaction)model).setId(null);
                        IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (Transaction) model);
                        break;

                    case Constants.ACTION_DELETE:
                        removeToolbar(cardAdapter);
                        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<Transaction>() {
                            @Override
                            public Transaction executeAction() throws Exception {
                                return new TransactionBusiness(getActivity()).delete((Transaction) model);
                            }

                            @Override
                            public void onComplete(LoaderResult<Transaction> data) {
                                if (data.isSuccess()) {
                                    cardAdapter.remove(data.getData());
                                } else {
                                    // TODO Tratar exception
                                }
                            }
                        });
                        break;
                }
            }
        }

        private void removeToolbar(CardAdapter cardAdapter) {
            cardAdapter.remove(toolbar);
            toolbar = null;
        }
    }
}
