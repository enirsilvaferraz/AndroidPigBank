package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalVO implements CardAdapter.CardModel {

    private CardAdapter.CardModeItem cardStrategy;

    private Double total;

    public TotalVO() {
    }

    public TotalVO(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(final Double total) {
        this.total = total;
    }


    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_FOOTER;
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
