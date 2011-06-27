package com.android.findmyphone;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
 
public class SmsReceiver extends BroadcastReceiver
	    {
	
	private final LocationListener locationListener = new LocationListener() {
		  public void onLocationChanged(Location location) {
	
			  
		  }

		  public void onProviderDisabled(String provider){}
		  public void onProviderEnabled(String provider) {}
		  public void onStatusChanged(String provider, int status, Bundle extras) {}
		};
		
	public void onReceive(Context context, Intent intent) 
    {
	        //This is supposed to fire on SMS delivery....
	        Bundle bundle = intent.getExtras();        
	        SmsMessage[] msgs = null;
	        String str = "";     
	        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
	        //Get as fine grained location as possible, while saving battery life.
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        String provider = lm.getBestProvider(criteria, true);
	        lm.requestLocationUpdates(provider, 60000, 0, locationListener);
	        
	        
	        DataHelper dh = new DataHelper(context);

	        
	        
	        if (bundle != null)
	        {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];            
	            for (int i=0; i<msgs.length; i++){
	                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]); 
	                if(msgs[i].getMessageBody().equals("findme")) //slug
	                {
	                	if(dh.IsApprovedSender(msgs[i].getOriginatingAddress()))
	                	{
	                		if(lm != null)
	                		{
	                			
	                			Location last_good = lm.getLastKnownLocation(provider);
	                			if(last_good != null)
	                			{
	                				Toast.makeText(context, "Got Message from " + msgs[i].getOriginatingAddress() + " , Sending" + last_good.toString(), Toast.LENGTH_SHORT).show();
	                				sendSMS(msgs[i].getOriginatingAddress(), "http://maps.google.com?q=" + last_good.convert(last_good.getLatitude(), Location.FORMAT_DEGREES) + "," + last_good.convert(last_good.getLongitude(), Location.FORMAT_DEGREES), context);
	                			}
	                			else
	                			{
	                				Toast.makeText(context, "Got Message from " + msgs[i].getOriginatingAddress() + " , Not Replying", Toast.LENGTH_SHORT).show();
	                			}
	                		}
	                	}
	                	else
	                	{
	                		Toast.makeText(context, "Got Message from " + msgs[i].getOriginatingAddress() + " , Not Approved", Toast.LENGTH_SHORT).show();
	                		
	                	}
	                } 
	            }
	            //---display the new SMS message---
//	           // Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	        }                         
	 }
	
	 private void sendSMS(String phoneNumber, String message, Context context)
	    {                     
		 try{
	        SmsManager sms = SmsManager.getDefault();
	       
	       if(sms != null)
	       {
	        sms.sendTextMessage(phoneNumber, null, message, null, null);   
	       }
	       else
	       {
	    	   Toast.makeText(context, "SMS Manager is NULL", Toast.LENGTH_SHORT).show();
	       }
		 }catch(Exception ex)
		 {
			 //Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		 }
	    }   
	
	
}
