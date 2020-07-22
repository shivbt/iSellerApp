package com.ione.iseller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MainScreen extends AppCompatActivity implements View.OnClickListener,
        BottomSheetForProfilePicChange.PhotoShareCommunicator, StoreFragment.StoreToMainActivity {

    private DrawerLayout mDrawerLayout = null;
    private ActionBarDrawerToggle mDrawerToggle;
    static final int NUMBER_OF_NAV_ITEMS = 7;
    private ImageView profileImageView = null;
    private static final int CAMERA_PIC_REQUEST = 201;
    private static final int GALLERY_PIC_REQUEST = 202;
    private DisplayMetrics displayMetrics;
    private int setBackground;
    private View view = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // By setting this it is indicated that no click has been happened
        // and then change accordingly.
        setBackground = 0;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            ImageView mCameraIconView = (ImageView) findViewById(R.id.camera_icon);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                    R.string.drawer_close) {

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu();
                }

            };

            mCameraIconView.setOnClickListener(this);

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }

        buildDrawerAndHandleClicking();

        // Setting for navigation drawer
        // Call function to make image rounded
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);


        bitmap = Bitmap.createScaledBitmap(bitmap, Utility.convertDpToPixel(100,
                displayMetrics), Utility.convertDpToPixel(100, displayMetrics), true);


        profileImageView = (ImageView) findViewById(R.id.nav_user_photo);
        profileImageView.setImageBitmap(Utility.getRoundedImage(bitmap));

        setMainContent(1);

    }

    private void buildDrawerAndHandleClicking() {
        // Set list adapter to show data.
        String[] navItems = {"Profile Setting", "SetUp iPay Account", "Payment History",
                "FAQ", "Contact Us", "Feedback", "T & C"};
        int[] navItemIcons = {R.drawable.ic_person_outline_black_24dp, R.drawable.
                ic_account_balance_black_24dp, R.drawable.ic_local_atm_black_24dp,
                R.drawable.ic_help_outline_black_24dp, R.drawable.ic_call_black_24dp,
                R.drawable.ic_thumbs_up_down_black_24dp, R.drawable.ic_copyright_black_24dp};
        ShivNavigationDrawerAdapter drawerAdapter = new ShivNavigationDrawerAdapter(this, navItems,
                navItemIcons);
        ListView navDrawerList = (ListView) findViewById(R.id.nav_items);
        navDrawerList.setAdapter(drawerAdapter);

        // Setting listener for bottom navigation bar
        final BottomNavigationView bottomNavigationBar = (BottomNavigationView) findViewById(
                R.id.bottom_nav_bar);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.store:
                        bottomNavigationBar.setBackgroundColor(Color.parseColor("#0dec76"));
                        return true;

                    case R.id.update_product:
                        bottomNavigationBar.setBackgroundColor(Color.parseColor("#FF4081"));
                        return true;

                    case R.id.order_summery:
                        bottomNavigationBar.setBackgroundColor(Color.parseColor("#E67E22"));
                        return true;

                    case R.id.emergency:
                        bottomNavigationBar.setBackgroundColor(Color.parseColor("#ED3C54"));
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    void setMainContent(int contentIndex) {
        if (contentIndex == 1) {
            Fragment storeFragment = new StoreFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.flContent, storeFragment,
                    "Store Fragment").commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.camera_icon) {
            setBackground = 1;
            BottomSheetForProfilePicChange bottomSheetDialogFragment = new
                    BottomSheetForProfilePicChange();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom_Sheet_Pic_Change");
        }
    }

    @Override
    public void photoShare(int resultCode) {

        if (resultCode == 1) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }

        if (resultCode == 2) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                    GALLERY_PIC_REQUEST);
        }

        if (resultCode == 3) {
            if (setBackground == 1) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);



                // Added
                bitmap = Bitmap.createScaledBitmap(bitmap, Utility.convertDpToPixel(100,
                        displayMetrics), Utility.convertDpToPixel(100, displayMetrics), true);



                profileImageView.setImageBitmap(Utility.getRoundedImage(bitmap));
            }

            if (view != null) {
                ImageView imageView;
                if (setBackground == 2) {
                    imageView = (ImageView) view.findViewById(R.id.shop_owner_image);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
                    setOwnerPicture(imageView, bitmap);
                }

                if (setBackground == 3) {
                    imageView = (ImageView) view.findViewById(R.id.shop_background);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.app_background);
                    setBackgroundPicture(imageView, bitmap);
                }
            }
        }
    }

    private void setBackgroundPicture(ImageView shopBackground, Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels ,
                Utility.convertDpToPixel(200, displayMetrics), true);
        shopBackground.setImageBitmap(bitmap);
    }

    private void setOwnerPicture(ImageView shopOwnerPic, Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, Utility.convertDpToPixel(100,
                displayMetrics), Utility.convertDpToPixel(100, displayMetrics), true);
        shopOwnerPic.setImageBitmap(Utility.getRoundedImage(bitmap));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Bitmap bitmap = null;
            if ((requestCode == CAMERA_PIC_REQUEST) && (resultCode == Activity.RESULT_OK)) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            }

            if ((requestCode == GALLERY_PIC_REQUEST) && (resultCode == Activity.RESULT_OK)) {
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            data.getData());

                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                if (setBackground == 1) {
                    int newWidth = Utility.convertDpToPixel(120, displayMetrics);
                    int newHeight = Utility.convertDpToPixel(120, displayMetrics);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
                            true);
                    profileImageView.setImageBitmap(Utility.getRoundedImage(resizedBitmap));
                } else {
                    ImageView imageView;

                    if (setBackground == 2) {
                        imageView = (ImageView) view.findViewById(R.id.shop_owner_image);
                        setOwnerPicture(imageView, bitmap);
                    }

                    if (setBackground == 3) {
                        imageView = (ImageView) view.findViewById(R.id.shop_background);
                        setBackgroundPicture(imageView, bitmap);
                    }
                }
                setBackground = 0;
            }
        }
    }

    @Override
    public void storeToMainActMessage(int id, View view) {
        this.view = view;
        if (id == 1) {
            setBackground = 2;
        }
        if (id == 2) {
            setBackground = 3;
        }

        BottomSheetForProfilePicChange bottomSheetDialogFragment = new
                BottomSheetForProfilePicChange();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom_Sheet_Pic_Change");
    }
}



class ShivNavigationDrawerAdapter extends BaseAdapter {

    private class SingleRowHolder {
        String navItem;
        int navItemImageId;

        SingleRowHolder(String navItem, int navItemImageId) {
            this.navItem = navItem;
            this.navItemImageId = navItemImageId;
        }
    }

    private Context context;
    private ArrayList<SingleRowHolder> arrayList;

    ShivNavigationDrawerAdapter(Context context, String[] navItem, int[] navItemsImageId) {

        this.context = context;
        arrayList = new ArrayList<>();

        // Define number of iteration for the for loop
        int iteration = MainScreen.NUMBER_OF_NAV_ITEMS;
        for (int i = 0; i < iteration; i++) {
            arrayList.add(new SingleRowHolder(navItem[i], navItemsImageId[i]));
        }
    }

    private class ViewHolder {
        ImageView navItemImage;
        TextView navItemText;

        ViewHolder(View row) {
            navItemImage = (ImageView) row.findViewById(R.id.nav_item_symbol);
            navItemText = (TextView) row.findViewById(R.id.nav_item_text);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_nav_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        SingleRowHolder singleRowHolder = arrayList.get(position);
        holder.navItemImage.setImageResource(singleRowHolder.navItemImageId);
        holder.navItemText.setText(singleRowHolder.navItem);

        return row;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
}
