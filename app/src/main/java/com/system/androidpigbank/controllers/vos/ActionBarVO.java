package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.system.architecture.activities.CardAdapterAbs;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarVO implements CardAdapterAbs.CardModel, Cloneable {

    private CardAdapterAbs.CardModel cardReferency;
    private Actions[] actionsToHide;

    public ActionBarVO(CardAdapterAbs.CardModel cardReferency) {
        this.cardReferency = cardReferency;
    }

    public Actions[] getActionsToHide() {
        return actionsToHide;
    }

    public void setActionsToHide(Actions... actionsToHide) {
        this.actionsToHide = actionsToHide;
    }

    public CardAdapterAbs.CardModel getCardReferency() {
        return cardReferency;
    }

    /**
     *
     */
    public enum Actions {
        COPY, EDIT, DELETE
    }
}
