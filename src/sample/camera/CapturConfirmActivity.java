package sample.camera;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sample.camera.utils.BitmapUtil;
import sample.camera.utils.DateUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * ÉJÉÅÉâActivity
 * 
 * @author k-daigo
 */
public class CapturConfirmActivity extends Activity {
	private ImageView capturView;
	private Bitmap capturBitmap;

	/**
	 * Create
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.captur_confirm);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.capturView = (ImageView) findViewById(R.id.capturView);

		Intent intent = getIntent();
		
		this.capturBitmap = intent.getParcelableExtra("captur");
		this.capturView.setImageBitmap(this.capturBitmap);
	}

	/**
	 * ï€ë∂ Button click
	 * 
	 * @param view
	 */
	public void onSaveButtonClick(View view)
	{
		String filePath = DateUtil.getFormatDate("yyyyMMddhhmmssS") + ".png";
        byte[] pngData = BitmapUtil.bmp2data(this.capturBitmap, Bitmap.CompressFormat.PNG, 100);
        
        BufferedOutputStream bos = null;
    	try {
            bos = new BufferedOutputStream(openFileOutput(filePath, Context.MODE_WORLD_READABLE));
			bos.write(pngData, 0, pngData.length);
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bos != null){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Toast.makeText(this, "ï€ë∂ÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG).show();
		finish();
	}
}
