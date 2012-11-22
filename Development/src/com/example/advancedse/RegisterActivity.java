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
import android.widget.Toast;
import android.widget.ViewFlipper;

public class RegisterActivity extends Activity{
	protected boolean registerComplete=false;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        
        super.findViewById(R.id.register_registerButton).setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			TextView email = (TextView)findViewById(R.id.register_emailText);
    			TextView password = (TextView) findViewById(R.id.register_passwordText);
    			TextView displayName = (TextView) findViewById(R.id.register_displayNameText);
    			if(email.getText().toString().matches("(\\w)*@(\\w)*(\\.(\\w)*)*")){//check email address is valid
    				if(password.getText().toString().length() > 0){
		    			RegisterTask registerTask = new RegisterTask();
		    			registerTask.execute(email.getText().toString(), password.getText().toString(), displayName.getText().toString());
		    			synchronized(registerTask){
			                try {
			                	registerTask.wait();//wait for login operation to complete
							} catch (InterruptedException e) {
								
							}
		    	        }
		    			if(registerComplete){
			    			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			    			i.putExtra("email", email.getText().toString());
							startActivity(i);
							finish();
		    			}else{
		    				email.setText("");
		    				password.setText("");
		    				
		    			}
    				}else{
    					Toast.makeText(getBaseContext(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
    				}
    			}else{
    				Toast.makeText(getBaseContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
    			}
    		}});
        
    }
	
	private class RegisterTask extends AsyncTask<String, Void, Void> {

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(String... params) {
			registerComplete = LoginDB.register(params[0], params[1], params[2]);
			synchronized(this){
				notifyAll();
			}
			return null;
		}
	}
}

