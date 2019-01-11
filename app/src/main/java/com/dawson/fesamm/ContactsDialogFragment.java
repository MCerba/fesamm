package com.dawson.fesamm;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dawson.fesamm.data.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsDialogFragment extends android.app.DialogFragment implements ContactsAdapter.ItemClickListener {
    RecyclerView contactRecyclerView;
    EditText searchEditText;
    View view;
    String inputForSearch;
    List<Contact> contactList;
    String emailContent;
    Contact selectedContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        emailContent = getArguments().getString("emailContent");
        //inflate layout with recycler view
        view = inflater.inflate(R.layout.dialog_contacts, container, false);
        contactRecyclerView  = view.findViewById(R.id.contacts_view);
        searchEditText  = view.findViewById(R.id.search_edit_text);
        Button dialogButton = view.findViewById(R.id.search_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateContacts();
            }
        });
        return view;
    }

    /**
     *  This method populate recycle view with contacts
     */
    private void populateContacts() {
        contactList = new ArrayList();
        Contact contact;
        inputForSearch = searchEditText.getText().toString();
        ContentResolver contentResolver = view.getContext().getContentResolver();
        Cursor cursor = getContacts();
        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            contact = new Contact();
            contact.setContactName(name);
            contact.setContactEmail(email);
            contactList.add(contact);

        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ContactsAdapter contactAdapter = new ContactsAdapter(contactList, view.getContext());

        contactRecyclerView.setLayoutManager(layoutManager);
        contactRecyclerView.setAdapter(contactAdapter);
        contactAdapter.setClickListener(this);
    }

    /**
     * This method create and populate cursor with contacts
     * @return Cursor from contentprovider
     */
    private Cursor getContacts() {
        ContentResolver cr = view.getContext().getContentResolver();
        String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";
        String filter;
        if (inputForSearch.equals("")){
            filter = ContactsContract.Contacts.DISPLAY_NAME + " NOT LIKE ''";
        }else {
            filter = ContactsContract.Contacts.DISPLAY_NAME + " LIKE '"+ inputForSearch +"%'";
        }

        return cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedContact = contactList.get(position);
        Toast.makeText(getActivity(),"Sending email to "+  selectedContact.getContactName(),
                Toast.LENGTH_LONG).show();
        String[] addresses = new   String[]{selectedContact.getContactEmail()};
        composeEmail(addresses,"Credit calculation results");
    }

    /**
     *  This method compose and send email to intent
     * @param addresses - String Address to send
     * @param subject - String subject of email
     */
    public void composeEmail(String[] addresses, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
