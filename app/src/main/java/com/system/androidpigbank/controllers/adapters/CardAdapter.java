package com.system.androidpigbank.controllers.adapters;

import android.view.View;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.ActionBarViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.CategoryViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.MonthViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.TitleViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.TotalViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.TransactionViewHolder;
import com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder.WhiteSpaceViewHolder;
import com.system.androidpigbank.controllers.vos.ActionBarVO;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;

/**
 * Created by eferraz on 01/12/16.
 */

public class CardAdapter extends CardAdapterAbs {


    public CardAdapter(CardViewHolder.OnClickListener onClickListener) {
        super(onClickListener);
    }

    @Override
    public CardViewHolder getViewHolder(int viewType, View v) {

        switch (Card.values()[viewType]) {

            case CARD_TRANSACTION:
                return new TransactionViewHolder(v);

            case CARD_CATEGOTY:
                return new CategoryViewHolder(v);

            case CARD_FOOTER:
                return new TotalViewHolder(v);

            case CARD_TITLE:
                return new TitleViewHolder(v);

            case CARD_ACTION_BAR:
                return new ActionBarViewHolder(v);

            case CARD_WHITESPACE:
                return new WhiteSpaceViewHolder(v);

            case CARD_MONTH:
                return new MonthViewHolder(v);

            default:
                throw new IllegalArgumentException("View Holder não encontrado!");
        }
    }

    @Override
    public int getLayoutId(int viewType) {

        switch (Card.values()[viewType]) {

            case CARD_ACTION_BAR:
                return R.layout.item_view_holder_card_bar;
            case CARD_CATEGOTY:
                return R.layout.item_view_holder_category_summary;
            case CARD_FOOTER:
                return R.layout.item_view_holder_total;
            case CARD_MONTH:
                return R.layout.item_view_holder_month;
            case CARD_TITLE:
                return R.layout.item_view_holder_title;
            case CARD_TRANSACTION:
                return R.layout.item_view_holder_transaction;
            case CARD_WHITESPACE:
                return R.layout.item_view_holder_white_space;
            default:
                throw new IllegalArgumentException("View Holder não encontrado!");
        }
    }

    @Override
    public int getViewType(CardModel cardModel) {

        if (cardModel instanceof TransactionVO) {
            return Card.CARD_TRANSACTION.ordinal();
        } else if (cardModel instanceof CategoryVO) {
            return Card.CARD_CATEGOTY.ordinal();
        } else if (cardModel instanceof TotalVO) {
            return Card.CARD_FOOTER.ordinal();
        } else if (cardModel instanceof TitleVO) {
            return Card.CARD_TITLE.ordinal();
        } else if (cardModel instanceof ActionBarVO) {
            return Card.CARD_ACTION_BAR.ordinal();
        } else if (cardModel instanceof WhiteSpaceVO) {
            return Card.CARD_WHITESPACE.ordinal();
        } else if (cardModel instanceof MonthVO) {
            return Card.CARD_MONTH.ordinal();
        } else {
            throw new IllegalArgumentException("View Holder não encontrado!");
        }
    }

    private enum Card {
        CARD_TRANSACTION,
        CARD_CATEGOTY,
        CARD_FOOTER,
        CARD_TITLE,
        CARD_ACTION_BAR,
        CARD_WHITESPACE,
        CARD_MONTH;
    }
}
