package ownerapp.com.ikvox.pratikriya.crm.Branch.BranchLayoutDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;

/**
 * Created by MyMac on 21/06/16.
 */
public class BranchDetailsLayout extends ActionBarActivity {
    Toolbar toolbar;
    FrameLayout statusBar;
    static TextView ManagerName;
    RecyclerView EmployeeDetails;
    EmployeeAdapter empAdapter;
    public static String BName;
    public static MyDatabase db;
    public static SQLiteDatabase sdb;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    static String CompanyName;
    static String Manager;
    static Context c;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_details_layout);
        toolbarStatusBar();
        navigationBarStatusBar();
        c=getApplicationContext();
        toolbar.setBackgroundColor(MainActivity.color);
        sp= getSharedPreferences(PREFER_NAME,MODE_PRIVATE);
        CompanyName= sp.getString(KEY_COMPANYNAME,null);
        db= new MyDatabase(getApplicationContext());
        ManagerName= (TextView)findViewById(R.id.ManagerName);
        EmployeeDetails=(RecyclerView)findViewById(R.id.EmployeeLayout);
        EmployeeDetails.setLayoutManager(new LinearLayoutManager(this));
        empAdapter= new EmployeeAdapter(BranchDetailsLayout.this,Employee_list.getData());
        EmployeeDetails.setAdapter(empAdapter);

        try{
        String selectQuery = "SELECT  Employee FROM " +CompanyName+ " where Branch = "+"'"+BName.toLowerCase()+"' and Designation = 'manager'";
        sdb = db.getWritableDatabase();
        Cursor cursor1 = sdb.rawQuery(selectQuery, null);
        if (cursor1.moveToFirst()) {
            do {
                String n=cursor1.getString(0);
                n= n.valueOf(n.charAt(0)).toUpperCase() + n.substring(1, n.length());
                ManagerName.setText(n);
            } while (cursor1.moveToNext());
        }}catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Finding data. Please sync to Update Data.", Toast.LENGTH_SHORT).show();}


    }
    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_branch);
        toolbar = (Toolbar) findViewById(R.id.toolbar_branch);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(BName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Dialog dialog = new Dialog(BranchDetailsLayout.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.about_dialog);
                dialog.show();
                break;
            case android.R.id.home:

                onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                BranchDetailsLayout.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_branch);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                BranchDetailsLayout.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_branch);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                BranchDetailsLayout.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_branch);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                BranchDetailsLayout.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    public static void BranchName(String BranchName)
    {
        BName= BranchName;
    }

    public static class Employee_list {
        public static ArrayList<EmployeeInformation> getData() {

            ArrayList<EmployeeInformation> employee = new ArrayList<>();
            try {
                String selectQuery = "SELECT  Employee, Designation, Phone, Email FROM " + CompanyName + " where Branch = " + "'" + BName.toLowerCase() + "'";
                sdb = db.getWritableDatabase();
                Cursor cursor = sdb.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        EmployeeInformation current = new EmployeeInformation();
                        String n = cursor.getString(0);
                        Log.d("data", cursor.getString(0));
                        n = n.valueOf(n.charAt(0)).toUpperCase() + n.substring(1, n.length());
                        current.EmployeeName = n;
                        String d = cursor.getString(1);
                        Log.d("data", cursor.getString(1));
                        d = d.valueOf(d.charAt(0)).toUpperCase() + d.substring(1, d.length());
                        current.EmployeeDesignation = d;
                        current.EmployeeNumber = cursor.getString(2);
                        Log.d("data", cursor.getString(2));
                        current.EmployeeMailId = cursor.getString(3);
                        Log.d("data", cursor.getString(3));
                        employee.add(current);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e){
                Toast.makeText(c,"Error Finding data. Please sync to Update Data.", Toast.LENGTH_SHORT).show();}
                return employee;
            }

    }

}
