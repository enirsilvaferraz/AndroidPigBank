package com.system.androidpigbank.controllers.adapters.recyclerv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.architecture.utils.JavaUtils;
import com.system.androidpigbank.controllers.vos.Month;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Enir on 22/07/2016.
 */
public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClicked onItemClicked;

    private List<Month> itens;

    public MonthAdapter() {
        this.itens = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_view_holder_month, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(itens.get(position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void addItens(List<Month> itens) {
        this.itens.clear();
        this.itens.addAll(itens);
        notifyDataSetChanged();
    }

    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public interface OnItemClicked {
        void onClick(Date date);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_month)
        TextView tvMonth;

        @BindView(R.id.item_total)
        TextView tvTotal;

        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        private void bind(Month item) {

            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, item.getMonth());
            calendar.set(Calendar.YEAR, item.getYear());

            tvMonth.setText(JavaUtils.DateUtil.format(calendar.getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));
            tvTotal.setText(JavaUtils.NumberUtil.currencyFormat(item.getValue().toString()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClicked != null) {
                        onItemClicked.onClick(calendar.getTime());
                    }
                }
            });
        }
    }
}
