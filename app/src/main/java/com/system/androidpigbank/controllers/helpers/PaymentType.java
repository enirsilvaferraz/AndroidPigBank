package com.system.androidpigbank.controllers.helpers;

import android.support.annotation.DrawableRes;

import com.system.androidpigbank.R;

public enum PaymentType {

        MONEY(R.drawable.ic_attach_money_green),
        ITAU_DEBIT(R.drawable.ic_payment_orange),
        NUBANK_CARD(R.drawable.ic_payment_purple),
        ITAU_CREDIT(R.drawable.ic_payment_blue),
        ITAU_TRANSFER(R.drawable.ic_swap_horiz_orange),
        HSBC_TRANSFER(R.drawable.ic_swap_horiz_red),
        ALELO_CARD(R.drawable.ic_payment_yellow);

        private int resId;

        PaymentType(@DrawableRes int resId) {
            this.resId = resId;
        }

        public int getId() {
            return ordinal();
        }

        public static PaymentType getEnum(int position) {
            return PaymentType.values()[position];
        }

        public int getResId() {
            return resId;
        }
    }