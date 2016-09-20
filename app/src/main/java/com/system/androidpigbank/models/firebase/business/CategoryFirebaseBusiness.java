package com.system.androidpigbank.models.firebase.business;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.TitleVO;
import com.system.androidpigbank.controllers.vos.TotalVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.androidpigbank.controllers.vos.WhiteSpaceVO;
import com.system.androidpigbank.models.firebase.dtos.CategoryDTO;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.architecture.adapters.CardAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 08/09/2016.
 */

public class CategoryFirebaseBusiness extends FirebaseDaoAbs<CategoryVO> {


    @Override
    public void update(final CategoryVO entity) {

        final TransactionFirebaseBusiness business = new TransactionFirebaseBusiness();
        business.findTransactionByCategory(entity, new FirebaseMultiReturnListener<TransactionVO>() {
            @Override
            public void onFindAll(List<TransactionVO> list) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbUpdateRef = database.getReference(BuildConfig.FLAVOR + "-database");

                Map<String, Object> map = new HashMap<>();
                map.put("Category/" + entity.getKey(), pupulateMap(entity));

                for (TransactionVO transactionVO : list) {
                    Map innerMap = business.pupulateMap(transactionVO);
                    innerMap.put("category", entity.getName());
                    map.put("Transaction/" + transactionVO.getKey(), innerMap);
                }

                dbUpdateRef.updateChildren(map);

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected Map<String, Object> pupulateMap(CategoryVO vo) {

        CategoryDTO dto = (CategoryDTO) vo.toDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("name", dto.getName());
        map.put("primary", dto.isPrimary());

        return map;
    }

    @Override
    protected Class<? extends DTOAbs> getDTOClass() {
        return CategoryDTO.class;
    }

    @NonNull
    public List<CardAdapter.CardModel> organizeCategorySummaryList(List<CategoryVO> data) {

        boolean hasTitleSecondary = false;
        List<CardAdapter.CardModel> itens = new ArrayList<>();

        for (int position = 0; position < data.size(); position++) {

            CategoryVO category = data.get(position);

            if (!hasTitleSecondary && !category.isPrimary() && position != data.size() - 1) {
                itens.add(new WhiteSpaceVO());
                itens.add(new TitleVO("Secondary Categories"));
                hasTitleSecondary = true;
            }

            itens.add(new WhiteSpaceVO());
            itens.add(category);

            if (!category.getTransactionList().isEmpty()) {
                itens.addAll(getTransactionByCategory(category.getTransactionList()));
            }
        }

        itens.add(new WhiteSpaceVO());

        return itens;
    }

    private List<CardAdapter.CardModel> getTransactionByCategory(List<TransactionVO> list) {

        Double value = 0D;

        List<CardAdapter.CardModel> itens = new ArrayList<>();
        for (int position = 0; position < list.size(); position++) {

            TransactionVO transactionAct = list.get(position);
            TransactionVO transactionProx = list.size() > position + 1 ? list.get(position + 1) : null;

            String categoryAct = transactionAct.getCategorySecondary() != null ? transactionAct.getCategorySecondary().getName() : "";
            String categoryProx = transactionProx != null && transactionProx.getCategorySecondary() != null ? transactionProx.getCategorySecondary().getName() : "";

            itens.add(transactionAct);
            value += transactionAct.getValue();

            if (transactionProx == null || !categoryAct.equals(categoryProx)) {
                itens.add(new TotalVO(null, value));
                value = 0D;
            }
        }

        return itens;
    }
}