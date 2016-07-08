package ownerapp.com.ikvox.pratikriya;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.LoginSessionManagement.UserSessionManager;
import ownerapp.com.ikvox.pratikriya.ProgBarAppear.AndroidUtils;


/**
 * Created by Preetam on 05-Apr-16.
 */
public class LoginActivity extends ActionBarActivity {
    Button loginB;
    TextView Signup;
    EditText mNumber, password;
    String mNumberS, passwordS;

    private String resp;
    private String errorMsg;


    // User Session Manager Class
    UserSessionManager session;
    View progressOverlay;
    ImageView image;
    AnimationDrawable animate;
    int error,network;


    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json;
    private static String url_login = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/login.do";

    public static String email=null;
    public  static String companyName=null;
    public static String ownerFName=null;
    public static String ownerLName=null;
    String id=null;
    ArrayList<String> Employee_name;

    FrameLayout statusBar;
    Toolbar toolbar;

    MyDatabase db;
    SQLiteDatabase sdb;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        db= new MyDatabase(this);
        sdb= db.getWritableDatabase();
        toolbarStatusBar();
        navigationBarStatusBar();
        error=0;network=0;
        progressOverlay = findViewById(R.id.progress_overlay);
        image= (ImageView)findViewById(R.id.progressBarMain);
        animate= (AnimationDrawable)image.getBackground();
        // User Session Manager
        session = new UserSessionManager(getApplicationContext());


        loginB = (Button) findViewById(R.id.btn_login);
        Signup = (TextView)findViewById(R.id.link_signup);

        mNumber = (EditText) findViewById(R.id.input_number);
        password = (EditText) findViewById(R.id.input_password);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNumberS = mNumber.getText().toString().trim();
                passwordS = password.getText().toString().trim();

                if (mNumberS.equals("")) {
                    mNumber.setError("enter a valid Mobile Number");
                    // Toast.makeText(LoginActivity.this, "Please enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (mNumberS.length() != 10) {
                    mNumber.setError("enter a valid Mobile Number");
                    // Toast.makeText(LoginActivity.this, "10 digit mobile number please", Toast.LENGTH_SHORT).show();
                } else if (passwordS.equals("")) {
                    password.setError("Password please");
                    // Toast.makeText(LoginActivity.this, "Password Please", Toast.LENGTH_SHORT).show();
                } else {
                    new MyTask().execute();
                }
            }

        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_login);


    }
    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                LoginActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_login);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                LoginActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_login);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                LoginActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_login);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                LoginActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            AndroidUtils.animateView(progressOverlay,image,animate, View.VISIBLE, 0.8f, 200);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                    postParameters.add(new BasicNameValuePair("mobile", mNumberS));
                    postParameters.add(new BasicNameValuePair("password", passwordS));
                    json = jParser.makeHttpRequest(url_login, "GET", postParameters);
                    String s = null;
                    try {
                        /*response = SimpleHttpClient
                                .executeHttpPost(
                                        "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/login.do",
                                        postParameters);*/

                        s = json.getString("status");
                        email = json.getString("email");
                        companyName = json.getString("companyName");
                        ownerFName = json.getString("ownerFName");
                        ownerLName = json.getString("ownerLName");
                        id = json.getString("id");


                       /* String res = response.toString();
                        System.out.println(res);
                        resp = res.replaceAll("\\s+", "");
                            */
                        resp=s;
                    } catch (Exception e) {
                        e.printStackTrace();
                        network=1;
                        errorMsg = e.getMessage();
                    }
                }
            }).start();
            try {
                Thread.sleep(5000);
                /**
                 * Inside the new thread we cannot update the main thread So
                 * updating the main thread outside the new thread
                 */
                // Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_LONG).show();
                if (resp.equals("success")) {
                    session.createUserLoginSession(mNumberS, passwordS, companyName);
                    //Toast.makeText(LoginActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                    sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
                    String number = sp.getString(KEY_NAME, null);
                    sdb = getApplicationContext().openOrCreateDatabase(MyDatabase.DBNAME, MODE_PRIVATE, null);
                    sdb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + "Login"
                            + " (Number TEXT, FirstName TEXT, LastName TEXT, EmailId TEXT, Company TEXT);");

                    String count = "SELECT count(*) FROM Login";
                    Cursor mcursor = sdb.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    if (icount > 0) {
                        String delete = "delete from Login";
                        sdb.execSQL(delete);
                        ContentValues value = new ContentValues();
                        value.put("Number", number);
                        value.put("FirstName", ownerFName);
                        value.put("LastName", ownerLName);
                        value.put("EmailId", email);
                        value.put("Company", companyName);
                        sdb.insert("Login", null, value);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        ContentValues value = new ContentValues();
                        value.put("Number", number);
                        value.put("FirstName", ownerFName);
                        value.put("LastName", ownerLName);
                        value.put("EmailId", email);
                        value.put("Company", companyName);
                        sdb.insert("Login", null, value);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if (resp.equals("Failed")) {
                    error = 1;
                }
            } catch (Exception e1){
                    e1.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            AndroidUtils.animateView(progressOverlay,image,animate, View.GONE, 0, 200);
            resp="null";
            if (error == 1) {

                Toast.makeText(LoginActivity.this, "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                error = 0;
                network=0;
            }
            else if (network==1)
            {
                // Toast.makeText(LoginActivity.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                error = 0;
                network=0;
                final AlertDialog.Builder retry = new AlertDialog.Builder(LoginActivity.this);
                retry.setMessage("Connection Problem!!");
                retry.setTitle("Alert");
                retry.setCancelable(false);
                retry.setIcon(R.drawable.feedbot_final);
                retry.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new MyTask().execute();
                        } catch (NullPointerException e) {

                        }
                    }
                });
                retry.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                retry.show();
            }

            /*if (!msg.equals(""))
            {
                Toast.makeText(LoginActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }*/
            //Toast.makeText(getApplicationContext(), "hiii", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

    }

}
