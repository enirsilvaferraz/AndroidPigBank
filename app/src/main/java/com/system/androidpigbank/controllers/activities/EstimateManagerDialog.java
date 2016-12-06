package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.helpers.Quinzena;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.EstimateVO;
import com.system.androidpigbank.models.firebase.business.CategoryFirebaseBusiness;
import com.system.androidpigbank.models.firebase.business.EstimateFirebaseBusiness;
import com.system.architecture.activities.BaseActivity;
import com.system.architecture.models.FirebaseAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EstimateManagerDialog extends BaseManagerDialog<EstimateVO> {

    @BindView(R.id.estimate_manager_bt_cancel)
    Button btCancel;

    @BindView(R.id.estimate_manager_bt_save)
    Button btSave;

    @BindView(R.id.estimate_manager_category)
    AutoCompleteTextView editCategory;

    @BindView(R.id.estimate_manager_category_secondary)
    AutoCompleteTextView editCategorySecondary;

    @BindView(R.id.estimate_manager_date_lanc)
    EditText editDay;

    @BindView(R.id.estimate_manager_value)
    EditText editValue;

    @BindView(R.id.estimate_manager_quinzena)
    EditText editQuinzena;

    private List<CategoryVO> categories;

    public static EstimateManagerDialog newInstance(EstimateVO model) {
        EstimateManagerDialog frag = new EstimateManagerDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BUNDLE_MODEL_DEFAULT, model);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_estimate_manager, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editValue.requestFocus();

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

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final Parcelable parcelable = getArguments().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
        if (getArguments() != null && parcelable != null) {
            model = (EstimateVO) parcelable;

            try {
                model.setOld((EstimateVO) model.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }

            editDay.setText(model.getDay() != null ? model.getDay().toString() : "");
            editQuinzena.setText(String.valueOf(model.getQuinzena().getId()));
            editValue.setText(String.valueOf(model.getPlannedValue()));
            editCategory.setText(model.getCategory().getName());
            editCategorySecondary.setText(model.getCategorySecondary() != null ? model.getCategorySecondary().getName() : null);

        } else {
            model = new EstimateVO();
        }

        editDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    Integer day = Integer.valueOf(charSequence.toString());
                    Integer dayAux = (day < 20 ? Quinzena.PRIMEIRA.getId() : Quinzena.SEGUNDA.getId());
                    editQuinzena.setText(dayAux.toString());
                } else {
                    editQuinzena.setText(String.valueOf(Quinzena.PRIMEIRA.getId()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        new CategoryFirebaseBusiness().findAll(new FirebaseAbs.FirebaseMultiReturnListener<CategoryVO>() {
            @Override
            public void onFindAll(List<CategoryVO> list) {
                autocompleteCategory(list);
            }

            @Override
            public void onError(String error) {
                ((BaseActivity) getActivity()).showMessage(error);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected FirebaseAbs<EstimateVO> getFirebaseBusinessInstance() {
        return new EstimateFirebaseBusiness();
    }

    @Override
    protected void prepareToPersist() throws Exception {

        if (TextUtils.isEmpty(editCategory.getText().toString())
                //|| TextUtils.isEmpty(editCategorySecondary.getText().toString())
                || TextUtils.isEmpty(editQuinzena.getText().toString())
                || TextUtils.isEmpty(editValue.getText().toString())) {
            throw new Exception("Campo obrigatório!");
        }

        if (model == null) {
            model = new EstimateVO();
            model.setPlannedValue(0D);
        }

        model.setDay(!TextUtils.isEmpty(editDay.getText().toString()) ? Integer.valueOf(editDay.getText().toString()) : null);
        model.setPlannedValue(Double.parseDouble(editValue.getText().toString()));

        model.setQuinzena(Quinzena.getEnum(Integer.valueOf(editQuinzena.getText().toString())));

        for (CategoryVO category : categories) {
            if (category.getName().equals(editCategory.getText().toString())) {
                model.setCategory(category);
                break;
            }
        }
        if (model.getCategory() == null) {
            model.setCategory(new CategoryVO(editCategory.getText().toString()));
        }

        if (!JavaUtils.StringUtil.isEmpty(editCategorySecondary.getText().toString())) {
            for (CategoryVO category : categories) {
                if (category.getName().equals(editCategorySecondary.getText().toString())) {
                    model.setCategorySecondary(category);
                    break;
                }
            }
            if (model.getCategorySecondary() == null) {
                model.setCategorySecondary(new CategoryVO(editCategorySecondary.getText().toString()));
            }
        } else {
            model.setCategorySecondary(null);
        }
    }

    private void autocompleteCategory(List<CategoryVO> data) {

        categories = data;
        String[] categoriesArray = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoriesArray[i] = categories.get(i).getName();
        }

        editCategory.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray));
        editCategorySecondary.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray));
    }
}