package com.system.androidpigbank.models.firebase.business;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.models.firebase.dtos.DTOAbs;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 07/09/2016.
 */

public abstract class FirebaseDaoAbs<T extends EntityAbs> {

    public static void enableOffline() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public T save(T entity) {

        if (JavaUtils.StringUtil.isEmpty(entity.getKey())) {
            getDatabaseReference().push().setValue(entity.toDTO());
        } else {
            update(entity);
        }

        return entity;
    }

    protected void update(T entity) {
        DatabaseReference dbUpdateRef = getDatabaseReference();
        Map<String, Object> map = new HashMap<>();
        map.put(entity.getKey(), pupulateMap(entity));
        dbUpdateRef.updateChildren(map);
    }

    protected abstract Map<String, Object> pupulateMap(T vo);

    public void findByKey(String key, final FirebaseSingleReturnListener<T> firebaseSingleReturnListener) {

        Query reference = getDatabaseReference().child(key).orderByKey();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseSingleReturnListener.onFind(getTInstance(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseSingleReturnListener.onError(databaseError.getMessage());
            }
        });
    }

    public void findAll(final FirebaseMultiReturnListener<T> firebaseMultiReturnListener) {

        Query reference = getDatabaseReference().orderByKey();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<T>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(getTInstance(postSnapshot));
                }
                firebaseMultiReturnListener.onFindAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseMultiReturnListener.onError(databaseError.getMessage());
            }
        });
    }

    public T delete(T entity) {
        throw new RuntimeException("Not implemented yet!");
    }

    public T getTInstance(DataSnapshot postSnapshot) {
        T entity = postSnapshot.getValue(getDTOClass()).toEntity();
        entity.setKey(postSnapshot.getKey());
        return entity;
    }

    protected abstract Class<? extends DTOAbs> getDTOClass();

    public DatabaseReference getDatabaseReference() {
        String simpleName = getClass().getSimpleName().replace("FirebaseBusiness", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(BuildConfig.FLAVOR + "-database/" + simpleName);
    }

    public interface FirebaseMultiReturnListener<T extends EntityAbs> {
        void onFindAll(List<T> list);

        void onError(String error);
    }

    public interface FirebaseSingleReturnListener<T extends EntityAbs> {
        void onFind(T list);

        void onError(String error);
    }
}
