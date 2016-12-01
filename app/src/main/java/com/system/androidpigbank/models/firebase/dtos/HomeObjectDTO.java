package com.system.androidpigbank.models.firebase.dtos;

import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.models.firebase.serializers.GsonUtil;
import com.system.architecture.models.VOAbs;
import com.system.architecture.models.DTOAbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eferraz on 29/07/16.
 */

public class HomeObjectDTO extends DTOAbs {

    private int month;
    private int year;
    private List<CategoryDTO> listCategorySummary;
    private List<TransactionDTO> listTransaction;
    private List<MonthDTO> listMonth;

    public HomeObjectDTO() {
        listCategorySummary = new ArrayList<>();
        listTransaction = new ArrayList<>();
        listMonth = new ArrayList<>();
    }

    @Override
    public VOAbs toEntity() {
        return GsonUtil.getInstance().fromHomeObject().toEntity(this, CategoryVO.class);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<CategoryDTO> getListCategorySummary() {
        return listCategorySummary;
    }

    public void setListCategorySummary(List<CategoryDTO> listCategorySummary) {
        this.listCategorySummary = listCategorySummary;
    }

    public List<TransactionDTO> getListTransaction() {
        return listTransaction;
    }

    public void setListTransaction(List<TransactionDTO> listTransaction) {
        this.listTransaction = listTransaction;
    }

    public List<MonthDTO> getListMonth() {
        return listMonth;
    }

    public void setListMonth(List<MonthDTO> listMonth) {
        this.listMonth = listMonth;
    }
}
