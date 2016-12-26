package com.system.architecture.activities;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 05/12/15.
 */
public abstract class CardAdapterAbs extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardViewHolder.OnClickListener onClickListener;
    private List<CardModel> itens;

    public CardAdapterAbs(CardViewHolder.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = getLayoutId(viewType);
        View  view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return getViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CardViewHolder) holder).bind(getItens().get(position), onClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(getItens().get(position));
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

    public void remove(CardModel model) {
        int index = getItens().indexOf(model);
        getItens().remove(index);
        notifyItemRemoved(index);
    }

    public abstract CardViewHolder getViewHolder(int viewType, View view);

    public abstract int getLayoutId(int viewType);

    public abstract int getViewType(CardModel cardModel);

    public int findCard(CardModel card) {
        return itens.indexOf(card);
    }

    /**
     *
     */
    public interface CardModel {

    }
}
