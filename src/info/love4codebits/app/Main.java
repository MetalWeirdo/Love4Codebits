package info.love4codebits.app;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;


public class Main extends Activity implements OnClickListener{
	
	HttpClient client = new DefaultHttpClient();
	static HttpPost post = new HttpPost("http://love4codebits.info/rest.php");
	public static String MY_PREFS_FILE_NAME = "info.love4codebits.app.prefs";
	static String urlt = "http://love4codebits.info/rest.php";
	TextView tv1;
	ImageView iv1;
	Button btn1;
	Button btn2;
	Uri outputfileuri;
	Calendar c = Calendar.getInstance();
	AlertDialog.Builder builder;
	public static SharedPreferences prefs;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);

	        prefs = new ObscuredSharedPreferences(this, this.getSharedPreferences(
					MY_PREFS_FILE_NAME, Context.MODE_PRIVATE));
	        /** set the objects **/
	        iv1 = (ImageView) findViewById(R.id.ivAvatar);
	        iv1.setImageDrawable((LoadAvatar(prefs.getString("avatar",""))));
	        tv1 = (TextView) findViewById(R.id.tvNickname);
	        tv1.setText(prefs.getString("nick",""));
	        tv1 = (TextView) findViewById(R.id.tvUsername);
	        tv1.setText(prefs.getString("name",""));
	        tv1 = (TextView) findViewById(R.id.date);
	        btn1 = (Button) findViewById(R.id.btnCam);
	        btn1.setOnClickListener(this);
	        btn2 = (Button) findViewById(R.id.btnChoose);
	        btn2.setOnClickListener(this);
	        
	        /**if (checkDate()){
	        	tv1.setText("You haven't showed your love for Codebits today! Do it now! ");
	        }
	        else
	        {
	        	tv1.setText("You already showed your love for Codebits today! Comeback tomorrow :)");
	        	btn1.setEnabled(false);
	        	btn2.setEnabled(false);
	        }**/
	        
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
	    	  builder = new AlertDialog.Builder(Main.this);
		      builder.setCancelable(false);
	    	  builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			     	   dialog.cancel();
			        }
			    });
	    	 builder.setMessage("Developed by Rafael Pato (MetalWeirdo)" + "\n" + "This app is open-source , check it out at http://goo.gl/gx9Zc");
	    	 AlertDialog alert = builder.create();
			 alert.show();
	    	 return true;
		case R.id.logout:
	         prefs.edit().clear().commit();
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
			  prefs.edit().putString("picmode", "cam").commit();break;
		  case R.id.btnChoose:
			prefs.edit().putString("picmode", "choose").commit();break;
		 }
		 
		 Intent i = new Intent(Main.this, ImagePreview.class);
		 startActivity(i);
		 finish();
				 
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
	 public boolean checkDate(){
		 int date = c.get(Calendar.DAY_OF_MONTH) ;
		 if ( prefs.getInt("date", 0) == date){
			 
			 return false;
		 }
		else{
			return true;
		 }
		 
	 }
	@Override
	public void onResume(){
		if (checkDate()){
        	tv1.setText("You haven't showed your love for Codebits today! Do it now! ");
        }
        else
        {
        	tv1.setText("You already showed your love for Codebits today! Comeback tomorrow :)");
        	btn1.setEnabled(false);
        	btn2.setEnabled(false);
        }
		super.onResume();
	}
	 
	 
} 
	

