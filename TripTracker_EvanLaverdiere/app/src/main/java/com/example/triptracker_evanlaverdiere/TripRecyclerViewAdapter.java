package com.example.triptracker_evanlaverdiere;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triptracker_evanlaverdiere.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.triptracker_evanlaverdiere.databinding.FragmentTripBinding;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Trip}.
 */
public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder> {

    private final List<Trip> mValues;
    public final FragmentActivity activity;
    public ITripRepository repository; // The repository which will hold all information on the Trips to be displayed.

    public TripRecyclerViewAdapter(FragmentActivity activity, List<Trip> items, ITripRepository repository) {
        this.activity = activity;
        mValues = items;
        this.repository = repository;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTripBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Trip mItem;
        private FragmentTripBinding binding;
        private String itemType;

        public ViewHolder(FragmentTripBinding binding) {
            super(binding.getRoot());
//            mIdView = binding.itemNumber;
//            mContentView = binding.content;
            this.binding = binding;

            // When the user clicks on any Trip in RecyclerView list, take them to
            // the DisplayTripFragment for that Trip.
            this.binding.tripContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), String.format("Clicked on %s", binding.tripNumberTextView.getText()), Toast.LENGTH_LONG).show();
                    DisplayTripFragment fragment = new DisplayTripFragment(mItem, TripRecyclerViewAdapter.this, TripRecyclerViewAdapter.this.repository);
                    FragmentManager fm = activity.getFragmentManager();
                    fm.beginTransaction().replace(R.id.tripList_Container, fragment).commit();
                }
            });
        }


        public void bind(Trip trip) {
            mItem = trip;
            itemType = trip.getType(); // Personal or Uber
            int color = 0;

            // To ensure that we aren't displaying milliseconds for the time, we establish a specific format of hours:minutes:seconds.
            // Found out how to do this from here: https://simplesolution.dev/java-convert-localtime-to-string/
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            binding.tripNumberTextView.setText(String.format("Trip %d", trip.getId()));
            binding.tripDateTextView.setText(String.format("Date %s", trip.getDate().toString()));
            binding.tripTimeTextView.setText(String.format("Time %s", trip.getTime().format(formatter)));
            binding.tripStartTextView.setText(String.format("Start %d km", trip.getStartOdometer()));
            binding.tripEndTextView.setText(String.format("End %d km", trip.getEndOdometer()));

            // Set the color of the trip to blue (if it's an Uber trip) or yellow (if it's personal).
            if(itemType.toUpperCase().equals(Trip.TripTypes.UBER.name()))
                color = itemView.getResources().getColor(R.color.uber_blue);
            else if(itemType.toUpperCase().equals(Trip.TripTypes.PERSONAL.name()))
                color = itemView.getResources().getColor(R.color.personal_yellow);
            binding.tripContainer.setBackgroundColor(color);
        }
    }
}