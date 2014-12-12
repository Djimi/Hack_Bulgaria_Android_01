package com.example.damyan.expenselist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damyan on 3.12.2014 г..
 */
public class ExpenseListOpenHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "List";
    public static final String ID = "ID";
    public static final String ADDED_TIME_COLUMN = "ADDED_TIME";
    public static final String LABEL_COLUMN = "Label";
    public static final String PRICE_COLUMN = "Price";

    public static final String DATABASE_NAME = "MyDataBase";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + LABEL_COLUMN + " " + "TEXT, "
            + PRICE_COLUMN + " " + "real )";


    public ExpenseListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addExpense(double price, String label) {
        SQLiteDatabase changer = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LABEL_COLUMN, label);
        values.put(PRICE_COLUMN, price);

        changer.insert(TABLE_NAME, null, values);

        changer.close();
    }

    public void delete(String label, double price) {
        SQLiteDatabase changer = getWritableDatabase();

        String where = ExpenseListOpenHelper.LABEL_COLUMN + "='" + label + "' AND " + ExpenseListOpenHelper.PRICE_COLUMN + "=" + price;

        changer.delete(ExpenseListOpenHelper.TABLE_NAME, where, null);
        changer.close();
    }

    public List<MyActivity.LabelToPricePair> getCurrentItems(Adapter adapter) {
        List<MyActivity.LabelToPricePair> list = new ArrayList<MyActivity.LabelToPricePair>();

        SQLiteDatabase changer = getReadableDatabase();

        String query = "SELECT * FROM " + ExpenseListOpenHelper.TABLE_NAME;
        Cursor cursor = changer.rawQuery(query, null);

        if(cursor.moveToFirst()){
            MyActivity.LabelToPricePair pair = null;
            do{
                pair = new MyActivity.LabelToPricePair(cursor.getString(0),  cursor.getFloat(1));

                list.add(pair);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public void update(MyActivity.LabelToPricePair pairForDelete, MyActivity.LabelToPricePair pairForAdd){

        SQLiteDatabase changer = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseListOpenHelper.LABEL_COLUMN, pairForAdd.label);
        values.put(ExpenseListOpenHelper.PRICE_COLUMN, pairForAdd.price);

        String where = ExpenseListOpenHelper.LABEL_COLUMN + "='" + pairForDelete.label + "' AND "
                + ExpenseListOpenHelper.PRICE_COLUMN + "=" + pairForDelete.price;
        changer.update(ExpenseListOpenHelper.TABLE_NAME, values, where, null);
    }
}
