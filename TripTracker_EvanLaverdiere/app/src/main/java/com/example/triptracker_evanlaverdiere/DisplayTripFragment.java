package com.example.triptracker_evanlaverdiere;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.databinding.FragmentDisplayTripBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 *
 * Fragment which displays information about a selected Trip. Allows the user to edit or delete
 * the displayed Trip.
 */
public class DisplayTripFragment extends Fragment {

//    private int position;
    private TripRecyclerViewAdapter adapter;
    private FragmentDisplayTripBinding binding;
    private View view;
    private Trip trip; // The Trip which will be displayed in this Fragment.
    private ITripRepository repository; // The repository to which the Trip belongs.

    public DisplayTripFragment() {
        // Required empty public constructor
    }

    public DisplayTripFragment(Trip mItem, TripRecyclerViewAdapter tripRecyclerViewAdapter, ITripRepository repository){
        this.trip = mItem;
        this.adapter = tripRecyclerViewAdapter;
        this.repository = repository;
//        this.position = position;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_display_trip, container, false);
        binding = FragmentDisplayTripBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // Fill the various fields of the layout with the Trip's data.
        binding.tripTextView.setText(String.format("Trip %d", trip.getId()));
        binding.dateTextView.setText(trip.getDate().toString());
        binding.timeTextView.setText(trip.getTime().toString());
        binding.startTextView.setText(String.format("Start:\t%d km", trip.getStartOdometer()));
        binding.endTextView.setText(String.format("End:\t%d km", trip.getEndOdometer()));
        binding.typeTextView.setText(trip.getType());

        // Set the color of the little rectangle in the corner. Blue for Uber, yellow for Personal.
        int color = 0;

        if(trip.getType().toUpperCase().equals(Trip.TripTypes.PERSONAL.name()))
            color = view.getResources().getColor(R.color.personal_yellow);
        else if(trip.getType().toUpperCase().equals(Trip.TripTypes.UBER.name()))
            color = view.getResources().getColor(R.color.uber_blue);

        binding.colourView.setBackgroundColor(color);

        // Open an EditTripDialogFragment when the user clicks on the Edit button.
        binding.editTripButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Clicked on Edit button", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getActivity().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.tripList_Container, new EditTripDialogFragment(trip, repository, adapter));
            ft.commit();
        });

        // Confirm that the user wants to delete this Trip when they click on the Delete button.
        binding.deleteTripButton.setOnClickListener(view -> {
            confirmDeletion();
        });

        return view;
    }

    /**
     * Prompts the user to confirm that they want to delete the selected Trip via an AlertDialog.
     */
    private void confirmDeletion() {
        Toast.makeText(getActivity(), "Clicked on Delete button", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(getActivity());

        // Set the dialog title and message.
        myAlertBuilder.setTitle(R.string.confirm_delete_title);
        myAlertBuilder.setMessage(R.string.confirm_delete_message);

        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("YES", (dialogInterface, i) -> {
            // User clicked the YES button. Delete the Trip and return us to the main TripFragment.
            Toast.makeText(getActivity(), "Pressed YES", Toast.LENGTH_SHORT);
            repository.delete(trip);
            Toast.makeText(getActivity(), "Successfully deleted the Trip.", Toast.LENGTH_SHORT).show();
            returnToListFragment();
//                    returnToListFragment();
        });

        myAlertBuilder.setNegativeButton("NO", (dialogInterface, i) -> {
            // User clicked the NO button. They don't want to delete the Trip.
            Toast.makeText(getActivity(), "Pressed NO", Toast.LENGTH_SHORT).show();
        });

        // Create and show this AlertDialog.
        myAlertBuilder.show();
    }

    /**
     * Return to the Main Activity's Trip listing.
     */
    private void returnToListFragment(){
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tripList_Container, new TripFragment(this.repository));
        ft.commit();
    }
}