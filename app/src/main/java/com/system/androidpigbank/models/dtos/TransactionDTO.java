package com.system.androidpigbank.models.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.architecture.utils.JavaUtils;

import java.util.Date;

/**
 * Created by Enir on 07/09/2016.
 */

public class TransactionDTO extends DTOAbs{

    @Expose
    private String key;

    @Expose
    private String dateTransaction;

    @Expose
    private String datePayment;

    @Expose
    private Double value;

    @Expose
    private String category;

    @Expose
    private String categorySecondary;

    @Expose
    private String content;

    @Expose
    private String paymentType;

    public String getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(String dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(String datePayment) {
        this.datePayment = datePayment;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategorySecondary() {
        return categorySecondary;
    }

    public void setCategorySecondary(String categorySecondary) {
        this.categorySecondary = categorySecondary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public EntityAbs toEntity() {
        return JavaUtils.GsonUtil.getInstance().fromTransaction().toEntity(this, Transaction.class);
    }
}
