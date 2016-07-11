package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.entities.Category;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 08/07/16.
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_category_name)
        TextView tvName;

        @BindView(R.id.item_category_range)
        TextView tvRange;

        @BindView(R.id.item_category_progress)
        ProgressBar progressBar;

        @BindView(R.id.item_category_progress_value)
        TextView tvProgressValue;

        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        private void bind(Category item) {
            tvName.setText(item.getName());

            NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            tvRange.setText(cf.format(item.getAmount()) + " de " + cf.format(0));
            tvProgressValue.setText("N/A");
            progressBar.setProgress(0);
        }
    }
}
