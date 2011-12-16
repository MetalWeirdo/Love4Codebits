package info.love4codebits.app;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ImageSent extends Activity implements OnClickListener {
	Calendar c = Calendar.getInstance();
	public static SharedPreferences prefs;
	public static String MY_PREFS_FILE_NAME = "info.love4codebits.app.prefs";
	Button btn1;
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgsent);
		
		prefs = new ObscuredSharedPreferences(this, this.getSharedPreferences(
				MY_PREFS_FILE_NAME, Context.MODE_PRIVATE));
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		
		prefs.edit().putInt("date", c.get(Calendar.DAY_OF_MONTH)).commit();
	 } 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
		
	}
}
