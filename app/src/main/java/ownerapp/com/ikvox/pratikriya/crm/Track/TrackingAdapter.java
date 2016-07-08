package ownerapp.com.ikvox.pratikriya.crm.Track;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.R;

/**
 * Created by MyMac on 22/06/16.
 */
class TrackingAdapter extends BaseExpandableListAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private ArrayList<inf> query;
    public TrackingAdapter(Context context,ArrayList<inf> query) {
        this.context = context;
        this.query=query;
        this.layoutInflater = layoutInflater;
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
        Animate.animate(v,false);
        /*if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.status, null);
        }*/
        TextView keyword=(TextView)v.findViewById(R.id.QueryKeyword);
        TextView assigned=(TextView)v.findViewById(R.id.QueryAssigned);
        LinearLayout ChangeStatus= (LinearLayout)v.findViewById(R.id.changeStatus);
        final TextView pos=(TextView)v.findViewById(R.id.StatusValue);
        final ImageView l1=(ImageView)v.findViewById(R.id.status1);
        final ImageView l2=(ImageView)v.findViewById(R.id.status2);
        final ImageView l3=(ImageView)v.findViewById(R.id.status3);
        final ImageView logo1=(ImageView)v.findViewById(R.id.assignedLogo);
        final ImageView logo2=(ImageView)v.findViewById(R.id.progressLogo);
        final  ImageView logo3=(ImageView)v.findViewById(R.id.closedLogo);
        keyword.setText("Query Keyword: "+ query.get(groupPosition).QueryKeyword);
        assigned.setText("Assigned to: "+ query.get(groupPosition).QueryAssignedTo);
        pos.setText(query.get(groupPosition).QueryStatus);
        if(pos.getText().toString().equals("closed"))
        {
            logo1.setImageResource(R.drawable.assigned);
            logo2.setImageResource(R.drawable.progress);
            logo3.setImageResource(R.drawable.closed);
            l1.setColorFilter(Color.rgb(176,62,121));
            l2.setColorFilter(Color.rgb(202,200,57));
            l3.setColorFilter(Color.rgb(77,202,57));
        }
        else if(pos.getText().toString().equals("progress"))
        {
            logo1.setImageResource(R.drawable.assigned);
            logo2.setImageResource(R.drawable.progress);
            l1.setColorFilter(Color.rgb(176,62,121));
            l2.setColorFilter(Color.rgb(202,200,57));
        }
        else if(pos.getText().toString().equals("assigned"))
        {
            logo1.setImageResource(R.drawable.assigned);
            l1.setColorFilter(Color.rgb(176,62,121));
        }
        ChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder AddNewQuery = new AlertDialog.Builder(context);
                View vw1 = v.inflate(context,R.layout.chnage_status, null);
                final AlertDialog dialog = AddNewQuery.create();
                dialog.setView(vw1);
                AddNewQuery.setView(vw1);
                dialog.show();

              final  RadioButton rb1= (RadioButton)vw1.findViewById(R.id.rbopen);
               final RadioButton rb2= (RadioButton)vw1.findViewById(R.id.rbassigned);
               final  RadioButton rb3= (RadioButton)vw1.findViewById(R.id.rbprogress);
               final RadioGroup rg= (RadioGroup)vw1.findViewById(R.id.rgStatus);
                final Button sub= (Button)vw1.findViewById(R.id.submitStatus);
                pos.setText(query.get(groupPosition).QueryStatus);
                if(pos.getText().toString().equals("open"))
                {
                   rb1.setChecked(true);
                }
                else if(pos.getText().toString().equals("assigned"))
                {
                    rb2.setChecked(true);
                }
                else if(pos.getText().toString().equals("progress"))
                {
                    rb3.setChecked(true);
                }
                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       switch (rg.getCheckedRadioButtonId())
                       {
                           case R.id.rbassigned:
                               logo1.setImageResource(R.drawable.assigned);
                               l1.setColorFilter(Color.rgb(176,62,121));
                               dialog.dismiss();
                               break;
                           case R.id.rbprogress:
                               logo1.setImageResource(R.drawable.assigned);
                               logo2.setImageResource(R.drawable.progress);
                               l1.setColorFilter(Color.rgb(176,62,121));
                               l2.setColorFilter(Color.rgb(202,200,57));
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
