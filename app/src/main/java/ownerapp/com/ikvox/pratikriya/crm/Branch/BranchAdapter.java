package ownerapp.com.ikvox.pratikriya.crm.Branch;

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
import ownerapp.com.ikvox.pratikriya.crm.Branch.BranchLayoutDetails.BranchDetailsLayout;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {


    private Context context;

    private ArrayList<BranchInformation> branch;

    private LayoutInflater inflater;
    String Name;


    public BranchAdapter(Context context, ArrayList<BranchInformation> branch) {
        this.context = context;
        this.branch = branch;
       // inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        final TextView BranchName= (TextView)myViewHolder.view.findViewById(R.id.BranchName);
        final TextView BranchDetails =(TextView)myViewHolder.view.findViewById(R.id.BranchDetails);
        final LinearLayout BranchLayout =(LinearLayout)myViewHolder.view.findViewById(R.id.BranchLayout);
        BranchName.setText(branch.get(position).BranchName);
        BranchDetails.setText(branch.get(position).BranchDetails);
        Name=BranchName.getText().toString();

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
                BranchDetailsLayout.BranchName(BranchName.getText().toString());
                Intent i = new Intent(context, BranchDetailsLayout.class);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return branch.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view =itemView;
        }


    }
    // This method adds(duplicates) a Object (item ) to our Branch set as well as Recycler View.
}