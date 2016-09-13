package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by Enir on 17/08/2016.
 */

public class TitleVO implements CardAdapter.CardModel {

    private CardAdapter.CardModeItem cardStrategy;
    private String title;

    public TitleVO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_TITLE;
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
