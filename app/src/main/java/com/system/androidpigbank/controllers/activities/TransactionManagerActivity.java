package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseManagerActivity;
import com.system.androidpigbank.architecture.managers.LoaderResult;
import com.system.androidpigbank.architecture.managers.ManagerHelper;
import com.system.androidpigbank.architecture.utils.JavaUtils;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionManagerActivity extends BaseManagerActivity<Transaction> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.transaction_manager_date)
    EditText editDate;

    @BindView(R.id.transaction_manager_date_payment)
    EditText editDatePayment;

    @BindView(R.id.transaction_manager_value)
    EditText editValue;

    @BindView(R.id.transaction_manager_category)
    AutoCompleteTextView editCategory;

    @BindView(R.id.transaction_manager_category_secondary)
    AutoCompleteTextView editCategorySecondary;

    @BindView(R.id.transaction_manager_content)
    EditText editContent;

    @BindView(R.id.transaction_manager_container)
    View container;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_manager);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        editValue.requestFocus();

        editDate.setText(JavaUtils.DateUtil.format(Calendar.getInstance().getTime()));

        if (getIntent() != null && getIntent().hasExtra(Constants.BUNDLE_MODEL_DEFAULT)) {
            model = getIntent().getExtras().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
            if (model != null) {
                editDate.setText(JavaUtils.DateUtil.format(model.getDate()));
                editValue.setText(String.valueOf(model.getValue()));
                editCategory.setText(model.getCategory().getName());
                editContent.setText(model.getContent());
                editCategorySecondary.setText(model.getCategorySecondary() != null ? model.getCategorySecondary().getName() : null);
                editDatePayment.setText(model.getDatePayment() != null ? JavaUtils.DateUtil.format(model.getDatePayment()) : null);
            }
        }

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<Category>>() {

            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(TransactionManagerActivity.this).findAll();
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                autocompleteCategory(data);
            }
        });
    }

    @Override
    protected DaoAbs<Transaction> getBusinessInstance() {
        return new TransactionBusiness(this);
    }

    @Override
    protected void prepareToPersist() throws Exception {

        validateFields();

        if (model == null) {
            model = new Transaction();
        }

        model.setDate(JavaUtils.DateUtil.parse(editDate.getText().toString()));
        model.setValue(Double.parseDouble(editValue.getText().toString()));
        model.setContent(editContent.getText().toString());

        if (!editDatePayment.getText().toString().isEmpty()) {
            model.setDatePayment(JavaUtils.DateUtil.parse(editDatePayment.getText().toString()));
        }

        final Category category = new Category(editCategory.getText().toString());
        if (categories.contains(category)) {
            model.setCategory(categories.get(categories.indexOf(category)));
        } else {
            model.setCategory(category);
        }

        final Category categorySecondary = new Category(editCategorySecondary.getText().toString());
        if (categories.contains(categorySecondary)) {
            model.setCategorySecondary(categories.get(categories.indexOf(categorySecondary)));
        } else {
            model.setCategorySecondary(categorySecondary);
        }
    }

    private void autocompleteCategory(LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

            categories = data.getData();
            String[] categoriesArray = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoriesArray[i] = categories.get(i).getName();
            }

            editCategory.setAdapter(new ArrayAdapter<>(TransactionManagerActivity.this, android.R.layout.simple_list_item_1, categoriesArray));
            editCategorySecondary.setAdapter(new ArrayAdapter<>(TransactionManagerActivity.this, android.R.layout.simple_list_item_1, categoriesArray));

        } else {
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void validateFields() throws Exception {
        if (editDate.getText().toString().trim().isEmpty() ||
                editValue.getText().toString().trim().isEmpty() ||
                editCategory.getText().toString().trim().isEmpty() ||
                editContent.getText().toString().trim().isEmpty()) {
            throw new Exception("Campo obrigatório!");
        }
    }
}
