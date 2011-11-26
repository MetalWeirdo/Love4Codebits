package info.love4codebits.app;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class Main extends Activity{
	
	TextView tv1;
	ImageView iv1;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        iv1 = (ImageView) findViewById(R.id.ivAvatar);
	        iv1.setImageDrawable((LoadAvatar(LoadPreferences("avatar"))));
	        tv1 = (TextView) findViewById(R.id.tvNickname);
	        tv1.setText(LoadPreferences("nick"));
	        tv1 = (TextView) findViewById(R.id.tvUsername);
	        tv1.setText(LoadPreferences("name"));
	        
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
