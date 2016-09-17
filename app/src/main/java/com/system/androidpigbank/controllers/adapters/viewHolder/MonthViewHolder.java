package com.system.androidpigbank.controllers.adapters.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.vos.MonthVO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.adapters.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthViewHolder extends CardViewHolder {

    @BindView(R.id.item_month)
    TextView tvMonth;

    @BindView(R.id.item_total)
    TextView tvTotal;

    Context context;

    public MonthViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(final CardAdapter.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        final MonthVO item = (MonthVO) model;

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, item.getMonth());
        calendar.set(Calendar.YEAR, item.getYear());

        tvMonth.setText(JavaUtils.DateUtil.format(calendar.getTime(), JavaUtils.DateUtil.MMMM_DE_YYYY));

        if (item.getValue() != 0D) {
            tvTotal.setText(JavaUtils.NumberUtil.currencyFormat(item.getValue().toString()));
        }
    }
}