package com.example.advancedse;

import com.example.advancedse.exceptions.IncorrectPasswordException;
import com.example.advancedse.exceptions.InvalidEmailException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LoginActivity extends Activity{
	private boolean authorised = false;
	
	//toast handlers can be called from within other threads
	Handler toastHandler = new Handler();
	Runnable invaildEmailToastRunnable = new Runnable() {public void run() {Toast.makeText(LoginActivity.this, "Email Address is Invaild. Make sure you have registerd.", Toast.LENGTH_SHORT).show();}};
	Runnable invaildPasswordToastRunnable = new Runnable() {public void run() {Toast.makeText(LoginActivity.this, "Incorrect Password. Please try again.", Toast.LENGTH_SHORT).show();}};
	Runnable loggingInToastRunnable = new Runnable() {public void run() {Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_LONG).show();}};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        loadLoginDetails();//load any saved login details.
        
        super.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			TextView email = (TextView)findViewById(R.id.emailView);
    			TextView password = (TextView) findViewById(R.id.passwordView);
    			CheckBox rememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
    			
    			if(email.getText().toString().length()>0){
    				LogingInMessageTask loginmessageTask = new LogingInMessageTask();
    				loginmessageTask.execute("");
    				if(rememberMe.isChecked()){
    					saveLoginDetails(email.getText().toString(), password.getText().toString());
    				}else{
    					forgetLoginDetails();
    				}
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
	    				i.putExtra("email", email.getText().toString());
	    				startActivity(i);
	    				finish();
	    			}else{
	    				password.setText("");
	    			}
    			}else{
    				Toast.makeText(getBaseContext(), "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
    			}
    		}

			private void saveLoginDetails(String email, String password) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
			    Editor ed = prefs.edit();
			    ed.putString("savedUsername", email);
			    ed.putString("savedPassword", password);
			    ed.commit();
			}
			private void forgetLoginDetails() {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
			    Editor ed = prefs.edit();
			    ed.remove("savedUsername");
			    ed.remove("savedPassword");
			    ed.commit();
			}	
        
        });
        
        
        
        super.findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				finish();
				
			}
		});
        
        
    }
	
	private void loadLoginDetails() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String username = prefs.getString("savedUsername", "");
		String password = prefs.getString("savedPassword", ""); //return nothing if no pass saved
		((TextView)findViewById(R.id.emailView)).setText(username);
		((TextView)findViewById(R.id.passwordView)).setText(password);
		if(username.length()>0){
			((CheckBox)findViewById(R.id.checkBoxRememberMe)).setChecked(true);
		}
	}

	private class LoginTask extends AsyncTask<String, Void, Void> {
		
		
		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(String... params){
			try {
				authorised = LoginDB.login(params[0], params[1]);
			} catch (IncorrectPasswordException e) {
				toastHandler.post(invaildPasswordToastRunnable);
			} catch (InvalidEmailException e) {
				toastHandler.post(invaildEmailToastRunnable);
			}
			synchronized(this){
				notifyAll();
			}
			return null;
		}

		
	}
	
private class LogingInMessageTask extends AsyncTask<String, Void, Void> {
		
		
		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(String... params){
			toastHandler.post(loggingInToastRunnable);
			return null;
		}

		
	}
	
	
}

