package com.system.architecture.adapters;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.viewHolder.TotalViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TransactionViewHolder;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.architecture.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ViewHolderModel.OnClickListener onClickListener;
    private Activity activity;
    private List<CardModel> itens;

    public CardAdapter(Activity activity, ViewHolderModel.OnClickListener onClickListener) {
        this.itens = new ArrayList<>();
        this.activity = activity;
        this.onClickListener = onClickListener;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CardViewType.getEnum(viewType).getViewHolderInstance(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderModel) holder).bind(itens.get(position), onClickListener);
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

    public void removeItem(Transaction data) {
        int index = itens.indexOf(data);
        itens.remove(index);

        notifyItemRemoved(index);

        Double value = 0D;
        for (CardModel item : itens) {

            if (item instanceof Transaction) {
                value += ((Transaction) item).getValue();
            } else if (item instanceof TotalFooter) {
                ((TotalFooter) item).setTotal(value);
                notifyItemChanged(itens.indexOf(item));
            }
        }

        ((BaseActivity) activity).showMessage(R.string.message_delete_sucess);
    }

    /**
     *
     */
    public enum CardViewType {

        CARD_TRANSACTION(R.layout.item_view_holder_transaction),
        CARD_FOOTER(R.layout.item_view_holder_total2);

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

        public ViewHolderModel getViewHolderInstance(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View v = layoutInflater.inflate(getLayoutId(), parent, false);

            ViewHolderModel viewHolder = null;

            switch (this) {
                case CARD_TRANSACTION:
                    viewHolder = new TransactionViewHolder(v);
                    break;

                case CARD_FOOTER:
                    viewHolder = new TotalViewHolder(v);
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
    }
}
