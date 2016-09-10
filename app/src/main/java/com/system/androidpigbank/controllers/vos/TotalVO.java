package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalVO implements CardAdapter.CardModel {

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
    public CardAdapter.TransactionViewType getViewType() {
        return CardAdapter.TransactionViewType.CARD_FOOTER;
    }
}
