package com.example.cryptoclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SignerClient extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Intent intent = new Intent("org.axades.signer.START_CORE_SERVICE");
        intent.putExtra("p12path", "kspath");
        intent.putExtra("p12pass", "password");
        intent.putExtra("signdata", "path");
        startService(intent);
        Log.v("test","Intent created");
    }
}