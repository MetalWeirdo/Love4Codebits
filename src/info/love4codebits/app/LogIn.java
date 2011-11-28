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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	String APIurl = "https://services.sapo.pt/Codebits";
    JSONObject jObject;
    EditText etText ;
    Button login;
    CheckBox cb;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        /** Check if the checkbox "remember me" was ticked to make a auto-login**/
        if(LoadPreferences("autologin")!="")
        {
        	if ( login()){
        		proccedtoMain();
        	}
        	else
        		Toast.makeText(this, "Something went wrong :|", Toast.LENGTH_LONG).show();
        }	
    }
    
    public void onClick(View v){
    	
    	/** Changing the visiblity of the button to false and show the progress bar **/
    	ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar1);
    	login.setVisibility(View.GONE);
    	pg.setVisibility(View.VISIBLE);
    	cb = (CheckBox) findViewById(R.id.cbRemember);
    	
    	/** Saving the e-mail and password to SharedPreferences**/

    	etText = (EditText) findViewById(R.id.etE_mail);
    	SavePreferences("mail",etText.getText().toString() );
    	etText = (EditText) findViewById(R.id.etPassword);
    	SavePreferences("password",  etText.getText().toString());
    	
    	/** Setting the auto-login to "true" if checkbox is ticked **/
    	
    	if (cb.isChecked()){
    		SavePreferences("autologin", "true");
    	}
    	
    	/** Login to Codebits **/
    	
    	if (login()){
    		proccedtoMain();
    	}
    	else{
    		login.setVisibility(View.VISIBLE);
        	pg.setVisibility(View.GONE);
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
    
    /** Load/Save of SharedPreferences, will change this to a single class **/
    
    public void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        }
  
    public String LoadPreferences(String key){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return (sharedPreferences.getString(key, ""));
    }
    
    /** Function to get the Codebits Token to use on Login **/
    
    private boolean getToken(){
		try {
			jObject = new JSONObject(getJSON("/gettoken?user="
					+ LoadPreferences("mail") + "&password="
					+ LoadPreferences("password")));
			SavePreferences("uid", jObject.getString("uid"));
			SavePreferences("token", jObject.getString("token"));
			return true;
		} catch (JSONException e) {
			Toast.makeText(this, "Codebits said noo!", 9999999).show();
			return false;
		}
	}
    
    /** Function to get the User Info (nickname,real name, twitter, avatar,etc...) after getting the token **/
    
	private boolean getUserInfo() {
		try {
			jObject = new JSONObject(getJSON("/user/" + LoadPreferences("uid")
					+ "?token=" + LoadPreferences("token")));
			SavePreferences("nick", jObject.getString("nick"));
			SavePreferences("avatar", jObject.getString("avatar"));
			SavePreferences("twitter", jObject.getString("twitter"));
			SavePreferences("name", jObject.getString("name"));
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
 }

       
