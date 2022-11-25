package com.example.triptracker_evanlaverdiere;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which acts as a repository for {@link Trip} objects.
 */
public class TripRepository implements ITripRepository{
    //#region BACKING FIELDS
    private List<Trip> trips;

    //#endregion

    //#region CONSTRUCTORS

    /**
     * Creates a new TripRepository containing a default collection of pre-generated Trips.
     */
    public TripRepository(){
        this.trips = TripSampleData.getTrips();
    }
    //#endregion

    //#region METHODS


    @Override
    public Trip[] get() {
        return this.trips.toArray(new Trip[0]);
    }

    /**
     * Searches the Repository for a Trip matching the passed ID and retrieves it if found.
     * @param id The ID of the desired Trip.
     * @return The desired Trip if found, null otherwise.
     */
    @Override
    public Trip getById(int id) {
        // Cycle through the list of Trips.
        for(Trip trip: this.trips){
            if(trip.getId() == id)
                return trip; // If we find a Trip matching the passed ID, return it.
        }

        return null; // Otherwise, return null.
    }

    @Override
    public Trip[] getByDate(LocalDate date) {
        List<Trip> tripsByDate = new ArrayList<>();

        // Grab all trips which occurred on the passed date and add them to the new list.
        for (Trip trip:
             this.trips) {
            if(trip.getDate().equals(date))
                tripsByDate.add(trip);
        }

        return tripsByDate.toArray(new Trip[0]);
    }

    @Override
    public void add(Trip trip) {
        trips.add(trip);
    }

    @Override
    public Trip update(Trip trip, LocalDate date, LocalTime time, int startOdometer, int endOdometer, String type) {
//        // The only property of a Trip which the user cannot modify is its ID. We'll use this to get the existing trip from the list.
//        Trip targetTrip = this.getById(trip.getId());
//
//        // Then we'll get the target trip's index...
//        int targetIndex = trips.indexOf(targetTrip);
//
//        // ...and replace it with the modified Trip.
//        trips.set(targetIndex, trip);

        // Verify that the Trip exists.
        Trip targetTrip = this.getById(trip.getId());

        if(targetTrip == null)
            return null; // If it doesn't exist, return null.

        // Otherwise, update the trip's fields.
        targetTrip.setDate(date);
        targetTrip.setTime(time);
        targetTrip.setStartOdometer(startOdometer);
        targetTrip.setEndOdometer(endOdometer);
        targetTrip.setType(type);

        return targetTrip;
    }

    @Override
    public void delete(Trip trip) {
        trips.remove(trip);
    }
    //#endregion
}
