package com.system.androidpigbank.controllers.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.androidpigbank.models.sqlite.business.TransactionBusiness;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardFragment;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;

/**
 * Created by Enir on 11/09/2016.
 */

public class CardFragmentImpl extends CardFragment {

    private int fragmentID;
    private ActionBarVO toolbar;

    public static CardFragment newInstance(int id) {
        CardFragmentImpl fragment = new CardFragmentImpl();
        fragment.setFragmentID(id);
        return fragment;
    }

    private void setFragmentID(int fragmentID) {
        this.fragmentID = fragmentID;
    }

    @Override
    public int getFragmentID() {
        return fragmentID;
    }

    public void performClick(int action, final CardAdapter.CardModel model) {
        final CardAdapter cardAdapter = (CardAdapter) recyclerview.getAdapter();

        if (model instanceof Transaction) {
            switch (action) {
                case Constants.ACTION_VIEW:
                    if (toolbar == null) {
                        toolbar = new ActionBarVO(model, CardAdapter.CardModeItem.MIDDLE);
                        cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                    } else {
                        boolean mustAdd = !toolbar.getCardReferency().equals(model);
                        removeToolbar(cardAdapter);
                        if (mustAdd) {
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
                    ((Transaction) model).setId(null);
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