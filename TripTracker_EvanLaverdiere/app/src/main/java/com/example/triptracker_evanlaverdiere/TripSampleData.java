package com.example.triptracker_evanlaverdiere;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing sample data for a series of {@link Trip} objects.
 * Used to populate the initial collection of Trips in a {@link TripRepository}.
 */
public class TripSampleData {
//    public static List<Trip> trips = new ArrayList<>();

    /**
     * Produces a predefined list of sample Trips.
     * @return The list of predefined Trips.
     */
    public static List<Trip> getTrips(){
        ArrayList<Trip> trips = new ArrayList<>();
        try{
            trips.add(new Trip(LocalDate.now(), LocalTime.now(), 50000, 50005, "Personal"));
            trips.add(new Trip(LocalDate.now(), LocalTime.now(), 50005, 50010, "Uber" ));
            trips.add(new Trip(LocalDate.of(2022, 10, 31), LocalTime.now(), 66600, 66620, "Uber"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66666, 66670, "Uber"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66670, 66677, "Uber"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66677, 66682, "Personal"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66677, 66682, "Personal"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66677, 66682, "Personal"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66677, 66682, "Personal"));
            trips.add(new Trip(LocalDate.of(2022, 10, 26), LocalTime.now(), 66677, 66682, "Personal"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return trips;
    }
}
