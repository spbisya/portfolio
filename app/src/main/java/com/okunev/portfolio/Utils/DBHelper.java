package com.okunev.portfolio.Utils;


/**
 * Created by gwa on 3/25/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "nomad.db";
    public static final String CONTACTS_TABLE_NAME = "films";
    public static final String CONTACTS_COLUMN_NUMBER = "number";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DATE = "date";
    public static final String CONTACTS_COLUMN_TIME = "time";
    public static final String CONTACTS_COLUMN_RATE = "rate";
    public static final String CONTACTS_COLUMN_PLOT = "plot";
    public static final String CONTACTS_COLUMN_TRAILERS = "trailers";
    public static final String CONTACTS_COLUMN_REVIEWS = "reviews";
    public static final String CONTACTS_COLUMN_POSTER = "poster";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(id integer primary key,  " +
                        CONTACTS_COLUMN_NUMBER + " text, " +
                        CONTACTS_COLUMN_NAME + " text," +
                        CONTACTS_COLUMN_DATE + " text," +
                        CONTACTS_COLUMN_TIME + " text, " +
                        CONTACTS_COLUMN_RATE + " text," +
                        CONTACTS_COLUMN_PLOT + " text," +
                        CONTACTS_COLUMN_TRAILERS + " text," +
                        CONTACTS_COLUMN_REVIEWS + " text," +
                        CONTACTS_COLUMN_POSTER + " blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFilm(String number, String name, String date, String time, String rate,
                              String plot, String trailers, String reviews, byte[] poster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NUMBER, number);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_TIME, time);
        contentValues.put(CONTACTS_COLUMN_RATE, rate);
        contentValues.put(CONTACTS_COLUMN_PLOT, plot);
        contentValues.put(CONTACTS_COLUMN_TRAILERS, trailers);
        contentValues.put(CONTACTS_COLUMN_REVIEWS, reviews);
        contentValues.put(CONTACTS_COLUMN_POSTER, poster);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getFilmbyNumber(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" where number=" + number + "", null);
        return res;
    }

    public Cursor getFilmbyId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateFilm(Integer id, String number, String name, String date, String time, String rate,
                                 String plot, String trailers, String reviews, byte[] poster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NUMBER, number);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_TIME, time);
        contentValues.put(CONTACTS_COLUMN_RATE, rate);
        contentValues.put(CONTACTS_COLUMN_PLOT, plot);
        contentValues.put(CONTACTS_COLUMN_TRAILERS, trailers);
        contentValues.put(CONTACTS_COLUMN_REVIEWS, reviews);
        contentValues.put(CONTACTS_COLUMN_POSTER, poster);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteFilm(Integer number) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                "number = ? ",
                new String[]{Integer.toString(number)});
    }

    public ArrayList<String> getAllFilmNames() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Bitmap> getAllFilmPosters() {
        ArrayList<Bitmap> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            byte[] imgByte = res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_POSTER));
            array_list.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Integer> getAllId() {
        ArrayList<Integer> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getInt(res.getColumnIndex("id")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllNumbers() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NUMBER)));
            res.moveToNext();
        }
        return array_list;
    }
}