package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarVO implements VOIf, CardAdapter.CardModel {

    private CardAdapter.CardModel cardReferency;

    private Actions[] actionsToHide;

    public ActionBarVO(CardAdapter.CardModel cardReferency) {
        this.cardReferency = cardReferency;
    }

    public void setActionsToHide(Actions... actionsToHide) {
        this.actionsToHide = actionsToHide;
    }

    public Actions[] getActionsToHide() {
        return actionsToHide;
    }

    public CardAdapter.CardModel getCardReferency() {
        return cardReferency;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_ACTION_BAR;
    }

    /**
     *
     */
    public enum Actions {
        COPY, EDIT, DELETE
    }
}
