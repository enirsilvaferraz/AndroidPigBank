package com.system.androidpigbank.models.sqlite.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.models.sqlite.entities.EntityAbs;
import com.system.androidpigbank.models.sqlite.persistences.DaoAbs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BackupService extends IntentService {

    public BackupService() {
        super(BackupService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            new BackupImpl(new TransactionBusiness(this)).backupData();
            new BackupImpl(new CategoryBusiness(this)).backupData();

            new BackupFirebaseImpl(new TransactionBusiness(this)).backupData();
            new BackupFirebaseImpl(new CategoryBusiness(this)).backupData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class BackupImpl {

        private final String FILE_NAME;
        private final String DIR_NAME;

        private final DaoAbs business;

        public BackupImpl(DaoAbs business) {
            this.business = business;
            FILE_NAME = business.getClass().getSimpleName().replace("Business", "") + ".txt";
            DIR_NAME = Environment.getExternalStorageDirectory().toString() + File.separator + "Unidade" + File.separator + "AndroidPigBank" + File.separator + BuildConfig.FLAVOR + File.separator;
        }

        void backupData() throws SQLException {

            try {

                List<EntityAbs> list = business.findAll();

                StringBuilder sb = new StringBuilder();

                File dir = new File(DIR_NAME);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        return;
                    }
                }

                File file = new File(dir, FILE_NAME);

                FileOutputStream fos = new FileOutputStream(file);

                sb.append(new Gson().toJson(list));

                fos.write(sb.toString().getBytes());
                fos.close();

            } catch (IOException e) {
                Crashlytics.logException(e);
            }
        }
    }

    private class BackupFirebaseImpl {

        private DaoAbs<EntityAbs> business;

        BackupFirebaseImpl(DaoAbs business) {
            this.business = business;
        }

        void backupData() {

            try {

                List<EntityAbs> list = business.findAll();
                DatabaseReference myRef = getDatabaseReference(business);

                myRef.removeValue();

                for (EntityAbs entityAbs : list) {
                    myRef.push().setValue(entityAbs.toDTO());
                }

            } catch (SQLException e) {
                Crashlytics.logException(e);
            }
        }

        public DatabaseReference getDatabaseReference(DaoAbs<EntityAbs> business) {
            String simpleName = business.getClass().getSimpleName().replace("Business", "");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            return database.getReference(BuildConfig.FLAVOR + "-database/" + simpleName);
        }
    }
}
