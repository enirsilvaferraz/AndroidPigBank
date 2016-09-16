package com.system.androidpigbank.models.sqlite.business;

import android.content.Context;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.system.androidpigbank.BuildConfig;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;
import com.system.architecture.managers.EntityAbs;
import com.system.architecture.managers.DaoAbs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecoverBusiness {

    private static RecoverBusiness instance;

    public static RecoverBusiness getInstance() {
        if (instance == null) {
            instance = new RecoverBusiness();
        }
        return instance;
    }

    public void execute(Context context) {

        new RecoverImpl(new CategoryBusiness(context)).recoverData(new TypeToken<ArrayList<CategoryVO>>() {}.getType());
        new RecoverImpl(new TransactionBusiness(context)).recoverData(new TypeToken<ArrayList<TransactionVO>>() {}.getType());
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

        void recoverData(Type type) {

            try {

                File dir = new File(DIR_NAME);
                File file = new File(dir, FILE_NAME);

                if (!file.exists()) {
                    return;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                List<EntityAbs> list = new Gson().fromJson(sb.toString(), type);
                for (EntityAbs entityAbs : list) {

                    if (entityAbs instanceof TransactionVO) {
                        ((TransactionVO) entityAbs).setDatePayment(((TransactionVO) entityAbs).getDateTransaction());
                    }

                    business.save(entityAbs);
                }

                br.close();

            } catch (IOException | SQLException e) {
                Crashlytics.logException(e);
            }
        }
    }
}
