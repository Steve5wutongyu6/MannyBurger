package com.wutongyu.mannyburger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ITEMS = "items";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_TOTAL_PRICE = "total_price";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEMS + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_TOTAL_PRICE + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long insertOrder(String items, String note, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEMS, items);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        return db.insert(TABLE_ORDERS, null, values);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String items = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEMS));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE));
                orders.add(new Order(id, items, note, totalPrice));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }
}