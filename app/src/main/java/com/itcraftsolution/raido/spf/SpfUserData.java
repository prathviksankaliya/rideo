package com.itcraftsolution.raido.spf;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfUserData {

    Context context;

    public SpfUserData(Context context)
    {
        this.context = context;
    }

    public void setSpfUserLoginDetails(String userName, String userImage, String userEmail, String userPhone, String userGender)
    {
        SharedPreferences setSpfUserLoginDetails = context.getSharedPreferences("UserLoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setSpfUserLoginDetails.edit();
        editor.putString("userName", userName);
        editor.putString("userImage", userImage);
        editor.putString("userPhone", userPhone);
        editor.putString("userEmail", userEmail);
        editor.putString("userGender", userGender);
        editor.apply();
    }

    public SharedPreferences getSpfUserLoginDetails()
    {
        return context.getSharedPreferences("UserLoginDetails", Context.MODE_PRIVATE);
    }
}
