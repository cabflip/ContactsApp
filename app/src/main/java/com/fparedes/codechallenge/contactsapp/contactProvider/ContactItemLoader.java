package com.fparedes.codechallenge.contactsapp.contactProvider;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.fparedes.codechallenge.contactsapp.contactItem.Contact;

import java.util.List;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class ContactItemLoader extends AsyncTaskLoader<List<Contact>> {

    private static final String TAG = "ContactItemLoader";
    private final String mUrl;

    public ContactItemLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Contact> loadInBackground() {
        try {
            return ContactProvider.buildContactList(mUrl);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch contacts data", e);
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
