package com.example.advancedse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LoginActivity extends Activity{
	private boolean authorised = false;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        super.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			TextView email = (TextView)findViewById(R.id.emailView);
    			TextView password = (TextView) findViewById(R.id.passwordView);
    			LoginTask loginTask = new LoginTask();
    			loginTask.execute(email.getText().toString(), password.getText().toString());
    			synchronized(loginTask){
	                try {
						loginTask.wait();//wait for login opertion to complete
					} catch (InterruptedException e) {
						
					}
    	        }
    			if(authorised){
    				Intent i = new Intent(getApplicationContext(), MainActivity.class);
    				startActivity(i);
    				finish();
    			}else{
    				password.setText("");
    			}
    		}});
        
    }
	
	private class LoginTask extends AsyncTask<String, Void, Void> {

		
		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(String... params) {
			authorised = LoginDB.login(params[0], params[1]);
			synchronized(this){
				notifyAll();
			}
			return null;
		}

		
	}
	
	
}

