package com.fparedes.codechallenge.contactsapp.contactItem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class Phone implements Parcelable{

    private String work;
    private String home;
    private String mobile;

    public String getWork() {
        if(work == null || work.isEmpty())
            return "";

        return formatPhone(work);
    }

    public String getHome() {
        if(home == null || home.isEmpty())
            return "";

        return formatPhone(home);
    }

    public String getMobile() {
        if(mobile == null || mobile.isEmpty())
            return "";

        return formatPhone(mobile);
    }

    private String formatPhone(String phone) {
        // Phone format (xxx) xxx-xxxx
        return "(" + phone.substring(0,3) + ") " + phone.substring(4);
    }

    /**
     * Parcelable interface
     */
    public static final Creator<Phone> CREATOR = new Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel in) {
            return new Phone(in);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };

    private Phone(Parcel in) {
        work = in.readString();
        home = in.readString();
        mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(work);
        parcel.writeString(home);
        parcel.writeString(mobile);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
