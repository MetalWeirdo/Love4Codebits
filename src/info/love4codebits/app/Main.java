package info.love4codebits.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.ads.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity implements OnClickListener{
	
	HttpClient client = new DefaultHttpClient();
	static HttpPost post = new HttpPost("http://love4codebits.info/rest.php");
	static String urlt = "http://love4codebits.info/rest.php";
	TextView tv1;
	ImageView iv1;
	Button btn1;
	Button btn2;
	Uri outputfileuri;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        /** set the objects **/
	        iv1 = (ImageView) findViewById(R.id.ivAvatar);
	        iv1.setImageDrawable((LoadAvatar(LoadPreferences("avatar"))));
	        tv1 = (TextView) findViewById(R.id.tvNickname);
	        tv1.setText(LoadPreferences("nick"));
	        tv1 = (TextView) findViewById(R.id.tvUsername);
	        tv1.setText(LoadPreferences("name"));
	        btn1 = (Button) findViewById(R.id.btnCam);
	        btn1.setOnClickListener(this);
	        btn2 = (Button) findViewById(R.id.btnChoose);
	        btn2.setOnClickListener(this);
	     // Look up the AdView as a resource and load a request.
	        AdView adView = (AdView)this.findViewById(R.id.adView);
	        adView.loadAd(new AdRequest());
	        
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	     MenuInflater inflater = getMenuInflater();
	     inflater.inflate(R.menu.menu, menu);
	     return true;
	 }
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     // Handle item selection
	     switch (item.getItemId()) {
	     case R.id.about:
	         /** about me and love4codebits **/
	    	 return true;
		case R.id.logout:
	         ClearPreferences();
	         Intent i = new Intent(Main.this, LogIn.class);
 			 startActivityForResult(i, 0);
	 		 finish();	 		 
	     }
	     return true;
	 }
	public void onClick (View v){
		// Set the image pick mode 
		switch(v.getId())
		 {
		  case R.id.btnCam: 
			  SavePreferences("picmode", "cam");break;
		  case R.id.btnChoose:
			  SavePreferences("picmode", "choose");break;
		 }
		 
		 Intent i = new Intent(Main.this, ImagePreview.class);
		 startActivity(i);
		 
				 
	 }
	
		 
	/** Load/Save of SharedPreferences, will change this to a single class **/
	 private String LoadPreferences(String key){
	        SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
	        return (sharedPreferences.getString(key, ""));
	        
	 }
	 public void SavePreferences(String key, String value){
	        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.putString(key, value);
	        editor.commit();
	        }
	 public void ClearPreferences (){
		  SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.clear();
	        editor.commit();
	 }
	 /** Getting the user's avatar to a drawable, this will probably bee removed to get the avatar via URI**/
	 public static Drawable LoadAvatar(String url) {
		    try {
		        InputStream is = (InputStream) new URL(url).getContent();
		        Drawable d = Drawable.createFromStream(is, "src name");
		        return d;
		    } catch (Exception e) {
		        return null;
		    }
		}
	 
	
	 
	 
} 
	

