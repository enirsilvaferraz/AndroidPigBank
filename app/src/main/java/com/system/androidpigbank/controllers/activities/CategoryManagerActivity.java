package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.system.androidpigbank.R;
import com.system.architecture.activities.BaseManagerActivity;
import com.system.androidpigbank.controllers.adapters.array.CategoryColorsArrayAdapter;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;
import com.system.architecture.helpers.JavaHelper;
import com.system.androidpigbank.controllers.helpers.constant.Colors;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryManagerActivity extends BaseManagerActivity<Category> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.category_manager_category)
    AutoCompleteTextView editCategory;

    @BindView(R.id.category_manager_primary)
    AppCompatCheckBox chPrimary;

    @BindView(R.id.category_manager_color)
    Spinner spColor;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        spColor = (Spinner) findViewById(R.id.category_manager_color);
        spColor.setAdapter(new CategoryColorsArrayAdapter(this, R.layout.item_view_holder_category_colors));
        spColor.setSelection(Colors.GRAY_300.ordinal());
        spColor.setSelected(true);

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

        if (getIntent() != null && getIntent().hasExtra(Constants.BUNDLE_MODEL_DEFAULT)) {

            model = getIntent().getExtras().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);

            editCategory.setText(model.getName());
            chPrimary.setChecked(model.isPrimary());

            if (model.getColor() != null) {
                spColor.setSelection(model.getColor().ordinal());
                spColor.setSelected(true);
            }
        }

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
        }

        model.setName(editCategory.getText().toString());
        model.setColor((Colors) spColor.getSelectedItem());
        model.setPrimary(chPrimary.isChecked());
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
