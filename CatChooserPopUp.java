package com.ione.iseller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shiv on 23/2/17.
 */

public class CatChooserPopUp extends DialogFragment {

    Communicator communicator;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        communicator = (Communicator) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cat_chooser_pop_up, null);
        String message = "Have you choosen categories correctly?";
        ((TextView)view.findViewById(R.id.text)).setText(message);

        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                communicator.catChooserPopUpMessage(1);
                dismiss();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return (builder.create());
    }

    interface Communicator {
        void catChooserPopUpMessage(int status);
    }
}
