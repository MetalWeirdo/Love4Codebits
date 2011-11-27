package info.love4codebits.app;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImagePreview extends Activity implements OnClickListener{
	Button bnt1;
	ImageView iv1;
	Bitmap pic;
	private Uri mImageUri = null;
	File photo;
	
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.imgpreview);
		  		  
		  String picmode = LoadPreferences("picmode");
	      if (picmode.equalsIgnoreCase("cam")){
	    	  cam();
	    	     	  
	      }
	      else if (picmode == "gallery"){
	    	  // code for gallery intent
	    	  Toast.makeText(this, "WTF!?  " + picmode, 999999);
	      }
	    
	    	 
	      	      
	      
	      
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if ((requestCode == 0) && (resultCode == Activity.RESULT_OK)) {
		    // Check if the result includes a thumbnail Bitmap
		    if (intent == null) {    
		    	iv1 = (ImageView) findViewById(R.id.ivPreview);
		    	iv1.setImageURI(mImageUri);
		    }
		  }
	}
	
	private void cam(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");
		mImageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(intent, 0);
	}
	
	  
	  

	private String LoadPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Main",MODE_PRIVATE);
        return (sharedPreferences.getString(key, ""));
        
 }

}
