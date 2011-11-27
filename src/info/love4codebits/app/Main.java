package info.love4codebits.app;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity implements OnClickListener{
	
	TextView tv1;
	ImageView iv1;
	Button btn1;
	Button btn2;
	Uri outputfileuri;
	
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
	        btn2 = (Button) findViewById(R.id.btnChoose);
	        btn2.setOnClickListener(this);
	        
	 }
	public void onClick (View v){
		 switch(v.getId())
		 {
		  case R.id.btnCam: 
			  Toast.makeText(this, "entrou", 9999999).show();
			  SavePreferences("picmode", "cam");break;
		  case R.id.btnChoose:
			  SavePreferences("picmode", "choose");break;
		 }
		 
		 Intent i = new Intent(Main.this, ImagePreview.class);
		 startActivity(i);
		 
				 
	 }
	
		 
	 
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
