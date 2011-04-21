package com.example.cryptoclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class SignerClient extends Activity {
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.v("client", intent.getStringExtra("result"));
        }
    };
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
	protected void onResume() {
	    super.onResume();
	    
	    IntentFilter filter = new IntentFilter();
        String broadcastid = "servbc3";
	    filter.addAction(broadcastid);
        registerReceiver(receiver, filter);

        
        Intent intent = new Intent("org.axades.core.START_CORE_SERVICE");
        intent.putExtra("p12path", "kspath");
        intent.putExtra("p12pass", "password");
        intent.putExtra("signdata", "path");
        intent.putExtra("broadcastid", broadcastid);
        
        startService(intent);
        
    }
}