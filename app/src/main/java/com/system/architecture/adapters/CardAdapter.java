package com.system.architecture.adapters;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.viewHolder.FooterViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TotalViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TransactionViewHolder;
import com.system.androidpigbank.controllers.behaviors.HighlightCardBehavior;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.sqlite.entities.EntityAbs;
import com.system.androidpigbank.models.sqlite.entities.Transaction;
import com.system.androidpigbank.views.CardActionBarView;
import com.system.androidpigbank.views.RoundedImageView;
import com.system.architecture.activities.BaseActivity;
import com.system.architecture.utils.JavaUtils;
import com.system.architecture.viewHolders.ViewHolderAbs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 05/12/15.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     *
     */
    private Activity activity;
    private List<CardModel> itens;

    public CardAdapter(Activity activity) {
        this.itens = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TransactionViewType.getEnum(viewType).getViewHolderInstance(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderModel) holder).bind(itens.get(position));
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
    public enum TransactionViewType {

        CARD_TRANSACTION(R.layout.item_view_holder_transaction),
        CARD_FOOTER(R.layout.item_view_holder_total2);

        @LayoutRes
        private final int layoutId;

        TransactionViewType(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public static TransactionViewType getEnum(int viewType) {
            for (TransactionViewType enumerator : TransactionViewType.values())
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
        TransactionViewType getViewType();
    }
}
