package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by Enir on 17/08/2016.
 */

public class TitleVO implements VOIf, CardAdapter.CardModel {

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

}
