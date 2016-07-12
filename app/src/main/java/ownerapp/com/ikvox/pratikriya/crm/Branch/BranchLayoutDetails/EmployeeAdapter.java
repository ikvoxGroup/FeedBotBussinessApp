package ownerapp.com.ikvox.pratikriya.crm.Branch.BranchLayoutDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.R;

/**
 * Created by MyMac on 21/06/16.
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {


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


    public EmployeeAdapter(Context context, ArrayList<EmployeeInformation> employee) {

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

        EmployeeName.setText(employee.get(position).EmployeeName);
        EmployeeDesignation.setText(employee.get(position).EmployeeDesignation);
        EmployeeNumber.setText(employee.get(position).EmployeeNumber);
        EmployeeMail.setText(employee.get(position).EmployeeMailId);


        /*if (position > previousPosition) { // We are scrolling DOWN

            AnimationUtil.animate(myViewHolder, true);

        } else { // We are scrolling UP

            AnimationUtil.animate(myViewHolder, false);


        }*/

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
