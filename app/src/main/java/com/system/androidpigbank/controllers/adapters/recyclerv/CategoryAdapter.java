package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 27/04/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> itens;

    public CategoryAdapter() {
        this.itens = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_view_holder_category, parent, false);
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

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_category_name);
        }

        private void bind(Category item) {

            name.setText(item.getName());

            if (item.getColor() != null) {
                itemView.setBackgroundResource(item.getColor().getColorId());
            }
        }
    }
}
