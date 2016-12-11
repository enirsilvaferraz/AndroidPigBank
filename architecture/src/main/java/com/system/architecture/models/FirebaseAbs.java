package com.system.architecture.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.system.architecture.utils.JavaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enir on 07/09/2016.
 */

public abstract class FirebaseAbs<T extends VOAbs> {

    private String flavor;

    public FirebaseAbs(String flavor) {
        this.flavor = flavor;
    }

    public static void enableOffline(String flavor) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference(flavor + "-database/").keepSynced(true);
    }

    public void save(T entity, FirebaseSingleReturnListener<T> listener) {

        if (JavaUtils.StringUtil.isEmpty(entity.getKey())) {
            DatabaseReference push = getDatabaseReference().push();
            entity.setKey(push.getKey());
            push.setValue(entity.toDTO());
        } else {
            DatabaseReference dbUpdateRef = getDatabaseReference();
            Map<String, Object> map = new HashMap<>();
            map.put(entity.getKey(), pupulateMap(entity));
            dbUpdateRef.updateChildren(map);
        }

        listener.onFind(entity);
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
                    T tInstance = getTInstance(postSnapshot);
                    tInstance.setKey(postSnapshot.getKey());
                    list.add(tInstance);
                }
                firebaseMultiReturnListener.onFindAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseMultiReturnListener.onError(databaseError.getMessage());
            }
        });
    }

    public void delete(final T entity, final FirebaseSingleReturnListener listener) {
        getDatabaseReference().child(entity.getKey()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                listener.onFind(entity);
            }
        });
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
        return database.getReference(flavor + "-database/" + simpleName);
    }

    public interface FirebaseMultiReturnListener<T extends VOAbs> {
        void onFindAll(List<T> list);

        void onError(String error);
    }

    public interface FirebaseSingleReturnListener<T extends VOAbs> {
        void onFind(T list);

        void onError(String error);
    }
}
