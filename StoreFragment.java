package com.ione.iseller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by shiv on 2/3/17.
 * Class to show shop background and shop owner photo.
 */

public class StoreFragment extends Fragment implements View.OnClickListener{

    private ImageView shopBackground, shopOwnerPic;
    private DisplayMetrics displayMetrics;
    private StoreToMainActivity storeToMainActivity;
    private View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storeToMainActivity = (StoreToMainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_fragment_layout, container, false);

        // Get the width of device and set the shop background accordingly.
        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        shopBackground = (ImageView) view.findViewById(R.id.shop_background);
        shopOwnerPic = (ImageView) view.findViewById(R.id.shop_owner_image);

        // Set background and shopOwner pictures.
        setBackgroundPicture();
        setOwnerPicture();


        ImageView shopBackground = (ImageView) view.findViewById(R.id.background_camera_icon);
        ImageView shopOwner = (ImageView) view.findViewById(R.id.owner_camera_icon);
        shopBackground.setOnClickListener(this);
        shopOwner.setOnClickListener(this);

        // Return the view.
        return view;
    }

    private void setBackgroundPicture() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.app_background);
        bitmap = Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels,
                Utility.convertDpToPixel(200, displayMetrics), true);
        shopBackground.setImageBitmap(bitmap);
    }

    private void setOwnerPicture() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
        bitmap = Bitmap.createScaledBitmap(bitmap, Utility.convertDpToPixel(80,
                displayMetrics), Utility.convertDpToPixel(80, displayMetrics), true);
        shopOwnerPic.setImageBitmap(Utility.getRoundedImage(bitmap));
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.owner_camera_icon) {
            storeToMainActivity.storeToMainActMessage(1, view);
        }

        if (resId == R.id.background_camera_icon) {
            storeToMainActivity.storeToMainActMessage(2, view);
        }
    }

    interface StoreToMainActivity {
        void storeToMainActMessage(int id, View view);
    }
}
