package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.constant.Constants;
import com.system.androidpigbank.models.business.CategoryBusiness;
import com.system.androidpigbank.models.business.TransactionBusiness;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;
import com.system.androidpigbank.models.persistences.DaoAbs;
import com.system.architecture.activities.BaseActivity;
import com.system.architecture.activities.BaseManagerDialog;
import com.system.architecture.managers.LoaderResult;
import com.system.architecture.managers.ManagerHelper;
import com.system.architecture.utils.JavaUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 18/08/16.
 */

public class TransactionManagerDialog extends BaseManagerDialog<Transaction> {

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

    @BindView(R.id.transaction_manager_bt_delete)
    Button btDelete;

    @BindView(R.id.transaction_manager_bt_save)
    Button btSave;

    private List<Category> categories;

    public static TransactionManagerDialog newInstance(Transaction transaction) {
        TransactionManagerDialog frag = new TransactionManagerDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_MODEL_DEFAULT, transaction);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_transaction_manager, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editValue.requestFocus();
        editDate.setText(JavaUtils.DateUtil.format(Calendar.getInstance().getTime()));

        final Parcelable parcelable = getArguments().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
        if (getArguments() != null && parcelable != null) {
            model = (Transaction) parcelable;

            editDate.setText(JavaUtils.DateUtil.format(model.getDate()));
            editValue.setText(String.valueOf(model.getValue()));
            editCategory.setText(model.getCategory().getName());
            editContent.setText(model.getContent());
            editCategorySecondary.setText(model.getCategorySecondary() != null ? model.getCategorySecondary().getName() : null);
            editDatePayment.setText(model.getDatePayment() != null ? JavaUtils.DateUtil.format(model.getDatePayment()) : null);

            btDelete.setVisibility(View.VISIBLE);
        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    execute(Action.SAVE);
                } catch (Exception e) {
                    ((BaseActivity) getActivity()).showMessage(e);
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    execute(Action.DELETE);
                } catch (Exception e) {
                    ((BaseActivity) getActivity()).showMessage(e);
                }
            }
        });

        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<Category>>() {

            @Override
            public List<Category> executeAction() throws Exception {
                return new CategoryBusiness(getContext()).findAll();
            }

            @Override
            public void onComplete(LoaderResult<List<Category>> data) {
                autocompleteCategory(data);
            }
        });

        super.onViewCreated(view, savedInstanceState);

    }

    private void autocompleteCategory(LoaderResult<List<Category>> data) {

        if (data.isSuccess()) {

            categories = data.getData();
            String[] categoriesArray = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoriesArray[i] = categories.get(i).getName();
            }

            editCategory.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray));
            editCategorySecondary.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray));

        } else {
            ((BaseActivity) getActivity()).showMessage(data.getException());
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
    protected DaoAbs<Transaction> getBusinessInstance() {
        return new TransactionBusiness(getContext());
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

        if (!JavaUtils.StringUtil.isEmpty(editCategorySecondary.getText().toString())) {
            final Category categorySecondary = new Category(editCategorySecondary.getText().toString());
            if (categories.contains(categorySecondary)) {
                model.setCategorySecondary(categories.get(categories.indexOf(categorySecondary)));
            } else {
                model.setCategorySecondary(categorySecondary);
            }
        } else {
            model.setCategorySecondary(null);
        }
    }


}
