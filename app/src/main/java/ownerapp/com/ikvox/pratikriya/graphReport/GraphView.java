package ownerapp.com.ikvox.pratikriya.graphReport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;


public class GraphView extends Activity {

    private FrameLayout chartContainer;
    private PieChart mChart;
    private Button share;
    Bitmap myBitmap;
    private float[] ydata={10,20,30};
    private String[] xdata={"Ambience","Speed of service","Food Quality"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ikvox_graph_activity);
        share=(Button)findViewById(R.id.share);
        chartContainer =(FrameLayout)findViewById(R.id.chartContainer);
        mChart=new PieChart(this);

        //add pie chart to main layout
        chartContainer.addView(mChart);
        chartContainer.setBackgroundColor(Color.LTGRAY);

        //configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDescription("FeedBack System");

        //enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        //enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout iv = (FrameLayout) findViewById(R.id.chartContainer);
                //View v1 = getWindow().getDecorView().getRootView();
                View v1 = iv.getRootView(); //even this works
                // View v1 = findViewById(android.R.id.content); //this works too
                // but gives only content
                v1.setDrawingCacheEnabled(true);
                myBitmap = v1.getDrawingCache();
                saveBitmap(myBitmap);
            }
        });

        //set a chart value listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //display msg
                if(e== null){
                    return;
                    //Toast.makeText(IkVoxOwnerActivity.this, xdata[e.getXIndex()] +" = " + e.getVal()+"%", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //add data
        addData();
        //customize legends
        Legend l=mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }
    private void addData(){
        ArrayList<Entry>yVals1=new ArrayList<Entry>();
        for (int i=0;i<ydata.length; i++)
            yVals1.add(new Entry(ydata[i],i));

        ArrayList<String> xVals=new ArrayList<String>();
        for(int i=0;i<xdata.length;i++)
            xVals.add(xdata[i]);

        //create pie data set
        PieDataSet dataset=new PieDataSet(yVals1,"Feedback");
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(5);

        //add many color
        ArrayList<Integer> colors=new ArrayList<Integer>();
        for (int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c:ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataset.setColors(colors);

        //instatiate pie chart object
        PieData data=new PieData(xVals,dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        //undo all highlights
        mChart.highlightValue(null);

        //update pie chart
        mChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(GraphView.this, MainActivity.class);
        startActivity(i);
    }
    public void saveBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendMail(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
    public void sendMail(String path) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "mahapatra.preetam@gmail.com" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Ikvox feedback Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "This is the report submited by Ikvox feedback Application");
        emailIntent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(emailIntent, "Sharing feedback report..."));
    }
}