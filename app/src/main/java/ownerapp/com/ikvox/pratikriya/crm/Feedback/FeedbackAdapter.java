package ownerapp.com.ikvox.pratikriya.crm.Feedback;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.crm.Feedback.FeedbackReportDetails.FeedbackbranchDetails;

/**
 * Created by MyMac on 22/06/16.
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {


    private Context context;

    private ArrayList<FeedbackInformation> FBbranch;

    String Name;


    public FeedbackAdapter(Context context, ArrayList<FeedbackInformation> FBbranch) {
        this.context = context;
        this.FBbranch = FBbranch;
        // inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_branch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        final TextView BranchName = (TextView) myViewHolder.view.findViewById(R.id.FeedbackBranchName);
        final LinearLayout BranchLayout = (LinearLayout) myViewHolder.view.findViewById(R.id.FeedbackBranchLayout);
        BranchName.setText(FBbranch.get(position).BranchName);
        Name = BranchName.getText().toString();

        /*if (position > previousPosition) { // We are scrolling DOWN

            AnimationUtil.animate(myViewHolder, true);

        } else { // We are scrolling UP

            AnimationUtil.animate(myViewHolder, false);


        }*/


        /*final int currentPosition = position;
        final BranchInformation infoData = branch.set(position, new BranchInformation());*/

        BranchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackbranchDetails.BranchName(BranchName.getText().toString());
                Intent i = new Intent(context, FeedbackbranchDetails.class);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return FBbranch.size();
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
