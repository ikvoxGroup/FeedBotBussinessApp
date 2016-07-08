package ownerapp.com.ikvox.pratikriya.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Preetam on 22-Feb-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "IkVox.db";

    public static final String TABLE_NAME = "IkvoxSignUpTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "companyNameS";
    public static final String COL_3 = "ownerFNameS";
    public static final String COL_4 = "ownerLNameS";
    public static final String COL_5 = "emailS";
    public static final String COL_6 = "mobileS";
    public static final String COL_7 = "password";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER,companyNameS TEXT,ownerFNameS TEXT,ownerLNameS Text,emailS Text,mobileS Text PRIMARY KEY,password Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertDataForProfile(String uniqueID, String companyNameS, String ownerFNameS, String ownerLNameS, String emailS, String mobileS, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, uniqueID);
        contentValues.put(COL_2, companyNameS);
        contentValues.put(COL_3, ownerFNameS);
        contentValues.put(COL_4, ownerLNameS);
        contentValues.put(COL_5, emailS);
        contentValues.put(COL_6, mobileS);
        contentValues.put(COL_7, password);
        long results = db.insert(TABLE_NAME, null, contentValues);
        if (results == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " where mobileS ='" + mobile + "'";
        Cursor result = db.rawQuery(query, null);
        return result;
    }

    public boolean updateDate(String id, String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{id});
        return true;
    }

    public Integer delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID =?", new String[]{id});


    }
}
