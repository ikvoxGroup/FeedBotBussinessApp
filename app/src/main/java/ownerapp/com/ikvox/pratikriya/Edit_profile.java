package ownerapp.com.ikvox.pratikriya;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;

/**
 * Created by MyMac on 13/06/16.
 */
public class Edit_profile extends ActionBarActivity {
    ImageView cover;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int theme;
    FrameLayout statusBar;
    Toolbar toolbar;
    Boolean homeButton = false,themeChanged;
    Intent intent;
    Button Submit;
    EditText Email,FName, LName,Company;
    TextView ShowFName,ShowLName,ShowNumber;
    LinearLayout ClickEmail, ClickFName, ClickLName, ClickCompany;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";
    SharedPreferences sp;
    String FirstName, LastName,Comp,Contact,Mail;

    MyDatabase db;
    SQLiteDatabase sdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        db= new MyDatabase(this);
        sdb= db.getWritableDatabase();
        theme();
        toolbarStatusBar();
        navigationBarStatusBar();
        sp = getSharedPreferences(PREFER_NAME,MODE_PRIVATE);
        final String number= sp.getString(KEY_NAME,null);
        cover= (ImageView)findViewById(R.id.cover_edit_profile);
        //cover.setImageResource(R.drawable.drawer_header);
        Submit = (Button)findViewById(R.id.Edit_profile_submit);
        Submit.setBackgroundColor(MainActivity.color);
        toolbar.setBackgroundColor(MainActivity.color);
        Email= (EditText)findViewById(R.id.EmailID);
        FName= (EditText)findViewById(R.id.FName);
        LName=(EditText)findViewById(R.id.LName);
        Company=(EditText)findViewById(R.id.ComanyName);
        ShowNumber=(TextView) findViewById(R.id.ShowNumber);
        ShowFName=(TextView)findViewById(R.id.ShowFName);
        ShowLName=(TextView)findViewById(R.id.ShowLName);
        ClickEmail=(LinearLayout)findViewById(R.id.ClickEmail);
        ClickFName=(LinearLayout) findViewById(R.id.ClickFName);
        ClickLName=(LinearLayout)findViewById(R.id.ClickLName);
        ClickCompany=(LinearLayout)findViewById(R.id.ClickCompany);

        String q= "select * from Login";
        Cursor cursor = sdb.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            do {
                Contact= cursor.getString(0);
                FirstName=cursor.getString(1);
                LastName=cursor.getString(2);
                Mail=cursor.getString(3);
                Comp=cursor.getString(4);
            } while (cursor.moveToNext());


        ShowFName.setText(FirstName);
        ShowLName.setText(LastName);
        ShowNumber.setText(Contact);
        Email.setText(Mail);
        FName.setText(FirstName);
        LName.setText(LastName);
        Company.setText(Comp);
        Company.setEnabled(false);


        FName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ShowFName.setText(s.toString());
            }
        });

        LName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            ShowLName.setText(s.toString());
            }
        });
    }
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().equals(""))
                {
                    Email.setError("The field cannot be blank");
                }
                else if (FName.getText().toString().equals(""))
                {
                    FName.setError("The field cannot be blank");
                }
                else if (LName.getText().toString().equals(""))
                {
                    LName.setError("The field cannot be blank");
                }
                else {
                    String qry = "Update " + number + " set FirstName= '" + FName + "', LastName= '" + LName + "', EmailId= '" + Email + "' where Number= '" + number + "'";
                    sdb.execSQL(qry);
                }
            }
        });
    }

    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }
    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(LoginActivity.companyName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void settingTheme(int theme) {
        switch (theme) {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_post clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Dialog dialog = new Dialog(Edit_profile.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);
            dialog.show();
            return true;
        }
        if (id == android.R.id.home) {
            if (!homeButton) {
                NavUtils.navigateUpFromSameTask(Edit_profile.this);
            }
            if (homeButton) {
                if (!themeChanged) {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("DOWNLOAD", false);
                    editor.apply();
                }
                intent = new Intent(Edit_profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(Edit_profile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Edit_profile.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar1);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                Edit_profile.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar1);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                Edit_profile.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar1);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                Edit_profile.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }


}
