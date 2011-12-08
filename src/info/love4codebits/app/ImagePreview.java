package info.love4codebits.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	static HttpClient client = new DefaultHttpClient();
	static HttpPost post = new HttpPost("http://love4codebits.info/rest.php");
	static String urlt = "http://love4codebits.info/rest.php";
	static String token;
	Button btn1;
	Button btn2;
	ImageView iv1;
	Bitmap pic;
	private Uri mImageUri = null;
	File photo;
	private String selectedImagePath;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgpreview);
		iv1 = (ImageView) findViewById(R.id.ivPreview);
		
		/** Getting the selected mode for image picking **/
		
		String picmode = LoadPreferences("picmode");
		if (picmode.equalsIgnoreCase("cam")) {
			cam();
		} else if (picmode == "choose") {
			gallery();
		}
	
		btn1 = (Button) findViewById(R.id.btnYes);
		btn2 = (Button) findViewById(R.id.btnNo);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		/** User confirms if that's the image they want to send to the love4codebits posterous**/
		switch (v.getId()) {
		case R.id.btnYes:
			
			break;
		case R.id.btnNo:
			finish();
			break;
		}
		
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		/** Identify the ActivityResult if it's the Cam Result or the Gallery Result**/
		 switch(requestCode){
		 /**CAM**/
		 case 0:
			 if(resultCode == RESULT_OK){
				 if (intent == null) {    
				    	iv1.setImageURI(mImageUri);
				    }
				 break;
			 }
			 else if (resultCode == RESULT_CANCELED){
				 finish();
			 }
		 /**GALLERY**/
		 case 100:
			 if(resultCode == RESULT_OK){  
				Uri selectedImageUri = intent.getData();
				selectedImagePath = getPath(selectedImageUri);
				System.out.println("Image Path : " + selectedImagePath);
				try {
					iv1.setImageBitmap(decodeUri(selectedImageUri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 else if (resultCode == RESULT_CANCELED){
				 finish();
			 }
		 }
		    // Check if the result includes a thumbnail Bitmap
		    
		  }
	/**Intent for image picking with the Gallery**/
	private void gallery(){
		 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
  		 intent.setType("image/*");
  		 startActivityForResult(intent, 100);
	}
	/**Intent for taking a photo with the Cam**/
	private void cam(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");
		mImageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(intent, 0);
	}
	
	/**Function taken from the interwebz to decode a URI and return a bitmap**/
	
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 180;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
               || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	    }
	
	  
	/** Load/Save of SharedPreferences, will change this to a single class **/

	private String LoadPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Main",MODE_PRIVATE);
        return (sharedPreferences.getString(key, ""));
        
 }
	public void getAPIToken (){
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		 pairs.add(new BasicNameValuePair("API", "GTKN"));
		try {
			postdata(pairs);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			
		
	}

	 public static void postdata(List<NameValuePair> pairs) throws UnsupportedEncodingException{
		 post.setEntity(new UrlEncodedFormEntity(pairs));
	 }
	 public String getResponse(){
		 	
			try {
				HttpResponse response = client.execute(post);
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder str = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null)
				{
				    str.append(line);
				}
				in.close();
				return str.toString();
					
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
			
	 }

}
