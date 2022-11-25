package com.example.triptracker_evanlaverdiere;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Interface which acts as a repository for {@link Trip}s. Provides functionality for an implementing class
 * to get one Trip, all Trips, or Trips which occurred on a specified date, and to add new Trips,
 * edit existing Trips, or delete Trips.
 */
public interface ITripRepository {
    /**
     * Gets an array of all Trips.
     * @return The array of Trip objects.
     */
    Trip[] get();

    /**
     * Retrieves a specific Trip by a passed ID.
     * @param id The ID of the desired Trip.
     * @return The desired Trip.
     */
    Trip getById(int id);

    /**
     * Retrieves a collection of all Trips which took place on a specified date.
     * @param date The desired date.
     * @return The trips which occurred on that date.
     */
    Trip[] getByDate(LocalDate date);

    /**
     * Adds a new Trip to the collection.
     * @param trip The Trip to be added.
     */
    void add(Trip trip);

    /**
     * Updates an existing Trip object.
     * @param trip The Trip to be updated.
     * @param date The date of the updated Trip.
     * @param time The time of the updated Trip.
     * @param startOdometer The starting odometer value of the updated Trip.
     * @param endOdometer The ending odometer value of the updated Trip.
     * @param type The type (Personal or Uber) of the updated Trip.
     * @return The updated Trip.
     */
    Trip update(Trip trip, LocalDate date, LocalTime time, int startOdometer, int endOdometer, String type);

    /**
     * Deletes an existing Trip.
     * @param trip The Trip to be deleted.
     */
    void delete(Trip trip);
}
