package com.system.architecture.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.viewHolder.ActionBarViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.CategoryViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TitleViewHolder2;
import com.system.androidpigbank.controllers.adapters.viewHolder.TotalViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TransactionViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardViewHolder.OnClickListener onClickListener;
    private final boolean isCardMode;
    private List<CardModel> itens;

    public CardAdapter(CardViewHolder.OnClickListener onClickListener) {
        this(false, onClickListener);
    }

    public CardAdapter(boolean isCardMode, CardViewHolder.OnClickListener onClickListener) {
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
        ((CardViewHolder) holder).bind(getItens().get(position), onClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return getItens().get(position).getViewType().ordinal();
    }

    @Override
    public int getItemCount() {
        return getItens().size();
    }

    public void addItens(List<CardModel> itens) {
        getItens().clear();
        getItens().addAll(itens);
        notifyDataSetChanged();
    }

    public List<CardModel> getItens() {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        return itens;
    }

    public void add(CardModel model, int position) {
        getItens().add(position, model);
        int index = getItens().indexOf(model);
        notifyItemInserted(index);
    }

    public void remove(CardModel model){
        int index = getItens().indexOf(model);
        getItens().remove(index);
        notifyItemRemoved(index);
    }

    /**
     *
     */
    public enum CardViewType {

        CARD_TRANSACTION(R.layout.item_view_holder_transaction),
        CARD_CATEGOTY(R.layout.item_view_holder_category_summary),
        CARD_FOOTER(R.layout.item_view_holder_total2),
        CARD_TITLE(R.layout.item_view_holder_title),
        CARD_ACTION_BAR(R.layout.custom_view_card_bar);

        @LayoutRes
        private final int layoutId;

        CardViewType(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
        }

        public static CardViewType getEnum(int viewType) {
            for (CardViewType enumerator : CardViewType.values())
                if (enumerator.ordinal() == viewType) {
                    return enumerator;
                }
            throw new RuntimeException("Resouce not found for view type " + viewType);
        }

        public int getLayoutId() {
            return layoutId;
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

                case CARD_ACTION_BAR:
                    viewHolder = new ActionBarViewHolder(v, isCardMode);
                    break;
            }

            return viewHolder;
        }
    }

    /**
     *
     */
    public enum CardModeItem {
        SINGLE, START, END, MIDDLE, NO_STRATEGY
    }

    /**
     *
     */
    public interface CardModel {

        CardViewType getViewType();

        CardModeItem getCardStrategy();

        void setCardStrategy(CardAdapter.CardModeItem cardStrategy);
    }
}
