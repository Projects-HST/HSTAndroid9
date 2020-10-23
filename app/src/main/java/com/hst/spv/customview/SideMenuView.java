package com.hst.spv.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.hst.spv.R;
import com.hst.spv.activity.SettingsActivity;
import com.hst.spv.activity.SplashScreenActivity;
import com.hst.spv.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;


public class SideMenuView extends RelativeLayout implements View.OnClickListener{
    private static final String TAG_FOOTER = "footer";
    private static final String TAG_HEADER = "header";
    private ViewGroup vg;
    @DrawableRes
    private static final int DEFAULT_DRAWABLE_ATTRIBUTE_VALUE = 0b11111111111111110010101111001111;
    @LayoutRes
    private static final int DEFAULT_LAYOUT_ATTRIBUTE_VALUE = 0b11111111111111110010101111010000;

    @DrawableRes
    private int mBackgroundDrawableId;
    @LayoutRes
    private int mHeaderViewId;
    @LayoutRes
    private int mFooterViewId;

    private OnMenuClickListener mOnMenuClickListener;
    private DataSetObserver mDataSetObserver;
    private MenuViewHolder mMenuViewHolder;
    private LayoutInflater mLayoutInflater;
    private Adapter mAdapter;

    public SideMenuView(Context context) {
        this(context, null);
    }

    public SideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
        initialize();
    }

    private void readAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SideMenuView);

        try {
            mBackgroundDrawableId = typedArray.getResourceId(R.styleable.SideMenuView_background, DEFAULT_DRAWABLE_ATTRIBUTE_VALUE);
            mHeaderViewId = typedArray.getResourceId(R.styleable.SideMenuView_header, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
            mFooterViewId = typedArray.getResourceId(R.styleable.SideMenuView_footer, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Initialize the menu view.
     */
    private void initialize() {
        ViewGroup rootView = (ViewGroup) inflate(getContext(), R.layout.side_view_menu, this);

        mMenuViewHolder = new MenuViewHolder(rootView, getContext());
        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
//                handleOptions();
                postInvalidate();
                requestLayout();
            }
        };

        handleBackground();
        handleHeader();
        handleFooter();
    }

    /**
     * Handles the background.
     */
    private void handleBackground() {
        if (mMenuViewHolder.mMenuBackground == null) {
            return;
        }

        if (mBackgroundDrawableId != DEFAULT_DRAWABLE_ATTRIBUTE_VALUE) {
            Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), mBackgroundDrawableId);

            if (backgroundDrawable != null) {
//                mMenuViewHolder.mMenuBackground.setImageDrawable(backgroundDrawable);
                return;
            }
        }

//        mMenuViewHolder.mMenuBackground.setBackgroundColor(getPrimaryColor());
    }

    /**
     * Handles the header view.
     */
    private void handleHeader() {
        if (mHeaderViewId == DEFAULT_LAYOUT_ATTRIBUTE_VALUE || mMenuViewHolder.mMenuHeader == null) {
            return;
        }

        View view = mLayoutInflater.inflate(mHeaderViewId, null, false);

        if (view != null) {
            if (mMenuViewHolder.mMenuHeader.getChildCount() > 0) {
                mMenuViewHolder.mMenuHeader.removeAllViews();
            }

            mMenuViewHolder.mMenuHeader.addView(view);
            view.setTag(TAG_HEADER);
            view.bringToFront();
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuClickListener != null) {
                        mOnMenuClickListener.onHeaderClicked();
                    }
                }
            });
        }
    }

    /**
     * Handles the footer view.
     */
    private void handleFooter() {
        if (mFooterViewId == DEFAULT_LAYOUT_ATTRIBUTE_VALUE || mMenuViewHolder.mMenuFooter == null) {
            return;
        }

        View view = mLayoutInflater.inflate(mFooterViewId, null, false);

        if (view != null) {
            if (mMenuViewHolder.mMenuFooter.getChildCount() > 0) {
                mMenuViewHolder.mMenuFooter.removeAllViews();
            }

            mMenuViewHolder.mMenuFooter.addView(view);
            view.setTag(TAG_FOOTER);
            view.bringToFront();

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof Button) {
                        viewGroup.getChildAt(i).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnMenuClickListener != null) {
                                    mOnMenuClickListener.onFooterClicked();
                                }
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    /**
     * Handles the menu options when adapter is set.
     */
    private void handleOptions() {
        if (mAdapter == null || mAdapter.isEmpty() || mMenuViewHolder.mMenuOptions == null) {
            return;
        }

        if (mMenuViewHolder.mMenuOptions.getChildCount() > 0) {
            mMenuViewHolder.mMenuOptions.removeAllViews();
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int index = i;

            View optionView = mAdapter.getView(i, null, this);

            if (optionView != null) {
                mMenuViewHolder.mMenuOptions.addView(optionView);
                optionView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuClickListener != null) {
                            mOnMenuClickListener.onOptionClicked(index, mAdapter.getItem(index));
                        }
                    }
                });
            }
        }
    }

    /**
     * Gets the primary color of this project.
     *
     * @return primary color of this project.
     */
    private int getPrimaryColor() {
        TypedArray typedArray = getContext().obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        return color;
    }

    /**
     * Sets the listener for menu clicks.
     *
     * @param onMenuClickListener Listener that registers menu clicks.
     */
    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    /**
     * Returns the header.
     *
     * @return The current header view.
     */
    public View getHeaderView() {
        return findViewWithTag(TAG_HEADER);
    }

    /**
     * Sets the header view.
     *
     * @param headerViewId View that becomes the header.
     */
    public void setHeaderView(@LayoutRes int headerViewId) {
        mHeaderViewId = headerViewId;
        handleHeader();
    }

    /**
     * Returns the footer.
     *
     * @return The current footer view.
     */
    public View getFooterView() {
        return findViewWithTag(TAG_FOOTER);
    }

    /**
     * Sets the footer view.
     *
     * @param footerViewId View that becomes the footer.
     */
    public void setFooterView(@LayoutRes int footerViewId) {
        mFooterViewId = footerViewId;
        handleFooter();
    }

    /**
     * Sets the background drawable of the MenuView.
     *
     * @param backgroundDrawableId Drawable that becomes the background.
     */
    public void setBackground(@DrawableRes int backgroundDrawableId) {
        mBackgroundDrawableId = backgroundDrawableId;
        handleBackground();
    }

    /**
     * Returns the adapter currently in use.
     *
     * @return The adapter currently used to display data in the Menu.
     */
    public Adapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets the data behind this MenuView.
     *
     * @param adapter The Adapter which is responsible for maintaining the
     *                data backing this list and for producing a view to represent an
     *                item in that data set.
     * @see #getAdapter()
     */
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) mAdapter.unregisterDataSetObserver(mDataSetObserver);
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
        handleOptions();
    }

    /**
     * Disables/Enables a view and all of its child views.
     * Leaves the toolbar enabled at all times.
     *
     * @param view    The view to be disabled/enabled
     * @param enabled True or false, enabled/disabled
     */
    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof Toolbar) {
                    setViewAndChildrenEnabled(child, true);
                } else {
                    setViewAndChildrenEnabled(child, enabled);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Holds the views in this menu
     */
    private class MenuViewHolder {
        private LinearLayout mMenuOptions;
        private RelativeLayout vHome, vYourSPV, vGallery, vNamakaga, vParty, vBlog, vRateUs, vSignOut, vSettings, vProfile, vUserGuide, vBecomOrg;
        private ImageView mMenuBackground, vUserImage, vNotification;
        private ViewGroup mMenuHeader;
        private ViewGroup mMenuFooter;
        private TextView profileName, userName;

        MenuViewHolder(ViewGroup rootView, final Context context) {
            vg = rootView;
            this.mMenuOptions = (LinearLayout) rootView.findViewById(R.id.side_view_menu_options_layout);
            this.mMenuBackground = (ImageView) rootView.findViewById(R.id.side_view_menu_background);
            this.mMenuHeader = (ViewGroup) rootView.findViewById(R.id.side_view_menu_header_layout);
            this.mMenuFooter = (ViewGroup) rootView.findViewById(R.id.side_view_menu_footer_layout);

            profileName = (TextView) rootView.findViewById(R.id.profile_name);
            if (!PreferenceStorage.getUserId(context).isEmpty()) {
                if(PreferenceStorage.getFullName(context).equalsIgnoreCase("")){
                    profileName.setText("");
                } else {
                    profileName.setText(PreferenceStorage.getFullName(context));
                }
            } else {
                profileName.setText("");
            }
            vUserImage = (ImageView) rootView.findViewById(R.id.profile_img);
            String url = PreferenceStorage.getUserPicture(context);
            if (((url != null) && !(url.isEmpty()))) {
                Picasso.get().load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
            }
            this.vHome = (RelativeLayout) rootView.findViewById(R.id.home_img);
            this.vHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PreferenceStorage.getUserId(context).isEmpty()) {
//                        Intent homeIntent = new Intent(context, BookingHistoryActivity.class);
//                        context.startActivity(homeIntent);
                    } else {
//                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//                        alertDialogBuilder.setTitle("Login");
//                        alertDialogBuilder.setMessage("Log in to Access");
//                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                sharedPreferences.edit().clear().apply();
//                                Intent homeIntent = new Intent(context, SplashScreenActivity.class);
//                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(homeIntent);
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alertDialogBuilder.show();
                    }
                }
            });
            this.vYourSPV = (RelativeLayout) rootView.findViewById(R.id.your_spv_img);
            this.vYourSPV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent homeIntent = new Intent(context, SetUpPreferenceActivity.class);
//                    context.startActivity(homeIntent);
                }
            });
            this.vGallery = (RelativeLayout) rootView.findViewById(R.id.gallery_img);
            this.vGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent homeIntent = new Intent(context, SelectCityActivity.class);
//                    homeIntent.putExtra("page", "side");
//                    context.startActivity(homeIntent);
                }
            });
            this.vNamakaga = (RelativeLayout) rootView.findViewById(R.id.namakaaga_img);
            this.vNamakaga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (PreferenceStorage.getUserType(context).equalsIgnoreCase("1")) {
//                        Intent homeIntent = new Intent(context, WishListActivity.class);
//                        context.startActivity(homeIntent);
//                    } else {
//                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//                        alertDialogBuilder.setTitle("Login");
//                        alertDialogBuilder.setMessage("Log in to Access");
//                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                sharedPreferences.edit().clear().apply();
////        TwitterUtil.getInstance().resetTwitterRequestToken();
//
//                                Intent homeIntent = new Intent(context, SplashScreenActivity.class);
//                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(homeIntent);
//
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alertDialogBuilder.show();
//                    }
                }
            });
            this.vParty = (RelativeLayout) rootView.findViewById(R.id.party_img);
            this.vParty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
//                    i.setType("text/plain");
//                    i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
//                    i.putExtra(android.content.Intent.EXTRA_TEXT, "Hi! Get Heyla app and win exciting rewards. https://goo.gl/JTmdEX");
//                    context.startActivity(Intent.createChooser(i, "Share via"));
                }
            });
            this.vBlog = (RelativeLayout) rootView.findViewById(R.id.blod_img);
            this.vBlog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent homeIntent = new Intent(context, BlogViewActivity.class);
//                    homeIntent.putExtra("pageval", "blog");
//                    context.startActivity(homeIntent);
                }
            });
            this.vSettings = (RelativeLayout) rootView.findViewById(R.id.settings_img);
            this.vSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (PreferenceStorage.getUserType(context).equalsIgnoreCase("1")) {
//                        Intent homeIntent = new Intent(context, SettingsActivity.class);
//                        context.startActivity(homeIntent);
//                    }
//                    else {
//                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//                        alertDialogBuilder.setTitle("Login");
//                        alertDialogBuilder.setMessage("Log in to Access");
//                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                sharedPreferences.edit().clear().apply();
////        TwitterUtil.getInstance().resetTwitterRequestToken();
//
//                                Intent homeIntent = new Intent(context, SplashScreenActivity.class);
//                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(homeIntent);
//
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alertDialogBuilder.show();
//                    }
                    Intent homeIntent = new Intent(context, SettingsActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(homeIntent);
                }
            });
            this.vSignOut = (RelativeLayout) rootView.findViewById(R.id.sign_out_img);
            this.vSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Signout");
                    alertDialogBuilder.setMessage("Do you really want to signout?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

                            Intent homeIntent = new Intent(context, SplashScreenActivity.class);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(homeIntent);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.show();
                }
            });
            this.vProfile = (RelativeLayout) rootView.findViewById(R.id.sign_in_layout);
            this.vProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (PreferenceStorage.getUserType(context).equalsIgnoreCase("1")) {
//                        PreferenceStorage.saveCheckFirstTimeProfile(context, "reuse");
//                        Intent homeIntent = new Intent(context, ProfileActivity.class);
//                        context.startActivity(homeIntent);
//                    } else {
//                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//                        alertDialogBuilder.setTitle("Login");
//                        alertDialogBuilder.setMessage("Log in to Access");
//                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                sharedPreferences.edit().clear().apply();
////        TwitterUtil.getInstance().resetTwitterRequestToken();
//
//                                Intent homeIntent = new Intent(context, SplashScreenActivity.class);
//                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(homeIntent);
//
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alertDialogBuilder.show();
//                    }
                }
            });
        }
    }

    /**
     * Listener that listens to menu click events.
     */
    public interface OnMenuClickListener {
        /**
         * Will be called when user pressed a button in the footer view.
         * Will only be called when footer contains a button.
         */
        void onFooterClicked();

        /**
         * Will be called when user pressed on the header view.
         */
        void onHeaderClicked();

        /**
         * Will be called when user pressed an option view.
         */
        void onOptionClicked(int position, Object objectClicked);
    }
}
