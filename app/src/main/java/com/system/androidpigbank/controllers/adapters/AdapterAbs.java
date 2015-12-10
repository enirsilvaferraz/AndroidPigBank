package com.system.androidpigbank.controllers.adapters;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.models.entities.EntityAbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class AdapterAbs <T extends EntityAbs> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> itens;

    public AdapterAbs() {
        this.itens = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(getResourceId(), parent, false);
        return getViewHolderInstance(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderAbs) holder).bind(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<T> itens) {
        this.itens.clear();
        this.itens.addAll(itens);
        notifyDataSetChanged();
    }

    @IdRes
    protected abstract int getResourceId();

    protected abstract ViewHolderAbs getViewHolderInstance(View v);
}
