package com.fparedes.codechallenge.contactsapp.contactItem;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class Contact implements Parcelable {
    
    private String name;
    private String id;
    private String companyName;
    private boolean isFavorite;
    private String smallImageURL;
    private String largeImageURL;
    private String emailAddress;
    private String birthdate;
    private Phone phone;
    private Address address;
    
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean toggleFavorite(){
        return this.isFavorite = !isFavorite;
    }

    public String getSmallImageURL() {
        return smallImageURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getBirthdate() {
        return birthdate;
    }

    /**
     * @return The birthdate with format: MMM dd, yyyy
     */
    public String getForrmattedBirthdate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            date = dateFormat.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
            return birthdate;
        }
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Parcelable interface
     */
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    private Contact(Parcel in) {
        name = in.readString();
        id = in.readString();
        companyName = in.readString();
        isFavorite = in.readByte() != 0;
        smallImageURL = in.readString();
        largeImageURL = in.readString();
        emailAddress = in.readString();
        birthdate = in.readString();
        phone = in.readParcelable(Phone.class.getClassLoader());
        address = in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(companyName);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeString(smallImageURL);
        parcel.writeString(largeImageURL);
        parcel.writeString(emailAddress);
        parcel.writeString(birthdate);
        parcel.writeParcelable(phone, i);
        parcel.writeParcelable(address, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
