package com.fparedes.codechallenge.contactsapp.contactProvider;

import android.util.Log;

import com.fparedes.codechallenge.contactsapp.contactItem.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class ContactProvider {

    private static final String TAG = "ContactProvider";

    private static HashMap<String, Contact> contactsHashMap;
    private static List<Contact> contactList;

    public static List<Contact> buildContactList(String urlString) {
        if (contactList != null)
            return contactList;

        String contactsJSonString = new ContactProvider().parseUrl(urlString);
        if (contactsJSonString == null) {
            Log.d(TAG, "Failed to parse the json for contact list");
            return null;
        }

        Gson gson = new Gson();

        // Initialize and populate contacts List
        contactList = new ArrayList<>();
        contactList = gson.fromJson(contactsJSonString,
                new TypeToken<List<Contact>>() {
                }.getType());

        // Initialize and populate contacts HashMap
        contactsHashMap = new HashMap<>();
        for (Contact contact: contactList) {
            contactsHashMap.put(contact.getId(), contact);
        }

        return contactList;
    }

    private String parseUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            Log.d(TAG, "Failed to parse the json for contact list", e);
            return null;
        }
    }

    public void toggleFav(Contact contact){
        contactsHashMap.get(contact.getId()).toggleFavorite();
    }
}
