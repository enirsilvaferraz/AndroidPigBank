package com.system.androidpigbank.controllers.vos;

import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 29/07/16.
 */

public class HomeObject {

    private List<Category> listCategorySummary;
    private List<Transaction> listTransaction;
    private List<Month> listMonth;

    public HomeObject (){
        listCategorySummary = new ArrayList<>();
        listTransaction = new ArrayList<>();
        listMonth = new ArrayList<>();
    }

    public List<Category> getListCategorySummary() {
        return listCategorySummary;
    }

    public void setListCategorySummary(List<Category> listCategorySummary) {
        this.listCategorySummary = listCategorySummary;
    }

    public List<Transaction> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<Transaction> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public List<Month> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<Month> listMonth) {
        this.listMonth = listMonth;
    }
}
