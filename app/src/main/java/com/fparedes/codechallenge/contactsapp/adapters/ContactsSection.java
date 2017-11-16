package com.fparedes.codechallenge.contactsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fparedes.codechallenge.contactsapp.R;
import com.fparedes.codechallenge.contactsapp.contactItem.Contact;
import com.fparedes.codechallenge.contactsapp.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Facundo A. Paredes on 14/11/2017.
 */

public class ContactsSection extends StatelessSection{

    private final static int FAV_EMOJI = 0x2B50;

    private Context mContext;
    private String mTitle;
    private List<Contact> mContacts;
    private ItemClickListener mClickListener;
    private String favEmoji;

    public ContactsSection(Context context, String title, ItemClickListener itemClickListener){
        super(new SectionParameters.Builder(R.layout.contact_section_item)
                .headerResourceId(R.layout.contact_section_header)
                .build());

        this.mContext = context.getApplicationContext();
        this.mTitle = title;
        this.mClickListener = itemClickListener;
        this.mContacts = new ArrayList<>();
        // Create the star emoji String
        favEmoji = new String(Character.toChars(FAV_EMOJI));
    }

    public void setContacts(List<Contact> contacts){
        this.mContacts = contacts;
    }

    public void addContact(Contact contact){
        mContacts.add(contact);
    }

    public List<Contact> getContacts(){
        return mContacts;
    }

    public void removeAllContacts(){
        mContacts.clear();
    }

    @Override
    public int getContentItemsTotal() {
        return mContacts.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        Contact contact = mContacts.get(position);

        itemHolder.tvContactName.setText(contact.getName());
        itemHolder.tvContactCompany.setText(contact.getCompanyName());
        itemHolder.tvFavEmoji.setText(contact.isFavorite() ? favEmoji : "");

        GlideApp.with(mContext)
                .load(contact.getSmallImageURL())
                .placeholder(R.drawable.user_icon_small)
                .into(itemHolder.imgProfile);

        itemHolder.rootView.setOnClickListener(view -> {
            Contact selectedContact = mContacts.get(position);
            mClickListener.itemClicked(itemHolder, selectedContact, position);
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.tvHeaderTitle.setText(mTitle);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvHeaderTitle;

        HeaderViewHolder(View view) {
            super(view);
            tvHeaderTitle = view.findViewById(R.id.section_header_tv);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgProfile;
        private final TextView tvContactName;
        private final TextView tvContactCompany;
        private final TextView tvFavEmoji;

        ItemViewHolder(View view){
            super(view);
            rootView = view;
            imgProfile = view.findViewById(R.id.contact_item_img);
            tvContactName = view.findViewById(R.id.contact_item_name);
            tvContactCompany = view.findViewById(R.id.contact_item_company);
            tvFavEmoji = view.findViewById(R.id.contact_fav_emoji);
        }

        public ImageView getImgProfile(){
            return imgProfile;
        }

        public TextView getTvContactName(){
            return tvContactName;
        }

        public TextView getTvContactCompany(){
            return tvContactCompany;
        }
    }

    public interface ItemClickListener {
        void itemClicked(ItemViewHolder itemViewHolder, Contact contact, int position);
    }
}
