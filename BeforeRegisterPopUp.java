package com.ione.iseller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 08/ 02/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

public class BeforeRegisterPopUp extends DialogFragment {

    private Communicator communicator;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        communicator = (Communicator) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.before_register_pop_up, null);
        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                communicator.communication();
                dismiss();
            }
        });
        return (builder.create());
    }
    interface Communicator {
        void communication();
    }
}

