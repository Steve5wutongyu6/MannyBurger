package com.wutongyu.mannyburger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mannyburger.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCTS = "products";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String getTableProducts() {
        return TABLE_PRODUCTS;
    }

    // 表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                "pid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pname TEXT NOT NULL," +
                "shop_price REAL NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE);

        insertInitialData(db);
    }

    // 样例数据，在进入应用时插入数据库中
    private void insertInitialData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("pname", "经典牛肉汉堡");
        values.put("shop_price", 20.0);
        db.insert(TABLE_PRODUCTS, null, values);

        values.put("pname", "芝士培根汉堡");
        values.put("shop_price", 25.0);
        db.insert(TABLE_PRODUCTS, null, values);

        values.put("pname", "鸡肉汉堡");
        values.put("shop_price", 18.0);
        db.insert(TABLE_PRODUCTS, null, values);

        values.put("pname", "墨西哥风味汉堡");
        values.put("shop_price", 23.0);
        db.insert(TABLE_PRODUCTS, null, values);

        values.put("pname", "素食汉堡");
        values.put("shop_price", 16.0);
        db.insert(TABLE_PRODUCTS, null, values);

        values.put("pname", "香辣炸鸡腿");
        values.put("shop_price", 16.0);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
}