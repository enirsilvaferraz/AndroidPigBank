package com.system.androidpigbank.controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.system.androidpigbank.R;
import com.system.androidpigbank.controllers.helpers.Constants;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.models.firebase.business.CategoryFirebaseBusiness;
import com.system.androidpigbank.models.firebase.business.FirebaseDaoAbs;
import com.system.androidpigbank.models.firebase.business.TransactionFirebaseBusiness;
import com.system.androidpigbank.models.sqlite.business.TransactionBusiness;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.helpers.PaymentType;
import com.system.architecture.managers.DaoAbs;
import com.system.architecture.activities.BaseActivity;
import com.system.architecture.activities.BaseManagerDialog;
import com.system.architecture.utils.JavaUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eferraz on 18/08/16.
 */

public class TransactionManagerDialog extends BaseManagerDialog<TransactionVO> {

    @BindView(R.id.transaction_manager_date_lanc)
    EditText editDateLanc;

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

    @BindView(R.id.transaction_manager_bt_cancel)
    Button btCancel;

    @BindView(R.id.transaction_manager_bt_save)
    Button btSave;

    @BindView(R.id.transaction_manager_payment_type)
    Spinner spPaymentType;

    private List<CategoryVO> categories;

    public static TransactionManagerDialog newInstance(TransactionVO transaction) {
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
        editDateLanc.setText(JavaUtils.DateUtil.format(Calendar.getInstance().getTime()));
        editDateLanc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editDatePayment.setText(editDateLanc.getText());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.payment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentType.setAdapter(adapter);
        spPaymentType.setSelection(PaymentType.ITAU_DEBIT.getId());
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                performTextChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final Parcelable parcelable = getArguments().getParcelable(Constants.BUNDLE_MODEL_DEFAULT);
        if (getArguments() != null && parcelable != null) {
            model = (TransactionVO) parcelable;

            editDateLanc.setText(JavaUtils.DateUtil.format(model.getDateTransaction()));
            editValue.setText(String.valueOf(model.getValue()));
            editCategory.setText(model.getCategory().getName());
            editContent.setText(model.getContent());
            editCategorySecondary.setText(model.getCategorySecondary() != null ? model.getCategorySecondary().getName() : null);
            if (model.getDatePayment() != null) {
                editDatePayment.setText(JavaUtils.DateUtil.format(model.getDatePayment()));
            }
            spPaymentType.setSelection(model.getPaymentType() != null ? model.getPaymentType().getId() : PaymentType.ITAU_DEBIT.getId());
        } else {
            model = new TransactionVO();
        }

//        ManagerHelper.execute((AppCompatActivity) getActivity(), new ManagerHelper.LoaderResultInterface<List<CategoryVO>>() {
//
//            @Override
//            public List<CategoryVO> executeAction() throws Exception {
//                return new CategoryBusiness(getContext()).findAll();
//            }
//
//            @Override
//            public void onComplete(LoaderResult<List<CategoryVO>> data) {
//                autocompleteCategory(data);
//            }
//        });

        new CategoryFirebaseBusiness().findAll(new FirebaseDaoAbs.FirebaseMultiReturnListener<CategoryVO>() {
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

    private void performTextChange() {
        try {

            CharSequence date = editDateLanc.getText();
            if (date.length() == JavaUtils.DateUtil.DD_MM_YYYY.length()) {

                switch (PaymentType.getEnum(spPaymentType.getSelectedItemPosition())) {

                    case ITAU_CREDIT:
                        editDatePayment.setText(JavaUtils.DateUtil.format(
                                getCredCardPaynentDate(Constants.DATE_ITAU_CARD_VENCIMENTO, Constants.DATE_ITAU_CARD_FECHAMENTO)));
                        break;
                    case NUBANK_CARD:
                        editDatePayment.setText(JavaUtils.DateUtil.format(
                                getCredCardPaynentDate(Constants.DATE_NUBANK_CARD_VENCIMENTO, Constants.DATE_NUBANK_CARD_FECHAMENTO)));
                        break;

                    default:
                        editDatePayment.setText(date);
                        break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace(); // TODO APRESENTAR MENSAGEM
        }
    }

    @NonNull
    private Date getCredCardPaynentDate(int dateVencimento, int dateFechamento) throws ParseException {
        Calendar calPayment = Calendar.getInstance();
        calPayment.setTime(JavaUtils.DateUtil.parse(editDateLanc.getText().toString()));

        if (calPayment.get(Calendar.DATE) >= dateFechamento) {
            calPayment.set(Calendar.DATE, dateVencimento); // Necessario para nao colocar 31 em fevereiro
            calPayment.add(Calendar.MONTH, 1);
        } else {
            calPayment.set(Calendar.DATE, dateVencimento);
        }

        return calPayment.getTime();
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

    private void validateFields() throws Exception {
        if (editDateLanc.getText().toString().trim().isEmpty() ||
                editValue.getText().toString().trim().isEmpty() ||
                editCategory.getText().toString().trim().isEmpty() ||
                editContent.getText().toString().trim().isEmpty() ||
                spPaymentType.getSelectedItem() == null) {
            throw new Exception("Campo obrigatório!");
        }
    }

    @Override
    protected DaoAbs<TransactionVO> getBusinessInstance() {
        return new TransactionBusiness(getContext());
    }

    @Override
    protected FirebaseDaoAbs<TransactionVO> getFirebaseBusinessInstance() {
        return new TransactionFirebaseBusiness();
    }

    @Override
    protected void prepareToPersist() throws Exception {

        validateFields();

        if (model == null) {
            model = new TransactionVO();
        }

        model.setDateTransaction(JavaUtils.DateUtil.parse(editDateLanc.getText().toString()));
        model.setValue(Double.parseDouble(editValue.getText().toString()));
        model.setContent(editContent.getText().toString());

        if (!editDatePayment.getText().toString().isEmpty()) {
            model.setDatePayment(JavaUtils.DateUtil.parse(editDatePayment.getText().toString()));
        }

        final CategoryVO category = new CategoryVO(editCategory.getText().toString());
        if (categories.contains(category)) {
            model.setCategory(categories.get(categories.indexOf(category)));
        } else {
            model.setCategory(category);
        }

        if (!JavaUtils.StringUtil.isEmpty(editCategorySecondary.getText().toString())) {
            final CategoryVO categorySecondary = new CategoryVO(editCategorySecondary.getText().toString());
            if (categories.contains(categorySecondary)) {
                model.setCategorySecondary(categories.get(categories.indexOf(categorySecondary)));
            } else {
                model.setCategorySecondary(categorySecondary);
            }
        } else {
            model.setCategorySecondary(null);
        }

        model.setPaymentType(PaymentType.getEnum(spPaymentType.getSelectedItemPosition()));
    }
}
