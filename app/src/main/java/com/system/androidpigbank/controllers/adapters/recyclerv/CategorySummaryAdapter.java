package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.views.DividerView;
import com.system.androidpigbank.views.TransactionView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 08/07/16.
 * Adapter de categorias
 */

public class CategorySummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> itens;

    public CategorySummaryAdapter() {
        this.itens = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_view_holder_category_summary, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<Category> itens) {
        this.itens.clear();
        this.itens.addAll(itens);
        notifyDataSetChanged();
    }

    private void updateAll(Category item) {
        for (Category category : itens) {
            if (!category.equals(item) && category.isExpanded()) {
                category.setExpanded(false);
                notifyItemChanged(itens.indexOf(category));
                break;
            }
        }

        notifyItemChanged(itens.indexOf(item));
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

            NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            tvRange.setText(cf.format(item.getAmount()) + " de " + cf.format(0));
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

            GridLayoutManager.LayoutParams param = (GridLayoutManager.LayoutParams) cvContainer.getLayoutParams();
            if (item.isExpanded()) {
                param.setMargins(30, 30, 30, 30);

                if (!item.getTransactionList().isEmpty()) {

                    transactionContainer.setVisibility(View.VISIBLE);
                    transactionContainer.addView(new DividerView(itemView.getContext()));

                    List<Transaction> secundaryList = new ArrayList<>();
                    for (Transaction transaction : item.getTransactionList()) {
                        if (transaction.getCategory().getId().equals(item.getId())) {
                            transactionContainer.addView(new TransactionView(itemView.getContext(), transaction));
                        } else {
                            secundaryList.add(transaction);
                        }
                    }

                    if (!secundaryList.isEmpty()) {
                        transactionContainer.addView(new DividerView(itemView.getContext()));
                        for (Transaction transaction : secundaryList) {
                            transactionContainer.addView(new TransactionView(itemView.getContext(), transaction));
                        }
                    }
                }

            } else {
                param.setMargins(0, 0, 0, 0);
                transactionContainer.setVisibility(View.GONE);
            }
            cvContainer.setLayoutParams(param);
        }
    }
}
