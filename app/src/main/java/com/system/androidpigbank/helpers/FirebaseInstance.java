package com.system.androidpigbank.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.Transaction;

/**
 * Created by eferraz on 18/04/16.
 */
public class FirebaseInstance {

    private static FirebaseInstance instance;

    //private Firebase firebaseCategory;
    private Firebase firebase;

    private FirebaseInstance(Context context) {
        Firebase.setAndroidContext(context);
    }

    public static FirebaseInstance getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseInstance(context);
        }
        return instance;
    }

    public Firebase getCategoryChild() {
        if (firebase == null) {
            firebase = getFirebaseInstance();
        }
        return firebase.child(BuildConfig.FLAVOR).child("database").child(Category.class.getName().replace(".", "_"));
    }

    public Firebase getTransactionChild() {
        if (firebase == null) {
            firebase = getFirebaseInstance();
        }
        return firebase.child(BuildConfig.FLAVOR).child("database").child(Transaction.class.getName().replace(".", "_"));
    }

    @NonNull
    private Firebase getFirebaseInstance() {
        return new Firebase("https://pig-bank-dev.firebaseIO.com/");
    }
}
