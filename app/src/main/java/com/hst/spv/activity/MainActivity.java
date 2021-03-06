package com.hst.spv.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hst.spv.R;
import com.hst.spv.customview.SideDrawerLayout;
import com.hst.spv.customview.SideDrawerToggle;
import com.hst.spv.customview.SideMenuView;
import com.hst.spv.fragment.EventFragment;
import com.hst.spv.fragment.HomeFragment;
import com.hst.spv.fragment.NewsFragment;
import com.hst.spv.fragment.SocialInitiativesFragment;
import com.hst.spv.fragment.SocialMediaFragment;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.PreferenceStorage;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Admin on 14-09-2020.
 */

public class MainActivity extends AppCompatActivity implements SideMenuView.OnMenuClickListener, DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();
    Toolbar toolbar;
    private ViewHolder mViewHolder;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        changeFragment(0);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_home:
                                changeFragment(0);
                                break;
                            case R.id.navigation_news:
                                changeFragment(1);
                                break;
                            case R.id.navigation_events:
                                changeFragment(2);
                                break;
                            case R.id.navigation_social_initiatives:
                                changeFragment(3);
                                break;
                            case R.id.navigation_social_media:
                                changeFragment(4);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_landing, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.action_filter).getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) mSearchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search button clicked");
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.d(TAG, "Query submitted with String:" + s);
//                int currentpage = viewPager.getCurrentItem();
//                Log.d(TAG, "current item is" + currentpage);

                if (s != null) {
                    makeSearch(s);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.d(TAG, "searchView closed");

//                exitSearch();

                return false;
            }
        });

        mSearchView.setQueryHint("Search Event name");
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Workaround for SearchView close listener
        switch (item.getItemId()) {
            case R.id.language_change:
                //ajaz
                // Toast.makeText(this, "advance filter clicked", Toast.LENGTH_SHORT).show();
//                Context appContext = this;

                startActivity(new Intent(this, LanguageChangeActivity.class));
                break;

            case R.id.notification_img:
//                if (userid.equalsIgnoreCase("1")) {
                startActivity(new Intent(this, NotificationActivity.class));
//                } else {
//                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
//                    alertDialogBuilder.setTitle("Login");
//                    alertDialogBuilder.setMessage("Log in to Access");
//                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//                            sharedPreferences.edit().clear().apply();
//
//                            Intent homeIntent = new Intent(this, SplashScreenActivity.class);
//                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            this.startActivity(homeIntent);
//
//                        }
//                    });
//                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeSearch(String eventname) {
//        PreferenceStorage.setSearchFor(this, eventname);
//        String id = "", name = "";
//        PreferenceStorage.savePaguthiID(this, id);
//        PreferenceStorage.savePaguthiName(this, name);
//        startActivity(new Intent(this, SearchResultConstituentActivity.class));
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        SideDrawerToggle SideDrawerToggle = new SideDrawerToggle(this,
                mViewHolder.mSideDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mSideDrawerLayout.setDrawerListener(SideDrawerToggle);
        SideDrawerToggle.syncState();

    }

    private void handleMenu() {
//        mMenuAdapter = new SideMenuAdapterTemp(mTitles);

        mViewHolder.mSideMenuView.setOnMenuClickListener(this);
//        mViewHolder.mSideMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
//        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
//        if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
//            startPersonDetailsActivity(-1);
//        } else {
//            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
//            alertDialogBuilder.setTitle("Login");
//            alertDialogBuilder.setMessage("Log in to Access");
//            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    doLogout();
//                }
//            });
//            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            alertDialogBuilder.show();
//        }
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
//        setTitle(mTitles.get(position));
//
//        // Set the right options selected
//        mMenuAdapter.setSelectView(position, true);
//
//        // Navigate to the right fragment
//        switch (position) {
//            default:
//                changeFragment(0);
//                break;
//        }

        // Close the drawer
        mViewHolder.mSideDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private SideDrawerLayout mSideDrawerLayout;
        private SideMenuView mSideMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mSideDrawerLayout = (SideDrawerLayout) findViewById(R.id.drawer);
            mSideMenuView = (SideMenuView) mSideDrawerLayout.getMenuView();
//            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);

            setSupportActionBar(mToolbar);
        }
    }

    public void startPersonDetailsActivity(long id) {
//        Intent homeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
//        startActivityForResult(homeIntent, 0);
//        finish();
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new HomeFragment();
//            imgHome.setImageResource(R.drawable.ic_home_selected);
//            txtHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//            imgGallery.setImageResource(R.drawable.ic_gallery);
//            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgEvent.setImageResource(R.drawable.ic_event);
//            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgSocial.setImageResource(R.drawable.ic_social);
//            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        } else if (position == 1) {
            newFragment = new NewsFragment();
//            imgHome.setImageResource(R.drawable.ic_home);
//            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgGallery.setImageResource(R.drawable.ic_gallery_selected);
//            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//            imgEvent.setImageResource(R.drawable.ic_event);
//            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgSocial.setImageResource(R.drawable.ic_social);
//            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        } else if (position == 2) {
            newFragment = new EventFragment();
//            imgHome.setImageResource(R.drawable.ic_home);
//            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgGallery.setImageResource(R.drawable.ic_gallery);
//            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgEvent.setImageResource(R.drawable.ic_event_selected);
//            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//            imgSocial.setImageResource(R.drawable.ic_social);
//            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
        } else if (position == 3) {
            newFragment = new SocialInitiativesFragment();
//            imgHome.setImageResource(R.drawable.ic_home);
//            txtHome.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgGallery.setImageResource(R.drawable.ic_gallery);
//            txtGallery.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgEvent.setImageResource(R.drawable.ic_event);
//            txtEvent.setTextColor(ContextCompat.getColor(this, R.color.menu_grey));
//            imgSocial.setImageResource(R.drawable.ic_social_selected);
//            txtSocial.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        else if (position == 4) {
            newFragment = new SocialMediaFragment();
        }
//        else if (position == 5) {
//            newFragment = new WebDevelopmentFragment();
//        } else if (position == 6) {
//            newFragment = new BrandingFragment();
//        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}