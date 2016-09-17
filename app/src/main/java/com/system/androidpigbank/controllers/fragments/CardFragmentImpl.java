package com.system.androidpigbank.controllers.fragments;

import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.HomeActivity;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.business.CategoryFirebaseBusiness;
import com.system.androidpigbank.models.firebase.business.TransactionFirebaseBusiness;
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

    @Override
    public int getFragmentID() {
        return fragmentID;
    }

    private void setFragmentID(int fragmentID) {
        this.fragmentID = fragmentID;
    }

    public void performClick(int action, final CardAdapter.CardModel model) {
        final CardAdapter cardAdapter = (CardAdapter) recyclerview.getAdapter();

        if (model instanceof TransactionVO) {
            performTransactionClick(action, model, cardAdapter);
        }
//        else if (model instanceof CategoryVO){
//            performCategoryClick(action, model, cardAdapter);
//        }

        else if (model instanceof MonthVO) {
            performMonthClick(action, (MonthVO) model, cardAdapter);
        }
    }

    private void performMonthClick(int action, MonthVO model, CardAdapter cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                ((HomeActivity) getActivity()).update(model.getMonth(), model.getYear());
                break;
        }
    }

    private void performCategoryClick(int action, final CardAdapter.CardModel model, final CardAdapter cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                if (toolbar == null) {
                    toolbar = new ActionBarVO(model);
                    toolbar.setActionsToHide(ActionBarVO.Actions.COPY);
                    cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                } else {
                    boolean mustAdd = !toolbar.getCardReferency().equals(model);
                    removeToolbar(cardAdapter);
                    if (mustAdd) {
                        toolbar = new ActionBarVO(model);
                        toolbar.setActionsToHide(ActionBarVO.Actions.COPY);
                        cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                    }
                }
                break;

            case Constants.ACTION_EDIT:
                removeToolbar(cardAdapter);
                IntentRouter.startCategoryManager((AppCompatActivity) getActivity(), (CategoryVO) model);
                break;

            case Constants.ACTION_DELETE:
                removeToolbar(cardAdapter);
                ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<CategoryVO>() {
                    @Override
                    public CategoryVO executeAction() throws Exception {
                        return new CategoryFirebaseBusiness().delete((CategoryVO) model);
                    }

                    @Override
                    public void onComplete(LoaderResult<CategoryVO> data) {
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

    private void performTransactionClick(int action, final CardAdapter.CardModel model, final CardAdapter cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                if (toolbar == null) {
                    toolbar = new ActionBarVO(model);
                    cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                } else {
                    boolean mustAdd = !toolbar.getCardReferency().equals(model);
                    removeToolbar(cardAdapter);
                    if (mustAdd) {
                        toolbar = new ActionBarVO(model);
                        cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                    }
                }
                break;

            case Constants.ACTION_EDIT:
                removeToolbar(cardAdapter);
                IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (TransactionVO) model);
                break;

            case Constants.ACTION_COPY:
                removeToolbar(cardAdapter);
                ((TransactionVO) model).setKey(null);
                IntentRouter.startTransactionManager((AppCompatActivity) getActivity(), (TransactionVO) model);
                break;

            case Constants.ACTION_DELETE:
                removeToolbar(cardAdapter);
                ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<TransactionVO>() {
                    @Override
                    public TransactionVO executeAction() throws Exception {
                        return new TransactionFirebaseBusiness().delete((TransactionVO) model);
                    }

                    @Override
                    public void onComplete(LoaderResult<TransactionVO> data) {
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


    private void removeToolbar(CardAdapter cardAdapter) {
        cardAdapter.remove(toolbar);
        toolbar = null;
    }
}
