package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseActivity;
import com.system.androidpigbank.controllers.adapters.viewHolder.DateSectionViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.FooterViewHolder;
import com.system.androidpigbank.controllers.adapters.viewHolder.TransactionViewHolder;
import com.system.androidpigbank.architecture.viewHolders.ViewHolderAbs;
import com.system.androidpigbank.controllers.vos.DateSection;
import com.system.androidpigbank.controllers.vos.TotalFooter;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        int resId;
        if (TransactionViewType.SECTION.ordinal() == viewType) {
            resId = R.layout.item_view_holder_date_section;
        } else if (TransactionViewType.FOOTER.ordinal() == viewType) {
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

        if (getItens().get(position) instanceof Transaction) {
            return TransactionViewType.CARD.ordinal();
        } else if (getItens().get(position) instanceof DateSection) {
            return TransactionViewType.SECTION.ordinal();
        } else {
            return TransactionViewType.FOOTER.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public List<EntityAbs> getItens() {
        return itens;
    }

    protected ViewHolderAbs getViewHolderInstance(View v, int viewType) {

        if (TransactionViewType.SECTION.ordinal() == viewType) {
            return new DateSectionViewHolder(v);
        } else if (TransactionViewType.FOOTER.ordinal() == viewType) {
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

    protected List<EntityAbs> organizeItens(List<Transaction> itens) {

        List<EntityAbs> newList = new ArrayList<>();

        Double total = 0d;
        Date date = null;
        for (Transaction transaction : itens) {
            if (date == null || date.before(transaction.getDate())) {
                date = transaction.getDate();
                //newList.add(new DateSection(date));
            }

            total += transaction.getValue();
            newList.add(transaction);
        }

        TotalFooter footer = new TotalFooter();
        footer.setTotal(total);
        newList.add(footer);

        return newList;
    }

    public void removeItem(Transaction data) {
        int index = getItens().indexOf(data);
        getItens().remove(index);
        notifyItemRemoved(index);
        ((BaseActivity) activity).showMessage(R.string.message_delete_sucess);
    }

    public enum TransactionViewType {

        CARD(false), SECTION(true), FOOTER(true);

        private boolean fullSpan;

        TransactionViewType(boolean fullSpan) {
            this.fullSpan = fullSpan;
        }

        public boolean isFullSpan() {
            return fullSpan;
        }
    }
}
