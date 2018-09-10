package com.example.charles.opencv.Profile;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Wrapper class for Firebase authentication.
 */
public class Authentication {
    private Context context;
    private Activity activity;
    private FirebaseAuth mAuth;
    private boolean loggedIn;

    public Authentication(Context context, Activity activity, FirebaseAuth mAuth) {
        this.context = context;
        this.activity = activity;
        this.mAuth = mAuth;

        FirebaseUser currentUser = mAuth.getCurrentUser();
        loggedIn = currentUser != null;
    }

    /**
     * Register an account using the provided email and password. If registration is successful the user is logged in.
     * Trim the email address before running.
     * Create a progressDialog before running this command.
     * @param email Non-empty email address
     * @param password Non empty password
     * @return True if the user successfully logged in
     */
    public boolean register(String email, String password) {
        if (loggedIn)
            return false;

        Log.d("register", "Checking email");
        //Check email is not empty
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }

        Log.d("register", "Checking password");
        //Check password is not empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_LONG).show();
            return false;
        }

        Log.d("register", "Creating listener");
        //Listener
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loggedIn = task.isSuccessful();
            }
        };

        Log.d("register", "Creating User");
        //Create User
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, listener);

        Log.d("register", "Logging In");
        //Login User
        if (loggedIn) {
            loggedIn = login(email, password);
        }

        return loggedIn;
    }

    /**
     * Log the user into their account. Returns false if login fails.
     * Trim the email address before running.
     * Create a progressDialog before running this command.
     * @param email Non-empty email address
     * @param password Non empty password
     * @return True if the user successfully logged in
     */
    public boolean login(String email, String password) {
        if (loggedIn)
            return true;

        //Check email is not empty
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }

        //Check password is not empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_LONG).show();
            return false;
        }

        //Listener
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loggedIn = task.isSuccessful();
            }
        };

        //Log in the user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, listener);

        return loggedIn;
    }

    public void logout() {
        if (!loggedIn)
            return;

        mAuth.signOut();
        loggedIn = false;
    }

    /**
     * Returns true if the user is logged into an account
     * @return True if the user is logged into an account
     */
    public boolean loggedIn() {
        return loggedIn;
    }

    /**
     * Returns the username of the user. If not logged in, null is returned
     * @return Username of the user, if not logged in null is returned
     */
    public String username() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (TextUtils.isEmpty(user.getDisplayName())) {
                return user.getEmail();
            } else {
                return user.getDisplayName();
            }
        } else {
            return "";
        }
    }
}
