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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Love4CodebitsActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    String e_mail="";
    String password = "";
    JSONObject jObject;
    EditText etText ;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button login = (Button) findViewById(R.id.btnLogin);
        
        login.setOnClickListener(this);
    }
    
    public void onClick(View v){
    	Button login = (Button) findViewById(R.id.btnLogin);
    	ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar1);
    	login.setVisibility(View.GONE);
    	pg.setVisibility(View.VISIBLE);
    	etText = (EditText) findViewById(R.id.etE_mail);
    	e_mail = etText.getText().toString();
    	etText = (EditText) findViewById(R.id.etPassword);
    	password = etText.getText().toString();
    	try {
			jObject = new JSONObject(getJSON("/gettoken?user=" + e_mail + "&password="+ password));
    		//jObject =new JSONObject(jString);	
			String uid = jObject.getString("uid");
    		String token = jObject.getString("token");
			
			jObject = new JSONObject(getJSON("/user" + uid + "?token=" + token));
			Toast.makeText(this, getJSON("/user/" + uid + "?token=" + token),999999999).show();
    	}
		 catch (JSONException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
    	
    
    	//Toast.makeText(this, password , Toast.LENGTH_SHORT).show();
    }
    public String getJSON(String rurl){	
	StringBuilder builder = new StringBuilder();
	HttpClient client = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet("https://services.sapo.pt/Codebits" + rurl);
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
}