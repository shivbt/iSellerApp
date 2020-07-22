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
 * Date Started: 12/ 03/ 2017.
 * Description: Class that handle forgot password window.
 * @copyright iOne: A company of Ikai.
 */

public class ForgotPasswordDialog extends DialogFragment {

    private EditText mEditTextMobile;
    private HandleOTPRequest handleOTPRequest;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        handleOTPRequest = (HandleOTPRequest) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password_window, null);
        mEditTextMobile = (EditText) view.findViewById(R.id.mobile_number_field);
        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.send_otp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mobileNumber = mEditTextMobile.getText().toString();
                if (mobileNumber.isEmpty()) {
                    showDialogMessage("Please enter mobile number.");
                } else {
                    if (mobileNumber.length() < 10) {
                        showDialogMessage("Please enter a valid mobile number.");
                    } else {
                        // Now send OTP to this mobile number.
                        // Make connection to the server and send OTP to the mobile number.
                        handleOTPRequest.sendOTP(convertToInt(mobileNumber));
                    }
                }
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

    private void showDialogMessage (String message) {
        EmailIssuesPopUp msg_dialog = new EmailIssuesPopUp();
        Bundle bundle = new Bundle();
        bundle.putString("Message", message);
        msg_dialog.setArguments(bundle);
        msg_dialog.show(getFragmentManager(), "Message");
    }

    interface HandleOTPRequest {
        void sendOTP(long mobileNumber);
    }

    private long convertToInt(String number) {
        long result = 0;
        int temp;
        for (int i = 0 ; i < number.length(); i++) {
            temp = ((int)number.charAt(i)) - ((int)'0');
            result = result * 10 + temp;
        }

        return result;
    }

}
