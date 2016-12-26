package com.system.androidpigbank.controllers.vos;

import com.system.architecture.activities.CardAdapterAbs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarVO implements CardAdapterAbs.CardModel, Cloneable {

    private CardAdapterAbs.CardModel cardReferency;
    private List<Actions> actionsToShow;

    public ActionBarVO(CardAdapterAbs.CardModel cardReferency) {
        this.cardReferency = cardReferency;
    }

    public List<Actions> getActionsToShow() {
        return actionsToShow;
    }

    public void setActionsToShow(Actions... actionsToShow) {
        this.actionsToShow = Arrays.asList(actionsToShow);
    }

    public CardAdapterAbs.CardModel getCardReferency() {
        return cardReferency;
    }

    /**
     *
     */
    public enum Actions {
        COPY, EDIT, DELETE, HIGHLIGHT
    }
}
