package com.system.androidpigbank.controllers.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.table.DatabaseTable;
import com.system.androidpigbank.controllers.helpers.PaymentType;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.models.DTOAbs;
import com.system.androidpigbank.models.firebase.dtos.TransactionDTO;
import com.system.architecture.adapters.CardAdapter;
import com.system.architecture.models.VOAbs;

import java.util.Date;

/**
 * Created by eferraz on 05/12/15.
 */
@DatabaseTable(tableName = "transaction")
@IgnoreExtraProperties
public class TransactionVO extends VOAbs implements VOIf, Parcelable, CardAdapter.CardModel, Cloneable {

    public static final Creator<TransactionVO> CREATOR = new Creator<TransactionVO>() {
        @Override
        public TransactionVO createFromParcel(Parcel source) {
            return new TransactionVO(source);
        }

        @Override
        public TransactionVO[] newArray(int size) {
            return new TransactionVO[size];
        }
    };
    @Expose
    private String key;
    @Expose
    private Date dateTransaction;
    @Expose
    private Date datePayment;
    @Expose
    private Double value;
    @Expose
    private CategoryVO category;
    @Expose
    private CategoryVO categorySecondary;
    @Expose
    private String content;
    @Expose
    private PaymentType paymentType;

    public TransactionVO() {
    }

    protected TransactionVO(Parcel in) {
        this.key = in.readString();
        long tmpDateTransaction = in.readLong();
        this.dateTransaction = tmpDateTransaction == -1 ? null : new Date(tmpDateTransaction);
        long tmpDatePayment = in.readLong();
        this.datePayment = tmpDatePayment == -1 ? null : new Date(tmpDatePayment);
        this.value = (Double) in.readValue(Double.class.getClassLoader());
        this.category = in.readParcelable(CategoryVO.class.getClassLoader());
        this.categorySecondary = in.readParcelable(CategoryVO.class.getClassLoader());
        this.content = in.readString();
        int tmpPaymentType = in.readInt();
        this.paymentType = tmpPaymentType == -1 ? null : PaymentType.values()[tmpPaymentType];
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

    public CategoryVO getCategory() {
        return category;
    }

    public void setCategory(CategoryVO category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionVO that = (TransactionVO) o;

        return key != null ? key.equals(that.key) : that.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String getKey() {

        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public DTOAbs toDTO() {
        return GsonUtil.getInstance().fromTransaction().toDTO(this, TransactionDTO.class);
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public CategoryVO getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(CategoryVO categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public CardAdapter.CardViewType getViewType() {
        return CardAdapter.CardViewType.CARD_TRANSACTION;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeLong(this.dateTransaction != null ? this.dateTransaction.getTime() : -1);
        dest.writeLong(this.datePayment != null ? this.datePayment.getTime() : -1);
        dest.writeValue(this.value);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.categorySecondary, flags);
        dest.writeString(this.content);
        dest.writeInt(this.paymentType == null ? -1 : this.paymentType.ordinal());
    }
}
