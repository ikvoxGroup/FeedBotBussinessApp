package ownerapp.com.ikvox.pratikriya.crm.Track;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;
import ownerapp.com.ikvox.pratikriya.JSONParserIkVox;
import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.slidingUpPanel.SlidingUpPanelLayout;

/**
 * Created by MyMac on 21/06/16.
 */
public class Tracking_activity extends Fragment {
    RelativeLayout OpenBranch;
    private SlidingUpPanelLayout mLayout;
    LinearLayout list;
    public static String selectedFromList;
    ExpandableListView mylist;
    private TrackingAdapter listAdapter;
    private int lastExpandedPosition = -1;

    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    public static String CompanyName;
    public static MyDatabase db;
    public static QueryDatabase qdb;
    public static SQLiteDatabase qsdb;
    public static SQLiteDatabase sdb;

    public static int icount;
    String br;
    String[] branch;
    public static ArrayList<String> branchArray;
    public static Context c;
    ImageView noti;
    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json;
    private static String url_setStatus = "http://feedbotappserver.cgihum6dcd.us-west-2.elasticbeanstalk.com/setStatus.do";
    private static String url_getStatus = "http://feedbotappserver.cgihum6dcd.us-west-2.elasticbeanstalk.com/getStatus.do";

    private String respS;
    private String errorMsg;

    public static JSONArray QuerySrNumberjson = null;
    public static JSONArray QueryAssignedByjson = null;
    public static JSONArray QueryAssignedTojson = null;
    public static JSONArray QueryStatusjson = null;

    int len;
    public static ArrayList<String> QuerySrNumber;
    public static ArrayList<String> QueryAssignedBy;
    public static ArrayList<String> QueryAssignedTo;
    public static ArrayList<String> QueryStatus;
    static ProgressDialog mProgressDialog;

    public static ArrayList<inf> query;

    public Tracking_activity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tracking_crm, container, false);
        c = getContext();
        sp = getContext().getSharedPreferences(PREFER_NAME, getContext().MODE_PRIVATE);
        CompanyName = sp.getString(KEY_COMPANYNAME, null);
        db = new MyDatabase(getActivity());
        sdb = db.getWritableDatabase();
        qdb = new QueryDatabase(getActivity());
        qsdb = qdb.getWritableDatabase();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Fetching Data!! Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout_tracking);
        list = (LinearLayout) rootView.findViewById(R.id.dragViewTracking);
        list.setBackgroundColor(MainActivity.color);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        OpenBranch = (RelativeLayout) rootView.findViewById(R.id.slidingBranchTracking);
        OpenBranch.setBackgroundColor(MainActivity.color);
        mylist = (ExpandableListView) rootView.findViewById(R.id.QueriesExpanded);
        final TextView Branch = (TextView) rootView.findViewById(R.id.selectBranchTracking);
        noti = (ImageView) rootView.findViewById(R.id.notiTrack);

        OpenBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout.getAnchorPoint() == 1.0f) {
                    mLayout.setAnchorPoint(0.7f);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    noti.setImageResource(R.drawable.up);
                }
            }
        });
        final ListView lv = (ListView) rootView.findViewById(R.id.listBranch);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getActivity(), "onItemClick", Toast.LENGTH_SHORT).show();
                noti.setImageResource(R.drawable.down);
                mLayout.setAnchorPoint(1.0f);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                selectedFromList = (String) (lv.getItemAtPosition(position));
                new MyTask().execute();
                Branch.setText(selectedFromList);
                listAdapter = new TrackingAdapter(getActivity(), qry.getData());
                mylist.setAdapter(listAdapter);
                mylist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (lastExpandedPosition != -1
                                && groupPosition != lastExpandedPosition) {
                            mylist.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = groupPosition;
                    }
                });
                    /*expandAll();
                    mylist.setOnClickListener();*/
                    /*adapter = new MyCustomAdapter(FeedbackAssign.this, Data.getData());
                    Content.setAdapter(adapter);*/

            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                GetBranch.getData());
        lv.setAdapter(arrayAdapter);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Branch", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("Branch", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.CUSTOM);
            }
        });
        return rootView;
    }


    public static class qry {


        public static ArrayList<inf> getData() {

            query = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + CompanyName + "_" + selectedFromList;
            qsdb = qdb.getWritableDatabase();
            String count = "SELECT count(*) FROM " + CompanyName + "_" + selectedFromList;
            try {
                Cursor mcursor = qsdb.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);
                if (icount > 0) {
                    Cursor cursor = qsdb.rawQuery(selectQuery, null);
                    if (cursor.moveToFirst()) {
                        do {
                            inf current = new inf();
                            current.QueryNumber = cursor.getString(0);
                            current.Query = cursor.getString(1);
                            // current.QueryAssignedTo = cursor.getString(2);
                            query.add(current);
                        } while (cursor.moveToNext());
                    }
                } else {
                    Toast.makeText(c, "No Queries added", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLiteException e) {
                Toast.makeText(c, "No Queries added", Toast.LENGTH_SHORT).show();
            }
            return query;
        }
        /*query = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CompanyName+"_"+selectedFromList+"_"+"Status";
        qsdb = qdb.getWritableDatabase();
        String count = "SELECT count(*) FROM "+CompanyName+"_"+selectedFromList+"_"+"Status";
        try {
            Cursor mcursor = qsdb.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            if (icount > 0) {
                Cursor cursor = qsdb.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        inf current = new inf();
                        current.Query = cursor.getString(0);
                        current.QueryKeyword = cursor.getString(1);
                        current.QueryAssignedTo = cursor.getString(2);
                        query.add(current);
                    } while (cursor.moveToNext());
                }
            } else {
                Toast.makeText(c, "No Queries added", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLiteException e) {
            Toast.makeText(c, "No Queries added", Toast.LENGTH_SHORT).show();}
        return query;
    }*/
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

    class TrackingAdapter extends BaseExpandableListAdapter {
        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<inf> query;

        public TrackingAdapter(Context context, ArrayList<inf> query) {
            this.context = context;
            this.query = query;
            // this.layoutInflater = layoutInflater;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            View v = View.inflate(context, R.layout.status, null);
            Animate.animate(v, false);
        /*if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.status, null);
        }*/
            TextView keyword = (TextView) v.findViewById(R.id.QueryKeyword);
            TextView assigned = (TextView) v.findViewById(R.id.QueryAssigned);
            LinearLayout ChangeStatus = (LinearLayout) v.findViewById(R.id.changeStatus);
            final TextView assignedTo = (TextView) v.findViewById(R.id.QueryAssignedBy);
            final TextView pos= (TextView) v.findViewById(R.id.pos);
            String p=null;
            final ImageView l1 = (ImageView) v.findViewById(R.id.status1);
            final ImageView l2 = (ImageView) v.findViewById(R.id.status2);
            final ImageView l3 = (ImageView) v.findViewById(R.id.status3);
            final ImageView logo1 = (ImageView) v.findViewById(R.id.assignedLogo);
            final ImageView logo2 = (ImageView) v.findViewById(R.id.progressLogo);
            final ImageView logo3 = (ImageView) v.findViewById(R.id.closedLogo);
            keyword.setText("Query Number: " + query.get(groupPosition).QueryNumber);
            String qry= "select AssignedBy, AssignedTo, Status from "+CompanyName+"_"+selectedFromList+"_"+"Status "+"where QueryNumber = '"+keyword.getText().toString()+"'";
            qsdb=qdb.getWritableDatabase();
            try {
                Cursor c = qsdb.rawQuery(qry, null);
                if (c.moveToFirst()) {
                    do {
                        assigned.setText(c.getString(0));
                        assignedTo.setText(c.getString(1));
                        pos.setText(c.getString(2));
                    } while (c.moveToNext());
                }
            }catch (Exception e)
            {
                assigned.setText("Action Not Taken");
                assignedTo.setText("Action not Taken");
                pos.setText("open");
            }
            assigned.setText("Assigned By: "+assigned.getText().toString());
            // pos.setText(query.get(groupPosition).QueryStatus);
           // pos.setText("open");
            if (pos.getText().toString().equals("close")) {
                logo1.setImageResource(R.drawable.assigned);
                logo2.setImageResource(R.drawable.progress);
                logo3.setImageResource(R.drawable.closed);
                l1.setColorFilter(Color.rgb(176, 62, 121));
                l2.setColorFilter(Color.rgb(202, 200, 57));
                l3.setColorFilter(Color.rgb(77, 202, 57));
            } else if (pos.getText().toString().equals("progress")) {
                logo1.setImageResource(R.drawable.assigned);
                logo2.setImageResource(R.drawable.progress);
                l1.setColorFilter(Color.rgb(176, 62, 121));
                l2.setColorFilter(Color.rgb(202, 200, 57));
            } else if (pos.getText().toString().equals("assign")) {
                logo1.setImageResource(R.drawable.assigned);
                l1.setColorFilter(Color.rgb(176, 62, 121));
            }
            ChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder ChangeStatus = new AlertDialog.Builder(context);
                    View vw1 = v.inflate(context, R.layout.change_status, null);
                    final AlertDialog dialog = ChangeStatus.create();
                    Button submit = (Button) vw1.findViewById(R.id.submitStatus);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    dialog.setView(vw1);
                    ChangeStatus.setView(vw1);
                    dialog.show();

                    final RadioButton rb1 = (RadioButton) vw1.findViewById(R.id.rbopen);
                    final RadioButton rb2 = (RadioButton) vw1.findViewById(R.id.rbassigned);
                    final RadioButton rb3 = (RadioButton) vw1.findViewById(R.id.rbprogress);
                    final RadioButton rb4 = (RadioButton) vw1.findViewById(R.id.rbclose);
                    final RadioGroup rg = (RadioGroup) vw1.findViewById(R.id.rgStatus);
                    final Button sub = (Button) vw1.findViewById(R.id.submitStatus);
                    //pos.setText(query.get(groupPosition).QueryStatus);
                    if (pos.getText().toString().equals("open")) {
                        rb1.setChecked(true);
                    } else if (pos.getText().toString().equals("assign")) {
                        rb2.setChecked(true);
                    } else if (pos.getText().toString().equals("progress")) {
                        rb3.setChecked(true);
                    } else if (pos.getText().toString().equals("close")) {
                        rb4.setChecked(true);
                    }
                    sub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (rg.getCheckedRadioButtonId()) {
                                case R.id.rbassigned:
                                    logo1.setImageResource(R.drawable.assigned);
                                    l1.setColorFilter(Color.rgb(176, 62, 121));
                                    dialog.dismiss();
                                    break;
                                case R.id.rbprogress:
                                    logo1.setImageResource(R.drawable.assigned);
                                    logo2.setImageResource(R.drawable.progress);
                                    l1.setColorFilter(Color.rgb(176, 62, 121));
                                    l2.setColorFilter(Color.rgb(202, 200, 57));
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
                }
            });

        /*if (groupPosition == 0) {
            v = View.inflate(context, R.layout.status, null);
            Animate.animate(v,false);
            //TextView txtView = (TextView) v.findViewById(R.id.txtChld1);
            //txtView.setText("Green");
            //txtView.setTextSize(15f);
            //txtView.setBackgroundColor(Color.GREEN);
        }
        if (groupPosition == 1) {
            v = View.inflate(context, R.layout.status, null);
            Animate.animate(v,true);

        }
        if (groupPosition == 2) {
            v = View.inflate(context, R.layout.status, null);
        }
        if (groupPosition == 3) {
            v = View.inflate(context, R.layout.status, null);
            //TextView txtView = (TextView) v.findViewById(R.id.txtChld1);
            //txtView.setText("Purple");
            //txtView.setTextSize(15f);
        }*/
            v.invalidate();
            return v;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public int getGroupCount() {
            return query.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
       /* if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.tracking_group_layout, null);
        }*/

            View v = convertView.inflate(context, R.layout.tracking_group_layout, null);
            TextView qry = (TextView) v.findViewById(R.id.txt1);
            qry.setText(query.get(groupPosition).Query);
        /*if(groupPosition == 0)
        {
            qry.setText("Q1");
            qry.setTextSize(15f);
        }
        if(groupPosition == 1)
        {
            qry.setText("Q2");
            qry.setTextSize(15f);
        }
        if(groupPosition == 2)
        {
            qry.setText("Q3");
            qry.setTextSize(15f);
        }
        if(groupPosition == 3)
        {
            qry.setText("Q4");
            qry.setTextSize(15f);
        }*/
            v.invalidate();
            return v;
        /*View v = convertView.inflate(context, R.layout.tracking_group_layout, null);
        TextView txtView = (TextView) v.findViewById(R.id.txt1);
        if (groupPosition == 0) {
            txtView.setText("Query 1");
            txtView.setTextSize(15f);
        }
        if (groupPosition == 1) {
            txtView.setText("Query 2");
            txtView.setTextSize(15f);
        }
        if (groupPosition == 2) {
            txtView.setText("Query 3");
            txtView.setTextSize(15f);
        }
        if (groupPosition == 3) {
            txtView.setText("Query 4");
            txtView.setTextSize(15f);
        }
        v.invalidate();*/

        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            //AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
//            mProgressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    // Log.i("kush",CompanyName+" "+FBName+" "+selectedFromList+" "+Assignee+" ");
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("CompanyName", CompanyName));
                    postParameters.add(new BasicNameValuePair("BranchLocation", selectedFromList));
                    // postParameters.add(new BasicNameValuePair("QueryNumber",));
                    json = jParser.makeHttpRequest(url_getStatus, "GET", postParameters);
                    String s=null ;
                    try {
                        /*response = SimpleHttpClient
                                .executeHttpPost(
                                        "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/login.do",
                                        postParameters);*/

                        s = json.getString("status");
                        respS = s;
                        QuerySrNumberjson = json.getJSONArray("querySrno");
                        QueryAssignedByjson = json.getJSONArray("assignedBy");
                        QueryAssignedTojson = json.getJSONArray("assignee");
                        QueryStatusjson = json.getJSONArray("crmstatus");

                        len=QuerySrNumberjson.length();

                    } catch (Exception e) {
                        e.printStackTrace();
                        //   network=1;
                        errorMsg = e.getMessage();
                    }
                }
            }).start();
            try {
                Thread.sleep(3000);
                if (respS.equals("success")) {
                   /* try {
                        //converting JSON value to Arraylist
                        // int len = QuerySrNumberListjson.length();
                        Gson gson = new Gson();
                        QuerySrNumber = gson.fromJson(QuerySrNumberjson, ArrayList.class);
                        QueryAssignedBy = gson.fromJson(QueryAssignedByjson, ArrayList.class);
                        QueryAssignedTo = gson.fromJson(QueryAssignedTojson, ArrayList.class);
                        QueryStatus = gson.fromJson(QueryStatusjson, ArrayList.class);
                    } catch (Exception e) {
                        Log.e("Kushal1", e.toString());
                    }*/
                    try{
                        for (int i = 0; i < len; i++) {

                            QuerySrNumber.add(QuerySrNumberjson.get(i).toString());
                            QueryAssignedBy.add(QueryAssignedByjson.get(i).toString());
                            QueryAssignedTo.add(QueryAssignedTojson.get(i).toString());
                            QueryStatus.add(QueryStatusjson.get(i).toString());
                        }
                    }catch(JSONException e){

                    }


                    qsdb = getContext().openOrCreateDatabase(QueryDatabase.DBNAME, getContext().MODE_PRIVATE, null);
                    qsdb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + CompanyName + "_" + selectedFromList + "_" + "Status"
                            + " (QueryNumber TEXT,AssignedBy TEXT ,AssignedTo TEXT,Status TEXT);");
                    int i;
                    for (i = 0; i < QuerySrNumber.size(); i++) {
                        ContentValues value = new ContentValues();
                        value.put("QueryNumber", QuerySrNumber.get(i).toString());
                        value.put("AssignedBy", QueryAssignedBy.get(i).toString());
                        value.put("AssignedTo", QueryAssignedTo.get(i).toString());
                        value.put("Status", QueryStatus.get(i).toString());
                        sdb.insert(CompanyName + "_" + selectedFromList + "_" + "Status", null, value);
                        /**
                         * Inside the new thread we cannot update the main thread So
                         * updating the main thread outside the new thread
                         */
                        // Toast.makeText(LoginActivity.this, respS, Toast.LENGTH_LONG).show();


                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           // AndroidUtils.animateView(progressOverlay, image, animate, View.GONE, 0, 200);
     //       mProgressDialog.dismiss();
           // respS = "null";
            /*if (error == 1) {

                // Toast.makeText(LoginActivity.this, "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                error = 0;
                network = 0;
            } else if (network == 1) {
                // Toast.makeText(LoginActivity.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                //error = 0;
                //network=0;

            }

            *//**//**//**//*if (!msg.equals(""))
            {
                Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }*/
            //Toast.makeText(getApplicationContext(), "hiii", Toast.LENGTH_SHORT).show();*//*
            super.onPostExecute(aVoid);
        }

    }
}
