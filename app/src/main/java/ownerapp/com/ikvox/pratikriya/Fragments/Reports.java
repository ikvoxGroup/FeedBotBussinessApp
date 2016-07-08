package ownerapp.com.ikvox.pratikriya.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ownerapp.com.ikvox.pratikriya.BarChart;
import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.graphReport.GraphView;


public class Reports extends ActionBarActivity {
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Activity context;
    Toolbar toolbar;
    FrameLayout statusBar;
    Boolean homeButton = false;
    Intent intent;
    public Reports() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        toolbarStatusBar();
        navigationBarStatusBar();
        TextView tv1 = (TextView) findViewById(R.id.reportTV1);
        TextView tv2 = (TextView) findViewById(R.id.reportTV2);
        TextView tv3 = (TextView) findViewById(R.id.reportTV3);
        TextView tv4 = (TextView)findViewById(R.id.reportTV4);
        Button bt1 = (Button) findViewById(R.id.reportButton1);
        Button bt2 = (Button) findViewById(R.id.reportButton2);
        Button bt3 = (Button) findViewById(R.id.reportButton3);
        Button bt4 = (Button) findViewById(R.id.reportButton4);
        bt1.setBackgroundColor(MainActivity.color);
        bt2.setBackgroundColor(MainActivity.color);
        bt3.setBackgroundColor(MainActivity.color);
        bt4.setBackgroundColor(MainActivity.color);
        /*Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lobster.otf");
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        tv3.setTypeface(custom_font);
        tv4.setTypeface(custom_font);
        bt1.setTypeface(custom_font);
        bt2.setTypeface(custom_font);
        bt3.setTypeface(custom_font);
        bt4.setTypeface(custom_font);*/


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        fromDateEtxt = (EditText) findViewById(R.id.editDate1);
        toDateEtxt = (EditText) findViewById(R.id.editDate2);

        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt.setInputType(InputType.TYPE_NULL);
        toDateEtxt.requestFocus();


        setDateTimeField();



        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), BarChart.class);
                startActivity(i);
                finish();

            }

        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), GraphView.class);
                startActivity(i);
                finish();

            }

        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), GraphView.class);
                startActivity(i);
                finish();
            }

        });
        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), GraphView.class);
                startActivity(i);
                finish();
            }

        });
    }



    private void setDateTimeField() {

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Date Picker
                if (v == fromDateEtxt) {
                    fromDatePickerDialog.show();
                }

            }
        });

        toDateEtxt.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Date Picker
                if (v == toDateEtxt) {
                    toDatePickerDialog.show();
                }
            }
        }));

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }
    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_main1);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar1);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Reports.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main1);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                Reports.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main1);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Reports.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_main1);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                Reports.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
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
                Dialog dialog = new Dialog(Reports.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.about_dialog);
                dialog.show();
                break;
            case android.R.id.home:
                if (!homeButton) {
                    NavUtils.navigateUpFromSameTask(Reports.this);
                }
                if (homeButton) {
                    intent = new Intent(Reports.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        intent = new Intent(Reports.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
