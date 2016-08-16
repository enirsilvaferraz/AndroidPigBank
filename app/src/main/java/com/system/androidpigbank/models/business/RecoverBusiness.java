package com.system.androidpigbank.models.business;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.architecture.helpers.JavaHelper;
import com.system.androidpigbank.models.entities.EntityAbs;
import com.system.androidpigbank.models.persistences.DaoAbs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class RecoverBusiness {

    private static RecoverBusiness instance;

    public static RecoverBusiness getInstance() {
        if (instance == null) {
            instance = new RecoverBusiness();
        }
        return instance;
    }

    public void execute(Context context) {

        new RecoverImpl(new CategoryBusiness(context)).recoverData();
        new RecoverImpl(new TransactionBusiness(context)).recoverData();
    }

    private class RecoverImpl {

        private final String FILE_NAME;
        private final String DIR_NAME;

        private final DaoAbs business;

        RecoverImpl(DaoAbs business) {
            this.business = business;
            FILE_NAME = business.getClass().getSimpleName().replace("Business", "") + ".txt";
            DIR_NAME = Environment.getExternalStorageDirectory().toString() + "/Unidade/AndroidPigBank/" + BuildConfig.FLAVOR;
        }

        void recoverData() {

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
