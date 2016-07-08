package ownerapp.com.ikvox.pratikriya;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.UUID;

import ownerapp.com.ikvox.pratikriya.Database.DatabaseHelper;
import ownerapp.com.ikvox.pratikriya.ProgBarAppear.AndroidUtils;
import ownerapp.com.ikvox.pratikriya.ServerLinkHttpClient.SimpleHttpClient;


/**
 * Created by Preetam on 05-Apr-16.
 */
public class SignupActivity extends Activity {

    EditText companyName, ownerFName, ownerLName, email, mobile, pass, repass;
    String companyNameS, ownerFNameS, ownerLNameS, emailS, mobileS, passS, repassS, password;
    Button signupB;
    TextView Login;


    DatabaseHelper myDb;
    private String resp;
    private String errorMsg;
    String uniqueID;


    public static final String OwnerDetails = "OwnerDetails" ;
    public static final String CompanyName = "companynameKey";
    public static final String fname = "fnameKey";
    public static final String lname = "lnameKey";
    public static final String Email = "emailKey";
    public static final String Mobile = "mobileKey";
    public static final String Password = "passwordKey";

    SharedPreferences sharedpreferences;

    View progressOverlay;
    ImageView image;
    AnimationDrawable animate;
    int error,network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        error=0;network=0;
        progressOverlay = findViewById(R.id.progress_overlay);
        image= (ImageView)findViewById(R.id.progressBarMain);
        animate= (AnimationDrawable)image.getBackground();

        myDb = new DatabaseHelper(this);
        sharedpreferences = getSharedPreferences(OwnerDetails, Context.MODE_PRIVATE);

        signupB = (Button) findViewById(R.id.signupB);
        companyName = (EditText) findViewById(R.id.companyName);
        ownerFName = (EditText) findViewById(R.id.ownerFName);
        ownerLName = (EditText) findViewById(R.id.ownerLName);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        pass = (EditText) findViewById(R.id.pass);
        repass = (EditText) findViewById(R.id.repass);
        Login = (TextView)findViewById(R.id.link_login);

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uniqueID = UUID.randomUUID().toString();
                companyNameS = companyName.getText().toString().toLowerCase().trim();
                ownerFNameS = ownerFName.getText().toString().trim();
                ownerLNameS = ownerLName.getText().toString().trim();
                emailS = email.getText().toString().trim();
                mobileS = mobile.getText().toString().trim();
                passS = pass.getText().toString().trim();
                repassS = repass.getText().toString().trim();



                if (companyNameS.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Company name Please", Toast.LENGTH_SHORT).show();
                } else if (ownerFNameS.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "First name Please", Toast.LENGTH_SHORT).show();
                } else if (ownerLNameS.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Last name Please", Toast.LENGTH_SHORT).show();
                } else if (isMailValide(emailS) == false) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid Email ID", Toast.LENGTH_SHORT).show();
                } else if (isValidMobile(mobileS) == false) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                }else if (mobileS.length()!=10 ) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else if (passS.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Password Please", Toast.LENGTH_SHORT).show();
                } else if (repassS.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "RE-Password Please", Toast.LENGTH_SHORT).show();
                } else if (passS.equals(repassS)) {
                    password = passS;
                    new MyTask().execute();
                } else {
                    Toast.makeText(SignupActivity.this, "Password and Re-password mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }

    private boolean isMailValide(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            AndroidUtils.animateView(progressOverlay,image,animate, View.VISIBLE, 0.8f, 200);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //boolean isInserted = myDb.insertDataForProfile(uniqueID, companyNameS, ownerFNameS, ownerLNameS, emailS, mobileS, password);

                            /*-----------------------save all the details  to shared preferences for future need-----------------------------*/
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(CompanyName, companyNameS);

                    editor.putString(fname, ownerFNameS);
                    editor.putString(lname, ownerLNameS);
                    editor.putString(Email, emailS);
                    editor.putString(Mobile, mobileS);
                    editor.putString(Password, password);
                    editor.commit();
                            /*---------------------------------------------------------------------------------------------------------------*/


                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("id", uniqueID));
                    postParameters.add(new BasicNameValuePair("companyName", companyNameS));
                    postParameters.add(new BasicNameValuePair("ownerFName", ownerFNameS));
                    postParameters.add(new BasicNameValuePair("ownerLName", ownerLNameS));
                    postParameters.add(new BasicNameValuePair("email", emailS));
                    postParameters.add(new BasicNameValuePair("mobile", mobileS));
                    postParameters.add(new BasicNameValuePair("password", password));

                    String response = null;
                    try {
                        response = SimpleHttpClient
                                .executeHttpPost(
                                        "http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/SignUp.do",
                                        postParameters);

                        String res = response.toString();
                        System.out.println(res);
                        resp = res.replaceAll("\\s+", "");

                    } catch (Exception e) {
                        network=1;
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
                //Toast.makeText(OwnerProfile.this, resp, Toast.LENGTH_SHORT).show();
                if (resp.equals("success")) {
                    //Toast.makeText(SignupActivity.this, "Thank you " + ownerFNameS, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    onBackPressed();
                } else {
                   // Toast.makeText(SignupActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }

                if (null != errorMsg && !errorMsg.isEmpty()) {

                    //  Toast.makeText(SignupActivity.this, ownerFNameS + "Please try again", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(SignupActivity.this, "Slow internet connection please try again", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            AndroidUtils.animateView(progressOverlay,image,animate, View.GONE, 0, 200);
            resp="null";
            if (network==1)
            {
                // Toast.makeText(LoginActivity.this, "Connectivity Problem!! Please try again and Check your Internet Connection.", Toast.LENGTH_SHORT).show();
                network=0;
                final AlertDialog.Builder retry = new AlertDialog.Builder(SignupActivity.this);
                retry.setMessage("Connection Problem!!");
                retry.setCancelable(false);
                retry.setTitle("Alert");
                retry.setIcon(R.drawable.feedbot_final);
                retry.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new MyTask().execute();
                        } catch (NullPointerException e) {

                        }
                    }
                });
                retry.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                retry.show();
            }

            /*if (!msg.equals(""))
            {
                Toast.makeText(LoginActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }*/
            //Toast.makeText(getApplicationContext(), "hiii", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }
}
