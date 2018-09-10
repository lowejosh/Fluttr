package com.example.charles.opencv.FeatureActivity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charles.opencv.Profile.Authentication;
import com.example.charles.opencv.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile);
        updateImage((ImageView)findViewById(R.id.backButton), "Back Button.png");

        auth = new Authentication(this, ProfileActivity.this, FirebaseAuth.getInstance());
        if (auth.loggedIn()) {
            showProfile();
        } else {
            showRegistration();
        }

        /*boolean registered = auth.login("charles@alleman.com.au", "Password2");
        Log.i("ProfileActivity", "registered: " + String.valueOf(registered));

        if (auth.loggedIn()) {
            TextView username = findViewById(R.id.profile_name);
            username.setText(auth.username());
        }

        auth.logout();*/
    }

    /**
     * Updates the image view with a file stored in assets/icons
     * @param v ImageView
     * @param filename Filename and extension of the icon
     */
    private void updateImage(ImageView v, String filename) {
        //Update image for ImageView
        try {
            v.setImageBitmap(BitmapFactory.decodeStream(this.getAssets().open("icons/" + filename)));
        } catch (IOException | IllegalArgumentException unused) {
            Log.e("AIActivity", "Failed to load image: " + "icons/" + filename);
        }
    }

    public void goBack(View v) {
        super.finish();
    }

    /**
     * Register Account
     * @param v
     */
    public void register(View v) {
        ProgressBar loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        hideKeyboard(this);

        EditText email_form = findViewById(R.id.email_form);
        String email = email_form.getText().toString().trim();

        EditText pass_form = findViewById(R.id.pass_form);
        String pass = pass_form.getText().toString().trim();

        boolean success = auth.register(email, pass);

        loading.setVisibility(View.GONE);

        if (success) {
            showProfile();
            clearError();
        } else {
            showError("Failed to Register.");
        }
    }

    /**
     * Register Account
     * @param v
     */
    public void login(View v) {
        ProgressBar loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        hideKeyboard(this);

        EditText email_form = findViewById(R.id.email_form);
        String email = email_form.getText().toString().trim();

        EditText pass_form = findViewById(R.id.pass_form);
        String pass = pass_form.getText().toString().trim();

        boolean success = auth.login(email, pass);

        loading.setVisibility(View.GONE);

        if (success) {
            showProfile();
            clearError();
        } else {
            showError("Failed to Login.");
        }
    }

    /**
     * Log the user out of their account.
     * @param v
     */
    public void logout(View v) {
        auth.logout();
        showRegistration();
    }

    /**
     * Show Registration form
     */
    private void showRegistration() {
        LinearLayout registration = findViewById(R.id.register);
        registration.setVisibility(View.VISIBLE);

        RelativeLayout loggedin = findViewById(R.id.logged_in);
        loggedin.setVisibility(View.GONE);

        Button logout = findViewById(R.id.logoutBtn);
        logout.setVisibility(View.GONE);
    }

    /**
     * Show Profile
     */
    private void showProfile() {
        LinearLayout registration = findViewById(R.id.register);
        registration.setVisibility(View.GONE);

        RelativeLayout loggedin = findViewById(R.id.logged_in);
        loggedin.setVisibility(View.VISIBLE);

        TextView username = findViewById(R.id.profile_name);
        username.setText(auth.username());

        Button logout = findViewById(R.id.logoutBtn);
        logout.setVisibility(View.VISIBLE);
    }

    /**
     * Display error message
     * @param msg Message to display
     */
    private void showError(String msg) {
        TextView errorMsg = findViewById(R.id.errorMsg);
        errorMsg.setText(msg);
        errorMsg.setVisibility(View.VISIBLE);
    }

    /**
     * Clear error message from screen
     */
    private void clearError() {
        TextView errorMsg = findViewById(R.id.errorMsg);
        errorMsg.setVisibility(View.GONE);
    }

    /**
     * Used to hide the keyboard when the user tries to login/register
     * @param activity Current activity
     */
    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
