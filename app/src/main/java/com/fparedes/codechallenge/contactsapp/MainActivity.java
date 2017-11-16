package com.fparedes.codechallenge.contactsapp;

import com.fparedes.codechallenge.contactsapp.adapters.ContactsSection;
import com.fparedes.codechallenge.contactsapp.contactItem.Contact;
import com.fparedes.codechallenge.contactsapp.contactProvider.ContactItemLoader;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements ContactsSection.ItemClickListener,
        LoaderManager.LoaderCallbacks<List<Contact>> {

    private static final String CONTACTS_JSON_URL =
            "https://s3.amazonaws.com/technical-challenge/v3/contacts.json";
    private static final String FAVORITES = "Favorite Contacts";
    private static final String OTHER_CONTACTS = "Other Contacts";

    private SectionedRecyclerViewAdapter sectionAdapter;
    private ContactsSection favoriteContactsSection;
    private ContactsSection otherContactsSection;

    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.contacts_list)
    RecyclerView contactsList;
    @BindView(R.id.progress_indicator)
    ProgressBar loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        contactsList.setLayoutManager(llManager);
        contactsList.addItemDecoration(new DividerItemDecoration(
                this, llManager.getOrientation()));
        // Sectioned Recycler adapter
        sectionAdapter = new SectionedRecyclerViewAdapter();
        // Contact sections
        favoriteContactsSection = new ContactsSection(
                getApplicationContext(), FAVORITES, this);
        otherContactsSection = new ContactsSection(
                getApplicationContext(), OTHER_CONTACTS, this);
        // Empty view click listener to reload
        emptyView.setOnClickListener(view -> restartContactLoader());
        // Load contacts
        getLoaderManager().initLoader(0, null, this);
    }

    private void restartContactLoader() {
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        sectionAdapter.removeAllSections();
        favoriteContactsSection.removeAllContacts();
        otherContactsSection.removeAllContacts();

        // Load contacts from URL
        getLoaderManager().restartLoader(0, null, this);
    }

    private void populateContacts(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty())
            return;

        // Sort contacts alphabetically by name
        Collections.sort(contacts, Comparator.comparing(Contact::getName));
        for (Contact contact : contacts) {
            if (contact.isFavorite())
                favoriteContactsSection.addContact(contact);
            else
                otherContactsSection.addContact(contact);
        }
        // Populate the adapter with both sections
        sectionAdapter.addSection(favoriteContactsSection);
        sectionAdapter.addSection(otherContactsSection);
        contactsList.setAdapter(sectionAdapter);
    }

    @Override
    public void itemClicked(ContactsSection.ItemViewHolder viewHolder,
                            Contact contact, int position) {
        // DetailActivity Intent
        Intent intent = new Intent(this, DetailActivity.class);
        // Pass contact parcelable object to DetailActivity
        intent.putExtra(DetailActivity.EXTRA_CONTACT, contact);
        // Get the views that will have transition between activities
        Pair<View, String> imagePair = Pair.create(
                viewHolder.getImgProfile(), getString(R.string.transition_contact_image));
        Pair<View, String> namePair = Pair.create(
                viewHolder.getTvContactName(), getString(R.string.transition_contact_name));
        Pair<View, String> companyPair = Pair.create(
                viewHolder.getTvContactCompany(), getString(R.string.transition_contact_company));

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, imagePair, namePair, companyPair);
        ActivityCompat.startActivityForResult(this, intent,
                DetailActivity.DETAIL_CONTACT_CODE_REQUEST, options.toBundle());
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int i, Bundle bundle) {
        return new ContactItemLoader(getApplicationContext(), CONTACTS_JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> contacts) {
        populateContacts(contacts);
        loadingView.setVisibility(View.GONE);

        boolean hasContacts = contacts == null || contacts.isEmpty();
        emptyView.setVisibility(hasContacts ? View.VISIBLE : View.GONE);
        contactsList.setVisibility(hasContacts ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        sectionAdapter.removeAllSections();
        favoriteContactsSection.removeAllContacts();
        otherContactsSection.removeAllContacts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DetailActivity.DETAIL_CONTACT_CODE_REQUEST
                && resultCode == RESULT_OK) {
            // If there's an update in the user, refresh lists
            restartContactLoader();
        }
    }
}
