package com.system.androidpigbank.controllers.adapters.recyclerv.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.Colors;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.views.RoundedTextView;
import com.system.architecture.activities.CardAdapterAbs;
import com.system.architecture.activities.CardViewHolder;
import com.system.architecture.utils.JavaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstimateViewHolder extends CardViewHolder {

    @BindView(R.id.item_transaction_category)
    TextView txtCategory;

    @BindView(R.id.item_transaction_rounded_view)
    RoundedTextView roudedDate;

    @BindView(R.id.item_transaction_planned_value)
    TextView txtPlannedValue;

    @BindView(R.id.item_transaction_spent_value)
    TextView txtSpentValue;

    @BindView(R.id.item_transaction_saved_value)
    TextView txtSavedValue;

    @BindView(R.id.item_transaction_percent_spent_value)
    TextView txtPercentSpentValue;

    private Context context;

    public EstimateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @Override
    public void bind(final CardAdapterAbs.CardModel model, final OnClickListener onClickListener) {
        super.bind(model, onClickListener);

        final EstimateVO item = (EstimateVO) model;

        txtCategory.setText(item.getCategory().getName() + " / " + item.getCategorySecondary().getName());
        txtSavedValue.setText(JavaUtils.NumberUtil.currencyFormat(item.getSavedValue()));
        txtPlannedValue.setText(JavaUtils.NumberUtil.currencyFormat(item.getPlannedValue()));
        txtSpentValue.setText(JavaUtils.NumberUtil.currencyFormat(item.getSpentValue()));
        txtPercentSpentValue.setText(JavaUtils.NumberUtil.percentFormat(item.getPercentualVelue()));

        if (item.getDay() != null) {
            roudedDate.setTextView(item.getDay().toString());
        }
        roudedDate.setTextColor(Colors.BLUE);

        if (item.getSavedValue() > 0) {
            txtSavedValue.setTextColor(context.getColor(R.color.material_green));
        } else if (item.getSavedValue() < 0) {
            txtSavedValue.setTextColor(context.getColor(R.color.material_red));
        } else {
            txtSavedValue.setTextColor(context.getColor(R.color.material_gray));
        }
    }
}