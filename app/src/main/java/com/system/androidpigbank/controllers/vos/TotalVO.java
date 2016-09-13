package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 03/01/16.
 */
public class TotalVO implements CardAdapter.CardModel {

    private Double valueStart;

    private Double valueEnd;

    public TotalVO(Double valueStart, Double valueEnd) {
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
    }

    public Double getValueStart() {
        return valueStart;
    }

    public Double getValueEnd() {
        return valueEnd;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_FOOTER;
    }
}
