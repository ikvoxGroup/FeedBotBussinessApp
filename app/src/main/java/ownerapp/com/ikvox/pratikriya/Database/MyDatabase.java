package ownerapp.com.ikvox.pratikriya.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ownerapp.com.ikvox.pratikriya.LoginActivity;

/**
 * Created by MyMac on 07/06/16.
 */
public class MyDatabase extends SQLiteOpenHelper{

    public static final String DBNAME = "MainData";
    public static final int VERSION = 1;
    Context context;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";
    SharedPreferences sp;
    static String number;

    public static final String table1=LoginActivity.companyName;
    public static final String table2="Login";
    public MyDatabase(Context c)
    {
        super(c,DBNAME,null,VERSION);
        context = c;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            String qryMain= "create table "+ LoginActivity.companyName+ " (Branch TEXT, Department TEXT,Employee TEXT, Designation TEXT,Phone TEXT, Email TEXT, Report TEXT)";
            db.execSQL(qryMain);

            sp = context.getSharedPreferences(PREFER_NAME,context.MODE_PRIVATE);
            number = sp.getString(KEY_NAME, null);

            String qryProf= "create table "+ "Login" +" (Number TEXT, FirstName TEXT, LastName TEXT, EmailId TEXT, Company TEXT)";
            db.execSQL(qryProf);

        }
        catch (Exception e)
        {
            Log.e("TAG : Table Creation",""+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +table1);
        db.execSQL("DROP TABLE IF EXISTS " +table2);
        onCreate(db);
    }
}
