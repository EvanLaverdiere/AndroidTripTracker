package com.example.triptracker_evanlaverdiere;

import android.app.DialogFragment;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.triptracker_evanlaverdiere.databinding.FragmentTripDatePickerDialogBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class TripDatePickerDialogFragment extends DialogFragment {



    private FragmentTripDatePickerDialogBinding binding;
    private View view;
    private int year;
    private int month;
    private int dayOfMonth;

    public TripDatePickerDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_trip_date_picker_dialog, container, false);
        binding = FragmentTripDatePickerDialogBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.simpleDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });

        return view;
    }
}