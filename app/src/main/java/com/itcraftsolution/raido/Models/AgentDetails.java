package com.itcraftsolution.raido.Models;

public class AgentDetails {
    private String carName,  vehicalNumber,  phoneNumber,  date,  emptySeats, totalJourney,  time,
     journeySource,  journeyDestination, ridePrice, journeyLoc;

    public AgentDetails(String carName, String vehicalNumber, String phoneNumber, String date, String emptySeats,
                        String totalJourney, String time, String journeySource, String journeyDestination, String ridePrice, String journeyLoc) {
        this.carName = carName;
        this.vehicalNumber = vehicalNumber;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.emptySeats = emptySeats;
        this.totalJourney = totalJourney;
        this.time = time;
        this.journeySource = journeySource;
        this.journeyDestination = journeyDestination;
        this.ridePrice = ridePrice;
        this.journeyLoc = journeyLoc;
    }

    public AgentDetails() {
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getVehicalNumber() {
        return vehicalNumber;
    }

    public void setVehicalNumber(String vehicalNumber) {
        this.vehicalNumber = vehicalNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmptySeats() {
        return emptySeats;
    }

    public void setEmptySeats(String emptySeats) {
        this.emptySeats = emptySeats;
    }

    public String getTotalJourney() {
        return totalJourney;
    }

    public void setTotalJourney(String totalJourney) {
        this.totalJourney = totalJourney;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJourneySource() {
        return journeySource;
    }

    public void setJourneySource(String journeySource) {
        this.journeySource = journeySource;
    }

    public String getJourneyDestination() {
        return journeyDestination;
    }

    public void setJourneyDestination(String journeyDestination) {
        this.journeyDestination = journeyDestination;
    }

    public String getRidePrice() {
        return ridePrice;
    }

    public void setRidePrice(String ridePrice) {
        this.ridePrice = ridePrice;
    }

    public String getJourneyLoc() {
        return journeyLoc;
    }

    public void setJourneyLoc(String journeyLoc) {
        this.journeyLoc = journeyLoc;
    }
}
