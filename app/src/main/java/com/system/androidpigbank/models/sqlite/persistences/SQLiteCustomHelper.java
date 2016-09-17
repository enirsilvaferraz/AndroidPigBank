package com.system.androidpigbank.models.sqlite.persistences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.system.androidpigbank.controllers.vos.CategoryVO;
import com.system.androidpigbank.controllers.vos.TransactionVO;

import java.sql.SQLException;

/**
 * Created by eferraz on 05/12/15.
 */
public class SQLiteCustomHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "m4.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public SQLiteCustomHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            createTables(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, CategoryVO.class);
        TableUtils.createTableIfNotExists(connectionSource, TransactionVO.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {

            createTables(connectionSource);

            switch (oldVersion) {

//                case 1:
//                    DaoManager.createDao(getConnectionSource(), TransactionVO.class)
//                            .executeRaw("ALTER TABLE 'category' ADD COLUMN 'primary' SMALLINT;");
//
//                case 2:
//                    DaoManager.createDao(getConnectionSource(), TransactionVO.class)
//                            .executeRaw("ALTER TABLE 'transaction' ADD COLUMN 'paymentType' SMALLINT;");


//                case 1:
//                    DaoManager.createDao(getConnectionSource(), TransactionVO.class)
//                            .executeRaw("ALTER TABLE 'transaction' ADD COLUMN 'fixed' SMALLINT;");
//
//                case 2:
//                    DaoManager.createDao(getConnectionSource(), TransactionVO.class)
//                            .executeRaw("ALTER TABLE 'transaction' DROP COLUMN 'fixed' SMALLINT;");


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
