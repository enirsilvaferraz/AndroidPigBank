package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.helpers.IntentRouter;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.views.RoundedTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_category_name)
        TextView name;

        @BindView(R.id.item_category_rounded_view)
        RoundedTextView roundedView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(final Category item) {

            name.setText(item.getName());
            roundedView.setColor(item.getColor());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentRouter.startCategoryManager(view.getContext(), item);
                }
            });
        }
    }
}
