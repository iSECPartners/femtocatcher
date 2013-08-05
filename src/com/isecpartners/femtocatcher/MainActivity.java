package com.isecpartners.femtocatcher;


import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity{

	private static final String TAG = "FemtoCellActivity";
	private TelephonyManager mTelephonyManager;
	private PhoneStateListener mListener;
	private int FEMTO_NID_MIN = 0xfa;
	private int FEMTO_NID_MAX = 0xff;
	private Boolean ChangeAirplaneMode;
	private TextView tv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Check if the app can turn on airplane mode */
		int api = android.os.Build.VERSION.SDK_INT;
		if(api < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ){
			ChangeAirplaneMode = true;
			setContentView(R.layout.main_screen_1);
			TextView tv = (TextView) findViewById(R.id.display_general_info);
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			final CheckBox c = (CheckBox) findViewById(R.id.chkDetect);
			getUserChoice(c);
		}
		else{
			ChangeAirplaneMode = false;
			setContentView(R.layout.main_screen_2);
			String s = "Your Phone is running version " + android.os.Build.VERSION.RELEASE + " of Android, and has removed API support for Airplane Mode.  iSEC’s Femtocatcher can alert you if you are attached to a femtocell, but cannot place you into Airplane mode.  If you wish to continue, choose ‘Settings’ below.";
			
			TextView tv = (TextView) findViewById(R.id.display_notice);
			tv.setText(s);
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			
			
		}
		
	}
	
	public void getUserChoice(final CheckBox c){
		c.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			    {
			    if (c.isChecked())
			        {
			        	Log.v(TAG, "start tracking");
			        	startTracking();
			        	
			        }
			    else{
					stopTracking();
			    }
			    }
			});
	}
	public void goToMainScreen(View v){
		setContentView(R.layout.main_screen_1);
		final CheckBox c = (CheckBox) findViewById(R.id.chkDetect);
		c.setText("Notify on Femtocells");
		
		TextView tv = (TextView) findViewById(R.id.display_general_info);
		tv.setText(R.string.non_airplane_mode);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		getUserChoice(c);
	}
	
	public void getNetworkInfo(View v){
		Intent myIntent = new Intent(MainActivity.this, NetworkInfoActivity.class);
        startActivity(myIntent);
	}
	
	public void toggleRadio(){
		/* If you can change the airplane mode, turn on the airplane mode and let the user know */
		if(ChangeAirplaneMode){
			
			System.putLong(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
			long isEnabled = System.getLong(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, -1);
			Log.v(TAG, "airplane mode now is: "+isEnabled);

			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", false);
			sendBroadcast(intent);
			
			
		}
		
		Intent myIntent = new Intent(MainActivity.this, DetectedFemtoActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("ChangeAirplaneMode", ChangeAirplaneMode);
        myIntent.putExtras(b); 
        startActivity(myIntent);

	}
	
	
	public void startTracking(){
        mTelephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        
        /* Check if it is a CDMA phone */
        if(mTelephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA){
        	tv1 = (TextView) findViewById(R.id.err_msg);
        	tv1.setText("This application can detect a femtocell on a CDMA phone only.");
        	return;
        }
        
      	 mListener = new PhoneStateListener() {
    		 public void onServiceStateChanged(ServiceState s){
    			 Log.d(TAG, "Service State changed!");
    			 getServiceStateInfo(s);
    		 }
    	};
    	mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CELL_LOCATION);
    	mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_SERVICE_STATE);
	}
	public void stopTracking(){
		if(mListener!=null){
			mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
			Log.v(TAG, "stopped tracking");
		}
	}
	
	public void getServiceStateInfo(ServiceState s) {
		
		if(s!= null && IsConnectedToCdmaFemto(s)){
			sendNotification();
			toggleRadio();
		}
		
	}

	private boolean IsConnectedToCdmaFemto(ServiceState s) {
		
		if (s == null){
			return false;
		}
		
		/* Get International Roaming indicator
		 * if indicator is not 0 return false
		 */
		
		//TODO
		
		/* Get the radio technology */
		int networkType = mTelephonyManager.getNetworkType();
		
		/* Check if it is EvDo network */
		boolean evDoNetwork = isEvDoNetwork(networkType); 
		
		/* If it is not an evDo network check the network ID range. 
		 * If it is connected to femtocell, the nid should be lie between [0xfa, 0xff)
		 */
		if(!evDoNetwork){
			/* get network ID */
			if(mTelephonyManager!=null){
				CdmaCellLocation c = (CdmaCellLocation) mTelephonyManager.getCellLocation();
				if(c!=null){
					int networkID = c.getNetworkId();
					if((networkID < FEMTO_NID_MIN) || (networkID >= FEMTO_NID_MAX)){
						return false; 
					}
					else{
						return true;
					}
				
				}
				else{
					Log.v(TAG, "Cell location info is null.");
					return false;
				}
			}
			else{
				Log.v(TAG, "Telephony Manager is null.");
				return false;
			}

			
		}
		
		/* if it is an evDo network */
		// TODO
		else{
			
			/* get network ID */
			if(mTelephonyManager!=null){
				CdmaCellLocation c = (CdmaCellLocation) mTelephonyManager.getCellLocation();
				if(c!=null){
					int networkID = c.getNetworkId();
					if((networkID < FEMTO_NID_MIN) || (networkID >= FEMTO_NID_MAX)){
						return false; 
					}
					else{
						return true;
					}
				
				}
				else{
					Log.v(TAG, "Cell location info is null.");
					return false;
				}
			}
			else{
				Log.v(TAG, "Telephony Manager is null.");
				return false;
			} 
		}

	}


	public void sendNotification() {
		int mId = 0;
		long pattern[] = {0, 1000, 1000};
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
	    .setSmallIcon(R.drawable.icon24)
	    .setContentTitle("Detected Femtocell")
	    .setVibrate(pattern)
	    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
	    .setAutoCancel(true)
	    .setContentText("Your device is connected to a femtocell.");
		
	NotificationCompat.InboxStyle inboxStyle =
	        new NotificationCompat.InboxStyle();
	String[] events = new String[6];

	inboxStyle.setBigContentTitle("Your device is connected to a femtocell.");

	for (int i=0; i < events.length; i++) {

	    inboxStyle.addLine(events[i]);
	}

	mBuilder.setStyle(inboxStyle);

		Intent resultIntent = new Intent(this, DetectedFemtoActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("ChangeAirplaneMode", ChangeAirplaneMode);
        resultIntent.putExtras(b); 

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		stackBuilder.addParentStack(DetectedFemtoActivity.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(mId, mBuilder.build());
		
	}

	private boolean isEvDoNetwork(int networkType) {
		
		if ((networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) ||
				(networkType == TelephonyManager.NETWORK_TYPE_EVDO_A) ||
					(networkType == TelephonyManager.NETWORK_TYPE_EVDO_B)||
						(networkType == TelephonyManager.NETWORK_TYPE_EHRPD)){
			return true;
		}
		
			return false;
		
	}

	
	/* src: from Android source code: com.google.android/android/4.2.2_r1/android/telephony/TelephonyManager.java getNetworkTypeName() */
	
    public static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }

}
