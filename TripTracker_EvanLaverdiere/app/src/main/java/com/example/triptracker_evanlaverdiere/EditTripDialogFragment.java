package com.example.triptracker_evanlaverdiere;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.databinding.FragmentEditTripDialogBinding;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 *
 * Fragment which lets the user edit an existing Trip.
 */
public class EditTripDialogFragment extends Fragment {

    private FragmentEditTripDialogBinding binding;
    private View view;
    private ITripRepository tripRepository; // The repository to which this Trip belongs.
    private Trip trip;                      // The Trip to be edited.
    private TripRecyclerViewAdapter adapter;

    public EditTripDialogFragment() {
        // Required empty public constructor
    }

    public EditTripDialogFragment(Trip trip, ITripRepository tripRepository, TripRecyclerViewAdapter adapter){
        this.trip = trip;
        this.tripRepository = tripRepository;
        this.adapter = adapter;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_edit_trip_dialog, container, false);
        binding = FragmentEditTripDialogBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.editTripTextView.setText(String.format("Trip %d", trip.getId()));
        binding.editDateEditText.setText(trip.getDate().toString());
        binding.editTimeEditText.setText(trip.getTime().toString());
        binding.editStartEditText.setText(String.valueOf(trip.getStartOdometer()));
        binding.editEndEditText.setText(String.valueOf(trip.getEndOdometer()));

        binding.editDateEditText.setOnClickListener(view -> {
            // Set the DatePicker to that stored by the current Trip.
            int mYear = trip.getDate().getYear();
            int mMonth = trip.getDate().getMonthValue();
            int mDay = trip.getDate().getDayOfMonth();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, year, month, day) -> {
                int trueMonth = month + 1;
                binding.editDateEditText.setText(String.format("%d-%d-%d", year, trueMonth, day));
            }, mYear, mMonth - 1, mDay); // Need an offset for the month, since getMonthValue() goes from 1-12 and Month goes from 0-11.
            datePickerDialog.show();
        });

        binding.editTimeEditText.setOnClickListener(view -> {
            // Set the TimePicker to the time stored in the current Trip.
            int mHour = trip.getTime().getHour();
            int mMinute = trip.getTime().getMinute();

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    binding.editTimeEditText.setText(String.format("%d:%d", hour, minute));
                }
            }, mHour, mMinute, true);
            timePickerDialog.show();
        });

        // Create the Spinner and populate it with values.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.trip_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Spinner's default selection to whichever option matches the Trip's Type.
        binding.editTypeSpinner.setAdapter(spinnerAdapter);
        if(trip.getType().toUpperCase().equals(Trip.TripTypes.UBER.name()))
            binding.editTypeSpinner.setSelection(Trip.TripTypes.UBER.ordinal());
        else if(trip.getType().toUpperCase().equals(Trip.TripTypes.PERSONAL.name()))
            binding.editTypeSpinner.setSelection(Trip.TripTypes.PERSONAL.ordinal());

        binding.editConfirmButton.setOnClickListener(view -> {
            // Verify that none of the fields are blank.
            if(checkFields()){
                // If everything is good, we can update the Trip.
                updateTrip();
            }

        });

        binding.editCancelButton.setOnClickListener(view -> {
            // When the user clicks the Cancel button, send them back to the DisplayTripFragment.
            returnToDisplayFragment(trip);
        });

        return view;
    }

    /**
     * Use the values stored in the Fragment's fields to update the Trip, then go to the list of Trips.
     */
    private void updateTrip() {
        // Get the date field's text, split it into parts, and use them to create a date object.
        String[] dateParts = binding.editDateEditText.getText().toString().split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate date = LocalDate.of(year, month, day);

        // Do the same with the time field's text to create a time object.
        String[] timeParts = binding.editTimeEditText.getText().toString().split(":");

        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalTime time = LocalTime.of(hour, minute);

        // Get the numbers for starting and ending odometer.
        int start = Integer.parseInt(binding.editStartEditText.getText().toString());
        int end = Integer.parseInt(binding.editEndEditText.getText().toString());

        // Get the type from the Spinner.
        String type = binding.editTypeSpinner.getSelectedItem().toString();

        //TODO: Find a better way to implement this.

//                    tempTrip.setDate(date);
//                    tempTrip.setTime(time);
//                    tempTrip.setStartOdometer(start);
//                    tempTrip.setEndOdometer(end);
//                    tempTrip.setType(type);

        // Finally, update the Trip.
        Trip updatedTrip = tripRepository.update(trip, date, time, start, end, type);
        Toast.makeText(getActivity(), String.format("Successfully updated Trip %d", updatedTrip.getId()), Toast.LENGTH_SHORT).show();

        // Once the trip has been updated, send it back to the DisplayTripFragment.
        returnToDisplayFragment(updatedTrip);
    }

    /**
     * Verify that all fields have been filled. Prints an error message if any are empty.
     * @return True if there are no missing fields, false otherwise.
     */
    private boolean checkFields(){
        StringBuilder errorBuilder = new StringBuilder();

        if(binding.editDateEditText.getText().toString().isEmpty())
            errorBuilder.append("Date field cannot be left blank.");

        if(binding.editTimeEditText.getText().toString().isEmpty())
            errorBuilder.append("Time field cannot be left blank.");

        if(binding.editStartEditText.getText().toString().isEmpty())
            errorBuilder.append("Start field cannot be left blank.");

        if(binding.editEndEditText.getText().toString().isEmpty())
            errorBuilder.append("End field cannot be left blank.");

        return errorBuilder.length() <= 0;
    }

    /**
     * Return to the Main Activity's Trip listing.
     */
    private void returnToDisplayFragment(Trip trip){
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tripList_Container, new DisplayTripFragment(trip, this.adapter, this.tripRepository));
        ft.commit();
    }
}