package ownerapp.com.ikvox.pratikriya.crm.Feedback;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.R;

/**
 * Created by MyMac on 21/06/16.
 */
public class FeedbackReport extends Fragment {
    public static MyDatabase db;
    public static SQLiteDatabase sdb;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    static String CompanyName;
    static Activity c;
    public FeedbackReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_report_crm, container, false);
        sp= getContext().getSharedPreferences(PREFER_NAME,getContext().MODE_PRIVATE);
        c=getActivity();
        CompanyName= sp.getString(KEY_COMPANYNAME,null);
        db= new MyDatabase(getContext());
        RecyclerView FbDetails =(RecyclerView)rootView.findViewById(R.id.FeedbackDetails);
        FbDetails.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getActivity());
        FbDetails.setLayoutManager(mLayoutManager);
            FeedbackAdapter mFbAdapter = new FeedbackAdapter(getContext(), FeedbackBranch.getData());
            FbDetails.setAdapter(mFbAdapter);
        return rootView;
    }

    public static class FeedbackBranch {
        public static ArrayList<FeedbackInformation> getData() {

            ArrayList<FeedbackInformation> branch = new ArrayList<>();
            try {
                String selectQuery = "SELECT  Distinct Branch  FROM " + CompanyName;
                    sdb = db.getWritableDatabase();
                    Cursor cursor = sdb.rawQuery(selectQuery, null);
                    if (cursor.moveToFirst()) {
                        do {
                            FeedbackInformation current = new FeedbackInformation();
                            String name = cursor.getString(0);
                            name = name.valueOf(name.charAt(0)).toUpperCase() + name.substring(1, name.length());
                            current.BranchName = name;
                            branch.add(current);
                        } while (cursor.moveToNext());
                    }
            }catch(SQLiteException e)
                        {sdb= c.openOrCreateDatabase(MyDatabase.DBNAME,c.MODE_PRIVATE,null);
                            sdb.execSQL("CREATE TABLE IF NOT EXISTS "
                                    + CompanyName
                                    + " (Branch TEXT, Department TEXT,Employee TEXT, Designation TEXT,Phone TEXT, Email TEXT, Report TEXT);");
                            Toast.makeText(c, "Error Finding data. Please sync to Update Data.", Toast.LENGTH_SHORT).show();
                        }

            return branch;
        }
    }

}
