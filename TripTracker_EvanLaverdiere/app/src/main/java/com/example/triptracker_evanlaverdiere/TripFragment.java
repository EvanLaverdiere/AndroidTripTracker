package com.example.triptracker_evanlaverdiere;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.databinding.FragmentTripListBinding;
import com.example.triptracker_evanlaverdiere.placeholder.PlaceholderContent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;

/**
 * A fragment representing a list of Items.
 */
public class TripFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private ITripRepository tripRepository; // The repository which holds all data for the Trips to be displayed.

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripFragment() {
    }

    public TripFragment(ITripRepository tripRepository){
        this.tripRepository = tripRepository;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TripFragment newInstance(int columnCount) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        FragmentTripListBinding binding = FragmentTripListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = binding.list;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new TripRecyclerViewAdapter((FragmentActivity) getActivity(), Arrays.asList(this.tripRepository.get()), this.tripRepository));

        // When the user clicks in the dateSelectorEditText field, they can use a DatePicker
        // to filter the displayed list of Trips.
        binding.dateSelectorEditText.setOnClickListener(view1 -> {
            // Set the default value for the DatePicker to the current date.
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, year, month, day) -> {
                int trueMonth = month + 1;
                LocalDate filterDate = LocalDate.of(year, trueMonth, day);
                binding.dateSelectorEditText.setText(String.format("%d-%d-%d", year, trueMonth, day));
                Toast.makeText(getActivity(), String.format("Filter date is %s", filterDate.toString()), Toast.LENGTH_LONG).show();

                // Use the derived date to get a filtered list of Trips.
                Trip[] filteredTrips = tripRepository.getByDate(filterDate);

                // Send the filtered list to a new TripRecyclerViewAdapter().
                // Based on this: https://stackoverflow.com/questions/31601366/notifydatasetchanged-to-the-recyclerview-from-an-outer-class
                recyclerView.setAdapter(new TripRecyclerViewAdapter((FragmentActivity) getActivity(), Arrays.asList(filteredTrips), tripRepository));
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        return view;
    }
}