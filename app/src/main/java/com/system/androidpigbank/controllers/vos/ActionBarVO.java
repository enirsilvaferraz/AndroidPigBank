package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarVO implements VOIf, CardAdapter.CardModel {

    public static final Creator<ActionBarVO> CREATOR = new Creator<ActionBarVO>() {
        @Override
        public ActionBarVO createFromParcel(Parcel source) {
            return new ActionBarVO(source);
        }

        @Override
        public ActionBarVO[] newArray(int size) {
            return new ActionBarVO[size];
        }
    };
    private CardAdapter.CardModel cardReferency;
    private Actions[] actionsToHide;

    public ActionBarVO(CardAdapter.CardModel cardReferency) {
        this.cardReferency = cardReferency;
    }

    protected ActionBarVO(Parcel in) {
        this.cardReferency = in.readParcelable(CardAdapter.CardModel.class.getClassLoader());
    }

    public Actions[] getActionsToHide() {
        return actionsToHide;
    }

    public void setActionsToHide(Actions... actionsToHide) {
        this.actionsToHide = actionsToHide;
    }

    public CardAdapter.CardModel getCardReferency() {
        return cardReferency;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_ACTION_BAR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cardReferency, flags);
    }

    /**
     *
     */
    public enum Actions {
        COPY, EDIT, DELETE
    }
}
