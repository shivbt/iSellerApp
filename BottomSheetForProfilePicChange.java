package com.ione.iseller;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by shiv on 4/3/17.
 */

public class BottomSheetForProfilePicChange extends BottomSheetDialogFragment implements
        View.OnClickListener {

    private PhotoShareCommunicator photoShareCommunicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhotoShareCommunicator)
            photoShareCommunicator = (PhotoShareCommunicator) context;
        else
            throw new RuntimeException("OnInterfaceOfFragmentListener not implemented in context");
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getActivity(), R.layout.bottom_sheet_profile_change, null);
        dialog.setContentView(contentView);

        // Initialising each view.
        ImageView cameraView = (ImageView) contentView.findViewById(R.id.camera_icon);
        ImageView galleryView = (ImageView) contentView.findViewById(R.id.gallery_icon);
        ImageView deleteView = (ImageView) contentView.findViewById(R.id.delete_icon);

        // Set onclick listener
        cameraView.setOnClickListener(this);
        galleryView.setOnClickListener(this);
        deleteView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.camera_icon) {
            photoShareCommunicator.photoShare(1);
            dismiss();
        }

        if (v.getId() == R.id.gallery_icon) {
            photoShareCommunicator.photoShare(2);
            dismiss();
        }

        if (v.getId() == R.id.delete_icon) {
            photoShareCommunicator.photoShare(3);
            dismiss();
        }
    }

    public interface PhotoShareCommunicator {
        void photoShare(int resultCode);
    }
}
