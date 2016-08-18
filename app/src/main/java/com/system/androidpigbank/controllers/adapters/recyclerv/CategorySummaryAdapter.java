package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.adapters.viewHolder.TitleViewHolder;
import com.system.androidpigbank.controllers.behaviors.HighlightCardBehavior;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.DividerView;
import com.system.androidpigbank.views.TransactionView;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 08/07/16.
 * Adapter de categorias
 */

public class CategorySummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EntityAbs> itens;

    public CategorySummaryAdapter() {
        this.itens = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (Type.CATEGORY.ordinal() == viewType) {
            return new ViewHolder(layoutInflater.inflate(R.layout.item_view_holder_category_summary, parent, false));
        } else {
            return new TitleViewHolder(layoutInflater.inflate(R.layout.item_view_holder_title, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (itens.get(position) instanceof Category) {
            ((ViewHolder) holder).bind((Category) itens.get(position));
        } else {
            ((TitleViewHolder) holder).bind(itens.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itens.get(position) instanceof Category) {
            return Type.CATEGORY.ordinal();
        } else {
            return Type.TITLE.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<Category> itens) {
        this.itens.clear();

        boolean alreadyAddedSecoundary = false;
        this.itens.add(new TitleVO("Primary Categories"));

        for (Category category : itens) {
            if (!alreadyAddedSecoundary && !category.isPrimary()) {
                this.itens.add(new TitleVO("Secondary Categories"));
                alreadyAddedSecoundary = true;
            }
            this.itens.add(category);
        }

        notifyDataSetChanged();
    }

    private void updateAll(Category item) {
        for (EntityAbs entity : itens) {
            if (entity instanceof Category) {
                Category category = (Category) entity;
                if (!category.equals(item) && category.isExpanded()) {
                    category.setExpanded(false);
                    notifyItemChanged(itens.indexOf(category));
                    break;
                }
            }
        }

        notifyItemChanged(itens.indexOf(item));
    }

    enum Type {
        CATEGORY, TITLE
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_container)
        CardView cvContainer;

        @BindView(R.id.item_category_name)
        TextView tvName;

        @BindView(R.id.item_category_range)
        TextView tvRange;

        @BindView(R.id.item_category_progress)
        ProgressBar progressBar;

        @BindView(R.id.item_category_progress_value)
        TextView tvProgressValue;

        @BindView(R.id.item_category_transaction_container)
        LinearLayout transactionContainer;

        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        private void bind(final Category item) {
            tvName.setText(item.getName());

            tvRange.setText(JavaUtils.NumberUtil.currencyFormat(item.getAmount()) + " de " +
                    JavaUtils.NumberUtil.currencyFormat(0D));
            tvProgressValue.setText("N/A");
            progressBar.setProgress(0);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setExpanded(!item.isExpanded());
                    updateAll(item);
                }
            });

            animate(item);
        }

        private void animate(Category item) {

            transactionContainer.removeAllViews();

            if (item.isExpanded()) {

                HighlightCardBehavior.turnOn(cvContainer);

                if (!item.getTransactionList().isEmpty()) {

                    transactionContainer.setVisibility(View.VISIBLE);
                    transactionContainer.addView(new DividerView(itemView.getContext()));

                    for (Transaction transaction : item.getTransactionList()) {
                        transactionContainer.addView(new TransactionView(itemView.getContext(), transaction));
                    }
                }

            } else {
                HighlightCardBehavior.turnOff(cvContainer);
                transactionContainer.setVisibility(View.GONE);
            }
        }
    }
}
