package com.system.androidpigbank.models.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.google.gson.Gson;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class BackupImpl {

        final private DaoAbs business;

        public BackupImpl(DaoAbs business) {
            this.business = business;
        }

        public void backupData() throws SQLException {

            try {

                StringBuilder sb = new StringBuilder();

                File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Unidade/AndroidPigBank/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, business.getClass().getSimpleName().replace("business", "") + ".txt");

                FileOutputStream fos = new FileOutputStream(file);

                List<EntityAbs> list = business.findAll();
                for (EntityAbs entity : list) {
                    sb.append(new Gson().toJson(entity)).append("\n");
                }

                fos.write(sb.toString().getBytes());
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
