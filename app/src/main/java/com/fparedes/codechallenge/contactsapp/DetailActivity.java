package com.fparedes.codechallenge.contactsapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fparedes.codechallenge.contactsapp.contactItem.Contact;
import com.fparedes.codechallenge.contactsapp.contactProvider.ContactProvider;
import com.fparedes.codechallenge.contactsapp.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Facundo A. Paredes on 15/11/2017.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    public static final int DETAIL_CONTACT_CODE_REQUEST = 12;

    private static final String PHONE_TITLE = "PHONE:";
    private static final String ADDRESS_TITLE = "ADDRESS:";
    private static final String BIRTHDATE_TITLE = "BIRTHDATE:";
    private static final String EMAIL_TITLE = "EMAIL:";

    private static final String PHONE_HOME = "Home";
    private static final String PHONE_MOBILE = "Mobile";
    private static final String PHONE_WORK = "Work";

    private Contact contact;

    @BindView(R.id.detail_contact_img)
    ImageView ivContactImg;
    @BindView(R.id.detail_contact_name)
    TextView tvContactName;
    @BindView(R.id.detail_contact_company)
    TextView tvContactCompany;
    @BindView(R.id.detail_container)
    LinearLayout detailContainer;

    private Drawable favIcon;
    private Drawable favFalseIcon;
    private ContactProvider contactProvider = new ContactProvider();
    private boolean favHasChanged = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = getIntent().getParcelableExtra(EXTRA_CONTACT);
        tvContactName.setText(contact.getName());
        tvContactCompany.setText(contact.getCompanyName());

        favFalseIcon = getDrawable(R.drawable.ic_action_fav_false);
        favIcon = getDrawable(R.drawable.ic_action_fav);

        GlideApp.with(this)
                .load(contact.getLargeImageURL())
                .placeholder(R.drawable.user_icon_large)
                .into(ivContactImg);

        populateDetails();
    }

    private void populateDetails() {
        // Add phones
        addView(PHONE_TITLE, contact.getPhone().getHome(), PHONE_HOME);
        addView(PHONE_TITLE, contact.getPhone().getMobile(), PHONE_MOBILE);
        addView(PHONE_TITLE, contact.getPhone().getWork(), PHONE_WORK);
        // Add Address
        addView(ADDRESS_TITLE, contact.getAddress().getFormattedAddress(), "");
        // Add BirthDate
        addView(BIRTHDATE_TITLE, contact.getForrmattedBirthdate(), "");
        // Add Email
        addView(EMAIL_TITLE, contact.getEmailAddress(), "");
    }

    private void addView(String itemTitle, String itemValue, String itemValueDetail) {
        // Skip if value doesn't exists or is empty
        if (itemValue == null || itemValue.isEmpty())
            return;

        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.contact_detail_item, null);

        TextView tvItemKey = item.findViewById(R.id.contact_detail_key);
        tvItemKey.setText(itemTitle);

        TextView tvItemValue = item.findViewById(R.id.contact_detail_value);
        tvItemValue.setText(itemValue);

        if (itemValueDetail != null && !itemValueDetail.isEmpty()) {
            TextView tvItemValueDet = item.findViewById(R.id.contact_detail_value_type);
            tvItemValueDet.setText(itemValueDetail);
        }

        detailContainer.addView(item);
    }

    private void showSnackBar(String msg){
        Snackbar.make(findViewById(R.id.detail_activity_container),
                msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (menuItem.getItemId() == R.id.action_fav) {
            // Toggle favHasChanged to know if it's necessary
            //  to update the list when returning to MainActivity
            favHasChanged = !favHasChanged;
            // Toggle contact favorite's attribute
            contact.toggleFavorite();
            contactProvider.toggleFav(contact);
            // Toggle menuItem's icon
            menuItem.setIcon(contact.isFavorite() ? favIcon : favFalseIcon);

            // If added to Favorites, notify with a Snackbar
            if (contact.isFavorite())
                showSnackBar(contact.getName() + " added to Favorites!");
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        // Set initial fav icon
        MenuItem favItem = menu.findItem(R.id.action_fav);
        favItem.setIcon(contact.isFavorite() ? favIcon : favFalseIcon);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (favHasChanged) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else
            supportFinishAfterTransition();
    }
}
