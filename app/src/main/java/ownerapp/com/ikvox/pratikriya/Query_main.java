package ownerapp.com.ikvox.pratikriya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;
import ownerapp.com.ikvox.pratikriya.ProgBarAppear.AndroidUtils;
import ownerapp.com.ikvox.pratikriya.QueryCardView.AnimationUtil;
import ownerapp.com.ikvox.pratikriya.QueryCardView.Information;
import ownerapp.com.ikvox.pratikriya.slidingUpPanel.SlidingUpPanelLayout;

/**
 * Created by MyMac on 16/06/16.
 */
public class Query_main extends AppCompatActivity {
    private static final String TAG = "Query";

    private SlidingUpPanelLayout mLayout;

    RelativeLayout open;
    TextView Branch;
    public static String selectedFromList;
    Toolbar toolbar;
    FrameLayout statusBar;
    Boolean homeButton = false;
    Intent intent;
    LinearLayout list;
    static RecyclerView Content;
    static ViewEditAdapter1 adapter;
    int id_layout = 0;
    String database = "0";

    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json;
    private static String url_query = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/RetriveQueryForTablet.do";
    private static String url_add = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/AddQuery.do";

    private String resp;
    private String errorMsg;
    int error, network;
    public static MyDatabase db;
    public static QueryDatabase qdb;
    public static SQLiteDatabase sdb;
    public static SQLiteDatabase adb;
    String br;
    String[] branch;
    Cursor c;
    public static ArrayList<String> branchArray;

    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;


    private static final String REFERNCE = "Reference";
    public static final String BRANCHNAME = "BranchName";
    SharedPreferences sp1;


    public static String CompanyName;
    public static String BranchName;

    public static int icount;
    public static ArrayList<String> QuerySrNumberList;
    public static ArrayList<String> QueryList;
    public static ArrayList<String> QueryTypeList;
    public static ArrayList<String> FocusKeyWordList;
    public static String QuerySrNumberListjson = null;
    public static String QueryListjson = null;
    public static String QueryTypeListjson = null;
    public static String FocusKeyWordListjson = null;
    public static String QueryDBName = selectedFromList;

    View progressOverlay;
    ImageView image;
    AnimationDrawable animate;
    FloatingActionButton fab;

    View vw1;

    static ProgressDialog mProgressDialog;
    static ProgressDialog mProgressDialog1;

    ImageView noti;

    boolean flag;
    boolean back;
    boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_drawer);
        progressOverlay = findViewById(R.id.progress_overlay);
        image = (ImageView) findViewById(R.id.progressBarMain);
        animate = (AnimationDrawable) image.getBackground();
        sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        sp1 = getSharedPreferences(REFERNCE, MODE_PRIVATE);
        CompanyName = sp.getString(KEY_COMPANYNAME, null);
        error = 0;
        network = 0;
        back = false;
        done = false;
        db = new MyDatabase(this);
        sdb = db.getWritableDatabase();
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        list = (LinearLayout) findViewById(R.id.dragView);
        list.setBackgroundColor(MainActivity.color);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        open = (RelativeLayout) findViewById(R.id.sliding);
        open.setBackgroundColor(MainActivity.color);
        Branch = (TextView) findViewById(R.id.selectBranch);
        Content = (RecyclerView) findViewById(R.id.recycleView);
        Content.setLayoutManager(new LinearLayoutManager(this));
        toolbarStatusBar();
        navigationBarStatusBar();
        mProgressDialog = new ProgressDialog(Query_main.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Submitting Data!! Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog1 = new ProgressDialog(Query_main.this);
        mProgressDialog1.setIndeterminate(false);
        mProgressDialog1.setMessage("Fetching Data!! Please Wait...");
        mProgressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        toolbar.setBackgroundColor(MainActivity.color);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_new_card);
        noti = (ImageView) findViewById(R.id.noti);


        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout.getAnchorPoint() == 1.0f) {
                    mLayout.setAnchorPoint(0.7f);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    noti.setImageResource(R.drawable.up);
                }
            }
        });
        final ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show();
                noti.setImageResource(R.drawable.down);
                mLayout.setAnchorPoint(1.0f);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                selectedFromList = (String) (lv.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(), "" + selectedFromList, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sp1.edit();
                editor.putString(BRANCHNAME, selectedFromList);
                editor.commit();
                qdb = new QueryDatabase(getApplicationContext());
                adb = qdb.getWritableDatabase();
                Branch.setText(selectedFromList);
                new MyTask().execute();

            }
        });


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter

        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, GetBranch.getData()
            );
            lv.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Toast.makeText(Query_main.this, "Please Sync to Update the data", Toast.LENGTH_SHORT).show();
        }


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }

        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.CUSTOM);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Query_main.this, "This will add new query", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder AddNewQuery = new AlertDialog.Builder(Query_main.this);
                LayoutInflater inflater = Query_main.this.getLayoutInflater();
                vw1 = inflater.inflate(R.layout.list_item_row, null);
                final AlertDialog dialog = AddNewQuery.create();
                dialog.setView(vw1);
                AddNewQuery.setView(vw1);
                dialog.show();


                final FloatingActionButton Next, Finish, Add;
                final EditText edittext, edittextkey;
                final TextView Query;
                final Animation fab_open, fab_close, rotate_forward, rotate_backward;
                final LinearLayout CardOptions;
                final RadioButton rb1, rb2, rb3, rb4;
                final RadioGroup rg;
                final String option;
                final TextInputLayout et, etk;

                fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
                rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
                rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

                edittext = (EditText) vw1.findViewById(R.id.QueryMain);
                edittextkey = (EditText) vw1.findViewById(R.id.QueryKeywordMain);
                Next = (FloatingActionButton) vw1.findViewById(R.id.next);
                Add = (FloatingActionButton) vw1.findViewById(R.id.add);
                Finish = (FloatingActionButton) vw1.findViewById(R.id.finish);
                Query = (TextView) vw1.findViewById(R.id.query);
                CardOptions = (LinearLayout) vw1.findViewById(R.id.OptionContent);
                rg = (RadioGroup) vw1.findViewById(R.id.rg);
                rb1 = (RadioButton) vw1.findViewById(R.id.rb1);
                rb2 = (RadioButton) vw1.findViewById(R.id.rb2);
                rb3 = (RadioButton) vw1.findViewById(R.id.rb3);
                rb4 = (RadioButton) vw1.findViewById(R.id.rb4);
                et = (TextInputLayout) vw1.findViewById(R.id.et);
                etk = (TextInputLayout) vw1.findViewById(R.id.etk);

                rb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardOptions.removeAllViews();
                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        LinearLayout l1 = new LinearLayout(Query_main.this);
                        l1.setLayoutParams(LayoutParams);
                        l1.setOrientation(LinearLayout.HORIZONTAL);
                        l1.setGravity(Gravity.CENTER);

                        Button yes = new Button(Query_main.this);
                        yes.setBackgroundColor(Color.GRAY);
                        yes.setTextColor(Color.BLACK);
                        yes.setText("Yes");
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                        yes.setLayoutParams(buttonLayoutParams);

                        Button no = new Button(Query_main.this);
                        no.setBackgroundColor(Color.GRAY);
                        no.setTextColor(Color.BLACK);
                        no.setText("No");
                        no.setLayoutParams(buttonLayoutParams);
                        l1.addView(yes);
                        l1.addView(no);
                        CardOptions.addView(l1);
                    }
                });

                rb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardOptions.removeAllViews();
                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        LinearLayout l = new LinearLayout(Query_main.this);
                        l.setLayoutParams(LayoutParams);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER);
                        l.setWeightSum(1);
                        LinearLayout l1 = new LinearLayout(Query_main.this);
                        l1.setOrientation(LinearLayout.HORIZONTAL);
                        l1.setGravity(Gravity.CENTER);
                        l1.setLayoutParams(LayoutParams);
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                        Button bad = new Button(Query_main.this);
                        bad.setLayoutParams(buttonLayoutParams);
                        bad.setBackgroundColor(Color.GRAY);
                        bad.setTextColor(Color.BLACK);
                        bad.setText("Bad");
                        Button good = new Button(Query_main.this);
                        good.setLayoutParams(buttonLayoutParams);
                        good.setBackgroundColor(Color.GRAY);
                        good.setTextColor(Color.BLACK);
                        good.setText("Good");
                        l1.addView(bad);
                        l1.addView(good);
                        LinearLayout l2 = new LinearLayout(Query_main.this);
                        l2.setOrientation(LinearLayout.HORIZONTAL);
                        l2.setGravity(Gravity.CENTER);
                        l2.setLayoutParams(LayoutParams);
                        Button vgood = new Button(Query_main.this);
                        vgood.setLayoutParams(buttonLayoutParams);
                        vgood.setBackgroundColor(Color.GRAY);
                        vgood.setTextColor(Color.BLACK);
                        vgood.setText("Very Good");
                        Button awesome = new Button(Query_main.this);
                        awesome.setLayoutParams(buttonLayoutParams);
                        awesome.setBackgroundColor(Color.GRAY);
                        awesome.setTextColor(Color.BLACK);
                        awesome.setText("Awesome");
                        l2.addView(vgood);
                        l2.addView(awesome);
                        l.addView(l1);
                        l.addView(l2);
                        CardOptions.addView(l);
                    }
                });

                rb3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardOptions.removeAllViews();
                        LinearLayout l = new LinearLayout(Query_main.this);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER_VERTICAL);
                        SeekBar sb = new SeekBar(Query_main.this);
                        sb.setMax(10);
                        l.addView(sb);
                        CardOptions.addView(l);
                    }
                });

                rb4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardOptions.removeAllViews();
                        LinearLayout l = new LinearLayout(Query_main.this);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER_VERTICAL);
                        LayoutInflater layoutInflater = (LayoutInflater) Query_main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        l.addView(layoutInflater.inflate(R.layout.rating_bar, CardOptions, false));
                        CardOptions.addView(l);
                    }
                });

                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Query.setText(s.toString());
                    }
                });


                Next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateFAB();
                    }

                    Boolean isFabOpen = false;

                    public void animateFAB() {

                        if (isFabOpen) {

                            Next.startAnimation(rotate_backward);
                            Finish.startAnimation(fab_close);
                            Add.startAnimation(fab_close);
                            Finish.setClickable(false);
                            Add.setClickable(false);
                            isFabOpen = false;
                            Log.d("Raj", "close");

                        } else {

                            Next.startAnimation(rotate_forward);
                            Finish.startAnimation(fab_open);
                            Add.startAnimation(fab_open);
                            Finish.setClickable(true);
                            Add.setClickable(true);
                            isFabOpen = true;
                            Log.d("Raj", "open");

                        }
                    }


                });
                Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String opt;
                        opt = "null";
                        switch (rg.getCheckedRadioButtonId()) {
                            case R.id.rb1:
                                opt = "1";
                                break;
                            case R.id.rb2:
                                opt = "2";
                                break;
                            case R.id.rb3:
                                opt = "3";
                                break;
                            case R.id.rb4:
                                opt = "4";
                                break;
                        }
                        if (edittext.getText().toString().equals("")) {
                            et.setError("The field cannot left blank");
                        } else if (edittextkey.getText().toString().equals("")) {
                            etk.setError("The field cannot left blank");
                        } else if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

                            Information i = new Information();
                            i.Query = edittext.getText().toString();
                            i.Keyword = edittextkey.getText().toString();
                            i.Options = opt;
                            sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
                            sp1 = getSharedPreferences(REFERNCE, MODE_PRIVATE);
                            CompanyName = sp.getString(KEY_COMPANYNAME, null);
                            BranchName = sp1.getString(BRANCHNAME, null);
                            qdb = new QueryDatabase(getApplicationContext());
                            adb = qdb.getWritableDatabase();
                            String count = "select count(*) from " + CompanyName + "_" + BranchName + "";
                            Cursor mcursor = adb.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount1 = mcursor.getInt(0);
                            String qry = "Insert into " + CompanyName + "_" + BranchName + " values ('Q" + (icount1 + 1) + "','" + i.Query + "','" + i.Options + "','" + i.Keyword + "')";
                            adb.execSQL(qry);
                            dialog.dismiss();
                            adapter = new ViewEditAdapter1(Query_main.this, Data.getData());
                            Content.setAdapter(adapter);
                        } else {
                            Toast.makeText(Query_main.this, "Please select the option", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }

                });
            }

        });
    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Query");
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
                Dialog dialog = new Dialog(Query_main.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.about_dialog);
                dialog.show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;


        }
        return false;

    }

    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Query_main.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                Query_main.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Query_main.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                Query_main.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (back) {
            final AlertDialog.Builder back = new AlertDialog.Builder(Query_main.this);
            back.setMessage("Submit the changes done to the Query");
            back.setTitle("Alert");
            back.setCancelable(false);
            back.setIcon(R.drawable.feedbot_final);
            back.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new MyTask2().execute();
                }
            });
            back.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent = new Intent(Query_main.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.cancel();
                }
            });
            back.show();
        } else {
            intent = new Intent(Query_main.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String companyName = CompanyName;


                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("CompanyName", companyName));
                    postParameters.add(new BasicNameValuePair("BranchLocation", selectedFromList.toLowerCase()));

                    // postParameters.add(new BasicNameValuePair("password",json1.toString()));


                    json = jParser.makeHttpRequest(url_query, "GET", postParameters);
                    String s = null;
                    try {
                        s = json.getString("status");
                        QuerySrNumberListjson = json.getString("QuerySrNumber");
                        QueryListjson = json.getString("QueryList");
                        QueryTypeListjson = json.getString("QueryType");
                        FocusKeyWordListjson = json.getString("FocusKeyword");

                        resp = s;
                        Log.d("Kushal", resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                        network = 1;
                        errorMsg = e.getMessage();
                        Log.e("Error", errorMsg);
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
                        // int len = QuerySrNumberListjson.length();
                        Gson gson = new Gson();
                        QuerySrNumberList = gson.fromJson(QuerySrNumberListjson, ArrayList.class);
                        QueryList = gson.fromJson(QueryListjson, ArrayList.class);
                        QueryTypeList = gson.fromJson(QueryTypeListjson, ArrayList.class);
                        FocusKeyWordList = gson.fromJson(FocusKeyWordListjson, ArrayList.class);
                    } catch (Exception e) {
                        Log.e("Kushal1", e.toString());
                    }
                    adb = getApplicationContext().openOrCreateDatabase(QueryDatabase.DBNAME, MODE_PRIVATE, null);
                    adb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + CompanyName + "_" + selectedFromList + " (QueryNumber TEXT, Query TEXT,QueryType TEXT, Keyword TEXT);");

                    String count = "SELECT count(*) FROM " + CompanyName + "_" + selectedFromList;
                    Cursor mcursor = adb.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    Log.d("Kushal", "" + icount);
                    if (icount > 0) {
                        String delete = "Delete from " + CompanyName + "_" + selectedFromList;
                        Log.d("Kushal", CompanyName + "_" + selectedFromList);
                        adb.execSQL(delete);
                        int i;
                        for (i = 0; i < QuerySrNumberList.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("QueryNumber", QuerySrNumberList.get(i).toString());
                            value.put("Query", QueryList.get(i).toString());
                            value.put("QueryType", QueryTypeList.get(i).toString());
                            value.put("Keyword", FocusKeyWordList.get(i).toString());
                            adb.insert(CompanyName + "_" + selectedFromList, null, value);
                        }
                    } else {
                        int i;
                        for (i = 0; i < QuerySrNumberList.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("QueryNumber", QuerySrNumberList.get(i).toString());
                            value.put("Query", QueryList.get(i).toString());
                            value.put("QueryType", QueryTypeList.get(i).toString());
                            value.put("Keyword", FocusKeyWordList.get(i).toString());
                            adb.insert(CompanyName + "_" + selectedFromList, null, value);
                        }
                        Log.d("Kushal", CompanyName + "_" + selectedFromList);
                    }
                    adb = qdb.getWritableDatabase();
                    mcursor = adb.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount1 = mcursor.getInt(0);
                    Log.d("Kushal", "" + icount);
                    if (icount1 > 0) {
                        flag = true;
                    } else {
                        ContentValues value = new ContentValues();
                        value.put("QueryNumber", "Q1");
                        value.put("Query", "Query 1");
                        value.put("QueryType", "1");
                        value.put("Keyword", "Keyword 1");
                        adb.insert(CompanyName + "_" + selectedFromList, null, value);
                        flag = false;

                    }

                } else if (resp.equals("Failed")) {
                    error = 1;
                }


                if (null != errorMsg && !errorMsg.isEmpty()) {

                }


            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (NullPointerException n) {
            }
            return null;
        }//catch (Exception e) { Log.d("Kushal2",e.toString());

        @Override
        protected void onPostExecute(Void aVoid) {
            AndroidUtils.animateView(progressOverlay, image, animate, View.GONE, 0, 200);
            resp = "null";
            // Log.d("k", EmpNamejson.toString());

            if (flag) {
                adapter = new ViewEditAdapter1(Query_main.this, Data.getData());
                Content.setAdapter(adapter);
                fab.setVisibility(View.VISIBLE);
                back = true;
                Content.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 || dy < 0 && fab.isShown())
                            fab.hide();
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            fab.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            } else {
                fab.setVisibility(View.VISIBLE);
                back = true;
                Content.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 || dy < 0 && fab.isShown())
                            fab.hide();
                    }
                //new MyTask3().execute();
            });
            }
            if (error == 1) {
                error = 0;
                network = 0;
                final AlertDialog.Builder retry = new AlertDialog.Builder(Query_main.this);
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
                // Toast.makeText(Query_main.this, "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                error = 0;
                network = 0;
            } else if (network == 1) {
                // Toast.makeText(Query_main.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                /*error = 0;
                network = 0;
                final AlertDialog.Builder retry = new AlertDialog.Builder(Query_main.this);
                retry.setMessage("Connection Problem!!");
                retry.setTitle("Alert");
                retry.setCancelable(false);
                retry.setIcon(R.drawable.feedbot_final);
                retry.show();
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
                retry.show();*/
                //new MyTask3().execute();
            }
            super.onPostExecute(aVoid);
        }

    }

    public static class Data {

        public static ArrayList<Information> getData() {

            ArrayList<Information> data = new ArrayList<>();
            String selectQuery = "SELECT  Query, QueryType ,Keyword FROM " + CompanyName + "_" + selectedFromList;
            adb = qdb.getWritableDatabase();
            Cursor cursor = adb.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Information current = new Information();
                    current.Query = cursor.getString(0);
                    current.Options = cursor.getString(1);
                    current.Keyword = cursor.getString(2);
                    data.add(current);
                } while (cursor.moveToNext());


            }
            return data;

        }
    }

    public static class GetBranch {

        public static ArrayList<String> getData() {

            ArrayList<String> data = new ArrayList<>();
            String branchQuery = "Select Distinct Branch from " + CompanyName;
            sdb = db.getWritableDatabase();
            Cursor cursor = sdb.rawQuery(branchQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String current;
                    current = cursor.getString(0);
                    current = current.valueOf(current.charAt(0)).toUpperCase() + current.substring(1, current.length());
                    data.add(current);
                } while (cursor.moveToNext());
            }
            return data;

        }
    }

    public static class BranchInformationQuery {
        public String Branch;
    }


    /**
     * Created by MyMac on 30/06/16.
     */
    public static class ViewEditAdapter1 extends RecyclerView.Adapter<ViewEditAdapter1.MyViewHolder> {


        private Context context;

        private ArrayList<Information> query;

        private LayoutInflater inflater;
        String Name;


        private int previousPosition = 0;

        QueryDatabase qdb;
        SQLiteDatabase qsdb;


        private static final String PREFER_NAME = "AndroidExamplePref";
        public static final String KEY_COMPANYNAME = "CompanyName";
        SharedPreferences sp;
        public String BranchName;

        private static final String REFERNCE = "Reference";
        public static final String BRANCHNAME = "BranchName";
        SharedPreferences sp1;
        public String CompanyName;


        public ViewEditAdapter1(Context context, ArrayList<Information> query) {
            this.context = context;
            this.query = query;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_edit_query, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

            final TextView Query = (TextView) myViewHolder.view.findViewById(R.id.ViewQuery);
            final TextView Keyword = (TextView) myViewHolder.view.findViewById(R.id.ViewKeyword);
            final RelativeLayout ViewLayout = (RelativeLayout) myViewHolder.view.findViewById(R.id.ViewLayout);
            final ImageView show = (ImageView) myViewHolder.view.findViewById(R.id.ViewOption);
            final ImageView more = (ImageView) myViewHolder.view.findViewById(R.id.icon_more);
            String OptMain = (query.get(position).Options);
            Query.setText(query.get(position).Query);
            Keyword.setText(query.get(position).Keyword);
            Name = Query.getText().toString();
            if (OptMain.equals("1")) {

                show.setColorFilter(Color.rgb(166, 70, 86));
            } else if (OptMain.equals("2")) {

                show.setColorFilter(Color.rgb(74, 166, 80));
            } else if (OptMain.equals("3")) {

                show.setColorFilter(Color.rgb(70, 74, 166));
            } else if (OptMain.equals("4")) {

                show.setColorFilter(Color.rgb(230, 198, 19));
            }

            if (position > previousPosition) { // We are scrolling DOWN

                AnimationUtil.animate(myViewHolder, true);

            } else { // We are scrolling UP

                AnimationUtil.animate(myViewHolder, false);
            }
            previousPosition = position;

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu Pmenu = new PopupMenu(context, v);
                    MenuInflater mi = Pmenu.getMenuInflater();
                    mi.inflate(R.menu.menu_overflow, Pmenu.getMenu());
                    Pmenu.show();
                    Pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit:
                                    final AlertDialog.Builder AddNewQuery = new AlertDialog.Builder(context);
                                    View vw1 = inflater.inflate(R.layout.list_item_row, null);
                                    final AlertDialog dialog = AddNewQuery.create();
                                    dialog.setView(vw1);
                                    AddNewQuery.setView(vw1);
                                    dialog.show();


                                    final FloatingActionButton Next, Finish, Add;
                                    final EditText edittext, edittextkey;
                                    final TextView Query;
                                    final Animation fab_open, fab_close, rotate_forward, rotate_backward;
                                    final LinearLayout CardOptions;
                                    final RadioButton rb1, rb2, rb3, rb4;
                                    final RadioGroup rg;
                                    final String option;
                                    final TextInputLayout et, etk;

                                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                                    fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);
                                    rotate_forward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
                                    rotate_backward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward);

                                    edittext = (EditText) vw1.findViewById(R.id.QueryMain);
                                    edittextkey = (EditText) vw1.findViewById(R.id.QueryKeywordMain);
                                    Next = (FloatingActionButton) vw1.findViewById(R.id.next);
                                    Add = (FloatingActionButton) vw1.findViewById(R.id.add);
                                    Finish = (FloatingActionButton) vw1.findViewById(R.id.finish);
                                    Query = (TextView) vw1.findViewById(R.id.query);
                                    CardOptions = (LinearLayout) vw1.findViewById(R.id.OptionContent);
                                    rg = (RadioGroup) vw1.findViewById(R.id.rg);
                                    rb1 = (RadioButton) vw1.findViewById(R.id.rb1);
                                    rb2 = (RadioButton) vw1.findViewById(R.id.rb2);
                                    rb3 = (RadioButton) vw1.findViewById(R.id.rb3);
                                    rb4 = (RadioButton) vw1.findViewById(R.id.rb4);
                                    et = (TextInputLayout) vw1.findViewById(R.id.et);
                                    etk = (TextInputLayout) vw1.findViewById(R.id.etk);


                                    edittext.setText(query.get(position).Query);
                                    edittextkey.setText(query.get(position).Keyword);
                                    option = query.get(position).Options;
                                    if (option.equals("1")) {
                                        rb1.setChecked(true);
                                        CardOptions.removeAllViews();
                                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                        LinearLayout l1 = new LinearLayout(context);
                                        l1.setLayoutParams(LayoutParams);
                                        l1.setOrientation(LinearLayout.HORIZONTAL);
                                        l1.setGravity(Gravity.CENTER);

                                        Button yes = new Button(context);
                                        yes.setBackgroundColor(Color.GRAY);
                                        yes.setTextColor(Color.BLACK);
                                        yes.setText("Yes");
                                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                                        yes.setLayoutParams(buttonLayoutParams);

                                        Button no = new Button(context);
                                        no.setBackgroundColor(Color.GRAY);
                                        no.setTextColor(Color.BLACK);
                                        no.setText("No");
                                        no.setLayoutParams(buttonLayoutParams);
                                        l1.addView(yes);
                                        l1.addView(no);
                                        CardOptions.addView(l1);
                                    } else if (option.equals("2")) {
                                        rb2.setChecked(true);
                                        CardOptions.removeAllViews();
                                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                        LinearLayout l = new LinearLayout(context);
                                        l.setLayoutParams(LayoutParams);
                                        l.setOrientation(LinearLayout.VERTICAL);
                                        l.setGravity(Gravity.CENTER);
                                        l.setWeightSum(1);
                                        LinearLayout l1 = new LinearLayout(context);
                                        l1.setOrientation(LinearLayout.HORIZONTAL);
                                        l1.setGravity(Gravity.CENTER);
                                        l1.setLayoutParams(LayoutParams);
                                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                                        Button bad = new Button(context);
                                        bad.setLayoutParams(buttonLayoutParams);
                                        bad.setBackgroundColor(Color.GRAY);
                                        bad.setTextColor(Color.BLACK);
                                        bad.setText("Bad");
                                        Button good = new Button(context);
                                        good.setLayoutParams(buttonLayoutParams);
                                        good.setBackgroundColor(Color.GRAY);
                                        good.setTextColor(Color.BLACK);
                                        good.setText("Good");
                                        l1.addView(bad);
                                        l1.addView(good);
                                        LinearLayout l2 = new LinearLayout(context);
                                        l2.setOrientation(LinearLayout.HORIZONTAL);
                                        l2.setGravity(Gravity.CENTER);
                                        l2.setLayoutParams(LayoutParams);
                                        Button vgood = new Button(context);
                                        vgood.setLayoutParams(buttonLayoutParams);
                                        vgood.setBackgroundColor(Color.GRAY);
                                        vgood.setTextColor(Color.BLACK);
                                        vgood.setText("Very Good");
                                        Button awesome = new Button(context);
                                        awesome.setLayoutParams(buttonLayoutParams);
                                        awesome.setBackgroundColor(Color.GRAY);
                                        awesome.setTextColor(Color.BLACK);
                                        awesome.setText("Awesome");
                                        l2.addView(vgood);
                                        l2.addView(awesome);
                                        l.addView(l1);
                                        l.addView(l2);
                                        CardOptions.addView(l);
                                    } else if (option.equals("3")) {
                                        rb3.setChecked(true);
                                        CardOptions.removeAllViews();
                                        LinearLayout l = new LinearLayout(context);
                                        l.setOrientation(LinearLayout.VERTICAL);
                                        l.setGravity(Gravity.CENTER_VERTICAL);
                                        SeekBar sb = new SeekBar(context);
                                        sb.setMax(10);
                                        l.addView(sb);
                                        CardOptions.addView(l);
                                    } else if (option.equals("4")) {
                                        rb4.setChecked(true);
                                        CardOptions.removeAllViews();
                                        LinearLayout l = new LinearLayout(context);
                                        l.setOrientation(LinearLayout.VERTICAL);
                                        l.setGravity(Gravity.CENTER_VERTICAL);
                                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        l.addView(layoutInflater.inflate(R.layout.rating_bar, CardOptions, false));
                                        CardOptions.addView(l);
                                    }

                                    rb1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CardOptions.removeAllViews();
                                            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            LinearLayout l1 = new LinearLayout(context);
                                            l1.setLayoutParams(LayoutParams);
                                            l1.setOrientation(LinearLayout.HORIZONTAL);
                                            l1.setGravity(Gravity.CENTER);

                                            Button yes = new Button(context);
                                            yes.setBackgroundColor(Color.GRAY);
                                            yes.setTextColor(Color.BLACK);
                                            yes.setText("Yes");
                                            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            buttonLayoutParams.setMargins(10, 10, 10, 10);
                                            yes.setLayoutParams(buttonLayoutParams);

                                            Button no = new Button(context);
                                            no.setBackgroundColor(Color.GRAY);
                                            no.setTextColor(Color.BLACK);
                                            no.setText("No");
                                            no.setLayoutParams(buttonLayoutParams);
                                            l1.addView(yes);
                                            l1.addView(no);
                                            CardOptions.addView(l1);
                                        }
                                    });

                                    rb2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CardOptions.removeAllViews();
                                            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            LinearLayout l = new LinearLayout(context);
                                            l.setLayoutParams(LayoutParams);
                                            l.setOrientation(LinearLayout.VERTICAL);
                                            l.setGravity(Gravity.CENTER);
                                            l.setWeightSum(1);
                                            LinearLayout l1 = new LinearLayout(context);
                                            l1.setOrientation(LinearLayout.HORIZONTAL);
                                            l1.setGravity(Gravity.CENTER);
                                            l1.setLayoutParams(LayoutParams);
                                            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                                            buttonLayoutParams.setMargins(10, 10, 10, 10);
                                            Button bad = new Button(context);
                                            bad.setLayoutParams(buttonLayoutParams);
                                            bad.setBackgroundColor(Color.GRAY);
                                            bad.setTextColor(Color.BLACK);
                                            bad.setText("Bad");
                                            Button good = new Button(context);
                                            good.setLayoutParams(buttonLayoutParams);
                                            good.setBackgroundColor(Color.GRAY);
                                            good.setTextColor(Color.BLACK);
                                            good.setText("Good");
                                            l1.addView(bad);
                                            l1.addView(good);
                                            LinearLayout l2 = new LinearLayout(context);
                                            l2.setOrientation(LinearLayout.HORIZONTAL);
                                            l2.setGravity(Gravity.CENTER);
                                            l2.setLayoutParams(LayoutParams);
                                            Button vgood = new Button(context);
                                            vgood.setLayoutParams(buttonLayoutParams);
                                            vgood.setBackgroundColor(Color.GRAY);
                                            vgood.setTextColor(Color.BLACK);
                                            vgood.setText("Very Good");
                                            Button awesome = new Button(context);
                                            awesome.setLayoutParams(buttonLayoutParams);
                                            awesome.setBackgroundColor(Color.GRAY);
                                            awesome.setTextColor(Color.BLACK);
                                            awesome.setText("Awesome");
                                            l2.addView(vgood);
                                            l2.addView(awesome);
                                            l.addView(l1);
                                            l.addView(l2);
                                            CardOptions.addView(l);
                                        }
                                    });

                                    rb3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CardOptions.removeAllViews();
                                            LinearLayout l = new LinearLayout(context);
                                            l.setOrientation(LinearLayout.VERTICAL);
                                            l.setGravity(Gravity.CENTER_VERTICAL);
                                            SeekBar sb = new SeekBar(context);
                                            sb.setMax(10);
                                            l.addView(sb);
                                            CardOptions.addView(l);
                                        }
                                    });

                                    rb4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CardOptions.removeAllViews();
                                            LinearLayout l = new LinearLayout(context);
                                            l.setOrientation(LinearLayout.VERTICAL);
                                            l.setGravity(Gravity.CENTER_VERTICAL);
                                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            l.addView(layoutInflater.inflate(R.layout.rating_bar, CardOptions, false));
                                            CardOptions.addView(l);
                                        }
                                    });

                                    edittext.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Query.setText(s.toString());
                                        }
                                    });


                                    Next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            animateFAB();
                                        }

                                        Boolean isFabOpen = false;

                                        public void animateFAB() {

                                            if (isFabOpen) {

                                                Next.startAnimation(rotate_backward);
                                                Finish.startAnimation(fab_close);
                                                Add.startAnimation(fab_close);
                                                Finish.setClickable(false);
                                                Add.setClickable(false);
                                                isFabOpen = false;
                                                Log.d("Raj", "close");

                                            } else {

                                                Next.startAnimation(rotate_forward);
                                                Finish.startAnimation(fab_open);
                                                Add.startAnimation(fab_open);
                                                Finish.setClickable(true);
                                                Add.setClickable(true);
                                                isFabOpen = true;
                                                Log.d("Raj", "open");

                                            }
                                        }


                                    });
                                    Add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String opt;
                                            opt = "null";
                                            switch (rg.getCheckedRadioButtonId()) {
                                                case R.id.rb1:
                                                    opt = "1";
                                                    break;
                                                case R.id.rb2:
                                                    opt = "2";
                                                    break;
                                                case R.id.rb3:
                                                    opt = "3";
                                                    break;
                                                case R.id.rb4:
                                                    opt = "4";
                                                    break;
                                            }
                                            if (edittext.getText().toString().equals("")) {
                                                et.setError("The field cannot left blank");
                                            } else if (edittextkey.getText().toString().equals("")) {
                                                etk.setError("The field cannot left blank");
                                            } else if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                                                Information i = new Information();
                                                i.Query = edittext.getText().toString();
                                                i.Keyword = edittextkey.getText().toString();
                                                i.Options = opt;
                                                query.set(position, i);
                                                sp = context.getSharedPreferences(PREFER_NAME, context.MODE_PRIVATE);
                                                sp1 = context.getSharedPreferences(REFERNCE, context.MODE_PRIVATE);
                                                CompanyName = sp.getString(KEY_COMPANYNAME, null);
                                                BranchName = sp1.getString(BRANCHNAME, null);
                                                qdb = new QueryDatabase(context);
                                                qsdb = qdb.getWritableDatabase();
                                                String qry = "Update " + CompanyName + "_" + BranchName + " set Query= '" + i.Query + "', QueryType= '" + i.Options + "', Keyword= '" + i.Keyword + "' where QueryNumber= 'Q" + (position + 1) + "'";
                                                qsdb.execSQL(qry);
                                                dialog.dismiss();
                                                adapter = new ViewEditAdapter1(context, Data.getData());
                                                Content.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(context, "Please select the option", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    });


                                    Finish.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    return true;
                                case R.id.action_delete:
                                    final AlertDialog.Builder delete = new AlertDialog.Builder(context);
                                    delete.setMessage("Are you sure you want to delete the Query?");
                                    delete.setTitle("Alert");
                                    delete.setCancelable(false);
                                    delete.setIcon(R.drawable.feedbot_final);
                                    delete.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (position != 0) {
                                                int currPosition = query.indexOf(query.get(position));
                                                query.remove(currPosition);
                                                notifyItemRemoved(currPosition);
                                                notifyItemRangeChanged(position, query.size());
                                                sp = context.getSharedPreferences(PREFER_NAME, context.MODE_PRIVATE);
                                                sp1 = context.getSharedPreferences(REFERNCE, context.MODE_PRIVATE);
                                                CompanyName = sp.getString(KEY_COMPANYNAME, null);
                                                BranchName = sp1.getString(BRANCHNAME, null);
                                                qdb = new QueryDatabase(context);
                                                qsdb = qdb.getWritableDatabase();
                                                String qry = "delete from " + CompanyName + "_" + BranchName + " where QueryNumber= 'Q" + (position + 1) + "'";
                                                qsdb.execSQL(qry);
                                                String qry1 = "select Query, QueryType, Keyword from " + CompanyName + "_" + BranchName + "";
                                                ArrayList<String> q = new ArrayList<String>();
                                                ArrayList<String> k = new ArrayList<String>();
                                                ArrayList<String> o = new ArrayList<String>();
                                                qsdb = qdb.getWritableDatabase();
                                                Cursor cursor = qsdb.rawQuery(qry1, null);
                                                if (cursor.moveToFirst()) {
                                                    do {

                                                        q.add(cursor.getString(0));
                                                        o.add(cursor.getString(1));
                                                        k.add(cursor.getString(2));
                                                    } while (cursor.moveToNext());
                                                }
                                                String qry2 = "Delete from " + CompanyName + "_" + BranchName + "";
                                                adb.execSQL(qry2);
                                                int i;
                                                for (i = 0; i < q.size(); i++) {
                                                    ContentValues value = new ContentValues();
                                                    value.put("QueryNumber", "Q" + (i + 1));
                                                    value.put("Query", q.get(i).toString());
                                                    value.put("QueryType", o.get(i).toString());
                                                    value.put("Keyword", k.get(i).toString());
                                                    adb.insert(CompanyName + "_" + BranchName, null, value);
                                                }


                                                adapter = new ViewEditAdapter1(context, Data.getData());
                                                Content.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(context, "At least one query should be present", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    delete.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    delete.show();

                                    return true;
                            }
                            return false;
                        }
                    });
                }
            });
            Query.setText(query.get(position).Query);
            Keyword.setText(query.get(position).Keyword);

            ViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder AddNewQuery = new AlertDialog.Builder(context);
                    View vw1 = inflater.inflate(R.layout.list_item_row, null);
                    final AlertDialog dialog = AddNewQuery.create();
                    dialog.setView(vw1);
                    AddNewQuery.setView(vw1);
                    dialog.show();


                    final FloatingActionButton Next, Finish, Add;
                    final EditText edittext, edittextkey;
                    final TextView Query;
                    final Animation fab_open, fab_close, rotate_forward, rotate_backward;
                    final LinearLayout CardOptions;
                    final RadioButton rb1, rb2, rb3, rb4;
                    final RadioGroup rg;
                    final String option;
                    final TextInputLayout et, etk;

                    fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
                    fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);
                  //  rotate_forward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
                    //rotate_backward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward);

                    edittext = (EditText) vw1.findViewById(R.id.QueryMain);
                    edittextkey = (EditText) vw1.findViewById(R.id.QueryKeywordMain);
                    Next = (FloatingActionButton) vw1.findViewById(R.id.next);
                    Add = (FloatingActionButton) vw1.findViewById(R.id.add);
                    Finish = (FloatingActionButton) vw1.findViewById(R.id.finish);
                    Query = (TextView) vw1.findViewById(R.id.query);
                    CardOptions = (LinearLayout) vw1.findViewById(R.id.OptionContent);
                    rg = (RadioGroup) vw1.findViewById(R.id.rg);
                    rb1 = (RadioButton) vw1.findViewById(R.id.rb1);
                    rb2 = (RadioButton) vw1.findViewById(R.id.rb2);
                    rb3 = (RadioButton) vw1.findViewById(R.id.rb3);
                    rb4 = (RadioButton) vw1.findViewById(R.id.rb4);
                    et = (TextInputLayout) vw1.findViewById(R.id.et);
                    etk = (TextInputLayout) vw1.findViewById(R.id.etk);


                    edittext.setText(query.get(position).Query);
                    edittextkey.setText(query.get(position).Keyword);
                    option = query.get(position).Options;
                    if (option.equals("1")) {
                        rb1.setChecked(true);
                        CardOptions.removeAllViews();
                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        LinearLayout l1 = new LinearLayout(context);
                        l1.setLayoutParams(LayoutParams);
                        l1.setOrientation(LinearLayout.HORIZONTAL);
                        l1.setGravity(Gravity.CENTER);

                        Button yes = new Button(context);
                        yes.setBackgroundColor(Color.GRAY);
                        yes.setTextColor(Color.BLACK);
                        yes.setText("Yes");
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                        yes.setLayoutParams(buttonLayoutParams);

                        Button no = new Button(context);
                        no.setBackgroundColor(Color.GRAY);
                        no.setTextColor(Color.BLACK);
                        no.setText("No");
                        no.setLayoutParams(buttonLayoutParams);
                        l1.addView(yes);
                        l1.addView(no);
                        CardOptions.addView(l1);
                    } else if (option.equals("2")) {
                        rb2.setChecked(true);
                        CardOptions.removeAllViews();
                        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        LinearLayout l = new LinearLayout(context);
                        l.setLayoutParams(LayoutParams);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER);
                        l.setWeightSum(1);
                        LinearLayout l1 = new LinearLayout(context);
                        l1.setOrientation(LinearLayout.HORIZONTAL);
                        l1.setGravity(Gravity.CENTER);
                        l1.setLayoutParams(LayoutParams);
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(10, 10, 10, 10);
                        Button bad = new Button(context);
                        bad.setLayoutParams(buttonLayoutParams);
                        bad.setBackgroundColor(Color.GRAY);
                        bad.setTextColor(Color.BLACK);
                        bad.setText("Bad");
                        Button good = new Button(context);
                        good.setLayoutParams(buttonLayoutParams);
                        good.setBackgroundColor(Color.GRAY);
                        good.setTextColor(Color.BLACK);
                        good.setText("Good");
                        l1.addView(bad);
                        l1.addView(good);
                        LinearLayout l2 = new LinearLayout(context);
                        l2.setOrientation(LinearLayout.HORIZONTAL);
                        l2.setGravity(Gravity.CENTER);
                        l2.setLayoutParams(LayoutParams);
                        Button vgood = new Button(context);
                        vgood.setLayoutParams(buttonLayoutParams);
                        vgood.setBackgroundColor(Color.GRAY);
                        vgood.setTextColor(Color.BLACK);
                        vgood.setText("Very Good");
                        Button awesome = new Button(context);
                        awesome.setLayoutParams(buttonLayoutParams);
                        awesome.setBackgroundColor(Color.GRAY);
                        awesome.setTextColor(Color.BLACK);
                        awesome.setText("Awesome");
                        l2.addView(vgood);
                        l2.addView(awesome);
                        l.addView(l1);
                        l.addView(l2);
                        CardOptions.addView(l);
                    } else if (option.equals("3")) {
                        rb3.setChecked(true);
                        CardOptions.removeAllViews();
                        LinearLayout l = new LinearLayout(context);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER_VERTICAL);
                        SeekBar sb = new SeekBar(context);
                        sb.setMax(10);
                        l.addView(sb);
                        CardOptions.addView(l);
                    } else if (option.equals("4")) {
                        rb4.setChecked(true);
                        CardOptions.removeAllViews();
                        LinearLayout l = new LinearLayout(context);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER_VERTICAL);
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        l.addView(layoutInflater.inflate(R.layout.rating_bar, CardOptions, false));
                        CardOptions.addView(l);
                    }

                    rb1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardOptions.removeAllViews();
                            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            LinearLayout l1 = new LinearLayout(context);
                            l1.setLayoutParams(LayoutParams);
                            l1.setOrientation(LinearLayout.HORIZONTAL);
                            l1.setGravity(Gravity.CENTER);

                            Button yes = new Button(context);
                            yes.setBackgroundColor(Color.GRAY);
                            yes.setTextColor(Color.BLACK);
                            yes.setText("Yes");
                            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            buttonLayoutParams.setMargins(10, 10, 10, 10);
                            yes.setLayoutParams(buttonLayoutParams);

                            Button no = new Button(context);
                            no.setBackgroundColor(Color.GRAY);
                            no.setTextColor(Color.BLACK);
                            no.setText("No");
                            no.setLayoutParams(buttonLayoutParams);
                            l1.addView(yes);
                            l1.addView(no);
                            CardOptions.addView(l1);
                        }
                    });

                    rb2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardOptions.removeAllViews();
                            LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            LinearLayout l = new LinearLayout(context);
                            l.setLayoutParams(LayoutParams);
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setGravity(Gravity.CENTER);
                            l.setWeightSum(1);
                            LinearLayout l1 = new LinearLayout(context);
                            l1.setOrientation(LinearLayout.HORIZONTAL);
                            l1.setGravity(Gravity.CENTER);
                            l1.setLayoutParams(LayoutParams);
                            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            buttonLayoutParams.setMargins(10, 10, 10, 10);
                            Button bad = new Button(context);
                            bad.setLayoutParams(buttonLayoutParams);
                            bad.setBackgroundColor(Color.GRAY);
                            bad.setTextColor(Color.BLACK);
                            bad.setText("Bad");
                            Button good = new Button(context);
                            good.setLayoutParams(buttonLayoutParams);
                            good.setBackgroundColor(Color.GRAY);
                            good.setTextColor(Color.BLACK);
                            good.setText("Good");
                            l1.addView(bad);
                            l1.addView(good);
                            LinearLayout l2 = new LinearLayout(context);
                            l2.setOrientation(LinearLayout.HORIZONTAL);
                            l2.setGravity(Gravity.CENTER);
                            l2.setLayoutParams(LayoutParams);
                            Button vgood = new Button(context);
                            vgood.setLayoutParams(buttonLayoutParams);
                            vgood.setBackgroundColor(Color.GRAY);
                            vgood.setTextColor(Color.BLACK);
                            vgood.setText("Very Good");
                            Button awesome = new Button(context);
                            awesome.setLayoutParams(buttonLayoutParams);
                            awesome.setBackgroundColor(Color.GRAY);
                            awesome.setTextColor(Color.BLACK);
                            awesome.setText("Awesome");
                            l2.addView(vgood);
                            l2.addView(awesome);
                            l.addView(l1);
                            l.addView(l2);
                            CardOptions.addView(l);
                        }
                    });

                    rb3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardOptions.removeAllViews();
                            LinearLayout l = new LinearLayout(context);
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setGravity(Gravity.CENTER_VERTICAL);
                            SeekBar sb = new SeekBar(context);
                            sb.setMax(10);
                            l.addView(sb);
                            CardOptions.addView(l);
                        }
                    });

                    rb4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardOptions.removeAllViews();
                            LinearLayout l = new LinearLayout(context);
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setGravity(Gravity.CENTER_VERTICAL);
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            l.addView(layoutInflater.inflate(R.layout.rating_bar, CardOptions, false));
                            CardOptions.addView(l);
                        }
                    });

                    edittext.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Query.setText(s.toString());
                        }
                    });


                    Next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            animateFAB();
                        }

                        Boolean isFabOpen = false;

                        public void animateFAB() {

                            if (isFabOpen) {

                             //   Next.startAnimation(rotate_backward);
                                Finish.startAnimation(fab_close);
                                Add.startAnimation(fab_close);
                                Finish.setClickable(false);
                                Add.setClickable(false);
                                isFabOpen = false;
                                Log.d("Raj", "close");

                            } else {

                             //   Next.startAnimation(rotate_forward);
                                Finish.startAnimation(fab_open);
                                Add.startAnimation(fab_open);
                                Finish.setClickable(true);
                                Add.setClickable(true);
                                isFabOpen = true;
                                Log.d("Raj", "open");

                            }
                        }


                    });
                    Add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String opt;
                            opt = "null";
                            switch (rg.getCheckedRadioButtonId()) {
                                case R.id.rb1:
                                    opt = "1";
                                    break;
                                case R.id.rb2:
                                    opt = "2";
                                    break;
                                case R.id.rb3:
                                    opt = "3";
                                    break;
                                case R.id.rb4:
                                    opt = "4";
                                    break;
                            }
                            if (edittext.getText().toString().equals("")) {
                                et.setError("The field cannot left blank");
                            } else if (edittextkey.getText().toString().equals("")) {
                                etk.setError("The field cannot left blank");
                            } else if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                                Information i = new Information();
                                i.Query = edittext.getText().toString();
                                i.Keyword = edittextkey.getText().toString();
                                i.Options = opt;
                                query.set(position, i);
                                sp = context.getSharedPreferences(PREFER_NAME, context.MODE_PRIVATE);
                                sp1 = context.getSharedPreferences(REFERNCE, context.MODE_PRIVATE);
                                CompanyName = sp.getString(KEY_COMPANYNAME, null);
                                BranchName = sp1.getString(BRANCHNAME, null);
                                qdb = new QueryDatabase(context);
                                qsdb = qdb.getWritableDatabase();
                                String qry = "Update " + CompanyName + "_" + BranchName + " set Query= '" + i.Query + "', QueryType= '" + i.Options + "', Keyword= '" + i.Keyword + "' where QueryNumber= 'Q" + (position + 1) + "'";
                                qsdb.execSQL(qry);
                                dialog.dismiss();
                                adapter = new ViewEditAdapter1(context, Data.getData());
                                Content.setAdapter(adapter);
                            } else {
                                Toast.makeText(context, "Please select the option", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });


                    Finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            });

        }


        @Override
        public int getItemCount() {
            return query.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;

            public MyViewHolder(View itemView) {
                super(itemView);
                view = itemView;
            }


        }
        // This method adds(duplicates) a Object (item ) to our Branch set as well as Recycler View.


    }

    private class MyTask2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
            mProgressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> id = new ArrayList<String>();
                    ArrayList<String> q = new ArrayList<String>();
                    ArrayList<String> k = new ArrayList<String>();
                    ArrayList<String> o = new ArrayList<String>();
                    adb = qdb.getWritableDatabase();
                    sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
                    sp1 = getSharedPreferences(REFERNCE, MODE_PRIVATE);
                    CompanyName = sp.getString(KEY_COMPANYNAME, null);
                    BranchName = sp1.getString(BRANCHNAME, null);
                    String qry1 = "select QueryNumber,Query, QueryType, Keyword from " + CompanyName + "_" + BranchName + "";
                    Cursor cursor = adb.rawQuery(qry1, null);
                    if (cursor.moveToFirst()) {
                        do {

                            id.add(cursor.getString(0));
                            q.add(cursor.getString(1));
                            o.add(cursor.getString(2));
                            k.add(cursor.getString(3));
                        } while (cursor.moveToNext());
                    }

                    Gson gson = new Gson();
                    String QuerySrNumberS = gson.toJson(id);
                    String QueryS = gson.toJson(q);
                    String QueryTypeS = gson.toJson(o);
                    String FocusKeywordS = gson.toJson(k);


                    Log.d("Info", CompanyName + " " + BranchName.toLowerCase() + " " + QuerySrNumberS + " " + QueryS + " " + QueryTypeS + " " + FocusKeywordS);


                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("CompanyName", CompanyName));
                    postParameters.add(new BasicNameValuePair("BranchLocation", BranchName.toLowerCase()));
                    postParameters.add(new BasicNameValuePair("QuerySrNumber", QuerySrNumberS));
                    postParameters.add(new BasicNameValuePair("Query", QueryS));
                    postParameters.add(new BasicNameValuePair("QueryType", QueryTypeS));
                    postParameters.add(new BasicNameValuePair("FocusKeyword", FocusKeywordS));


                    // postParameters.add(new BasicNameValuePair("password",json1.toString()));


                    json = jParser.makeHttpRequest(url_add, "GET", postParameters);
                    String s = null;
                    try {


                        s = json.getString("status");


                        resp = s;
                        Log.d("Info", resp);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //network=1;
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
                    intent = new Intent(Query_main.this, MainActivity.class);
                    startActivity(intent);
                    finish();

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
            // AndroidUtils.animateView(progressOverlay,image,animate, View.GONE, 0, 200);
            mProgressDialog.dismiss();
            resp = "null";
            // Log.d("k", EmpNamejson.toString());
            if (network == 1) {
                // Toast.makeText(LoginActivity.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                error = 0;
                network = 0;
                final AlertDialog.Builder retry = new AlertDialog.Builder(Query_main.this);
                retry.setMessage("Connection Problem!!");
                retry.setTitle("Alert");
                retry.setCancelable(false);
                retry.setIcon(R.drawable.feedbot_final);
                retry.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new MyTask2().execute();
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


            super.onPostExecute(aVoid);
        }

    }

}

