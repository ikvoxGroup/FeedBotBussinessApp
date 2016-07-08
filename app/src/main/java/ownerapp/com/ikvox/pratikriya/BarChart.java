package ownerapp.com.ikvox.pratikriya;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarChart extends Activity {
    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json;
    private static String url_getQuery = "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/GetResults.do";
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";

    private static final String APP_QUERY = "resultsSP";
    public static final String QS1 = "QS1";
    public static final String QS2 = "QS2";
    public static final String QS3 = "QS3";
    public static final String QS4 = "QS4";
    public static final String QS5 = "QS5";
    public static final String QS6 = "QS6";
    public static final String QS7 = "QS7";
    public static final String QS8 = "QS8";
    public static final String QS9 = "QS9";
    public static final String QS10 = "QS10";


    String mob;
    String Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10;
    SharedPreferences pref;

    private String resp;
    private String errorMsg;
    int c1,c0,show;
    SharedPreferences sharedpreferences;
    int a,b,c,d,e,f,g,h,i,j;
    static ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart);
        pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(APP_QUERY, MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(BarChart.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading Chart!! Please Wait.");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        c1 = 0;c0=0;show=0;
        new MyTask().execute();
    }
    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
           // AndroidUtils.animateView(progressOverlay, View.VISIBLE, 0.8f, 200);
            mProgressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mob=pref.getString(KEY_NAME, null);

                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("mobile", mob));

                    json = jParser.makeHttpRequest(url_getQuery, "GET", postParameters);
                    String s = null;

                    try {

                        s= json.getString("status");
                        Q1=json.getString("q1");
                        Q2=json.getString("q2");
                        Q3=json.getString("q3");
                        Q4=json.getString("q4");
                        Q5=json.getString("q5");
                        Q6=json.getString("q6");
                        Q7=json.getString("q7");
                        Q8=json.getString("q8");
                        Q9=json.getString("q9");
                        Q10=json.getString("q10");

                         a=Integer.parseInt(Q1);
                         b=Integer.parseInt(Q2);
                         c=Integer.parseInt(Q3);
                         d=Integer.parseInt(Q4);
                         e=Integer.parseInt(Q5);
                         f=Integer.parseInt(Q6);
                         g=Integer.parseInt(Q7);
                         h=Integer.parseInt(Q8);
                         i=Integer.parseInt(Q9);
                         j=Integer.parseInt(Q10);

                        resp=s;

                    } catch (JSONException e ) {
                        e.printStackTrace();
                        errorMsg = e.getMessage();
                    }catch (Exception e ) {
                        e.printStackTrace();
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
                // Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_LONG).show();
                if (resp.equals("success")) {
                    show=1;
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(QS1, Q1);
                    editor.putString(QS2, Q2);
                    editor.putString(QS3, Q3);
                    editor.putString(QS4, Q4);
                    editor.putString(QS5, Q5);
                    editor.putString(QS6, Q6);
                    editor.putString(QS7, Q7);
                    editor.putString(QS8, Q8);
                    editor.putString(QS9, Q9);
                    editor.putString(QS10, Q10);




                   } else {
                    c1 = 1;
                    // Toast.makeText(FeedbackActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

           // AndroidUtils.animateView(progressOverlay, View.GONE, 0, 200);
          // ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarChart);
           //progressBar.setVisibility(View.GONE);
            mProgressDialog.dismiss();
            if(show==1)
            {
                graph(a,b,c,d,e,f,g,h,i,j);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error Loading Graph", Toast.LENGTH_SHORT).show();
            }


            if (c1 == 1) {

                Toast.makeText(BarChart.this, "---Failed--- Retry Please or Check your Network Connection", Toast.LENGTH_SHORT).show();
            } else if (c0 == 1)
            {
                //  Toast.makeText(FeedbackActivity.this, "Welcome : "+UName, Toast.LENGTH_SHORT).show();
                Toast.makeText(BarChart.this,"No query found", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }
    }
    void graph(int q1,int q2,int q3,int q4,int q5,int q6,int q7,int q8,int q9,int q10){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(q1, 0));
        entries.add(new BarEntry(q2, 1));
        entries.add(new BarEntry(q3, 2));
        entries.add(new BarEntry(q4, 3));
        entries.add(new BarEntry(q5, 4));
        entries.add(new BarEntry(q6, 5));
        entries.add(new BarEntry(q7, 6));
        entries.add(new BarEntry(q8,7));
        entries.add(new BarEntry(q9, 8));
        entries.add(new BarEntry(q10, 9));


        BarDataSet dataset = new BarDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Query 1");
        labels.add("Query 2");
        labels.add("Query 3");
        labels.add("Query 4");
        labels.add("Query 5");
        labels.add("Query 6");
        labels.add("Query 7");
        labels.add("Query 8");
        labels.add("Query 9");
        labels.add("Query 10");
        com.github.mikephil.charting.charts.BarChart chart = new com.github.mikephil.charting.charts.BarChart(this);
        setContentView(chart);
        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.setDescription("# % of customer say good");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        chart.animateY(2000);
    }

}
