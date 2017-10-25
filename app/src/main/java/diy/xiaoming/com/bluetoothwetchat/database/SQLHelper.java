package diy.xiaoming.com.bluetoothwetchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_CONTENT;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_ID;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_NAME;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_TAG;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.TABLE_NAME;

/**
 * Created by Administrator on 2017-10-24.
 */

public class SQLHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COLUMN_ID + " varchar(40) , "
            + COLUMN_NAME + " varchar(20) ,"
            + COLUMN_TAG + " int , "
            + COLUMN_CONTENT + " varchar(100)) ";

    public SQLHelper(Context context) {
        super(context, CommonValues.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
