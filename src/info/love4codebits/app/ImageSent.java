package info.love4codebits.app;

import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageSent extends Activity implements OnClickListener {
	Calendar c = Calendar.getInstance();
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.imgsent);
	     setSent();
	 } 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setSent(){
			SharedPreferences sharedPreferences = getSharedPreferences("Main", MODE_PRIVATE);
	        SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.putString("date", Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
	        editor.commit();
	}
}
