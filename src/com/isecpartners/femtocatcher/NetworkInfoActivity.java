package com.isecpartners.femtocatcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NetworkInfoActivity extends Activity{
	private static final String TAG = "NetworkInfoActivity";
	private TelephonyManager mTelephonyManager;
	private PhoneStateListener mListener;
	private String text;
	private String text1;
	private TextView tv0;
	private TextView tv1;
	private TextView tv2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_info_layout);
		tv1 = (TextView) findViewById(R.id.get_cell_details);
		tv2 = (TextView) findViewById(R.id.get_service_details);
		
		startTracking();
		
	}
	
	public void startTracking(){
        mTelephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
      	 mListener = new PhoneStateListener() {
    		 public void onCellLocationChanged(CellLocation location) {
    			 Log.d(TAG, "Cell Location changed!");
    			 getCellNetworkInfo();
    			 
    		 }
//    		 public void onServiceStateChanged(ServiceState s){
//    			 Log.d(TAG, "Service State changed!");
//    			 getServiceStateInfo(s);
//    		 }
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
	
//	public void getServiceStateInfo(ServiceState s) {
//		
//		text = "\n\n";
//		Log.v(TAG, "getting service state info");
//		text = text + "STATE: "+s.getState();
//		
//		text = text + "\nOperator Info Long name: " + s.getOperatorAlphaLong();
//		text = text + "\nOperator Info Short name: " + s.getOperatorAlphaShort();
//		text = text + "\nOperator Info Numeric: " + s.getOperatorNumeric();
//		text = text + "\nRoaming: " + s.getRoaming();
//		
//		text = text + "\ntoString: " + s.toString();
//		
//			
//		
//		tv2.setText(text);
//		Log.v(TAG, "DUMPING SERVICE STATE INFO: "+text);
//		
//	}
	
	public void goBack(View v){
		stopTracking();
		finish();
		
	}
public void getCellNetworkInfo(){
		
		if(mTelephonyManager != null){
			text1 = "";
			Log.v(TAG, "getting cell network info");
			int phoneType = mTelephonyManager.getPhoneType();
			if(TelephonyManager.PHONE_TYPE_NONE == phoneType){
				text1 = text1 + "\nNo CDMA Phone Network found";
				tv1.setText(text1);
				return;
			}
			/* Check whether you are connected to a CDMA network */
			if(TelephonyManager.PHONE_TYPE_CDMA == phoneType){
				text1 = text1 + "Cell on CDMA Phone network";
			}
			else{
				text1 = text1 + "Cell is not on CDMA Phone network";
				tv1.setText(text1);
			}
			
			/* Get the network type and name*/
			if(mTelephonyManager!=null){
				int networkType = mTelephonyManager.getNetworkType();
				text1 = text1 + "\nNetwork Type = " + MainActivity.getNetworkTypeName(networkType);
				
				
				
				
				/* get network operator name */
				String operatorName = mTelephonyManager.getNetworkOperatorName();
				text1 = text1 +"\nNetwork Operator Name: "+operatorName;
				
				/* get CDMA cell location information */
				CdmaCellLocation c = (CdmaCellLocation) mTelephonyManager.getCellLocation();
				if(c!=null){
					text1 = text1 + "\nBaseStation ID: "+c.getBaseStationId();
					text1 = text1 + "\nNetwork ID: "+c.getNetworkId();
					text1 = text1 + "\nSystem ID: "+c.getSystemId();				
					text1 = text1 + "\nLatitude: "+c.getBaseStationLatitude();
					text1 = text1 + "\nLongitude: "+c.getBaseStationLongitude();
					
					tv1.setText(text1);
					
				}
			}
		}
	}

}
