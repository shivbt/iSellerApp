package com.ione.iseller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 16/ 03/ 2017.
 * Description: Class that handle OTP verification.
 * @copyright iOne: A company of Ikai.
 */

public class VerifyOTPDialog extends DialogFragment {

    private VerifyOTPInterface mverifyOTPInterface;
    private EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        mverifyOTPInterface = (VerifyOTPInterface) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.verify_otp_dialog, null);
        mEditText = (EditText) view.findViewById(R.id.otp_field);
        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.verify, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String otp = mEditText.getText().toString();
                mverifyOTPInterface.verifyOTP(convertToInt(otp));
                dismiss();
            }
        });

        // Setting negative button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return (builder.create());
    }

    interface VerifyOTPInterface {
        void verifyOTP(int otp);
    }

    private int convertToInt(String number) {
        int result = 0;
        int temp;
        for (int i = 0 ; i < number.length(); i++) {
            temp = ((int)number.charAt(i)) - ((int)'0');
            result = result * 10 + temp;
        }

        return result;
    }
}
