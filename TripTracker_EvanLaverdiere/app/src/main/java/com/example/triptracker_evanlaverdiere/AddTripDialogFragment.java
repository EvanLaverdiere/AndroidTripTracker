package com.example.triptracker_evanlaverdiere;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.databinding.FragmentAddTripDialogBinding;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 *
 * Fragment used to add a new Trip to the Tracker's repository.
 */
public class AddTripDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddTripDialogBinding binding;
    private View view;
    private ITripRepository tripRepository; // The repository to which the created Trip will be added.


    public AddTripDialogFragment() {
        // Required empty public constructor
    }

    public AddTripDialogFragment(ITripRepository tripRepository){
        this.tripRepository = tripRepository;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_trip_dialog, container, false);
        binding = FragmentAddTripDialogBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // Display a DatePicker when the user clicks on the Date EditText field.
        binding.addDateEditText.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (datePicker, year, month, dayOfMonth) -> {
                        int trueMonth = month + 1;
                        binding.addDateEditText.setText(String.format("%d-%d-%d", year, trueMonth, dayOfMonth));
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        // Display a TimePicker when the user clicks on the Time EditText field.
        binding.addTimeEditText.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, hour, minute) -> {
                binding.addTimeEditText.setText(String.format("%d:%d", hour, minute));
            }, mHour, mMinute, true
            );
            timePickerDialog.show();
        });

        // Populate the spinner dropdown menu with options for the Trip's Type.
        // Based on this: https://code.tutsplus.com/tutorials/how-to-add-a-dropdown-menu-in-android-studio--cms-37860
        // And this: https://developer.android.com/develop/ui/views/components/spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.trip_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.addTypeSpinner.setAdapter(spinnerAdapter);

        binding.addConfirmButton.setOnClickListener(view -> {
            makeTrip();
        });

        binding.addCancelButton.setOnClickListener(view -> {
            confirmCancellation();
        });



        return view;
    }

    /**
     * Prompts the user to confirm that they want to cancel adding a new Trip.
     * If they click yes, the method will return them to the list of Trips in the MainActivity.
     * If they click no, they will remain where they are.
     */
    private void confirmCancellation() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(getActivity());

        // Set the dialog title and message.
        myAlertBuilder.setTitle(R.string.confirm_button_label);
        myAlertBuilder.setMessage(R.string.confirm_cancellation_message);

        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("YES", (dialogInterface, i) -> {
            // User clicked the YES button. Go back to the TripFragment.
            Toast.makeText(getActivity(), "Pressed YES", Toast.LENGTH_SHORT);
            returnToListFragment();
        });

        myAlertBuilder.setNegativeButton("NO", (dialogInterface, i) -> {
            // User clicked the NO button. They want to stay here.
            Toast.makeText(getActivity(), "Pressed NO", Toast.LENGTH_SHORT).show();
        });

        // Create and show this AlertDialog.
        myAlertBuilder.show();
    }

    /**
     * Attempts to create a Trip object from user input taken from the layout's fields.
     * If any fields are blank, or have input of an incorrect format, the method will display an error message.
     * Otherwise, it will create a Trip, add it to the repository, and return the user to the
     * list of Trips in MainActivity.
     */
    private void makeTrip() {
        // Use a StringBuilder to keep track of any errors or unfilled fields.
        StringBuilder errorBuilder = new StringBuilder();

        // Declare a series of local variables that we'll use to construct the new Trip.
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int start = 0;
        int end = 0;
        String type;

        // Grab the text from the addDateEditText field.
        String dateText = binding.addDateEditText.getText().toString();

        // Is it blank?
        if(dateText.isEmpty()){
            errorBuilder.append("Date cannot be left blank."); // If so, add this to the builder.
        }
        else{
            // Otherwise, try to split the text into three parts.
            String[] dateParts = dateText.split("-");

            // Are there three parts? Is the first part (the year) not four chars in length?
            if(dateParts.length != 3 || dateParts[0].length() != 4){
                errorBuilder.append("Incorrect date format."); // If so, add this to the builder.
            }
            else{
                // Otherwise, grab the year, month, and day from these parts.
                year = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]);
                day = Integer.parseInt(dateParts[2]);
            }
        }

        // Do the same for the addTimeEditText field...
        String timeText = binding.addTimeEditText.getText().toString();
        if(timeText.isEmpty()){
            errorBuilder.append("Time cannot be left blank.");
        }
        else{
            String[] timeParts = timeText.split(":");
            if(timeParts.length != 2){
                errorBuilder.append("Incorrect time format.");
            }
            else {
                hour = Integer.parseInt(timeParts[0]);
                minute = Integer.parseInt(timeParts[1]);
            }
        }

        // The addStartEditText field...
        String startText = binding.addStartEditText.getText().toString();
        if(startText.isEmpty())
            errorBuilder.append("Start cannot be left blank.");
        else
            start = Integer.parseInt(startText);

        // The addEndEditText field...
        String endText = binding.addEndEditText.getText().toString();
        if (endText.isEmpty())
            errorBuilder.append("End cannot be left blank.");
        else
            end = Integer.parseInt(endText);

        // ...and the addTypeSpinner.
        type = binding.addTypeSpinner.getSelectedItem().toString();

        // Have we added any errors to the builder?
        if(errorBuilder.length() != 0){
            // If so, print the error message in a Toast and go no further.
            Toast.makeText(getActivity(), errorBuilder.toString(), Toast.LENGTH_LONG).show();
        }
        else {
            // Otherwise, create the date and time for this trip.

            LocalDate date = LocalDate.of(year, month, day);
            LocalTime time = LocalTime.of(hour, minute);

            // Use the values we've created or obtained from the fields to create the Trip itself.
            Trip trip = new Trip(date, time, start, end, type);

            // Add this Trip to the Trip Repository.
            this.tripRepository.add(trip);

            Toast.makeText(getActivity(), "Successfully added Trip", Toast.LENGTH_SHORT).show();

            // Then go back to the list of Trips in the TripFragment.
            returnToListFragment();
        }
    }

    /**
     * Replaces this Fragment on the MainActivity with the Trip list Fragment.
     */
    private void returnToListFragment() {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tripList_Container, new TripFragment(this.tripRepository));
        ft.commit();
    }

    //#region OnItemSelectedListener interface methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Object item = adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //#endregion
}