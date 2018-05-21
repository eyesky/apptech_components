package com.apptech.ComplexRecyclerView.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apptech.ComplexRecyclerView.Model.Model;
import com.apptech.ComplexRecyclerView.Utils.Util;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movie_management";
    public static final int DB_VERSION = 1;

    public static final String MOVIE_TABLE = "movie_data";
    public static final String ID_FIELD = "_id";
    public static final String STATUS_FIELD = "status";
    public static final String TITLE_FIELD = "title";
    public static final String IMAGE_FIELD = "image";
    public static final String RATING_FIELD = "rating";
    public static final String RELEASE_YEAR_FIELD = "releaseYear";

    public static final String JSON_TABLE_SQL = "CREATE TABLE "
            + MOVIE_TABLE + " (" + ID_FIELD + " INTEGER PRIMARY KEY, "
            + STATUS_FIELD + " TEXT, " + TITLE_FIELD + " TEXT, " + IMAGE_FIELD + " TEXT, "
            + RATING_FIELD + " TEXT, " + RELEASE_YEAR_FIELD + " TEXT)";


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
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);

        // Create tables again
        onCreate(db);
    }

    // insert
    public long insertJsonData(Model model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STATUS_FIELD, model.getStatus());
        values.put(TITLE_FIELD, model.getTitle());
        values.put(IMAGE_FIELD, model.getImage());
        values.put(RATING_FIELD, model.getRating());
        values.put(RELEASE_YEAR_FIELD, model.getReleaseYear());

        long inserted = db.insert(MOVIE_TABLE, null, values);

        db.close();

        return inserted;
    }

    // query
    public ArrayList<Object> getAllJsonData() {
        ArrayList<Object> allJsonData = new ArrayList<Object>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MOVIE_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
                String status = cursor.getString(cursor.getColumnIndex(STATUS_FIELD));
                String title = cursor.getString(cursor.getColumnIndex(TITLE_FIELD));
                String image = cursor.getString(cursor.getColumnIndex(IMAGE_FIELD)); // get web link
                String name = FilenameUtils.getName(image);
                String sdCardLink = Util.generateSDCardLink(Util.PATH, name); // get device storage link
                String rating = cursor.getString(cursor.getColumnIndex(RATING_FIELD));
                String release_year = cursor.getString(cursor.getColumnIndex(RELEASE_YEAR_FIELD));

                Model e = new Model(id, status, title, image, rating, release_year);

                allJsonData.add(e);

            } while (cursor.moveToNext());
        }
        db.close();

        return allJsonData;
    }

    public ArrayList<Object> getAllOderMovieList() {
        int downloadCar = 0;
        int previousCar = 0;
        int total = downloadCar + previousCar;
        ArrayList<Object> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM movie_data WHERE status = '1' ORDER BY _id ASC;", null);
        Cursor cursor2 = db.rawQuery("SELECT * FROM movie_data WHERE status = '0' ORDER BY _id ASC;", null);
        Cursor cursor3 = db.rawQuery("SELECT * FROM movie_data WHERE status = '2' ORDER BY _id ASC;", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Model info = new Model(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    if(info.getId() >= 16) {
                        list.add(0, info);
                    } else {
                        list.add(info);
                    }
                    downloadCar++;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        try {
            if (cursor2.moveToFirst()) {
                do {
                    Model info = new Model(cursor2.getInt(0), cursor2.getString(1), cursor2.getString(2), cursor2.getString(3), cursor2.getString(4), cursor2.getString(5));
                    if(info.getId() >= 16) {
                        list.add(downloadCar, info);
                    } else {
                        list.add(info);
                    }
                    previousCar++;
                } while (cursor2.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor2 != null) {
                cursor2.close();
            }
        }

        try {
            if (cursor3.moveToFirst()) {
                do {
                    Model info = new Model(cursor3.getInt(0), cursor3.getString(1), cursor3.getString(2), cursor3.getString(3), cursor3.getString(4), cursor3.getString(5));
                    if(info.getId() >= 16) {
                        Log.e("getAllOderMovieList: " , ""+total);
                        list.add(total, info);
                    } else {
                        list.add(info);
                    }
                } while (cursor3.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor3 != null) {
                cursor3.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return list;
    }

    // update
    public void updateMovie(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_FIELD, status);
        db.update(MOVIE_TABLE, values, ID_FIELD + "=" + id, null);
    }

    // Deleting single contact
    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(MOVIE_TABLE, ID_FIELD + "=" + id, null);
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
        String countQuery = "SELECT  * FROM " + MOVIE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
