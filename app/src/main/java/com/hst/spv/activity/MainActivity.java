package com.hst.spv.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hst.spv.R;
import com.hst.spv.customview.SideDrawerLayout;
import com.hst.spv.customview.SideDrawerToggle;
import com.hst.spv.customview.SideMenuView;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

/**
 * Created by Admin on 14-09-2020.
 */

public class MainActivity extends AppCompatActivity implements SideMenuView.OnMenuClickListener, DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();
    Toolbar toolbar;
    private ViewHolder mViewHolder;


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