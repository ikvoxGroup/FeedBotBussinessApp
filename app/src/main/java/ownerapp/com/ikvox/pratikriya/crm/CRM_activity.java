package ownerapp.com.ikvox.pratikriya.crm;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ownerapp.com.ikvox.pratikriya.MainActivity;
import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.crm.Branch.BranchDetails;
import ownerapp.com.ikvox.pratikriya.crm.Feedback.FeedbackReport;
import ownerapp.com.ikvox.pratikriya.crm.Track.Tracking_activity;

/**
 * Created by MyMac on 21/06/16.
 */
public class CRM_activity extends ActionBarActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FrameLayout statusBar;
    Boolean homeButton = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_crm);
        setSupportActionBar(toolbar);
        toolbarStatusBar();
        navigationBarStatusBar();
        toolbar.setBackgroundColor(MainActivity.color);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.content_crm);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(MainActivity.color);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BranchDetails(), "Branch Details");
        adapter.addFragment(new FeedbackReport(), "Feedback Report");
        adapter.addFragment(new Tracking_activity(), "Track");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar_crm);
        toolbar = (Toolbar) findViewById(R.id.toolbar_crm);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CRM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Dialog dialog = new Dialog(CRM_activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.about_dialog);
                dialog.show();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CRM_activity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_crm);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                CRM_activity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_crm);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CRM_activity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar_crm);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                CRM_activity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(CRM_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
