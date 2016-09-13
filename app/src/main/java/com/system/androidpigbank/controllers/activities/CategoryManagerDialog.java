package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.sqlite.business.CategoryBusiness;
import com.system.androidpigbank.models.sqlite.entities.Category;
import com.system.architecture.activities.BaseActivity;
import com.system.architecture.activities.BaseManagerDialog;
import com.system.architecture.helpers.JavaHelper;
import com.system.architecture.managers.DaoAbs;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryManagerDialog extends BaseManagerDialog<Category> {

    @BindView(R.id.category_manager_category)
    AutoCompleteTextView editCategory;

    @BindView(R.id.category_manager_primary)
    AppCompatCheckBox chPrimary;

    @BindView(R.id.transaction_manager_bt_cancel)
    Button btCancel;

    @BindView(R.id.transaction_manager_bt_save)
    Button btSave;

    private List<Category> categories;

    public static CategoryManagerDialog newInstance(Category category) {
        CategoryManagerDialog frag = new CategoryManagerDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_MODEL_DEFAULT, category);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_manager, container);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model = new Category(((AppCompatTextView) view).getText().toString());
            }
        });

        final Parcelable parcelable = getArguments().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
        if (getArguments() != null && parcelable != null) {

            model = (Category) parcelable;

            editCategory.setText(model.getName());
            chPrimary.setChecked(model.isPrimary());
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    save();
                } catch (Exception e) {
                    ((BaseActivity) getActivity()).showMessage(e);
                }
            }
        });

        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<Category>>() {

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
        return new CategoryBusiness(getContext());
    }

    @Override
    protected void prepareToPersist() throws Exception {

        if (JavaHelper.isEmpty(editCategory.getText().toString())) {
            throw new Exception("Campo obrigatório!");
        }

        if (model == null) {
            model = new Category();
        }

        model.setName(editCategory.getText().toString());
        model.setPrimary(chPrimary.isChecked());
    }

    private void autocompleteCategory(LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

            categories = data.getData();
            String[] categoriesArray = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoriesArray[i] = categories.get(i).getName();
            }

            editCategory.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray));

        } else {
            ((BaseActivity) getActivity()).showMessage(data.getException());
        }
    }
}
