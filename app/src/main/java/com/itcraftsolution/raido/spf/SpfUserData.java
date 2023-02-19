package com.itcraftsolution.raido.spf;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfUserData {

    private Context context;

    public SpfUserData(Context context)
    {
        this.context = context;
    }

    public void setSpfUserLoginDetails(String userName, String userImage, String userEmail, String userPhone,String userType, String userGender)
    {
        SharedPreferences setSpfUserLoginDetails = context.getSharedPreferences("UserLoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setSpfUserLoginDetails.edit();
        editor.putString("userName", userName);
        editor.putString("userImage", userImage);
        editor.putString("userPhone", userPhone);
        editor.putString("userEmail", userEmail);
        editor.putString("userType", userType);
        editor.putString("userGender", userGender);
        editor.apply();
    }

    public SharedPreferences getSpfUserLoginDetails()
    {
        return context.getSharedPreferences("UserLoginDetails", Context.MODE_PRIVATE);
    }

    public void setSpfAgentRideDetails(String carName, String vehicalNumber, String phoneNumber, String date, String emptySeats, String totalJourney, String time,
                                       String journeySource, String journeyDestination,String arrivalTime, String depTime,  String ridePrice,String journeyLoc, String agentId)
    {
        SharedPreferences setSpfUserLoginDetails = context.getSharedPreferences("AgentRideDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setSpfUserLoginDetails.edit();
        editor.putString("agentCarName", carName);
        editor.putString("agentVehicalNumber", vehicalNumber);
        editor.putString("agentPhoneNumber", phoneNumber);
        editor.putString("agentDate", date);
        editor.putString("agentEmptySeats", emptySeats);
        editor.putString("agentTotalJourney", totalJourney);
        editor.putString("agentTime", time);
        editor.putString("agentSource", journeySource);
        editor.putString("agentDestination", journeyDestination);
        editor.putString("agentArrivalTime", arrivalTime);
        editor.putString("agentDepTime", depTime);
        editor.putString("agentRidePrice", ridePrice);
        editor.putString("agentJourneyLoc", journeyLoc);
        editor.putString("agentId", agentId);
        editor.apply();
    }

    public SharedPreferences getSpfAgentRideDetails()
    {
        return context.getSharedPreferences("AgentRideDetails", Context.MODE_PRIVATE);
    }

    public void setSpfUserSearchRide(String from, String to, String date, String member)
    {
        SharedPreferences setSpfUserLoginDetails = context.getSharedPreferences("UserSearchRide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setSpfUserLoginDetails.edit();
        editor.putString("userSearchFrom", from);
        editor.putString("userSearchTo", to);
        editor.putString("userSearchDate", date);
        editor.putString("userSearchMember", member);
        editor.apply();
    }

    public SharedPreferences getSpfUserSearchRide()
    {
        return context.getSharedPreferences("UserSearchRide", Context.MODE_PRIVATE);
    }
}
