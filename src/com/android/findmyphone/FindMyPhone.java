package com.android.findmyphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FindMyPhone extends Activity {
	
	public static final String TAG = "FindMyPhone";

    private Button mAddAccountButton;
    private ListView mContactList;
    private DataHelper dh = null;
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {

		dh = new DataHelper(this);

		    
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Obtain handles to UI objects
        mAddAccountButton = (Button) findViewById(R.id.addContactButton);
        mContactList = (ListView) findViewById(R.id.contactList);
        mContactList.setOnItemClickListener(new OnItemClickListener() {
        	 public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		 Intent i = null;
        		 i = new android.content.Intent();
        		 i.setClass(FindMyPhone.this, EditActivity.class);
        		 i.putExtra("selecteditem", ((android.database.sqlite.SQLiteCursor)mContactList.getItemAtPosition(position)).getString(0));
        		 startActivity(i);
        	 }});

        // Initialize class properties
 

        // Register handler for UI elements
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchContactAdder();
                //();
            }
        });
       

        // Populate the contact list
       populateApprovedSendersList();
       
    }
	protected void launchContactAdder() {
		
        //Intent i = new Intent(this, ContactAdder.class);
        //startActivity(i);
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateApprovedSendersList() {
    
        // Build adapter with contact entries
    	Cursor cursor = null;
       cursor = dh.selectAllCurstor();

        String[] fields = new String[] {
                "PhoneNumber", "_id"
        };
        Log.v(TAG, "***CREATING CURSOR");
        
      SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor, fields, new int[]{R.id.contactEntryText});
       mContactList.setAdapter(adapter);
    }

}