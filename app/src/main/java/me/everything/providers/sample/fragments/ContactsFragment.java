package me.everything.providers.sample.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import me.everything.providers.android.contacts.Contact;
import me.everything.providers.android.contacts.ContactsProvider;
import me.everything.providers.core.Data;
import me.everything.providers.sample.R;
import me.everything.providers.sample.base.BaseFragment;

/**
 * Created by sromku
 */
public class ContactsFragment extends BaseFragment {

    private ListView mListView;
    private ContactsAdapter mAdapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setToolbarTitle("Contacts");

        mListView = (ListView) view.findViewById(R.id.list);

        // show contacts by using cursor


        showContacts();

        return view;
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            this.getActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            new GetContactsTask().execute();
        }
    }

    /**
     * Getting the cursor to all contacts
     */
    private class GetContactsTask extends AsyncTask<Void, Void, Data<Contact>> {

        @Override
        protected Data<Contact> doInBackground(Void... params) {
            ContactsProvider contactsProvider = new ContactsProvider(getApplicationContext());
            Data<Contact> contacts = contactsProvider.getContacts();
            return contacts;
        }

        @Override
        protected void onPostExecute(Data<Contact> data) {
            mAdapter = new ContactsAdapter(getApplicationContext(), data);
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * Example of using cursor. (CursorAdapter)
     */
    private final static class ContactsAdapter extends CursorAdapter {

        private Data<Contact> mData;

        private class ViewHolder {
            TextView name;
            TextView phone;
        }

        public ContactsAdapter(Context context, Data<Contact> data) {
            super(context, data.getCursor(), FLAG_REGISTER_CONTENT_OBSERVER);
            mData = data;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.title);
            viewHolder.phone = (TextView) view.findViewById(R.id.details);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            Contact contact = mData.fromCursor(cursor);
            viewHolder.name.setText(contact.displayName);
            viewHolder.phone.setText(contact.normilizedPhone);
        }

    }

}
