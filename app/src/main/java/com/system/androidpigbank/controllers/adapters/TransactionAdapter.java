package com.system.androidpigbank.controllers.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.activities.BaseActivity;
import com.system.androidpigbank.controllers.vIewHolders.DateSectionViewHolder;
import com.system.androidpigbank.controllers.vIewHolders.TransactionViewHolder;
import com.system.androidpigbank.controllers.vIewHolders.ViewHolderAbs;
import com.system.androidpigbank.controllers.vos.DateSection;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum TransactionViewType {
    CARD, SECTION;
}

/**
 * Created by eferraz on 05/12/15.
 */
public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity activity;
    private List<EntityAbs> itens;

    public TransactionAdapter(AppCompatActivity activity) {
        this.itens = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    @SuppressWarnings("ResourceType")
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        int resId = 0;
        if (TransactionViewType.SECTION.ordinal() == viewType) {
            resId = R.layout.item_view_holder_date_section;
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
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<Transaction> itens) {
        this.itens.clear();
        this.itens.addAll(organizeItens(itens));
        notifyDataSetChanged();
    }

    public List<EntityAbs> getItens() {
        return itens;
    }

    protected List<EntityAbs> organizeItens(List<Transaction> itens) {

        List<EntityAbs> newList = new ArrayList<>();

        Date date = null;
        for (Transaction transaction : itens) {
            if (date == null || date.before(transaction.getDate())) {
                date = transaction.getDate();
                newList.add(new DateSection(date));
            }
            newList.add(transaction);
        }

        return newList;
    }

    @Override
    public int getItemViewType(int position) {
        return getItens().get(position) instanceof Transaction ? TransactionViewType.CARD.ordinal() : TransactionViewType.SECTION.ordinal();
    }

    protected ViewHolderAbs getViewHolderInstance(View v, int viewType) {

        if (TransactionViewType.SECTION.ordinal() == viewType) {
            return new DateSectionViewHolder(v);
        } else {
            return new TransactionViewHolder(v, activity, this);
        }
    }

    public void removeItem(Transaction data) {
        int index = getItens().indexOf(data);
        getItens().remove(index);
        notifyItemRemoved(index);
        ((BaseActivity)activity).showMessage("Transaction removed!");
    }
}
