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

public class NewPasswordCreatorDialog extends DialogFragment {

    private ChangePasswordThroughOTPInterface mchangePasswordThroughOTPInterface;
    private EditText mPassword, mConfirmPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        mchangePasswordThroughOTPInterface = (ChangePasswordThroughOTPInterface) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_through_otp_dialog, null);
        mPassword = (EditText) view.findViewById(R.id.password_field);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirm_pass_field);
        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String passwordString = mPassword.getText().toString();
                String confirmPasswordString = mConfirmPassword.getText().toString();
                mchangePasswordThroughOTPInterface.changePasswordThroughOTP(passwordString,
                        confirmPasswordString);
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

    interface ChangePasswordThroughOTPInterface {
        void changePasswordThroughOTP(String password, String confirmPassword);
    }
}
