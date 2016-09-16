package com.system.androidpigbank.controllers.vos;

import com.system.architecture.adapters.CardAdapter;

/**
 * Created by eferraz on 12/09/16.
 */

public class WhiteSpaceVO implements VOIf, CardAdapter.CardModel {

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_WHITESPACE;
    }
}
