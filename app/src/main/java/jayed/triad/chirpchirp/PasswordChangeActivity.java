package jayed.triad.chirpchirp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import jayed.triad.chirpchirp.classes.Account;
import jayed.triad.chirpchirp.classes.Hash;


public class PasswordChangeActivity extends Activity {

    private EditText mCurrentPassword;
    private EditText mNewPassword;
    private EditText mNewPasswordConfirm;

    private PasswordChangeTask mPasswordChangeTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_passwordchange);

        mCurrentPassword = (EditText) findViewById(R.id.CurrentPassword);
        mNewPassword = (EditText) findViewById(R.id.NewPassword);
        mNewPasswordConfirm = (EditText) findViewById(R.id.NewPasswordConfirm);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) width * 4 / 5, (int) height * 3 / 5);

        Button b = (Button) findViewById(R.id.changePassword);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change the password with hashed version
                String currentPassword = mCurrentPassword.getText().toString();
                String newPassword = mNewPassword.getText().toString();
                String newPasswordConfirm = mNewPasswordConfirm.getText().toString();
                changePassword(currentPassword, newPassword, newPasswordConfirm);


            }
        });


    }

    private void changePassword(String current, String password, String confirm) {
        if (mPasswordChangeTask != null){
            return;
        }

        mCurrentPassword.setError(null);
        mNewPassword.setError(null);
        mNewPasswordConfirm.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(current)){
            mCurrentPassword.setError(getString(R.string.error_field_required));
            focusView = mCurrentPassword;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mNewPassword.setError(getString(R.string.error_invalid_password));
            focusView = mNewPassword;
            cancel = true;
        }
        if (!TextUtils.isEmpty(confirm) && !isPasswordValid(confirm)) {
            mNewPasswordConfirm.setError(getString(R.string.error_invalid_password));
            focusView = mNewPasswordConfirm;
            cancel = true;
        }
        if (!TextUtils.equals(password, confirm)){
            mNewPasswordConfirm.setError("Password does not match");
            focusView = mNewPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mNewPassword.setError(getString(R.string.error_field_required));
            focusView = mNewPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(confirm)){
            mNewPasswordConfirm.setError(getString(R.string.error_field_required));
            focusView = mNewPasswordConfirm;
            cancel = true;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            mPasswordChangeTask = new PasswordChangeTask(current, password);
            mPasswordChangeTask.execute((Void) null);
        }



    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    public class PasswordChangeTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPassword;
        private final String mPasswordConfirm;
        private String error;

        PasswordChangeTask(String password, String confirm) {
            mPassword = password;
            mPasswordConfirm = confirm;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                JsonObject json = new JsonObject();
                json.addProperty("userId", Account.getAccount().getAccountId());
                json.addProperty("password", Hash.getMD5Hash(mPassword));
                json.addProperty("newpassword", Hash.getMD5Hash(mPasswordConfirm));
                JsonObject response = Factory.getMyInterface().chirpChangePassword(json);
                Log.d("test", json.toString());
                Log.d("test", "login working");
//                Log.d("test", response.toString());
//                Account.getInstance(response); // parse userId into singleton
                // Simulate network access.
            } catch (LambdaFunctionException lfe) {
                error = lfe.getDetails().toString();
                Log.e("test", lfe.getDetails(), lfe);
                Log.e("test", lfe.getDetails());
                return false;

            }
            Log.d("test", "password changed");
//            Log.d("test", Account.getAccount().getUser().getChirps().toString());

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPasswordChangeTask = null;
//            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }

            Log.d("test", "FINISH CHANGING PASSWORD");
            finish();
        }

    }


}
