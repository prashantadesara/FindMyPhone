package com.android.findmyphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EditActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		Bundle selected = this.getIntent().getExtras();
		if(selected != null)
		{
		String selectedItem = selected.getString("selecteditem");
		if(selectedItem != null)
		{
			Toast.makeText(this, selectedItem, Toast.LENGTH_LONG);
		}
		}
       
    }

}
