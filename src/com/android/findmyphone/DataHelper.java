package com.android.findmyphone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataHelper {
	
	 private static final String DATABASE_NAME = "findmyphone.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "Users";

	   private Context context;
	   private SQLiteDatabase db;

	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into " 
	      + TABLE_NAME + "(_id, PhoneNumber, Name, Password)  values (?, ?, ?, ?)";
	   private static final String COUNT = "SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE PhoneNumber = (?)";

	   public DataHelper(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      this.db = openHelper.getWritableDatabase();
	      this.insertStmt = this.db.compileStatement(INSERT);
	   }

	   public long insert(String name, String number, String password) {
	      this.insertStmt.bindString(1, number);
	      this.insertStmt.bindString(2, number);
	      this.insertStmt.bindString(3, name);
	      this.insertStmt.bindString(4, password);
	      return this.insertStmt.executeInsert();
	   }
	   
	   public boolean IsApprovedSender(String number)
	   {
		   SQLiteStatement counter  = this.db.compileStatement(COUNT);
		   counter.bindString(1, number);
		   long val = counter.simpleQueryForLong();
		   if(val > 0)
			   return true;
		   else
			   return false;
	   }

	   public void deleteAll() {
	      this.db.delete(TABLE_NAME, null, null);
	   }

	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "PhoneNumber" }, 
	        null, null, null, null, "PhoneNumber desc");
	      if (cursor.moveToFirst()) {
	         do {
	            list.add(cursor.getString(0)); 
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return list;
	   }
	   
	   public Cursor selectAllCurstor()
	   {
		      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "_id" , "PhoneNumber" }, 
		        null, null, null, null, "_id  desc");
		      return cursor;
	   }

	   private static class OpenHelper extends SQLiteOpenHelper {

	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }

	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, Name TEXT, PhoneNumber TEXT, Password TEXT)");
	      }

	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	         onCreate(db);
	      }
	   }

}
