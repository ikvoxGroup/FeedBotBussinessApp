package ownerapp.com.ikvox.pratikriya;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import ownerapp.com.ikvox.pratikriya.Database.MyDatabase;
import ownerapp.com.ikvox.pratikriya.Database.QueryDatabase;
import ownerapp.com.ikvox.pratikriya.LoginSessionManagement.UserSessionManager;
import ownerapp.com.ikvox.pratikriya.RecyclerView.RecyclerViewAdapters.DrawerAdapter;
import ownerapp.com.ikvox.pratikriya.RecyclerView.RecyclerViewClasses.DrawerItem;
import ownerapp.com.ikvox.pratikriya.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import ownerapp.com.ikvox.pratikriya.Utils.JsonParser;
import ownerapp.com.ikvox.pratikriya.Utils.PicassoTransform.CircleTransformWhite;
import ownerapp.com.ikvox.pratikriya.crm.CRM_activity;

// You can check the methods that I use inside onCreate below menu methods

public class MainActivity extends ActionBarActivity {
    public static int color;
    final Context context = this;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    ScrollView scrollView;
    ActionBarDrawerToggle mDrawerToggle;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    Intent intent;
    FrameLayout statusBar;
    SharedPreferences.Editor editor;
    ActivityOptions options;
    TextView textViewName, textViewUsername;
    ImageView imageViewToogle, imageViewCover, imageViewPicture;
    ToggleButton toggleButtonDrawer;
    RelativeLayout relativeLayoutDrawerTexts, relativeLayoutChooseTheme, relativeLayoutSettings;
    LinearLayout linearLayoutMain, linearLayoutSecond;
    String urlName = "", urlPost = "";
    String urlProfile = "https://graph.facebook.com/" + urlName;
    String urlPicture = "https://graph.facebook.com/" + urlName + "picture?type=large&redirect=false";
    String urlCover = "https://graph.facebook.com/" + urlName + "cover";
    String facebookID, name, username, cover, picture;
    Bitmap bitmapPicture, bitmapCover;
    Drawable drawablePicture, drawableCover;
    File file, folder;
    Boolean downloaded, error = false;

    RecyclerView recyclerViewDrawer;

    RecyclerView.Adapter adapterDrawer;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    UserSessionManager session;
    MyDatabase db;
    QueryDatabase qdb;
    SQLiteDatabase sdb,qsdb;
    public static String[] drawerTitles = {"Home"};
    private Boolean exit = false;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    TextView UserNumber;

    JSONParserIkVox jParser = new JSONParserIkVox();
    JSONObject json,json1;
    private static String url_login = "http://feedbotappserver.cgihum6dcd.us-west-2.elasticbeanstalk.com/CompanyBranchDetails.do";

    JSONArray EmpNamejson,Designationjson,Departmentjson,locationjson,Emp_EmailIDjson,Phone_numberjson,Reporting_MailIDjson;
    ArrayList<String> EmpName;
    ArrayList<String> Designation;
    ArrayList<String> Department;
    ArrayList<String> location;
    ArrayList<String> Emp_EmailID;
    ArrayList<String> Phone_number;
    ArrayList<String> Reporting_MailID;

    private String resp;
    private String errorMsg;
    public static int size;

    int len;
    int error1,network;
    static ProgressDialog mProgressDialog;
    public static String CompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Select theme saved by user
        theme();

        super.onCreate(savedInstanceState);

        // Set content to the view
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(PREFER_NAME,MODE_PRIVATE);
        String number= sp.getString(KEY_NAME,null);
        UserNumber= (TextView)findViewById(R.id.textViewUsername);
        UserNumber.setText(number);
        //db = new MyDatabase(this);

        session = new UserSessionManager(getApplicationContext());

        //Setup Status Bar and Toolbar
        toolbarStatusBar();

        // Setup Fragments
        setFragment(sharedPreferences.getInt("FRAGMENT", 0));

        //Setup Navigation Drawer
        navigationDrawer();
        navigationBarStatusBar();

        // Setup drawer accounts toggle.
        toogleButtonDrawer();

        // Open settings method
        openSettings();
        openQuery();
       // openReport();
        openCRM();
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Syncing Data!! Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        location=new ArrayList<String>() ;
        Department=new ArrayList<String>() ;
        EmpName =new ArrayList<String>() ;
        Designation=new ArrayList<String>() ;
        Phone_number=new ArrayList<String>() ;
        Emp_EmailID=new ArrayList<String>() ;
        Reporting_MailID=new ArrayList<String>() ;
    }

    private void openCRM() {
            relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutCRM);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "CRM", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, CRM_activity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after some time
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
    }

    private void openQuery() {
        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutQuery);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Query_main.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after some time
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
    }
    /*private void openReport() {
        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutReport);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Reports.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after some time
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
    }*/

    private void openSettings() {
        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.relativeLayoutSettings);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after some time
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);
            dialog.show();
            return true;
        }else if (id == R.id.action_Sync) {
            //startService(new Intent(getBaseContext(), GetEmployeeDetailsService.class));
            new MyTask().execute();
            return true;
        }
        else if (id == R.id.action_Logout) {
            final AlertDialog.Builder logOut = new AlertDialog.Builder(MainActivity.this);
            logOut.setMessage("Are You sure You want to Logout!!");
            logOut.setCancelable(false);
            logOut.setIcon(R.drawable.feedbot_final);
            logOut.setTitle("Alert");
            logOut.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        session.logoutUser();
                        finish();
                    } catch (NullPointerException e) {

                    }
                }
            });
            logOut.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            logOut.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            logOut.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    @Override
    public void onBackPressed() {
        /*sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("DOWNLOAD", false);
        editor.apply();*/
        if (exit) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }


    }


    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
    }

    public void navigationDrawer() {

        // Get ID saved in settings
        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        facebookID = sharedPreferences.getString("FACEBOOKID", "javiersegoviacordoba");

        // Cast drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Get preferences
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        downloaded = sharedPreferences.getBoolean("DOWNLOAD", false);

        // Fix right margin to 56dp (portrait)
        View drawer = findViewById(R.id.scrimInsetsFrameLayout);
        ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = displayMetrics.widthPixels - (56 * Math.round(displayMetrics.density));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = displayMetrics.widthPixels + (20 * Math.round(displayMetrics.density)) - displayMetrics.widthPixels / 2;
        }

        name = sharedPreferences.getString("NAME", "");
        if (!name.equals("")) {
            // textViewName = (TextView) findViewById(R.id.textViewName);
            // textViewName.setText(name);
        }
        username = sharedPreferences.getString("USERNAME", "");
        if (!username.equals("")) {
            textViewUsername = (TextView) findViewById(R.id.textViewUsername);
            textViewUsername.setText(username);
        }
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/picture.png");
        if (file.length() != 0) {
            imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
            imageViewPicture.setImageDrawable(Drawable.createFromPath(file.toString()));
        }
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/cover.png");
        if (file.length() != 0) {
            //imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
            //imageViewCover.setImageDrawable(Drawable.createFromPath(file.toString()));
        }

        if (!downloaded) {

            // Get facebook items (name, username, picture, cover)
            new AsyncTaskFacebookParseJson().execute(facebookID);

        } else {
            Toast.makeText(MainActivity.this, downloaded.toString(), Toast.LENGTH_SHORT).show();
        }

        // Setup Drawer Icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        // statusBar color behind navigation drawer
        TypedValue typedValueStatusBarColor = new TypedValue();
        MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueStatusBarColor, true);
        final int colorStatusBar = typedValueStatusBarColor.data;
        mDrawerLayout.setStatusBarBackgroundColor(colorStatusBar);

        // Setup RecyclerView inside drawer
        recyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawer);
        recyclerViewDrawer.setHasFixedSize(false);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        // final String[] drawerTitles = getResources().getStringArray(R.array.drawer);
        final TypedArray drawerIcons = getResources().obtainTypedArray(R.array.drawerIcons);
        for (int i = 0; i < drawerTitles.length; i++) {
            drawerItems.add(new DrawerItem(drawerTitles[i], drawerIcons.getDrawable(i)));
        }
        drawerIcons.recycle();
        adapterDrawer = new DrawerAdapter(drawerItems);
        recyclerViewDrawer.setAdapter(adapterDrawer);
        final TypedValue typedValue = new TypedValue();
        MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        color = typedValue.data;

        //TODO try to get status bar translucent in landscape mode (lollipop)
        //mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(android.R.color.transparent));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        /*recyclerViewDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                for (int i = 0; i < drawerTitles.length; i++) {
                    if (i == sharedPreferences.getInt("FRAGMENT", 0)) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                    } else if (i == sharedPreferences.getInt("FRAGMENT", 1)) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                    }

                    else if(i == sharedPreferences.getInt("FRAGMENT", 2)) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);
                        *//*textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));*//*
                    }
                }

                // unregister listener (this is important)
                recyclerViewDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });*/


        // RecyclerView item listener.
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerViewDrawer);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, final int position, long id) {

                for (int i = 0; i < drawerTitles.length; i++) {
                    if (i == position) {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(color);
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerIcon.setAlpha(255);
                        }
                        textViewDrawerTitle.setTextColor(color);
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        TypedValue typedValueDrawerSelected = new TypedValue();
                        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawerSelected, true);
                        int colorDrawerItemSelected = typedValueDrawerSelected.data;
                        colorDrawerItemSelected = (colorDrawerItemSelected & 0x00FFFFFF) | 0x30000000;
                        relativeLayoutDrawerItem.setBackgroundColor(colorDrawerItemSelected);

                    } else {
                        ImageView imageViewDrawerIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
                        TextView textViewDrawerTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                        imageViewDrawerIcon.setColorFilter(getResources().getColor(R.color.md_text));
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerIcon.setImageAlpha(138);
                        } else {
                            imageViewDrawerIcon.setAlpha(138);
                        }
                        textViewDrawerTitle.setTextColor(getResources().getColor(R.color.md_text));
                        RelativeLayout relativeLayoutDrawerItem = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
                        relativeLayoutDrawerItem.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after some time
                        setFragment(position);
                    }
                }, 250);
                mDrawerLayout.closeDrawers();
            }
        });
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

    public void toogleButtonDrawer() {
        toggleButtonDrawer = (ToggleButton) findViewById(R.id.toggleButtonDrawer);
        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);
        linearLayoutSecond = (LinearLayout) findViewById(R.id.linearLayoutSecond);
        toggleButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(false);
                    imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_down));
                    linearLayoutMain.setVisibility(View.VISIBLE);
                    linearLayoutSecond.setVisibility(View.GONE);
                }
                if (toggleButtonDrawer.isChecked()) {
                    toggleButtonDrawer.setChecked(true);
                    imageViewToogle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_drop_up));
                    linearLayoutMain.setVisibility(View.GONE);
                    linearLayoutSecond.setVisibility(View.VISIBLE);
                }*/
                startActivity(new Intent(getApplicationContext(), Edit_profile.class));


            }
        });
    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //editor.putInt("POSITION", 0).apply();
        editor.putBoolean("DOWNLOAD", false);
        editor.apply();

        Boolean prueba = sharedPreferences.getBoolean("DOWNLOAD", false);
        //Toast.makeText(this, "Leave " + prueba.toString(), Toast.LENGTH_SHORT).show();
    }


    public class AsyncTaskFacebookParseJson extends AsyncTask<String, String, String> {

        // facebook urls
        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlName = url[0];
            urlProfile = "https://graph.facebook.com/" + urlName;
            urlPicture = "https://graph.facebook.com/" + urlName + "/picture?type=large&redirect=false";
            urlCover = "https://graph.facebook.com/" + urlName + "/?fields=cover";

            JSONObject jsonObjectProfile, jsonObjectPicture, jsonObjectCover;
            try {
                jsonObjectProfile = JsonParser.readJsonFromUrl(urlProfile);
                jsonObjectPicture = JsonParser.readJsonFromUrl(urlPicture);
                jsonObjectCover = JsonParser.readJsonFromUrl(urlCover);

                // Storing each json item_post in variable
                name = jsonObjectProfile.getString("name");
                username = "Facebook ID: " + jsonObjectProfile.getString("username");
                picture = jsonObjectPicture.getJSONObject("data").getString("url");
                cover = jsonObjectCover.getJSONObject("cover").getString("source");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            //  textViewName = (TextView) findViewById(R.id.textViewName);
            // textViewName.setText(name);
            //textViewUsername = (TextView) findViewById(R.id.textViewUsername);
            //textViewUsername.setText(username);
            //imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
            //  imageViewCover = (ImageView) findViewById(R.id.imageViewCover);

            sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("NAME", name);
            editor.putString("USERNAME", username);
            editor.apply();

            folder = new File(Environment.getExternalStorageDirectory() + "/MaterialDesignApp");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Target targetPicture = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/picture.png");
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                ostream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Toast.makeText(MainActivity.this, "Creating Picture", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            Target targetCover = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MaterialDesignApp/cover.png");
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                ostream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            Picasso.with(context).load(picture).transform(new CircleTransformWhite()).into(targetPicture);
            Picasso.with(context).load(cover).into(targetCover);

//           imageViewPicture.setTag(targetPicture);
            //           imageViewCover.setTag(targetCover);

            //           Picasso.with(context).load(picture).placeholder(imageViewPicture.getDrawable()).transform(new CircleTransformWhite()).into(imageViewPicture);
            //          Picasso.with(context).load(cover).placeholder(imageViewCover.getDrawable()).into(imageViewCover);

            sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("DOWNLOAD", true);
            editor.apply();
        }
    }

    public void setFragment(int position) {
        switch (position) {
            case 0:
                // sharedPreferences.edit().putInt("FRAGMENT", 0).apply();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Welcome_activity fragmentDesign = new Welcome_activity();
                fragmentTransaction.replace(R.id.content, fragmentDesign);
                fragmentTransaction.commit();
                break;

        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // AndroidUtils.animateView(progressOverlay, image, animate, View.VISIBLE, 0.8f, 200);
            mProgressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    /*JSONArray Employee_nameJson = new JSONArray(EmpName);
                    try {
                        json1.put("empName",Employee_nameJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    CompanyName = sp.getString(KEY_COMPANYNAME, null);


                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("companyName", CompanyName));
                    // postParameters.add(new BasicNameValuePair("password",json1.toString()));


                    json = jParser.makeHttpRequest(url_login, "GET", postParameters);
                    String s = null;
                    try {


                        s = json.getString("status");
                        EmpNamejson = json.getJSONArray("Employee_name");
                        Designationjson = json.getJSONArray("Designation");
                        Departmentjson = json.getJSONArray("Department");
                        locationjson = json.getJSONArray("location");
                        Emp_EmailIDjson = json.getJSONArray("Emp_EmailID");
                        Phone_numberjson = json.getJSONArray("Phone_number");
                        Reporting_MailIDjson = json.getJSONArray("Reporting_MailID");

                        len = EmpNamejson.length();
                        resp = s;
                    } catch (Exception e) {
                        e.printStackTrace();
                        network = 1;
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

                if (resp.equals("success")) {

                    try {
                        //converting JSON value to Arraylist

                        for (int i = 0; i < len; i++) {

                            EmpName.add(EmpNamejson.get(i).toString());
                            Designation.add(Designationjson.get(i).toString());
                            Department.add(Departmentjson.get(i).toString());
                            location.add(locationjson.get(i).toString());
                            Emp_EmailID.add(Emp_EmailIDjson.get(i).toString());
                            Phone_number.add(Phone_numberjson.get(i).toString());
                            Reporting_MailID.add(Reporting_MailIDjson.get(i).toString());

                        }
                    } catch (JSONException e) {

                    }
                    // now we can fatch data from the arraylist using ex.  EmpName.get(2);   2 is the index value
                    //Toast.makeText(getApplicationContext(), "PM" + EmpNamejson.get(3).toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), EmpName.get(3), Toast.LENGTH_SHORT).show();
                    db = new MyDatabase(getApplicationContext());
                    sdb = db.getWritableDatabase();
                    sdb = getApplicationContext().openOrCreateDatabase(MyDatabase.DBNAME, MODE_PRIVATE, null);
                    sdb.execSQL("CREATE TABLE IF NOT EXISTS "
                            + CompanyName
                            + " (Branch TEXT, Department TEXT,Employee TEXT, Designation TEXT,Phone TEXT, Email TEXT, Report TEXT);");
                    String[] branch = new String[location.size()];
                    for (int i = 0; i < location.size(); i++)
                        branch[i] = location.get(i).toString().trim();
                    branch = new HashSet<String>(Arrays.asList(branch)).toArray(new String[0]);
                    for (int i = 0; i < branch.length; i++) {
                        branch[i] = branch[i].valueOf(branch[i].charAt(0)).toUpperCase() + branch[i].substring(1, branch[i].length());
                    }

                    qdb = new QueryDatabase(getApplicationContext());
                    qsdb = qdb.getWritableDatabase();
                    qsdb = getApplicationContext().openOrCreateDatabase(QueryDatabase.DBNAME, MODE_PRIVATE, null);
                    for (int i = 0; i < branch.length; i++) {
                        qsdb.execSQL("create table IF NOT EXISTS " + CompanyName + "_" + branch[i] + " (QueryNumber TEXT, Query TEXT,QueryType TEXT, Keyword TEXT);");
                    }
                    sdb.execSQL("Delete from " + CompanyName);
                    int i;
                    for (i = 0; i < location.size(); i++) {
                        ContentValues value = new ContentValues();
                        value.put("Branch", location.get(i).toString().toLowerCase());
                        value.put("Department", Department.get(i).toString().toLowerCase());
                        value.put("Employee", EmpName.get(i).toString().toLowerCase());
                        value.put("Designation", Designation.get(i).toString().toLowerCase());
                        value.put("Phone", Phone_number.get(i).toString().toLowerCase());
                        value.put("Email", Emp_EmailID.get(i).toString().toLowerCase());
                        value.put("Report", Reporting_MailID.get(i).toString().toLowerCase());
                        sdb.insert(CompanyName, null, value);
                    }
                }




                    /*String count = "SELECT count(*) FROM "+CompanyName;
                    Cursor mcursor = sdb.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    if(icount>0) {
                        sdb.execSQL("Delete from "+CompanyName);
                        int i;
                        for (i = 0; i < location.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("Branch", location.get(i).toString().toLowerCase());
                            value.put("Department", Department.get(i).toString().toLowerCase());
                            value.put("Employee", EmpName.get(i).toString().toLowerCase());
                            value.put("Designation", Designation.get(i).toString().toLowerCase());
                            value.put("Phone", Phone_number.get(i).toString().toLowerCase());
                            value.put("Email", Emp_EmailID.get(i).toString().toLowerCase());
                            value.put("Report", Reporting_MailID.get(i).toString().toLowerCase());
                            sdb.insert(CompanyName,null, value);
                        }
                    }
                    else
                    {
                        int i;
                        for (i = 0; i < location.size(); i++) {
                            ContentValues value = new ContentValues();
                            value.put("Branch", location.get(i).toString().toLowerCase());
                            value.put("Department", Department.get(i).toString().toLowerCase());
                            value.put("Employee", EmpName.get(i).toString().toLowerCase());
                            value.put("Designation", Designation.get(i).toString().toLowerCase());
                            value.put("Phone", Phone_number.get(i).toString().toLowerCase());
                            value.put("Email", Emp_EmailID.get(i).toString().toLowerCase());
                            value.put("Report", Reporting_MailID.get(i).toString().toLowerCase());
                            sdb.insert(CompanyName,null, value);
                        }
                    }
                } else */if (resp.equals("Failed")) {
                    error1 = 1;
                }


                if (null != errorMsg && !errorMsg.isEmpty()) {

                }
            } catch (Exception e) {


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            resp = "null";
            // Log.d("k", EmpNamejson.toString());

            if (error1 == 1) {

                Toast.makeText(getApplicationContext(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                error1 = 0;
                network = 0;
            } else if (network == 1) {

                error1 = 0;
                network = 0;
            }
            super.onPostExecute(aVoid);
        }

    }

}