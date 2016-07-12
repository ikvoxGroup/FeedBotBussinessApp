package ownerapp.com.ikvox.pratikriya.crm.Track;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;
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
   public static  Context c;
    ImageView noti;
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
        c= getContext();
        sp= getContext().getSharedPreferences(PREFER_NAME,getContext().MODE_PRIVATE);
        CompanyName= sp.getString(KEY_COMPANYNAME,null);
        db= new MyDatabase(getActivity());
        sdb= db.getWritableDatabase();
        qdb= new QueryDatabase(getActivity());
        qsdb= qdb.getWritableDatabase();
        mLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout_tracking);
        list= (LinearLayout)rootView.findViewById(R.id.dragViewTracking);
        list.setBackgroundColor(MainActivity.color);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        OpenBranch= (RelativeLayout) rootView.findViewById(R.id.slidingBranchTracking);
        OpenBranch.setBackgroundColor(MainActivity.color);
        mylist =(ExpandableListView)rootView.findViewById(R.id.QueriesExpanded);
        final TextView Query= (TextView)rootView.findViewById(R.id.selectBranchTracking);
        noti= (ImageView)rootView.findViewById(R.id.notiTrack);

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
                selectedFromList =(String) (lv.getItemAtPosition(position));

                Query.setText(selectedFromList);
                    listAdapter = new TrackingAdapter(getActivity(),qry.getData());
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
                GetBranch.getData() );
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


    public static  class  qry {


        public static ArrayList<inf> getData() {

            query = new ArrayList<>();
            String selectQuery = "SELECT  Query, QueryType ,Keyword FROM " + CompanyName+"_"+selectedFromList;
            qsdb = qdb.getWritableDatabase();
            String count = "SELECT count(*) FROM "+CompanyName+"_"+selectedFromList;
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
                    current= cursor.getString(0);
                    current = current.valueOf(current.charAt(0)).toUpperCase() + current.substring(1, current.length());
                    data.add(current);
                } while (cursor.moveToNext());
            }
            return data;

        }
    }


}