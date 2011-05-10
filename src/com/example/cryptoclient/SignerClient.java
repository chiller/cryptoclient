package com.example.cryptoclient;

import com.example.filechooser.FileChooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class SignerClient extends Activity {
	
	//A service hívás eredménye ide érkezik
	private BroadcastReceiver receiver = null;
	
	private int requestCode;

	//Az aláíráskérés paraméterei
	private String path;
	private String kspath;
	private String password;
	
	//Ezen a néven kell regisztrálni a broadcast figyelőt és küldeni a broadcast üzenetet
	private String broadcastid = "bcfilter";
	
	
	/* Az Activity létrejötte után hívódik meg */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Elindítom a FileKeresőt "document" paraméterrel jelezve azt
        //Hogy az aláírandó adat elérési útvonalát kérem
        Intent i = new Intent(this,FileChooser.class);      
        i.putExtra("request","document");
        
        //Ezzel a hívással visszatérési értéke lesz az Activitynek,
        //amit az onActivityResult kap meg
        startActivityForResult(i, requestCode);
    }
    
   
    
    @Override
	protected void onDestroy() {
	    super.onDestroy();
	    //Az activity leállításánál meg kell szüntetni a receivert
	    unregisterReceiver(receiver);
        
    }
    
    /** A FileChooser Activity eredményét kapja meg, ha document paraméter jelen van 
     * akkor az adat elérési útvonalát kapta meg, ha nem akkor a kulcstár elérését.
     * Ha a kulcstár helye is megvan akkor a jelszókérést kezdeményezi.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("client","onActivityResult and resultCode = "+resultCode);
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
        	if (data.hasExtra("document")){
        		//Ha az aláírandó adat útvonalát kapta meg akkor tárolja és
        		//Kezdeményezze a kulcstár helyének a bekérését
            	path = data.getStringExtra("document");
    	        Intent i2 = new Intent(this,FileChooser.class);
    	        //Ezúttal a kulcstárat keressük
    	        i2.putExtra("request","keystore");
    	        startActivityForResult(i2, requestCode);            	
            }
            else {
            	kspath = data.getStringExtra("keystore");
            	Toast.makeText(this, path+" "+kspath , Toast.LENGTH_LONG).show();
            	//Jelszó bekérés
            	passworddialog();            	
            }
        }
    }
    
    public void passworddialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
	
		alert.setMessage("Keystore password:");
	
		//EditText view segítségével kérjük be a jelszót 
		final EditText input = new EditText(this);
		alert.setView(input);
		
		/**Az OK gomb hozzáadása és a handler beállítás
		   Ekkor a jelszóval együt minden adat megvan és indulhat az aláírás szolgáltatás*/
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {	
			public void onClick(DialogInterface dialog, int whichButton) {
					Editable value = input.getText();
					password = value.toString();
					//Core hívás kezdeményezés
					start_core_service();		  
		  }
		});
	    alert.show();	    	    	
	}
        
    private void start_core_service(){
    	
    	//Filter beállításával engedélyezzük azt, hogy az Activity
    	//megkaphassa a broadcastot
    	IntentFilter filter = new IntentFilter();
        filter.addAction(broadcastid);
        
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	//A service válasz feldolgozása
                Log.v("client", intent.getStringExtra("result"));
                Toast.makeText(context, intent.getStringExtra("result") , Toast.LENGTH_LONG).show();
            }
        };	        
        //Feliratkozás a broadcastra
        registerReceiver(receiver, filter);

        //A service elindítása
        Intent intent = new Intent("org.axades.core.START_CORE_SERVICE");
        intent.putExtra("p12path", kspath);
        intent.putExtra("p12pass", password);
        intent.putExtra("signdata", path);
        intent.putExtra("broadcastid", broadcastid);
        
        startService(intent);
        
    }
        
        
}