package com.isecpartners.femtocatcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetectedFemtoActivity extends Activity{
	private String TAG = "DetectedFemtoActivity";
	private boolean ChangeAirplaneMode;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detected_femto_layout);
        Bundle bundle = this.getIntent().getExtras();
        ChangeAirplaneMode = bundle.getBoolean("ChangeAirplaneMode");
        Log.v(TAG , "ChangeAirplaneMode: "+ChangeAirplaneMode);
        
        TextView tv = (TextView) findViewById(R.id.show_options);
        TextView tv1 = (TextView) findViewById(R.id.show_options_info);
        Button b = (Button) findViewById(R.id.airplane_result);
        
        if(ChangeAirplaneMode) {
        	tv.setText(R.string.airplane_mode_already_on);
        	b.setText("Go Back");
        }
        else {
        	tv.setText(R.string.airplane_mode_manually_on);
        	tv1.setText(R.string.airplane_mode_more_info);
        	b.setText("Manually enable Airplane mode");
        }
	}
	
	public void goToSettings(View v) {
		if(!ChangeAirplaneMode){
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
		finish();
	}
}
