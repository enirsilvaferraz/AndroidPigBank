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
import com.system.androidpigbank.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.FixedTransactionBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.FixedTransaction;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixedTransactionManagerActivity extends BaseManagerActivity<FixedTransaction> {

    @BindView(R.id.fixed_transaction_manager_container)
    View container;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fixed_transaction_manager_value)
    EditText editValue;

    @BindView(R.id.fixed_transaction_manager_category)
    AutoCompleteTextView editCategory;

    @BindView(R.id.fixed_transaction_manager_category_secondary)
    AutoCompleteTextView editCategorySecondary;

    @BindView(R.id.fixed_transaction_manager_content)
    EditText editContent;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_transaction_manager);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        editValue.requestFocus();

        if (getIntent() != null && getIntent().hasExtra(Constants.BUNDLE_MODEL_DEFAULT)) {
            model = getIntent().getExtras().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
            if (model != null) {
                editValue.setText(String.valueOf(model.getValue()));
                editCategory.setText(model.getCategory().getName());
                editContent.setText(model.getContent());
                editCategorySecondary.setText(model.getCategorySecondary() != null ? model.getCategorySecondary().getName() : null);
            }
        }

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<Category>>() {

            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(FixedTransactionManagerActivity.this).findAll();
            }

            @Override
            public int loaderId() {
                return Constants.LOADER_DEFAULT_ID;
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                autocompleteCategory(data);
            }
        });
    }

    @Override
    protected DaoAbs<FixedTransaction> getBusinessInstance() {
        return new FixedTransactionBusiness(this);
    }

    @Override
    protected void prepareToPersist() throws Exception {

        validateFields();

        if (model == null) {
            model = new FixedTransaction();
        }

        model.setValue(Double.parseDouble(editValue.getText().toString()));
        model.setContent(editContent.getText().toString());

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

            editCategory.setAdapter(new ArrayAdapter<>(FixedTransactionManagerActivity.this, android.R.layout.simple_list_item_1, categoriesArray));
            editCategorySecondary.setAdapter(new ArrayAdapter<>(FixedTransactionManagerActivity.this, android.R.layout.simple_list_item_1, categoriesArray));

        } else {
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void validateFields() throws Exception {
        if (editCategory.getText().toString().trim().isEmpty() ||
                editContent.getText().toString().trim().isEmpty()) {
            throw new Exception("Campo obrigatório!");
        }
    }
}
