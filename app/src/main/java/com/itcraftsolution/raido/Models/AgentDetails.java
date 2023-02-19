package com.itcraftsolution.raido.Models;

public class AgentDetails {
    private String carName,  vehicalNumber,  phoneNumber,  date,  emptySeats, totalJourney,  time,
     journeySource,  journeyDestination, arrivalTime, depTime, ridePrice, journeyLoc, agentId;

    public AgentDetails(String carName, String vehicalNumber, String phoneNumber, String date, String emptySeats,
                        String totalJourney, String time, String journeySource, String journeyDestination,
                        String arrivalTime,String depTime ,  String ridePrice, String journeyLoc, String agentId) {
        this.carName = carName;
        this.vehicalNumber = vehicalNumber;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.emptySeats = emptySeats;
        this.totalJourney = totalJourney;
        this.time = time;
        this.journeySource = journeySource;
        this.journeyDestination = journeyDestination;
        this.arrivalTime = arrivalTime;
        this.depTime = depTime;
        this.ridePrice = ridePrice;
        this.journeyLoc = journeyLoc;
        this.agentId = agentId;
    }

    public AgentDetails() {
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
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
