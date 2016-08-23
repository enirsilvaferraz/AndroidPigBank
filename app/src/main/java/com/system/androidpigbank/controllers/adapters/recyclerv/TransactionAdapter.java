package com.system.androidpigbank.controllers.adapters.recyclerv;

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
import com.system.androidpigbank.controllers.behaviors.HighlightCardBehavior;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.CardActionBarView;
import com.system.androidpigbank.views.RoundedTextView;
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
public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CardActionBarView.OnClickListener onCardBarClickListener;

    private AppCompatActivity activity;
    private List<EntityAbs> itens;

    public TransactionAdapter(AppCompatActivity activity) {
        this.itens = new ArrayList<>();
        this.activity = activity;
    }

    public void setOnCardBarClickListener(CardActionBarView.OnClickListener onCardBarClickListener) {
        this.onCardBarClickListener = onCardBarClickListener;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        int resId;
        if (TransactionViewType.FOOTER.ordinal() == viewType) {
            resId = R.layout.item_view_holder_total;
        } else {
            resId = R.layout.item_view_holder_transaction;
        }

        View v = layoutInflater.inflate(resId, parent, false);
        return getViewHolderInstance(v, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderAbs) holder).bind(itens.get(position));
    }

    @Override
    public int getItemViewType(int position) {

        if (itens.get(position) instanceof Transaction) {
            return TransactionViewType.CARD.ordinal();
        } else {
            return TransactionViewType.FOOTER.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    private ViewHolderAbs getViewHolderInstance(View v, int viewType) {

        if (TransactionViewType.FOOTER.ordinal() == viewType) {
            return new FooterViewHolder(v);
        } else {
            return new TransactionViewHolder(v, activity, this);
        }
    }

    public void addItens(List<Transaction> itens) {
        this.itens.clear();
        this.itens.addAll(organizeItens(itens));
        notifyDataSetChanged();
    }

    private void updateAll(Transaction item) {
        for (EntityAbs entity : itens) {
            if (entity instanceof Transaction) {
                Transaction category = (Transaction) entity;
                if (!category.equals(item) && category.isExpanded()) {
                    category.setExpanded(false);
                    notifyItemChanged(itens.indexOf(category));
                    break;
                }
            }
        }

        notifyItemChanged(itens.indexOf(item));
    }

    private List<EntityAbs> organizeItens(List<Transaction> itens) {

        List<EntityAbs> newList = new ArrayList<>();
        List<EntityAbs> newListAfter = new ArrayList<>();

        Double total = 0d;
        Double totalAfter = 0d;

        for (Transaction transaction : itens) {

            if (transaction.getDate().before(Calendar.getInstance().getTime())) {
                total += transaction.getValue();
                newList.add(transaction);
            } else {
                totalAfter += transaction.getValue();
                newListAfter.add(transaction);
            }
        }

        TotalFooter footer = new TotalFooter();
        footer.setTotal(total);
        newList.add(footer);

        if (!newListAfter.isEmpty()) {
            newList.addAll(newListAfter);

            TotalFooter footerAfter = new TotalFooter();
            footerAfter.setTotal(totalAfter + total);
            newList.add(footerAfter);
        }

        return newList;
    }

    public void removeItem(Transaction data) {
        int index = itens.indexOf(data);

        for (TotalFooter item : itens){

        }

        Double total = ((TotalFooter)itens.get(itens.size() - 1)).getTotal();
        total -= ((Transaction)itens.remove(index)).getValue();
        ((TotalFooter)itens.get(itens.size() - 1)).setTotal(total);

        itens.remove(index);
        notifyItemRemoved(index);
        ((BaseActivity) activity).showMessage(R.string.message_delete_sucess);
    }

    private enum TransactionViewType {

        CARD(false), FOOTER(true);

        private boolean fullSpan;

        TransactionViewType(boolean fullSpan) {
            this.fullSpan = fullSpan;
        }

        public boolean isFullSpan() {
            return fullSpan;
        }
    }

    public class TransactionViewHolder extends ViewHolderAbs {

        @BindView(R.id.item_bar_container)
        LinearLayout llContainer;

        @BindView(R.id.card_view_container)
        CardView cvContainer;

        @BindView(R.id.item_transaction_rounded_view)
        RoundedTextView roundedTextView;

        @BindView(R.id.item_transaction_content)
        TextView textContent;

        @BindView(R.id.item_transaction_date)
        TextView textDate;

        @BindView(R.id.item_transaction_category)
        TextView textCategory;

        @BindView(R.id.item_transaction_value)
        TextView textValue;

        public TransactionViewHolder(View v, AppCompatActivity activity, RecyclerView.Adapter adapter) {
            super(v, activity, adapter);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(final EntityAbs model) {

            final Transaction transaction = (Transaction) model;

            textValue.setText(JavaUtils.NumberUtil.currencyFormat(transaction.getValue()));
            roundedTextView.setTextView(transaction.getCategory().getName());
            textContent.setText(transaction.getContent());
            textDate.setText(JavaUtils.DateUtil.format(transaction.getDate()));

            roundedTextView.setColor(((Transaction) model).getCategory().getColor());

            String categoryName = transaction.getCategory().getName();
            if (transaction.getCategorySecondary() != null) {
                categoryName += " / " + transaction.getCategorySecondary().getName();
            }

            textCategory.setText(categoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    transaction.setExpanded(!transaction.isExpanded());
                    updateAll(transaction);
                }
            });

            animate(transaction);
        }

        private void animate(final Transaction transaction) {
            llContainer.removeAllViews();
            if (transaction.isExpanded()) {
                HighlightCardBehavior.turnOn(cvContainer);

                llContainer.setVisibility(View.VISIBLE);
                llContainer.addView(new CardActionBarView(itemView.getContext()).bind(transaction, onCardBarClickListener));

            } else {
                HighlightCardBehavior.turnOff(cvContainer);
                llContainer.setVisibility(View.GONE);
            }
        }
    }
}
