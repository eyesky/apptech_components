package com.apptech.RecyclerviewSearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movie_actor_management";
    public static final int DB_VERSION = 1;

    public static final String MOVIE_ACTOR_TABLE = "actor_data";
    public static final String ID_FIELD = "_id";
    public static final String NAME_FIELD = "name";
    public static final String PHONE_FIELD = "phone";
    public static final String IMAGE_FIELD = "image";
    public static final String STATUS_FIELD = "status";

    public static final String JSON_TABLE_SQL = "CREATE TABLE "
            + MOVIE_ACTOR_TABLE + " (" + ID_FIELD + " INTEGER PRIMARY KEY, "
            + NAME_FIELD + " TEXT, " + PHONE_FIELD + " TEXT, "
            + IMAGE_FIELD + " TEXT, " + STATUS_FIELD + " TEXT)";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JSON_TABLE_SQL);
        Log.e("TABLE CREATE", JSON_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_ACTOR_TABLE);

        // Create tables again
        onCreate(db);
    }

    // insert
    public long insertJsonData(RecyclerViewSearchModel model) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (db.isOpen()) {

            ContentValues values = new ContentValues();

            values.put(NAME_FIELD, model.getName());
            values.put(PHONE_FIELD, model.getPhone());
            values.put(IMAGE_FIELD, model.getImage());
            values.put(STATUS_FIELD, "0");

            long inserted = db.insert(MOVIE_ACTOR_TABLE, null, values);

            db.close();

            return inserted;

        } else {
            return -1;
        }
    }

    // update single row in status column
    public void updateMovie(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STATUS_FIELD, status);

        String whereClause = ID_FIELD + " = '" + id + "'";

        db.update(MOVIE_ACTOR_TABLE, values, whereClause, null);

        if (db.isOpen()) {
            db.close();
        }
    }

    // update all row in status column
    public void updateAllMovieStatus(String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        String rawQuery = "UPDATE " + MOVIE_ACTOR_TABLE + " SET " + STATUS_FIELD + " = '" + status + "'";

        db.execSQL(rawQuery);

        if (db.isOpen()) {
            db.close();
        }
    }


    // query
    public ArrayList<RecyclerViewSearchModel> getAllJsonData(int pos) {

        ArrayList<RecyclerViewSearchModel> allJsonData = new ArrayList<RecyclerViewSearchModel>();

        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(MOVIE_ACTOR_TABLE, null, null, null, null, null, null);
        String selectQuery = "SELECT * FROM " + MOVIE_ACTOR_TABLE + " WHERE " + ID_FIELD + " = '" + pos + "' AND " + STATUS_FIELD + " = '" + "1" + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                    String name = cursor.getString(cursor.getColumnIndex(NAME_FIELD));
                    String phone = cursor.getString(cursor.getColumnIndex(PHONE_FIELD));
                    String image = cursor.getString(cursor.getColumnIndex(IMAGE_FIELD)); // get web link
                    String status = cursor.getString(cursor.getColumnIndex(STATUS_FIELD));

                    RecyclerViewSearchModel e = new RecyclerViewSearchModel(id, name, phone, image, status);

                    allJsonData.add(e);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        return allJsonData;
    }


    // query
    public ArrayList<RecyclerViewSearchModel> getAllJsonData() {

        ArrayList<RecyclerViewSearchModel> allJsonData = new ArrayList<RecyclerViewSearchModel>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MOVIE_ACTOR_TABLE, null, null, null, null, null, null);
//        String selectQuery = "SELECT * FROM " + MOVIE_ACTOR_TABLE + " WHERE " + ID_FIELD + " = '" + pos + "' AND " + STATUS_FIELD + " = '" + "1" + "'";
//        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                    String name = cursor.getString(cursor.getColumnIndex(NAME_FIELD));
                    String phone = cursor.getString(cursor.getColumnIndex(PHONE_FIELD));
                    String image = cursor.getString(cursor.getColumnIndex(IMAGE_FIELD)); // get web link
                    String status = cursor.getString(cursor.getColumnIndex(STATUS_FIELD));

                    RecyclerViewSearchModel e = new RecyclerViewSearchModel(id, name, phone, image, status);

                    allJsonData.add(e);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        return allJsonData;
    }


    // query
    public RecyclerViewSearchModel getJsonDataObject(int pos) {

        RecyclerViewSearchModel model = new RecyclerViewSearchModel();

        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(MOVIE_ACTOR_TABLE, null, null, null, null, null, null);
        String selectQuery = "SELECT * FROM " + MOVIE_ACTOR_TABLE + " WHERE " + ID_FIELD + " = '" + pos + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                    String name = cursor.getString(cursor.getColumnIndex(NAME_FIELD));
                    String phone = cursor.getString(cursor.getColumnIndex(PHONE_FIELD));
                    String image = cursor.getString(cursor.getColumnIndex(IMAGE_FIELD)); // get web link
                    String status = cursor.getString(cursor.getColumnIndex(STATUS_FIELD));

//                    model = new RecyclerViewSearchModel(id, name, phone, image, status);
                    model.setId(id);
                    model.setName(name);
                    model.setPhone(phone);
                    model.setImage(image);
                    model.setStatus(status);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        return model;
    }

    // Deleting single contact
    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(MOVIE_ACTOR_TABLE, ID_FIELD + "=" + id, null);
        return del;
    }

    public int getLastID() {
        int status = 0;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + "_id" + " FROM " + "movie_data" + " order by _id DESC limit 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    status = cursor.getInt(i);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return status;
    }

    public int countRow() {
        String countQuery = "SELECT  * FROM " + MOVIE_ACTOR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
