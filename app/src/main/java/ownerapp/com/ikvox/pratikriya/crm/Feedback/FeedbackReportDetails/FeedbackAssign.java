package ownerapp.com.ikvox.pratikriya.crm.Feedback.FeedbackReportDetails;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;
import ownerapp.com.ikvox.pratikriya.EmailSender.GMailSender;
import ownerapp.com.ikvox.pratikriya.JSONParserIkVox;
import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.crm.Branch.BranchLayoutDetails.EmployeeInformation;
import ownerapp.com.ikvox.pratikriya.slidingUpPanel.SlidingUpPanelLayout;

/**
 * Created by MyMac on 22/06/16.
 */
public class FeedbackAssign extends ActionBarActivity {
    Toolbar toolbar;
    FrameLayout statusBar;
    public static String FBName;
    RelativeLayout OpenQuery;
    private SlidingUpPanelLayout mLayout;
    LinearLayout list;
    public static String selectedFromList;
    TextView PositiveFeedback, NegativeFeedback;
    String PFeedback = "0";
    String NFeedback = "0";
    LinearLayout Assign, AssignEmployee;
    RecyclerView Employee;
    AssignAdapter empAdapter;
    public static MyDatabase db;
    public static SQLiteDatabase sdb;

    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    public static final String KEY_NAME = "name";
    SharedPreferences sp;
    static String CompanyName;

    private static final String REFERNCE = "Reference";
    public static final String BRANCHNAME = "BranchName";
    SharedPreferences sp1;
    static String BranchName;
    public static QueryDatabase qdb;
    public static SQLiteDatabase qsdb;
    TextView ClickAssign;

    ImageView noti;
    static Context c;
    TextView Query;

    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json;
    private static String url_login = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/login.do";
    private String resp;
    private String errorMsg;


   public static String Fname,Lname;
    public static String Assignee;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_assignment);
        toolbarStatusBar();
        c= getApplicationContext();
        navigationBarStatusBar();
        toolbar.setBackgroundColor(MainActivity.color);
        sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        CompanyName = sp.getString(KEY_COMPANYNAME, null);
        ClickAssign= (TextView)findViewById(R.id.clickAssign);
        ClickAssign.setVisibility(View.INVISIBLE);

        sp1 = getSharedPreferences(REFERNCE, MODE_PRIVATE);
        BranchName = sp1.getString(BRANCHNAME, null);

        db = new MyDatabase(getApplicationContext());
        qdb= new QueryDatabase(getApplicationContext());
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_feedback);
        list = (LinearLayout) findViewById(R.id.dragViewFeedback);
        list.setBackgroundColor(MainActivity.color);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        OpenQuery = (RelativeLayout) findViewById(R.id.slidingQuery);
        Query = (TextView) findViewById(R.id.selectQuery);
        PositiveFeedback = (TextView) findViewById(R.id.PositiveFeedback);
        NegativeFeedback = (TextView) findViewById(R.id.NegativeFeedback);
        Assign = (LinearLayout) findViewById(R.id.AssignLayout);
        AssignEmployee = (LinearLayout) findViewById(R.id.AssignEmployee);
        Employee = (RecyclerView) findViewById(R.id.AssignEmployeeView);
        OpenQuery.setBackgroundColor(MainActivity.color);
        noti= (ImageView)findViewById(R.id.notiQuery);
        Assign.setEnabled(false);
        OpenQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout.getAnchorPoint() == 1.0f) {
                    mLayout.setAnchorPoint(0.7f);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    noti.setImageResource(R.drawable.up);
                }
            }
        });

        final ListView lv = (ListView)findViewById(R.id.listQuery);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show();
                noti.setImageResource(R.drawable.down);
                mLayout.setAnchorPoint(1.0f);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                selectedFromList = (String) (lv.getItemAtPosition(position));
                Query.setText(selectedFromList);
                if(Query.equals("")){

                    Assign.setEnabled(false);
                    ClickAssign.setVisibility(View.INVISIBLE);

                }
                else{

                    Assign.setEnabled(true);
                    ClickAssign.setVisibility(View.VISIBLE);

                }
                //need some changes in below to select more query and also it should be in dynamic

                if  (selectedFromList.equals("Q1")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.a);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.a));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q2")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.b);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.b));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q3")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.c);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.c));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q4")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.d);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.d));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q5")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.e);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.e));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q6")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.f);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.f));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q7")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.g);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.g));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q8")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.h);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.h));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }else if(selectedFromList.equals("Q9")) {

                    NFeedback=String.valueOf(Math.round((Integer.parseInt(FeedbackbranchDetails.FeedbackNumber)))- FeedbackbranchDetails.i);

                    PositiveFeedback.setText("Total No. of Positive Feedback: " +String.valueOf(FeedbackbranchDetails.i));
                    NegativeFeedback.setText("Total No. of Negative Feedback: " + NFeedback);

                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

                }
            }
        });

        Assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    empAdapter = new AssignAdapter(FeedbackAssign.this, Employee_list.getData());
                    Employee.setAdapter(empAdapter);
                    Employee.setLayoutManager(new LinearLayoutManager(FeedbackAssign.this));
            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                GetQuery.getData());

        lv.setAdapter(arrayAdapter);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Query", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("Query", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_assign);
        toolbar = (Toolbar) findViewById(R.id.toolbar_assign);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(FBName);
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
                Dialog dialog = new Dialog(FeedbackAssign.this);
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
                FeedbackAssign.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_assign);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                FeedbackAssign.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_assign);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                FeedbackAssign.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_assign);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                FeedbackAssign.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    public static void BranchName(String BranchName) {
        FBName = BranchName;
    }

    public static class Employee_list {
        public static ArrayList<EmployeeInformation> getData() {

            ArrayList<EmployeeInformation> employee = new ArrayList<>();
            try{
            String selectQuery = "SELECT  Employee, Designation, Phone, Email FROM " + CompanyName + " where Branch = " + "'" + FBName.toLowerCase() + "'";
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
    public static class GetQuery {

        public static ArrayList<String> getData() {
            ArrayList<String> data = new ArrayList<>();
            Log.d("test",""+CompanyName+"_"+FBName);
            try{
            String branchQuery = "Select QueryNumber from " +CompanyName+"_"+FBName;
            qsdb = qdb.getWritableDatabase();
            Cursor cursor = qsdb.rawQuery(branchQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String current;
                    current= cursor.getString(0);
                    data.add(current);
                } while (cursor.moveToNext());
            }
            }catch (Exception e){
                Toast.makeText(c,"Error Finding data. Please sync to Update Data.", Toast.LENGTH_SHORT).show();}
            return data;

        }
    }

    public class AssignAdapter extends RecyclerView.Adapter<AssignAdapter.MyViewHolder> {


        private Context context;

        private ArrayList<EmployeeInformation> employee;

        private LayoutInflater inflater;

        private int previousPosition = 0;

        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        };


        public AssignAdapter(Context context, ArrayList<EmployeeInformation> employee) {

            this.context = context;
            this.employee = employee;
            //inflater = LayoutInflater.from(context);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list, parent, false);
            view.setOnClickListener(onClickListener);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;

        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
            final TextView EmployeeName= (TextView)myViewHolder.view.findViewById(R.id.EmployeeName);
            final TextView EmployeeDesignation= (TextView)myViewHolder.view.findViewById(R.id.EmployeeDesignation);
            final TextView EmployeeNumber = (TextView) myViewHolder.view.findViewById(R.id.EmployeeNumber);
            final TextView EmployeeMail=(TextView)myViewHolder.view.findViewById(R.id.EmployeeMail);
            final LinearLayout Assign= (LinearLayout)myViewHolder.view.findViewById(R.id.LongClickAssign);
            final ImageView tick= (ImageView)myViewHolder.view.findViewById(R.id.assign_tick);

            EmployeeName.setText(employee.get(position).EmployeeName);
            EmployeeDesignation.setText(employee.get(position).EmployeeDesignation);
            EmployeeNumber.setText(employee.get(position).EmployeeNumber);
            EmployeeMail.setText(employee.get(position).EmployeeMailId);

            Assign.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new Thread(new Runnable() {
                        public void run() {
                    String Query=null;
                    Fname=null;
                    Lname=null;
                    String mail=null;
                    qsdb = qdb.getWritableDatabase();
                    String qry = "Select Query from " + CompanyName + "_" + FBName + " where QueryNumber= '" + selectedFromList + "'";
                    Cursor cursor = qsdb.rawQuery(qry, null);
                    if (cursor.moveToFirst()) {
                        do {
                            Query = cursor.getString(0);
                        } while (cursor.moveToNext());
                    }
                    String no= sp.getString(KEY_NAME, null);
                    String q= "Select FirstName,LastName,EmailId from Login where Number='"+no+"'";
                    Cursor cursor1 = sdb.rawQuery(q, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            Fname = cursor1.getString(0);
                            Lname= cursor1.getString(1);
                            mail=cursor1.getString(2);
                        } while (cursor1.moveToNext());
                    }

                    try {
                    GMailSender send = new GMailSender("noreply.ikvox@gmail.com",
                            "ikvox@1234");
                        GMailSender send1 = new GMailSender("noreply.ikvox@gmail.com",
                                "ikvox@1234");
                    String BodyOwner = EmployeeName.getText().toString() + " has been assigned to look after the Query( " + Query + " ) ";
                    String subject = "Query Assigned";
                    String BodyAssignee = Fname+" "+Lname+ " has assigned you to look after the Query ( " + Query + " ) ASAP.";
                        Assignee=EmployeeName.getText().toString();
                        send.sendMail(subject,BodyOwner,"noreply.ikvox@gmail.com",mail);
                        send1.sendMail(subject,BodyAssignee,"noreply.ikvox@gmail.com", EmployeeMail.getText().toString());


                    } catch (Exception e1) {
                        Log.d("Error Sending email", e1.toString());
                    }


                    sdb= getApplicationContext().openOrCreateDatabase(MyDatabase.DBNAME,MODE_PRIVATE,null);
                    sdb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + "Status"
                            + " (Query TEXT, Status TEXT);");
                    ContentValues value = new ContentValues();
                    value.put("Query", Query);
                    value.put("Status", "O");
                    sdb.insert("Status",null, value);
                        }
                    }).start();
                    tick.setVisibility(View.VISIBLE);
                   // Toast.makeText(getApplicationContext(), "Query Assigned to "+Fname+""+Lname, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Mail has been sent", Toast.LENGTH_SHORT).show();


                    //store crm status details
                    new MyTask().execute();





                    return false;
                }

            });

            //final int currentPosition = position;
            //final EmployeeInformation infoData = employee.set(position, new EmployeeInformation());
        }
        @Override
        public int getItemCount() {
            return employee.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public View view;

            public MyViewHolder(View v) {
                super(v);
                view = v;
            }


        }


    }
    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("CompanyName", CompanyName));
                    postParameters.add(new BasicNameValuePair("BranchLocation",FBName ));
                    postParameters.add(new BasicNameValuePair("QueryNumber",selectedFromList ));
                    postParameters.add(new BasicNameValuePair("status","Assign"));
                    postParameters.add(new BasicNameValuePair("assignee",Assignee ));
                    postParameters.add(new BasicNameValuePair("assignedBy",Fname+" "+Lname ));
                    json = jParser.makeHttpRequest(url_login, "GET", postParameters);
                    String s = null;
                    try {
                        /*response = SimpleHttpClient
                                .executeHttpPost(
                                        "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/login.do",
                                        postParameters);*/

                        s = json.getString("status");
                        resp=s;
                    } catch (Exception e) {
                        e.printStackTrace();
                     //   network=1;
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

            } catch (Exception e1){
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /*AndroidUtils.animateView(progressOverlay,image,animate, View.GONE, 0, 200);
            resp="null";
            if (error == 1) {

                Toast.makeText(LoginActivity.this, "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                error = 0;
                network=0;
            }
            else if (network==1)
            {
                // Toast.makeText(LoginActivity.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                //error = 0;
                //network=0;

            }

            *//*if (!msg.equals(""))
            {
                Toast.makeText(LoginActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }*//*
            //Toast.makeText(getApplicationContext(), "hiii", Toast.LENGTH_SHORT).show();*/
            super.onPostExecute(aVoid);
        }

    }
}
