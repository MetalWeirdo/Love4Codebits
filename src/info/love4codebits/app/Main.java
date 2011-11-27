package info.love4codebits.app;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Main extends Activity implements OnClickListener{
	
	TextView tv1;
	ImageView iv1;
	Button btn1;
	private static final int CAMERA_PIC_REQUEST = 4146;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        iv1 = (ImageView) findViewById(R.id.ivAvatar);
	        iv1.setImageDrawable((LoadAvatar(LoadPreferences("avatar"))));
	        tv1 = (TextView) findViewById(R.id.tvNickname);
	        tv1.setText(LoadPreferences("nick"));
	        tv1 = (TextView) findViewById(R.id.tvUsername);
	        tv1.setText(LoadPreferences("name"));
	        btn1 = (Button) findViewById(R.id.btnCam);
	        btn1.setOnClickListener(this);
	        
	        
	 }
	public void onClick (View v){
		 Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		 startActivityForResult(i, CAMERA_PIC_REQUEST);
		 
		 
	 }
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		    if (requestCode == CAMERA_PIC_REQUEST) {  
		    	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");  
		    	
		    	iv1.setImageBitmap(thumbnail);   
		    }  
		}  
	 
	 private String LoadPreferences(String key){
	        SharedPreferences sharedPreferences = getSharedPreferences("LogIn",MODE_PRIVATE);
	        return (sharedPreferences.getString(key, ""));
	        
	 }
	 
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
