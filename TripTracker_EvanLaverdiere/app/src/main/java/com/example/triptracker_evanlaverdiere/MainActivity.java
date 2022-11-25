package com.example.triptracker_evanlaverdiere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.DatePickerDialog;
import android.app.FragmentManager;

import android.app.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private View view;
    private AppBarConfiguration appBarConfiguration;
    private TripRepository repository; // The TripRepository which will hold information about all Trips in this app.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
//        NavController navController = Navigation.findNavController(view);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Create the TripRepository.
        repository = new TripRepository();

        // Display the list of Trips on the bottom of the main screen.
        loadFragment(new TripFragment(this.repository));

    }

    /**
     * Loads a specified Fragment in the Main Activity.
     * @param fragment The Fragment to be loaded.
     */
    private void loadFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace((R.id.tripList_Container), fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Create and populate the menu bar.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id){
            case R.id.home_ActionButton:
                // If the user clicks the home button, send them to the unfiltered TripFragment list.
                Toast.makeText(this, "Clicked on Home", Toast.LENGTH_LONG).show();
                loadFragment(new TripFragment(this.repository));
                break;
            case R.id.add_ActionButton:
                // If the user clicks the Add button, send them to the AddTripDialogFragment.
                Toast.makeText(this, "Clicked on Add", Toast.LENGTH_LONG).show();
                loadFragment(new AddTripDialogFragment(this.repository));
                break;
        }

        return true;
    }
}