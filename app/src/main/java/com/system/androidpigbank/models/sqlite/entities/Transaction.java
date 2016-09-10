package com.system.androidpigbank.models.sqlite.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.TransactionDTO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.utils.JavaUtils;

import java.util.Date;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "transaction")
@IgnoreExtraProperties
public class Transaction extends EntityAbs implements Parcelable, CardAdapter.CardModel {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;

    @Expose
    @DatabaseField
    private Date dateTransaction;

    @Expose
    @DatabaseField
    private Date datePayment;

    @Expose
    @DatabaseField
    private Double value;

    @Expose
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;

    @Expose
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category categorySecondary;

    @Expose
    @DatabaseField
    private String content;

    @Expose
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private PaymentType paymentType;

    private boolean expanded;

    public Transaction() {
    }

    public Date getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Date dateTransaction) {
        this.dateTransaction = dateTransaction;
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

    @Override
    public DTOAbs toDTO() {
        return JavaUtils.GsonUtil.getInstance().fromTransaction().toDTO(this, TransactionDTO.class);
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
        dest.writeLong(this.dateTransaction != null ? this.dateTransaction.getTime() : -1);
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
        this.dateTransaction = tmpDate == -1 ? null : new Date(tmpDate);
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

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_TRANSACTION;
    }
}
