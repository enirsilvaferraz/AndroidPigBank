package com.system.architecture.adapters;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.viewHolder.CategoryViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TitleViewHolder2;
import com.system.androidpigbank.controllers.adapters.viewHolder.TotalViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TransactionViewHolder;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardViewHolder.OnClickListener onClickListener;
    private final boolean isCardMode;
    private Activity activity;
    private List<CardModel> itens;

    public CardAdapter(Activity activity, CardViewHolder.OnClickListener onClickListener) {
        this(activity, false, onClickListener);
    }

    public CardAdapter(Activity activity, boolean isCardMode, CardViewHolder.OnClickListener onClickListener) {
        this.itens = new ArrayList<>();
        this.activity = activity;
        this.onClickListener = onClickListener;
        this.isCardMode = isCardMode;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardViewType anEnum = CardViewType.getEnum(viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(anEnum.getLayoutId(), parent, false);
        return anEnum.getViewHolderInstance(v, isCardMode);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CardViewHolder) holder).bind(itens.get(position), onClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return itens.get(position).getViewType().ordinal();
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<CardModel> itens) {
        this.itens.clear();
        this.itens.addAll(itens);
        notifyDataSetChanged();
    }

    /**
     *
     */
    public enum CardViewType {

        CARD_TRANSACTION(R.layout.item_view_holder_transaction),
        CARD_CATEGOTY(R.layout.item_view_holder_category_summary),
        CARD_FOOTER(R.layout.item_view_holder_total2),
        CARD_TITLE(R.layout.item_view_holder_title);

        @LayoutRes
        private final int layoutId;

        CardViewType(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public static CardViewType getEnum(int viewType) {
            for (CardViewType enumerator : CardViewType.values())
                if (enumerator.ordinal() == viewType) {
                    return enumerator;
                }
            throw new RuntimeException("Resouce not found for view type " + viewType);
        }

        public CardViewHolder getViewHolderInstance(View v, boolean isCardMode) {

            CardViewHolder viewHolder = null;

            switch (this) {
                case CARD_TRANSACTION:
                    viewHolder = new TransactionViewHolder(v, isCardMode);
                    break;

                case CARD_CATEGOTY:
                    viewHolder = new CategoryViewHolder(v, isCardMode);
                    break;

                case CARD_FOOTER:
                    viewHolder = new TotalViewHolder(v, isCardMode);
                    break;

                case CARD_TITLE:
                    viewHolder = new TitleViewHolder2(v, isCardMode);
                    break;
            }

            return viewHolder;
        }
    }

    /**
     *
     */
    public interface CardModel {

        CardViewType getViewType();
        CardModeItem getCardStrategy();
        void setCardStrategy(CardAdapter.CardModeItem cardStrategy);
    }

    /**
     *
     */
    public enum CardModeItem {
        SINGLE, START, END, MIDDLE, NO_STRATEGY
    }
}
