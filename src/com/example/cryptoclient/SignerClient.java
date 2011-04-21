package com.example.cryptoclient;

import java.util.Currency;

import com.example.filechooser.FileChooser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

public class SignerClient extends Activity {
	
	
    

	private int requestCode;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        Intent i = new Intent(this,FileChooser.class);      
//        i.putExtra("request","document");
//        startActivityForResult(i, requestCode);
//        
	    
	    

        
        Intent intent = new Intent("org.axades.core.START_CORE_SERVICE");
        intent.putExtra("p12path", "kspath");
        intent.putExtra("p12pass", "password");
        intent.putExtra("signdata", "path");
        
       
        Handler h = Thread.currentThread().
        intent.putExtra("resultreceiver", 
        
        		new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                
                    Log.v("client",resultData.getString("alma"));

            }
        }
        
        
        );
        
        startService(intent);
    }
    
    @Override
	protected void onResume() {
	    super.onResume();
        
    }

    
}