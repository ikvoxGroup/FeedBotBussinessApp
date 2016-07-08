package ownerapp.com.ikvox.pratikriya.crm.Branch;

import android.content.Context;
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
public class BranchDetails extends Fragment {

    private RecyclerView mBranchDetails;
    private RecyclerView.LayoutManager mLayoutManager;
    private BranchAdapter mAdapter;
    public static MyDatabase db;
    public static SQLiteDatabase sdb;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    static String CompanyName;
    static Context c;
    public BranchDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.branch_details, container, false);
        sp= getContext().getSharedPreferences(PREFER_NAME,getContext().MODE_PRIVATE);
        CompanyName= sp.getString(KEY_COMPANYNAME,null);
        db= new MyDatabase(getContext());
        c= getContext();
        mBranchDetails = (RecyclerView)rootView.findViewById(R.id.BranchDetailsCRM);
        mBranchDetails.setHasFixedSize(true);
        mLayoutManager= new LinearLayoutManager(getActivity());
        mBranchDetails.setLayoutManager(mLayoutManager);
        mAdapter = new BranchAdapter(getContext(),Branch.getdata());
        mBranchDetails.setAdapter(mAdapter);

        return rootView;
    }
    public static class Branch {
        public static ArrayList<BranchInformation> getdata() {
            ArrayList<BranchInformation> branch = new ArrayList<BranchInformation>();
            // Select All Query
            try {
            String selectQuery = "SELECT  Distinct Branch FROM " +CompanyName;
            sdb = db.getWritableDatabase();

                Cursor cursor = sdb.rawQuery(selectQuery, null);
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        BranchInformation current = new BranchInformation();
                        String name = cursor.getString(0);
                        name = name.valueOf(name.charAt(0)).toUpperCase() + name.substring(1, name.length());
                        current.BranchName = name;
                        String count = "SELECT count(Employee) FROM " + CompanyName + " where Branch = " + "'" + cursor.getString(0) + "'";
                        Cursor mcursor = sdb.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        current.BranchDetails = "Total number of employees " + icount;
                        branch.add(current);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e){
                Toast.makeText(c,"Error Finding data. Please sync to Update Data.", Toast.LENGTH_SHORT).show();}

            // return contact list
            return branch;
        }
    }

}