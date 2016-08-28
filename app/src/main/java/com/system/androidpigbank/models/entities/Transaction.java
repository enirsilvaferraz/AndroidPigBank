package com.system.androidpigbank.models.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.R;

import java.util.Date;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "transaction")
public class Transaction extends EntityAbs implements Parcelable {

    public enum PaymentType {

        MONEY(R.drawable.ic_attach_money_green),
        DIRECT_DEBIT(R.drawable.ic_payment_orange),
        NUBANK_CARD(R.drawable.ic_payment_purple),
        ITAU_CARD(R.drawable.ic_payment_blue);

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

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;
    @DatabaseField
    private Date date;
    @DatabaseField
    private Date datePayment;
    @DatabaseField
    private Double value;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category categorySecondary;
    @DatabaseField
    private String content;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private PaymentType paymentType;

    private boolean expanded;

    public Transaction() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public Category getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(Category categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeLong(this.datePayment != null ? this.datePayment.getTime() : -1);
        dest.writeValue(this.value);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.categorySecondary, flags);
        dest.writeString(this.content);
        dest.writeInt(this.paymentType == null ? -1 : this.paymentType.ordinal());
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
    }

    protected Transaction(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        long tmpDatePayment = in.readLong();
        this.datePayment = tmpDatePayment == -1 ? null : new Date(tmpDatePayment);
        this.value = (Double) in.readValue(Double.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.categorySecondary = in.readParcelable(Category.class.getClassLoader());
        this.content = in.readString();
        int tmpPaymentType = in.readInt();
        this.paymentType = tmpPaymentType == -1 ? null : PaymentType.values()[tmpPaymentType];
        this.expanded = in.readByte() != 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
