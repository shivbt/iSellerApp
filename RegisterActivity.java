package com.ione.iseller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends Activity implements
        View.OnClickListener, BirthDatePickerPopUp.Communicator,
        TextWatcher {

    private TextView strength_info;
    private EditText birth_date, password, adharId, fullName;
    private String email;
    private String mobileNumber;
    private String shopAddress;
    private String reTypedPassword;
    private String shopPinCode, shopCity, state, shopName;
    private boolean[] selectedCategory;
    private String adharCardString = "";
    private ImageView calender_symbol;
    private final int REQUEST_OF_CAT_CHOOSER = 200;
    private static final int CATEGORY_SIZE = 37;
    ProgressDialog progressDialog;
    private final String STATES[] = {" Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", " Jharkhand",
            "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", " Sikkim", "Tamil Nadu",
            "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and " +
            "Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu",
            "Lakshadweep", "Delhi", "Puducherry"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_register);

        // Initialization of objects.
        password = (EditText) findViewById(R.id.password_field);
        calender_symbol = (ImageView) findViewById(R.id.calender_symbol);
        birth_date = (EditText) findViewById(R.id.date_of_birth);
        strength_info = (TextView) findViewById(R.id.strength_text);
        AutoCompleteTextView mShopStateView = (AutoCompleteTextView)
                findViewById(R.id.state_field);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        // Getting sent adhar data bundle by the calling activity.
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Adhar Card Details");
        adharCardString = intent.getStringExtra("Adhar Card String");

        // Setting Adhar Card Details.
        setAdharCardDetail(bundle);


        // Setting click listener
        registerButton.setOnClickListener(this);
        password.addTextChangedListener(this);

        // Set autocomplete text view adapter to show suggestion.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, STATES);
        mShopStateView.setAdapter(adapter);
    }

    private void setAdharCardDetail(Bundle bundle) {
        adharId = (EditText) findViewById(R.id.adhar_id_field);
        fullName = (EditText) findViewById(R.id.full_name_field);

        // Setting the respective fields.
        adharId.setText(bundle.getString("AdharId"));
        fullName.setText(bundle.getString("Name"));

        String dob = bundle.getString("dob", "Not Found");

        if (dob.equals("Not Found")) {
            calender_symbol.setOnClickListener(this);
        } else {
            birth_date.setText(dob);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.calender_symbol) {
            BirthDatePickerPopUp datePicker = new BirthDatePickerPopUp();
            datePicker.show(getFragmentManager(), "BirthDate");
        }
        if (view.getId() == R.id.registerButton) {

            // Check whether password requirement matched.
            if (strength_info.getText().toString().equals("Ok")) {
                // Validate every field.
                if (isEveryInputFieldOk()) {
                    // Call the category Chooser activity.
                    Intent intent = new Intent(this, CategoryChooser.class);
                    startActivityForResult(intent, REQUEST_OF_CAT_CHOOSER);
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_OF_CAT_CHOOSER) && (resultCode == RESULT_OK)) {
            if (data != null) {
                selectedCategory = new boolean[CATEGORY_SIZE];
                selectedCategory = data.getBooleanArrayExtra("CategoryChoose");

                new RegisterTask().execute();

            } else {
                showDialogMessage("Something went wrong in choosing the category.");
            }
        }
    }

    private boolean isEveryInputFieldOk() {

        // Initialise input fields
        email = ((EditText) findViewById(R.id.email_field)).getText().toString();
        mobileNumber = ((EditText) findViewById(R.id.mobile_number_field)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_field)).getText().toString();
        reTypedPassword = ((EditText) findViewById(R.id.confirm_pass_field)).getText().toString();
        shopAddress = ((EditText) findViewById(R.id.shop_address_field)).getText().toString();
        shopPinCode = ((EditText) findViewById(R.id.pin_code_field)).getText().toString();
        shopCity = ((EditText) findViewById(R.id.shop_city_field)).getText().toString();
        state = ((AutoCompleteTextView) findViewById(R.id.state_field)).getText().toString();
        shopName = ((EditText)findViewById(R.id.shop_name_field)).getText().toString();

        // Initialise validity parameter.
        boolean emailValid = false;
        boolean mobileNumberValid = false;
        boolean retypedPasswordValid = false;
        boolean shopAddressValid = false;
        boolean pinCodeValid = false;
        boolean birthDateValid = false;
        boolean shopCityValid = false;
        boolean stateValid = false;
        boolean shopNameValid = false;

        // Perform email validation.
        if (!email.isEmpty()) {
            if (isEmailValid(email)) {
                emailValid = true;
            }
        } else {
            emailValid = true;
        }

        if (emailValid) {

            // Perform birth date validation.
            if (birth_date.getText().toString().isEmpty()) {
                showDialogMessage("Please select your Birth Date by clicking the calendar symbol.");
            } else {
                birthDateValid = true;
            }

            if (birthDateValid) {
                // Perform mobile number validation.
                if (mobileNumber.isEmpty()) {
                    showDialogMessage("Please enter Mobile Number first.");
                } else {
                    if (mobileNumber.charAt(0) == '0') {
                        showDialogMessage("Please do not include starting 0 in mobile number.");
                    } else {
                        if (mobileNumber.length() < 10) {
                            showDialogMessage("Please enter 10 digit mobile number.");
                        } else {
                            mobileNumberValid = true;
                        }
                    }
                }

                if (mobileNumberValid) {

                    // Perform retyped password validation.
                    if (reTypedPassword.isEmpty()) {
                        showDialogMessage("Please re-enter the password in Re-type Password" +
                                " field.");
                    } else {
                        if (password.compareTo(reTypedPassword) == 0) {
                            retypedPasswordValid = true;
                        } else {
                            showDialogMessage("You have entered wrong password in Re-type " +
                                    "Password field.");
                        }
                    }

                    if (retypedPasswordValid) {
                        // Perform Shop Name validation
                        if (shopName.isEmpty()) {
                            showDialogMessage("PLease enter your shop name.");
                        } else {
                            if (shopName.length() < 5) {
                                showDialogMessage("Please enter whole or correct shop name.");
                            } else {
                                shopNameValid = true;
                            }
                        }

                        if (shopNameValid) {
                            // Perform shop address validation.
                            if (shopAddress.isEmpty()) {
                                showDialogMessage("Please enter Shop address.");
                            } else {
                                if (shopAddress.length() < 5) {
                                    showDialogMessage("Please enter valid shop address.");
                                } else {
                                    shopAddressValid = true;
                                }
                            }

                            if (shopAddressValid) {
                                // Perform City validation.
                                if (shopCity.isEmpty()) {
                                    showDialogMessage("Please enter City of shop.");
                                } else {
                                    if (shopCity.length() < 4) {
                                        showDialogMessage("Please enter valid City of Shop.");
                                    } else {
                                        shopCityValid = true;
                                    }
                                }

                                if (shopCityValid) {
                                    // Perform Pin Code validation.
                                    if (shopPinCode.isEmpty()) {
                                        showDialogMessage("Please enter Pin Code of Shop.");
                                    } else {
                                        if (shopPinCode.length() < 6) {
                                            showDialogMessage("Please enter valid Pin Code of Shop.");
                                        } else {
                                            pinCodeValid = true;
                                        }
                                    }
                                }

                                if (pinCodeValid) {
                                    // perform State validation
                                    if (state.isEmpty()) {
                                        showDialogMessage("Please enter state of Shop.");
                                    } else {
                                        if (state.length() < 4) {
                                            showDialogMessage("Please enter state of Shop as " +
                                                    "suggested in the suggestion box.");
                                        } else {
                                            for (int i = 0; i < 36; i++) {
                                                if (state.compareTo(STATES[i]) == 0) {
                                                    stateValid = true;
                                                }
                                            }
                                            if (!stateValid) {
                                                showDialogMessage("Please enter valid state name.");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (emailValid && mobileNumberValid && retypedPasswordValid && pinCodeValid &&
                shopAddressValid && birthDateValid && shopCityValid && stateValid
                && shopNameValid) {
            return true;
        }

        return false;
    }

    private boolean isEmailValid(String email) {
        boolean valid = false;
        int alpha_count = 0;
        int dot_count = 0;
        int alpha_position = 0;
        boolean other_char = false;

        // Loop through email string and check whether it is a valid email string.
        for (int i = 0; i < email.length(); i++) {
            char temp = email.charAt(i);
            if (((int) temp >= 48) && ((int) temp <= 57)) {

            } else if (temp == '@') {
                alpha_count++;
                alpha_position = i;
            } else if (temp == '.') {
                dot_count++;
            } else if ((((int) temp < 97) || ((int) temp > 122))) {
                other_char = true;
                break;
            }
        }

        if ((alpha_count == 1) && (!(other_char)) && (dot_count >= 1) && (alpha_position >= 1)) {
            valid = true;
        }

        return valid;
    }


    @Override
    public void communication(int year, int month, int dayOfMonth) {
        String birthDate = "" + dayOfMonth + "/ " + (month + 1) + "/ " + year;
        birth_date.setText(birthDate);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            String password = editable.toString();
            int pass_length = password.length();
            if (pass_length < 6) {
                String message = "Password must be at least 6 character long.";
                showWarningPasswordStatus(strength_info, message);
            } else if (pass_length >= 6) {
                boolean contains_digit = false;
                boolean contains_alphabet = false;
                for (int i = 0; i < pass_length; i++) {
                    if ((((int) password.charAt(i)) >= 48) && (((int) password.charAt(i)) <= 57)) {
                        contains_digit = true;
                    }
                    if ((((int) password.charAt(i)) >= 65) &&
                            (((int) password.charAt(i)) <= 90)) {
                        contains_alphabet = true;
                    }
                    if ((((int) password.charAt(i)) >= 97) &&
                            (((int) password.charAt(i)) <= 122)) {
                        contains_alphabet = true;
                    }
                    if (contains_digit && contains_alphabet) {
                        break;
                    }

                }
                String message;
                if (!contains_digit) {
                    message = "Password must contain at least one number";
                    showWarningPasswordStatus(strength_info, message);

                } else {
                    if (!contains_alphabet) {
                        message = "Password must contain at least one alphabet";
                        showWarningPasswordStatus(strength_info, message);
                    } else {
                        message = "Ok";
                        strength_info.setText(message);
                        strength_info.setVisibility(View.GONE);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message: String that contains message to be displayed
     *                 in message dialog.
     * @function_desciption: Helper function of "handleLoginIssues()"
     * to create "EmailIssuesPopUp".
     */
    private void showDialogMessage(String message) {
        EmailIssuesPopUp msg_dialog = new EmailIssuesPopUp();
        Bundle bundle = new Bundle();
        bundle.putString("Message", message);
        msg_dialog.setArguments(bundle);
        msg_dialog.show(getFragmentManager(), "Message");
    }

    private void showWarningPasswordStatus(TextView strength_info, String message) {
        strength_info.setVisibility(View.VISIBLE);
        strength_info.setTextColor(Color.RED);
        strength_info.setText(message);
    }

    class RegisterTask extends AsyncTask<Void, Void, Integer> {

        HashMap<String, String> registerDetails;
        private JSONParser jsonParser;
        private final String URL_LOGIN = "https://www.ione.com/and_m_register.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerDetails = new HashMap<>();
            registerDetails.put("AdharId", adharId.getText().toString());
            registerDetails.put("FullName", fullName.getText().toString());
            registerDetails.put("DOB", birth_date.getText().toString());
            registerDetails.put("Pass", password.getText().toString());

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Registering...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            jsonParser = new JSONParser();

            registerDetails.put("Email", email);
            registerDetails.put("MobNo.", mobileNumber);
            registerDetails.put("ShopName", shopName);
            registerDetails.put("ShopAddr", shopAddress);
            registerDetails.put("ShopCity", shopCity);
            registerDetails.put("ShopPin", shopPinCode);
            registerDetails.put("ShopState", state);
            registerDetails.put("AdharString", adharCardString);

            StringBuilder selectedCatString = new StringBuilder();
            for (int i = 0; i < CATEGORY_SIZE; i++) {
                if (selectedCategory[i]) {
                    selectedCatString.append('1');
                } else {
                    selectedCatString.append('0');
                }
            }

            registerDetails.put("ChooseCategory", selectedCatString.toString());

            int status = 0;
            final JSONObject jsonObject = jsonParser.makeHttpRequest(URL_LOGIN, "POST",
                    registerDetails);
            if (jsonObject != null) {
                status = 1;
                try {
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    showDialogMessage(jsonObject.getString("error_msg"));
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer i) {
            progressDialog.dismiss();
            if (i == 0) {
                showDialogMessage("There is some error in connecting to the server.");
            }
        }
    }

}

