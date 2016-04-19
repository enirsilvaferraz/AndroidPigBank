package com.system.androidpigbank.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

/**
 * Created by eferraz on 18/04/16.
 */
public class FirebaseInstance {

    private static FirebaseInstance instance;

    private Firebase firebaseCategory;
    private Firebase firebaseTransaction;

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
        if (firebaseCategory == null) {
            firebaseCategory = getFirebaseInstance();
        }
        return firebaseCategory.child("database").child("category");
    }

    public Firebase getTransactionChild() {
        if (firebaseTransaction == null) {
            firebaseTransaction = getFirebaseInstance();
        }
        return firebaseTransaction.child("database").child("transaction");
    }

    @NonNull
    private Firebase getFirebaseInstance() {
        return new Firebase("https://pig-bank-dev.firebaseIO.com/");
    }
}
