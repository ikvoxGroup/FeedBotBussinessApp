
package ownerapp.com.ikvox.pratikriya;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ownerapp.com.ikvox.pratikriya.LoginSessionManagement.UserSessionManager;


/**
 * Splash Activity is the start-up activity that starts, various app
 * initialization operations are performed and user login details
 */
public class SplashActivity extends ActionBarActivity {
    private final static String LOG_TAG = SplashActivity.class.getSimpleName();

    // User Session Manager Class
    UserSessionManager session;

    ImageView splashImage;
 //   private ProgressBar spinner;
    ImageView loadingAnimationImageView;
    AnimationDrawable loadingProgressAnimation;
    FrameLayout statusBar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int theme;

    private Handler mHandler = new Handler();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        /*Window window = SplashActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Color c= new Color();
        c=R.attr.colorPrimary;
        window.setStatusBarColor(SplashActivity.this.getResources().getColor());*/
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        toolbarStatusBar();
        navigationBarStatusBar();
        /*spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);*/
        loadingAnimationImageView = (ImageView)findViewById(R.id.loadingAnimationImageView);
        loadingProgressAnimation = (AnimationDrawable) loadingAnimationImageView.getBackground();
        loadingAnimationImageView.setVisibility(View.GONE);
        splashImage = (ImageView) findViewById(R.id.splashImage);

        // Session class instance
        session = new UserSessionManager(getApplicationContext());
//        spinner.setVisibility(View.VISIBLE);
        loadingAnimationImageView.setVisibility(View.VISIBLE);
        loadingProgressAnimation.start();
        if (session.isUserLoggedIn()) {
           // Toast.makeText(SplashActivity.this, "Welcome to IkVox dashboard", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(SplashActivity.this, "Please login", Toast.LENGTH_SHORT).show();
        }

        /*Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();*/

        mHandler.postDelayed(new Runnable() {
            public void run() {
                doStuff();
            }
        }, 4000);
    }

    // Check user login
    private void doStuff() {

        // If User is not logged in , This will redirect user to LoginActivity.
        if (session.isUserLoggedIn()) {
           // spinner.setVisibility(View.INVISIBLE);
            loadingAnimationImageView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
           // spinner.setVisibility(View.INVISIBLE);
            loadingAnimationImageView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
        //for disable back button
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_splash);
    }


    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                SplashActivity.this.getTheme().resolveAttribute(Color.BLACK,typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_splash);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                SplashActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_splash);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                SplashActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_splash);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                SplashActivity.this.getTheme().resolveAttribute(Color.BLACK, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
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
    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }
}
