package com.system.androidpigbank.controllers.fragments;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.system.androidpigbank.controllers.activities.HomeActivity;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.helpers.IntentRouter;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.business.CategoryFirebaseBusiness;
import com.system.androidpigbank.models.firebase.business.EstimateFirebaseBusiness;
import com.system.architecture.models.FirebaseAbs;
import com.system.androidpigbank.models.firebase.business.TransactionFirebaseBusiness;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.models.VOAbs;

/**
 * Created by Enir on 11/09/2016.
 */

public class CardFragmentImpl extends CardFragmentAbs {

    private int fragmentID;
    private ActionBarVO toolbar;

    public static CardFragmentAbs newInstance(int id) {
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

    public void performClick(int action, final CardAdapterAbs.CardModel model) {
        final CardAdapterAbs cardAdapter = (CardAdapterAbs) recyclerview.getAdapter();

        if (model instanceof TransactionVO) {
            performTransactionClick(action, model, cardAdapter);
        }

        else if (model instanceof CategoryVO){
            performCategoryClick(action, model, cardAdapter);
        }

        else if (model instanceof MonthVO) {
            performMonthClick(action, (MonthVO) model, cardAdapter);
        }

        else if (model instanceof EstimateVO){
            performEstimateClick(action, (EstimateVO) model, cardAdapter);
        }
    }

    private void performEstimateClick(int action, final EstimateVO model, CardAdapterAbs cardAdapter) {

        switch (action){
            case Constants.ACTION_VIEW:
                if (toolbar == null) {
                    toolbar = new ActionBarVO(model);
                    if (model.isAuxItem()){
                        toolbar.setActionsToShow(ActionBarVO.Actions.EDIT, ActionBarVO.Actions.HIGHLIGHT);
                    } else {
                        toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.COPY, ActionBarVO.Actions.EDIT, ActionBarVO.Actions.HIGHLIGHT);
                    }
                    cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                } else {
                    boolean mustAdd = !toolbar.getCardReferency().equals(model);
                    removeToolbar(cardAdapter);
                    if (mustAdd) {
                        toolbar = new ActionBarVO(model);
                        if (model.isAuxItem()){
                            toolbar.setActionsToShow(ActionBarVO.Actions.EDIT, ActionBarVO.Actions.HIGHLIGHT);
                        } else {
                            toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.COPY, ActionBarVO.Actions.EDIT, ActionBarVO.Actions.HIGHLIGHT);
                        }
                        cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                    }
                }
                break;

            case Constants.ACTION_EDIT:
                removeToolbar(cardAdapter);
                IntentRouter.startEstimateManager((AppCompatActivity) getActivity(), model);
                break;

            case Constants.ACTION_COPY:
                removeToolbar(cardAdapter);
                model.setKey(null);
                IntentRouter.startEstimateManager((AppCompatActivity) getActivity(), model);
                break;

            case Constants.ACTION_HIGHLIGHT:
                removeToolbar(cardAdapter);
                //CategoryVO card = model.getCategorySecondary() != null ? model.getCategorySecondary() : model.getCategory();
                ((HomeActivity)getActivity()).highlightCard(Constants.FRAGMENT_ID_SUMMARY_CATEGORY, model.getCategory());
                break;

            case Constants.ACTION_DELETE:

                removeToolbar(cardAdapter);

                new AlertDialog.Builder(getContext()).setTitle("Delete Alert").setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new EstimateFirebaseBusiness().delete(model, new FirebaseAbs.FirebaseSingleReturnListener() {
                            @Override
                            public void onFind(VOAbs list) {
                                ((HomeActivity) getActivity()).callApi();
                            }

                            @Override
                            public void onError(String error) {
                                ((HomeActivity) getActivity()).showMessage(error);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();

                break;
        }

    }

    private void performMonthClick(int action, MonthVO model, CardAdapterAbs cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                ((HomeActivity) getActivity()).update(model.getMonth(), model.getYear());
                break;
        }
    }

    private void performCategoryClick(int action, final CardAdapterAbs.CardModel model, final CardAdapterAbs cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                if (toolbar == null) {
                    toolbar = new ActionBarVO(model);
                    toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.EDIT);
                    cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                } else {
                    boolean mustAdd = !toolbar.getCardReferency().equals(model);
                    removeToolbar(cardAdapter);
                    if (mustAdd) {
                        toolbar = new ActionBarVO(model);
                        toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.EDIT);
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

                new AlertDialog.Builder(getContext()).setTitle("Delete Alert").setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new CategoryFirebaseBusiness().delete((CategoryVO) model, new FirebaseAbs.FirebaseSingleReturnListener() {
                            @Override
                            public void onFind(VOAbs list) {
                                ((HomeActivity) getActivity()).callApi();
                            }

                            @Override
                            public void onError(String error) {
                                ((HomeActivity) getActivity()).showMessage(error);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();

                break;
        }

    }

    private void performTransactionClick(int action, final CardAdapterAbs.CardModel model, final CardAdapterAbs cardAdapter) {

        switch (action) {
            case Constants.ACTION_VIEW:
                if (toolbar == null) {
                    toolbar = new ActionBarVO(model);
                    toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.COPY, ActionBarVO.Actions.EDIT);
                    cardAdapter.add(toolbar, cardAdapter.getItens().indexOf(model) + 1);
                } else {
                    boolean mustAdd = !toolbar.getCardReferency().equals(model);
                    removeToolbar(cardAdapter);
                    if (mustAdd) {
                        toolbar = new ActionBarVO(model);
                        toolbar.setActionsToShow(ActionBarVO.Actions.DELETE, ActionBarVO.Actions.COPY, ActionBarVO.Actions.EDIT);
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

                new AlertDialog.Builder(getContext()).setTitle("Delete Alert").setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new TransactionFirebaseBusiness().delete((TransactionVO) model, new FirebaseAbs.FirebaseSingleReturnListener() {
                            @Override
                            public void onFind(VOAbs list) {
                                ((HomeActivity) getActivity()).callApi();
                            }

                            @Override
                            public void onError(String error) {
                                ((HomeActivity) getActivity()).showMessage(error);
                            }
                        });
                    }
                }).setNegativeButton("No", null).show();

                break;
        }
    }


    private void removeToolbar(CardAdapterAbs cardAdapter) {
        cardAdapter.remove(toolbar);
        toolbar = null;
    }
}
