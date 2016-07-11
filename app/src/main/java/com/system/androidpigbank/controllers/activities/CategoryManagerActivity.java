package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.system.androidpigbank.R;
import com.system.androidpigbank.architecture.activities.BaseManagerActivity;
import com.system.androidpigbank.controllers.adapters.array.CategoryColorsArrayAdapter;
import com.system.androidpigbank.controllers.managers.LoaderResult;
import com.system.androidpigbank.controllers.managers.ManagerHelper;
import com.system.androidpigbank.helpers.JavaHelper;
import com.system.androidpigbank.helpers.constants.Colors;
import com.system.androidpigbank.helpers.constants.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.util.List;

public class CategoryManagerActivity extends BaseManagerActivity<Category> {

    private AutoCompleteTextView editCategory;
    private Spinner spColor;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CategoryColorsArrayAdapter adapter = new CategoryColorsArrayAdapter(this,
                R.layout.item_view_holder_category_colors);

        spColor = (Spinner) findViewById(R.id.category_manager_color);
        spColor.setAdapter(adapter);
        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editCategory = (AutoCompleteTextView) findViewById(R.id.category_manager_category);
        editCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model = new Category(((AppCompatTextView) view).getText().toString());
                if (categories.contains(model)) {
                    int itemId = ((CategoryColorsArrayAdapter) spColor.getAdapter()).getPosition(model.getColor());
                    if (itemId != -1) {
                        spColor.setSelection(itemId);
                    }
                }
            }
        });

        ManagerHelper.execute(this, new ManagerHelper.LoaderResultInterface<List<Category>>() {

            @Override
            public List<Category> executeAction() throws Exception {
                return getBusinessInstance().findAll();
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
    public View getContainer() {
        return (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    protected DaoAbs<Category> getBusinessInstance() {
        return new CategoryBusiness(this);
    }

    @Override
    protected void prepareToPersist() throws Exception {

        if (JavaHelper.isEmpty(editCategory.getText().toString()) || JavaHelper.isEmpty(spColor.getSelectedItem())) {
            throw new Exception("Campo obrigatório!");
        }

        if (model == null) {
            model = new Category();
            model.setName(editCategory.getText().toString());
        }

        model.setColor((Colors) spColor.getSelectedItem());
    }

    private void autocompleteCategory(LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

            categories = data.getData();
            String[] categoriesArray = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoriesArray[i] = categories.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoriesArray);
            editCategory.setAdapter(adapter);

        } else {
            showMessage(data.getException());
        }
    }
}
