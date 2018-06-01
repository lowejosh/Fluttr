package com.example.charles.opencv;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.charles.opencv.Fragments.BirdBankFragment;
import com.example.charles.opencv.Fragments.HomeFragment;
import com.example.charles.opencv.Fragments.SearchFragment;

public class HomeScreen extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //BottomNavigationView navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(this);

        // Switch to camera activity upon click of button
        /*Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
            }
        });*/ // currently removed

        // load the HomeScreen fragment
        loadFragment(new HomeFragment());
    }

    // Loads the desired fragment into the fragment container
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Declare the fragment
        Fragment fragment = null;

        // Find out which button was pressed and load the desired fragment
        switch(item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_birdBank:
                fragment = new BirdBankFragment();
                break;
        }

        return loadFragment(fragment);
    }

    /**
     * On Button Press, Change Screen to Twenty Questions
     * @param v Attached View Object (Unused)
     */
    public void finderOnClick(View v) {
        loadFragment(new HomeFragment());
    }

    /**
     * On Button Press, Change Screen to Search AI
     * @param v Attached View Object (Unused)
     */
    public void identifierOnClick(View v) {
        loadFragment(new SearchFragment());
    }

    /**
     * On Button Press, Change Screen to Bird Bank
     * @param v Attached View Object (Unused)
     */
    public void bankOnClick(View v) {
        loadFragment(new BirdBankFragment());
    }
}
