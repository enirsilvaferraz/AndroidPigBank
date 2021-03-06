package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.managers.CategoryManager;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.TransactionManager;
import com.system.androidpigbank.helpers.Constants;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionManagerActivity extends BaseActivity<Transaction> {

    private View container;
    private EditText editDate;
    private EditText editValue;
    private AutoCompleteTextView editCategory;
    private EditText editContent;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = findViewById(R.id.transaction_manager_container);
        editDate = (EditText) findViewById(R.id.transaction_manager_date);
        editValue = (EditText) findViewById(R.id.transaction_manager_value);
        editCategory = (AutoCompleteTextView) findViewById(R.id.transaction_manager_category);
        editContent = (EditText) findViewById(R.id.transaction_manager_content);

        getSupportLoaderManager().initLoader(Constants.LOADER_CATEGORY, null, new LoaderManager.LoaderCallbacks<LoaderResult<List<Category>>>() {

            @Override
            public Loader<LoaderResult<List<Category>>> onCreateLoader(int id, Bundle args) {
                return new CategoryManager(TransactionManagerActivity.this).findAll();
            }

            @Override
            public void onLoadFinished(Loader<LoaderResult<List<Category>>> loader, LoaderResult<List<Category>> data) {

                if (data.isSuccess()) {

                    categories = data.getData();
                    String[] categoriesArray = new String[categories.size()];
                    for (int i = 0; i < categories.size(); i++) {
                        categoriesArray[i] = categories.get(i).getName();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TransactionManagerActivity.this,
                            android.R.layout.simple_list_item_1, categoriesArray);
                    editCategory.setAdapter(adapter);

                } else {
                    Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<LoaderResult<List<Category>>> loader) {

            }
        });
    }

    public void save() {

        try {

            validateFields();

            final Transaction model = new Transaction();
            model.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(editDate.getText().toString()));
            model.setValue(Double.parseDouble(editValue.getText().toString()));
            model.setContent(editContent.getText().toString());

            final Category category = new Category(editCategory.getText().toString());
            if (categories.contains(category)) {
                model.setCategory(categories.get(categories.indexOf(category)));
            }else{
                model.setCategory(category);
            }

            Bundle args = new Bundle();
            args.putParcelable(Constants.BUNDLE_TRANSACTION, model);
            getSupportLoaderManager().initLoader(Constants.LOADER_TRANSACTION, args, this);

        } catch (Exception e) {
            Snackbar.make(container, e.getMessage(), Snackbar.LENGTH_LONG).show();
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

    @Override
    public Loader<LoaderResult<Transaction>> onCreateLoader(int id, Bundle args) {
        Transaction model = args.getParcelable(Constants.BUNDLE_TRANSACTION);
        return new TransactionManager(this).save(model);
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<Transaction>> loader, LoaderResult<Transaction> data) {
        if (data.isSuccess()) {
            Snackbar.make(container, "Saved!", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(container, data.getException().getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<Transaction>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_transaction_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.transaction_manager_act_done:
                save();
                return true;
        }

        return false;
    }
}
