package com.ione.iseller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 26/ 03/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

class SessionManager {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "iSellerLogIn";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    void setLogIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
