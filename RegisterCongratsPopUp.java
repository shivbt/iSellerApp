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
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 08/ 02/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

public class RegisterCongratsPopUp extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initializing and setting rest of things
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.register_congrates_pop_up, null);

        // Setting supplied title and content.
        Bundle bundle = getArguments();
        TextView titleView, contentView;
        titleView = (TextView) view.findViewById(R.id.pop_up_title);
        contentView = (TextView) view.findViewById(R.id.pop_up_content);
        titleView.setText((String)bundle.get("Title"));
        contentView.setText((String)bundle.get("Content"));

        builder.setView(view);
        setCancelable(false);

        // Setting positive button
        builder.setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return (builder.create());
    }
}

