package com.ione.iseller;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 07/ 02/ 2017.
 * Description: Class that handle welcome and login Screen.
 *
 * @copyright iOne: A company of Ikai.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class WelcomeActivity extends Activity implements View.OnClickListener,
        BeforeRegisterPopUp.Communicator, ForgotPasswordDialog.HandleOTPRequest,
        VerifyOTPDialog.VerifyOTPInterface,
        NewPasswordCreatorDialog.ChangePasswordThroughOTPInterface,
        ErrorInChangePasswordThroughOTP.ChangePasswordThroughOTPInterface {

    private EditText email = null;
    private EditText password = null;
    private final int REQUEST_CODE1 = 100;
    private final int REQUEST_CODE2 = 101;
    private Bundle adharCardData = null;
    private String adharCardString = null;
    private FragmentManager fragmentManager;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Check if permission is granted otherwise take the permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}
                    , 100);
        }

        // Getting the respective XML into java objects
        Button registerBttn, logInBttn;
        TextView forgotText;
        registerBttn = (Button) findViewById(R.id.registerButton);
        logInBttn = (Button) findViewById(R.id.logInButton);
        forgotText = (TextView) findViewById(R.id.forgotPassword);
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

        // Create session and initialise database.
        sessionManager = new SessionManager(getApplicationContext());
        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        fragmentManager = getFragmentManager();

        // Setting onclick listeners to every clickable object.
        registerBttn.setOnClickListener(this);
        logInBttn.setOnClickListener(this);
        forgotText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            Intent intent = new Intent(this, BarCodeLauncherActivity.class);
            startActivityForResult(intent, REQUEST_CODE1);
        }
        if (v.getId() == R.id.logInButton) {
//            String emailString = email.getText().toString();
//            String passwordString = password.getText().toString();
//
//            if (emailString.isEmpty()) {
//                showDialogMessage("Please enter the email or mobile number.");
//            } else {
//                if (passwordString.isEmpty()) {
//                    showDialogMessage("Please enter the password.");
//                } else {
//                    new SignInTask().execute(emailString, passwordString);
//                }
//            }

            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.forgotPassword) {
            // Make forgot password UI.

            // Confusion here how to recover password.

            ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
            forgotPasswordDialog.show(fragmentManager, "Forgot Password");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK) {

            // Get barcode content i.e. AdharCard XML String and call async task to do computations.
            adharCardString = (String) data.getExtras().get("BarcodeContent");
            new AdharCardTask().execute(adharCardString);

        } else if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK) {

            // Show Congratulations pop up to user.
            RegisterCongratsPopUp congratsPopUp = new RegisterCongratsPopUp();
            String title = "Congratulations";
            String content = "You have successfully registered. Now Login to start" +
                    " selling things.";
            Bundle bundle = new Bundle();
            bundle.putString("Title", title);
            bundle.putString("Content", content);
            congratsPopUp.setArguments(bundle);
            congratsPopUp.show(fragmentManager, "Registration Congrats Dialog");

        } else if (!(requestCode == REQUEST_CODE2 && resultCode == RESULT_CANCELED)) {
            showDialogMessage("There is either some error in scanning or you have pressed back" +
                    " button. Please register once more. We are really sorry for inconvenience.");
        }

    }

    /**
     * @param message: String that contains message to be displayed in message dialog.
     *                 Helper function of "handleLoginIssues()" to create "EmailIssuesPopUp".
     */
    private void showDialogMessage(String message) {
        EmailIssuesPopUp msg_dialog = new EmailIssuesPopUp();
        Bundle bundle = new Bundle();
        bundle.putString("Message", message);
        msg_dialog.setArguments(bundle);
        msg_dialog.show(fragmentManager, "Message");
    }

    @Override
    public void communication() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("Adhar Card Details", adharCardData);
        intent.putExtra("Adhar Card String", adharCardString);
        startActivityForResult(intent, REQUEST_CODE2);
    }

    @Override
    public void sendOTP(long mobileNumber) {
        // Implement it not yet implemented.

        // Make a task that open a https connection and request for otp for that user.
        // Do it not done yet..........

        VerifyOTPDialog verifyOTPDialog = new VerifyOTPDialog();
        verifyOTPDialog.show(fragmentManager, "Verify OTP");

    }

    @Override
    public void verifyOTP(int otp) {
        // Implement it not yet implemented.

        // Make a task that open a https connection and send otp to server to authenticate.
        // Do it not done yet..........

        NewPasswordCreatorDialog newPasswordCreatorDialog = new NewPasswordCreatorDialog();
        newPasswordCreatorDialog.show(fragmentManager, "Change Password Through OTP");
    }

    @Override
    public void changePasswordThroughOTP(String password, String confirmPassword) {
        // Implement it not yet implemented
        String result;
        result = handlePasswordIssueChangedThroughOTP(password, confirmPassword);

        if (result.compareTo("Ok") == 0) {
            // Make a https connection and update the password of the current user.
            // On server side check whether password and confirm pass is ok then update.
            // Do it not done yet........

        } else {
            ErrorInChangePasswordThroughOTP errorInChangePasswordThroughOTP = new
                    ErrorInChangePasswordThroughOTP();
            Bundle bundle = new Bundle();
            bundle.putString("Message", result);
            errorInChangePasswordThroughOTP.setArguments(bundle);
            errorInChangePasswordThroughOTP.show(fragmentManager, "Error in Change Password " +
                    "Through OTP");
        }

    }

    private String handlePasswordIssueChangedThroughOTP(String pass, String confirmPass) {

        String message;
        if (pass.isEmpty()) {
            message = "Enter new password.";
        } else {
            if (confirmPass.isEmpty()) {
                message = "Enter your new password again in re-enter password field to confirm.";
            } else {
                int passLength = pass.length();
                if (passLength < 6) {
                    message = "Password must be at least 6 character long.";
                } else {
                    boolean containsDigit = false;
                    boolean containsAlphabet = false;
                    for (int i = 0; i < pass.length(); i++) {
                        int passCharAscii = (int) pass.charAt(i);
                        if ((passCharAscii >= 48) && (passCharAscii <= 57)) {
                            containsDigit = true;
                        } else if ((passCharAscii >= 97) && (passCharAscii <= 122)) {
                            containsAlphabet = true;
                        } else if ((passCharAscii >= 65) && (passCharAscii <= 90)) {
                            containsAlphabet = true;
                        }

                        if (containsAlphabet && containsDigit) {
                            break;
                        }
                    }

                    if (!containsAlphabet) {
                        message = "Password must contains at least one alphabet.";
                    } else {
                        if (!containsDigit) {
                            message = "Password must contains at least one digit.";
                        } else {
                            if (pass.compareTo(confirmPass) != 0) {
                                message = "You have entered wrong password in" +
                                        " re-enter password field.";
                            } else {
                                message = "Ok";
                            }
                        }
                    }
                }
            }
        }

        return message;
    }

    @Override
    public void reLaunchTheNewPasswordCreatorWindow() {
        NewPasswordCreatorDialog newPasswordCreatorDialog = new NewPasswordCreatorDialog();
        newPasswordCreatorDialog.show(fragmentManager, "Change Password Through OTP");
    }

    class AdharCardTask extends AsyncTask<String, Void, Bundle> {

        private int findDataToMatch(int index, String str1, String str2) {

            int i = str1.indexOf(str2, index);
            i = i + 1; // to point next character.

            // Store the occurrence of first letter in the string.
            int temp = i;

            boolean isContains = true;

            // Loop to find substring from next character.
            for (int j = 1; j < str2.length(); j++) {
                if (str1.charAt(i) != str2.charAt(j)) {
                    isContains = false;
                    break;
                }
                i++;
            }

            // If substring is found then return index to copy that value.
            // But remember to skip '=' and '"' character. That's why
            // i + 2 is returned.
            // Otherwise search from next index to find substring.
            if (isContains) {
                return (i + 2);
            } else {
                return (findDataToMatch((temp + 1), str1, str2));
            }

        }

        @Override
        protected Bundle doInBackground(String... data) {

            // Store barcode content in temp variable.
            String temp = data[0];
            Bundle adharCardData = new Bundle();

            String[] dataToMatch = {"uid", "name", "dob"};
            String[] dataToUser = {"AdharId", "Name", "dob"};

            int j = 0;
            int dataCount = 0;
            for (int i = 0; i < temp.length(); i++) {
                if (temp.contains(dataToMatch[j])) {
                    StringBuilder str = new StringBuilder();
                    dataCount = dataCount + 1;
                    i = findDataToMatch(0, temp, dataToMatch[j]);
                    while (temp.charAt(i) != '"') {
                        str.append(temp.charAt(i));
                        i++;
                    }
                    adharCardData.putString(dataToUser[j], str.toString());
                    i++;
                    j++;
                }

                // If everything is searched then just terminate the loop
                // to prevent index out of bound exception.
                if (j >= 3) {
                    break;
                }
            }

            // Check the validity of QRCode whether it is Adhar Card or something else
            if (dataCount >= 2) {

                // Show password necessity dialog before going for registration.
                BeforeRegisterPopUp beforeRegisterPopUp = new BeforeRegisterPopUp();
                beforeRegisterPopUp.show(fragmentManager, "Before Registration " +
                        "Password necessity PopUp");

            } else {
                showDialogMessage("Something went wrong or this is not a valid adhar card." +
                        " Sorry register with Adhar Card once more.");
            }

            return adharCardData;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            adharCardData = new Bundle();
            adharCardData = bundle;
        }
    }

    class SignInTask extends AsyncTask<String, Void, Integer> {

        private JSONParser jsonParser;
        private final String URL_LOGIN = "https://www.ione.com/and_m_login.php";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(WelcomeActivity.this);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {

            jsonParser = new JSONParser();

            HashMap<String, String> parameter = new HashMap<>();
            parameter.put("email", params[0]);
            parameter.put("password", params[1]);

            int status = 0;
            final JSONObject jsonObject = jsonParser.makeHttpRequest(URL_LOGIN, "POST", parameter);
            if (jsonObject != null) {
                status = 1;
                try {
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        sessionManager.setLogIn(true);
                        String uid = jsonObject.getString("uid");
                        String name = jsonObject.getString("name");
                        boolean isEmail = jsonObject.getBoolean("hasEmail");
                        String email = null;
                        if (isEmail) {
                            email = jsonObject.getString("email");
                        }
                        String mobile = jsonObject.getString("mobile");
                        sqLiteHandler.addUser(name, email, mobile, uid);
                        Intent intent = new Intent(WelcomeActivity.this, MainScreen.class);
                        startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteHandler.close();
    }
}