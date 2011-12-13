package info.love4codebits.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class LogIn extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	String APIurl = "https://services.sapo.pt/Codebits";
    JSONObject jObject;
    EditText etMail;
    EditText etPass;
    Button login;
    CheckBox cb;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alert;
    static SharedPreferences prefs;
    public static String MY_PREFS_FILE_NAME = "info.love4codebits.app.prefs";
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        prefs = new ObscuredSharedPreferences(this, this.getSharedPreferences(
   					MY_PREFS_FILE_NAME, Context.MODE_PRIVATE));
        builder = new AlertDialog.Builder(LogIn.this);
        builder.setCancelable(false);
	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
	       });
	     
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Signing you to Codebits! :D");
		progressDialog.setCancelable(false);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        etMail = (EditText) findViewById(R.id.etE_mail);    	
    	etPass = (EditText) findViewById(R.id.etPassword);
    	cb = (CheckBox) findViewById(R.id.cbRemember);
        /** Check if the checkbox "remember me" was ticked to make a auto-login**/
       
        if(prefs.getBoolean("autologin", false))
        {
        	etMail.setText("" +prefs.getString("mail","").toString());
    		etPass.setText("" +prefs.getString("password","").toString());
    		cb.setChecked(true);
        	progressDialog.show();
        	new Thread(new Runnable() {
				public void run () {
					if (isOnline()){
		        		
		        		if ( login()){
		            		proccedtoMain();
		            		progressDialog.dismiss();
		            	}
		            	else{
		            		progressDialog.dismiss();
		            		
		            		
		            	}
		        	}
		        	else{
		        		
		        		cb.setChecked(true);
		        		 
		        		builder.setMessage("You're not connected to the internet! You need that! You know you need that!");
		        		alert= builder.create();
		        		alert.show();
		        		
		        	}
		        	
				}
			
			}).start();
        
        }	
    }
    
    
	public void onClick(View v){
		if (etMail.getText().toString().length() !=0 && etPass.getText().toString().length() !=0)
    	{
    	 		progressDialog.show();
        		new Thread(new Runnable() {
    				public void run () {
    					if (isOnline()){
    		    	 	
    					/** Saving the e-mail and password to SharedPreferences**/
    	            	
    	        		prefs.edit().putString("mail",etMail.getText().toString()).commit();
    	        		prefs.edit().putString("password",  etPass.getText().toString()).commit();
    	            	
    	            	/** Setting the auto-login to "true" if checkbox is ticked **/
    	            	
    	            	if (cb.isChecked()){
    	            		prefs.edit().putBoolean("autologin", true).commit();
    	            	}
    	            	
    	            	/** Login to Codebits **/
    	            	
    	            	if (login()){
    	            		proccedtoMain();
    	            	}
    	            	else{
    	            		progressDialog.dismiss();
    	            	}
    	    	 	}	
    	        	else
    	        	{
    	            		builder.setMessage("You're not connected to the internet! You need that! You know you need that!");
    	            		alert= builder.create();
    	            		alert.show();

    	        	}
    	    	
    	        	
    	    	    	     	
    			}
        		}).start();
        		}
    	    	else{
    	    		builder.setMessage("Both the email and the password fields should be != null , you know that!");
    	    		alert= builder.create();
    	    		alert.show();
    	    	}

    	
    }
    
    /** Get the JSON file**/
    
    public String getJSON(String rurl){	
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(APIurl + rurl);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
    
    /** Function to get the Codebits Token to use on Login **/
    
    private boolean getToken(){
		try {
			jObject = new JSONObject(getJSON("/gettoken?user="
					+ prefs.getString("mail","") + "&password="
					+ prefs.getString("password","")));
			prefs.edit().putString("uid", jObject.getString("uid")).commit();
			prefs.edit().putString("token", jObject.getString("token")).commit();
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
    
    /** Function to get the User Info (nickname,real name, twitter, avatar,etc...) after getting the token **/
    
	private boolean getUserInfo() {
		try {
			jObject = new JSONObject(getJSON("/user/" + prefs.getString("uid","")
					+ "?token=" +prefs.getString("token","")));
			prefs.edit().putString("nick", jObject.getString("nick")).commit();
			prefs.edit().putString("avatar", jObject.getString("avatar")).commit();
			prefs.edit().putString("twitter", jObject.getString("twitter")).commit();
			prefs.edit().putString("name", jObject.getString("name")).commit();
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/** Function to proceed to the Main view after login in and get the user's info **/
	
	private void proccedtoMain() {
		Intent i = new Intent(LogIn.this, Main.class);
		startActivityForResult(i, 0);
		finish();
	}
	
	/** Function to call when you want to login. It calls the getToken and getUserInfo **/
	
    private boolean login() {
		if (getToken()) {
			if (getUserInfo()) {
				return true;
			} else
				return false;
		} else
			return false;
	}
    
    public boolean isOnline() {
    	   ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    	        return true;
    	    }
    	    return false;

    	}
 }

       
