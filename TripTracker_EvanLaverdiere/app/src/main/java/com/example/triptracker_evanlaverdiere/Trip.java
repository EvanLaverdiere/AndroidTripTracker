package com.example.triptracker_evanlaverdiere;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * Class representing a trip taken by an Uber driver.
 * Trips have a date on which they occurred, a time at which they occurred,
 * a starting odometer value, and an ending odometer value.
 * Trips can be Personal, meaning this was not done to take a passenger somewhere,
 * or Uber, which is the reverse.
 */
public class Trip {
    //#region BACKING FIELDS
    private LocalDate date;     // Date on which a Trip took place.
    private LocalTime time;     // Time at which the Trip took place.
    private int startOdometer;  // Odometer's value at the start of the Trip.
    private int endOdometer;    // Odometer's value at the end of the Trip.
    private String type;        // Number representing whether this Trip is personal, or for Uber.
    private int id;             // The ID number of the trip. Assigned and incremented automatically.
    private static int idCount = 0; // Static counter variable used to keep track of Trip IDs.

    // Represents the two valid values that a Trip's type can have.
    public enum TripTypes{
        PERSONAL,
        UBER
    }
    //#endregion

    //#region CONSTRUCTOR(S)

    /**
     * Creates a new Trip.
     * @param date The date of the Trip.
     * @param time The starting time of the Trip.
     * @param startOdometer The starting odometer value (in kilometers) for the Trip.
     * @param endOdometer The ending odometer value (in kilometers) for the Trip.
     * @param type The type of Trip. Valid types include "Personal" and "Uber".
     */
    public Trip(LocalDate date, LocalTime time, int startOdometer, int endOdometer, String type) {
        this.id = ++idCount; // Assigns an ID to this trip automatically.
        this.date = date;
        this.time = time;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.type = type;
    }

    //#endregion

    //#region GETTERS & SETTERS


    /**
     * Returns the ID of the Trip.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the Date on which the Trip took place.
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the time at which the Trip took place.
     * @return
     */
    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Returns the odometer reading from the start of the Trip.
     * @return
     */
    public int getStartOdometer() {
        return startOdometer;
    }

    public void setStartOdometer(int startOdometer) {
        this.startOdometer = startOdometer;
    }

    /**
     * Returns the odometer reading from the end of the Trip.
     * @return
     */
    public int getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(int endOdometer) {
        if(endOdometer < 0 || endOdometer < this.startOdometer)
            throw new IllegalArgumentException("The odometer's end value cannot be less than 0 or its starting value.");
        this.endOdometer = endOdometer;
    }

    /**
     * Returns the type of this Trip. Will either be "Personal" or "Uber".
     * @return
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
//        if(type < TripTypes.PERSONAL.ordinal() || type > TripTypes.UBER.ordinal())
//            throw new IllegalArgumentException();
        if(!type.toUpperCase().equals(TripTypes.PERSONAL.name()) && !type.toUpperCase().equals(TripTypes.UBER.name()))
            throw new IllegalArgumentException();
        this.type = type;
    }

    //#endregion

    //#region METHODS

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", startOdometer=" + startOdometer +
                ", endOdometer=" + endOdometer +
                ", type='" + type + '\'' +
                '}';
    }

    //#endregion
}
