package com.system.androidpigbank.models.business;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.google.gson.Gson;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.helpers.JavaHelper;
import com.system.androidpigbank.models.entities.Category;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class RecoverService extends IntentService {

    public RecoverService() {
        super(RecoverService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        new RecoverImpl(new CategoryBusiness(this)).recoverData();
        new RecoverImpl(new TransactionBusiness(this)).recoverData();

    }

    private class RecoverImpl {

        private final String FILE_NAME;
        private final String DIR_NAME;

        private final DaoAbs business;

        public RecoverImpl(DaoAbs business) {
            this.business = business;
            FILE_NAME = business.getClass().getSimpleName().replace("Business", "") + ".txt";
            DIR_NAME = Environment.getExternalStorageDirectory().toString() + "/Unidade/AndroidPigBank/" + BuildConfig.FLAVOR;
        }

        public void recoverData() {

            try {

                File dir = new File(DIR_NAME);
                File file = new File(dir, FILE_NAME);

                if (!file.exists()) {
                    return;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));

                String line;
                while ((line = br.readLine()) != null) {
                    business.save((EntityAbs) new Gson().fromJson(line, JavaHelper.getTClass(business)));
                }

                br.close();

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
