package ownerapp.com.ikvox.pratikriya.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ownerapp.com.ikvox.pratikriya.LoginActivity;
import ownerapp.com.ikvox.pratikriya.Query_main;

/**
 * Created by MyMac on 28/06/16.
 */
public class QueryDatabase extends SQLiteOpenHelper {

        public static final String DBNAME = "QueryData";
        public static final int VERSION = 1;
        Context context;


        public static final String table= Query_main.selectedFromList;
        public QueryDatabase(Context c)
        {
            super(c,DBNAME,null,VERSION);
            context = c;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try
            {
                String qry= "create table "+   LoginActivity.companyName+"_"+Query_main.selectedFromList  + " (QueryNumber TEXT, Query TEXT,QueryType TEXT, Keyword TEXT)";
                db.execSQL(qry);
            }
            catch (Exception e)
            {
                Log.e("TAG : Table Creation",""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " +table);
            onCreate(db);
        }
    }

