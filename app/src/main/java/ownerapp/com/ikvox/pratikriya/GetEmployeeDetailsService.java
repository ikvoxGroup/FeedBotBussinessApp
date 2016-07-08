package ownerapp.com.ikvox.pratikriya;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;

/**
 * Created by Preetam on 22-Jun-16.
 */

public class GetEmployeeDetailsService extends Service {
    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json,json1;
    private static String url_login = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/CompanyBranchDetails.do";

    JSONArray EmpNamejson,Designationjson,Departmentjson,locationjson,Emp_EmailIDjson,Phone_numberjson,Reporting_MailIDjson;
    ArrayList<String> EmpName;
    ArrayList<String> Designation;
    ArrayList<String> Department;
    ArrayList<String> location;
    ArrayList<String> Emp_EmailID;
    ArrayList<String> Phone_number;
    ArrayList<String> Reporting_MailID;

    private String resp;
    private String errorMsg;
    public static int size;

    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences pref;
    // User Session Manager Class
   // UserSessionManager session;
   // View progressOverlay;
  //  ImageView image;
   // AnimationDrawable animate;
    int error,network;

    MyDatabase db;
    SQLiteDatabase sdb;
    String CompanyName;
    int len;

    QueryDatabase qdb;
    SQLiteDatabase qsdb;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        CompanyName= pref.getString(KEY_COMPANYNAME,null);
        location=new ArrayList<String>() ;
        Department=new ArrayList<String>() ;
        EmpName =new ArrayList<String>() ;
        Designation=new ArrayList<String>() ;
        Phone_number=new ArrayList<String>() ;
        Emp_EmailID=new ArrayList<String>() ;
        Reporting_MailID=new ArrayList<String>() ;


        /*for (i=0; i<EmpName.size();i++);
        {
            ContentValues value= new ContentValues();
            value.put("Employee",EmpName.get(i));
            sdb.insert(LoginActivity.companyName,null,value);
        }*/





        //Toast.makeText(this, "Service started in onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        new MyTask().execute();
        //new MyTask1().execute();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    /*JSONArray Employee_nameJson = new JSONArray(EmpName);
                    try {
                        json1.put("empName",Employee_nameJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    String companyName = pref.getString(KEY_COMPANYNAME, null);


                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("companyName", companyName));
                    // postParameters.add(new BasicNameValuePair("password",json1.toString()));


                    json = jParser.makeHttpRequest(url_login, "GET", postParameters);
                    String s = null;
                    try {


                        s = json.getString("status");
                        EmpNamejson = json.getJSONArray("Employee_name");
                        Designationjson = json.getJSONArray("Designation");
                        Departmentjson = json.getJSONArray("Department");
                        locationjson = json.getJSONArray("location");
                        Emp_EmailIDjson = json.getJSONArray("Emp_EmailID");
                        Phone_numberjson = json.getJSONArray("Phone_number");
                        Reporting_MailIDjson = json.getJSONArray("Reporting_MailID");

                        len = EmpNamejson.length();
                        resp = s;
                    } catch (Exception e) {
                        e.printStackTrace();
                        network = 1;
                        errorMsg = e.getMessage();
                    }
                }
            }).start();
            try {
                Thread.sleep(3000);
                /**
                 * Inside the new thread we cannot update the main thread So
                 * updating the main thread outside the new thread
                 */

                if (resp.equals("success")) {

                    try {
                        //converting JSON value to Arraylist

                        for (int i = 0; i < len; i++) {

                            EmpName.add(EmpNamejson.get(i).toString());
                            Designation.add(Designationjson.get(i).toString());
                            Department.add(Departmentjson.get(i).toString());
                            location.add(locationjson.get(i).toString());
                            Emp_EmailID.add(Emp_EmailIDjson.get(i).toString());
                            Phone_number.add(Phone_numberjson.get(i).toString());
                            Reporting_MailID.add(Reporting_MailIDjson.get(i).toString());

                        }
                    } catch (JSONException e) {

                    }
                    // now we can fatch data from the arraylist using ex.  EmpName.get(2);   2 is the index value
                    //Toast.makeText(getApplicationContext(), "PM" + EmpNamejson.get(3).toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), EmpName.get(3), Toast.LENGTH_SHORT).show();
                    db= new MyDatabase(getApplicationContext());
                    sdb= db.getWritableDatabase();
                    sdb= getApplicationContext().openOrCreateDatabase(MyDatabase.DBNAME,MODE_PRIVATE,null);
                    sdb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + CompanyName
                            + " (Branch TEXT, Department TEXT,Employee TEXT, Designation TEXT,Phone TEXT, Email TEXT, Report TEXT);");
                   String[] branch= new String[location.size()];
                    for (int i=0; i<location.size();i++)
                        branch[i]=location.get(i).toString().trim();
                    branch = new HashSet<String>(Arrays.asList(branch)).toArray(new String[0]);
                    for (int i = 0; i < branch.length; i++) {
                      branch[i] = branch[i].valueOf(branch[i].charAt(0)).toUpperCase() + branch[i].substring(1, branch[i].length());
                     }

                    qdb= new QueryDatabase(getApplicationContext());
                    qsdb= qdb.getWritableDatabase();
                    qsdb= getApplicationContext().openOrCreateDatabase(QueryDatabase.DBNAME,MODE_PRIVATE,null);
                    for (int i= 0; i<branch.length;i++)
                    {
                        qsdb.execSQL( "create table "+   CompanyName+"_"+branch[i] + " (QueryNumber TEXT, Query TEXT,QueryType TEXT, Keyword TEXT);");
                    }

                    String count = "SELECT count(*) FROM "+CompanyName;
                    Cursor mcursor = sdb.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    if(icount>0) {
                        String delete = "delete from "+CompanyName;
                        sdb.execSQL(delete);
                        int i;
                        for (i = 0; i < location.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("Branch", location.get(i).toString().toLowerCase());
                            value.put("Department", Department.get(i).toString().toLowerCase());
                            value.put("Employee", EmpName.get(i).toString().toLowerCase());
                            value.put("Designation", Designation.get(i).toString().toLowerCase());
                            value.put("Phone", Phone_number.get(i).toString().toLowerCase());
                            value.put("Email", Emp_EmailID.get(i).toString().toLowerCase());
                            value.put("Report", Reporting_MailID.get(i).toString().toLowerCase());
                            sdb.insert(CompanyName,null, value);
                        }
                    }
                    else
                    {
                        int i;
                        for (i = 0; i < location.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("Branch", location.get(i).toString().toLowerCase());
                            value.put("Department", Department.get(i).toString().toLowerCase());
                            value.put("Employee", EmpName.get(i).toString().toLowerCase());
                            value.put("Designation", Designation.get(i).toString().toLowerCase());
                            value.put("Phone", Phone_number.get(i).toString().toLowerCase());
                            value.put("Email", Emp_EmailID.get(i).toString().toLowerCase());
                            value.put("Report", Reporting_MailID.get(i).toString().toLowerCase());
                            sdb.insert(CompanyName,null, value);
                        }
                    }
                } else if (resp.equals("Failed")) {
                    error = 1;
                }


                if (null != errorMsg && !errorMsg.isEmpty()) {

                }
            } catch (Exception e) {


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            resp = "null";
           // Log.d("k", EmpNamejson.toString());


                onDestroy();
                if (error == 1) {

                    Toast.makeText(getApplicationContext(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                    error = 0;
                    network = 0;
                } else if (network == 1) {

                    error = 0;
                    network = 0;
                }
                super.onPostExecute(aVoid);
            }

        }

    }
