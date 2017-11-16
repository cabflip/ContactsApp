package com.fparedes.codechallenge.contactsapp.contactItem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class Address implements Parcelable {

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getFormattedAddress(){
        return street + System.lineSeparator() +
                city + ", " + state + " " + zipCode + ", " + country;
    }

    /**
     * Parcelable interface
     */
    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel in) {
        street = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zipCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
