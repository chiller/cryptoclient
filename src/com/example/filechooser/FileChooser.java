package com.example.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.cryptoclient.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooser extends ListActivity {

		private File currentDir;
		FileArrayAdapter adapter;
		private String request;
		
		/** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Az SD kártyáról olvasunk
            currentDir = new File("/sdcard/");
            Intent caller = getIntent();
            
            //Ugyanazt a filechooser activity-t használom a kulcstár és a bemeneti adat keresésére
            //Ezért kiiratom, hogy melyiket kéri éppen a telefon
            request = caller.getStringExtra("request");
            Toast.makeText(this,request , Toast.LENGTH_LONG).show();        	
            
            fill(currentDir);
        }

        @Override
    	protected void onListItemClick(ListView l, View v, int position, long id) {
    		//File lista elem klikk-re reagál
    		super.onListItemClick(l, v, position, id);
    		Option o = adapter.getItem(position);
    		
    		//Ha mappára klikkeltem, akkor a navigálás a mappában folytatódik
    		if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
    				currentDir = new File(o.getPath());
    				fill(currentDir);
    		}
    		else
    		//Ha fájlra klikkeltem, akkor megvan a kiválasztott fájl
    		{
    			//Visszatérek a kiválasztott fájl elérési útvonalával
    			Intent in = new Intent();
    			in.putExtra(request, o.getPath());
    			setResult(1,in);
    	        finish();
    		}
    		
    	}


        //Ez metódus az aktuális mappa tartalmát iratja ki
        private void fill(File f){
        	File[]dirs = f.listFiles();
            
        	//Kiiratom, hogy éppen a kulcstárat, vagy a bemeneti adatot kell megkeresni
        	if (request.contentEquals( "document")) {
            	this.setTitle("Pick Document  ::  Current Dir: "+f.getName());}
            else{
            	this.setTitle("Pick Keystore  ::  Current Dir: "+f.getName());}	
            
            
            List<Option>dir = new ArrayList<Option>();
            List<Option>fls = new ArrayList<Option>();
            
            try{
                for(File ff: dirs)
                {
                   if(ff.isDirectory())
                       dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                   else
                   {
                       fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                   }
                }
            }catch(Exception e)
            {
                
            }
            Collections.sort(dir);
            Collections.sort(fls);
            dir.addAll(fls);
            if(!f.getName().equalsIgnoreCase("sdcard"))
                dir.add(0,new Option("..","Parent Directory",f.getParent()));
            
            
            adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
	         this.setListAdapter(adapter);
        	
        }
        
        
}

