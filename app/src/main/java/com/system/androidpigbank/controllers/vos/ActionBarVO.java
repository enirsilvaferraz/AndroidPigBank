package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by Enir on 11/09/2016.
 */

public class ActionBarVO implements CardAdapter.CardModel {

    private CardAdapter.CardModeItem cardStrategy;

    private CardAdapter.CardModel cardReferency;

    public ActionBarVO(CardAdapter.CardModel cardReferency, CardAdapter.CardModeItem cardStrategy) {
        this.cardReferency = cardReferency;
        this.cardStrategy = cardStrategy;
    }

    public CardAdapter.CardModel getCardReferency() {
        return cardReferency;
    }

    public void setCardReferency(CardAdapter.CardModel cardReferency) {
        this.cardReferency = cardReferency;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_ACTION_BAR;
    }

    @Override
    public CardAdapter.CardModeItem getCardStrategy() {
        return cardStrategy;
    }

    @Override
    public void setCardStrategy(CardAdapter.CardModeItem cardStrategy) {
        this.cardStrategy = cardStrategy;
    }
}
