package net.gshp.apicontroller.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ReziseFoto {

	
	
	public ReziseFoto() {
		// TODO Auto-generated constructor stub
		
	}
	public Bitmap getResizedBitmap(String file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap receipt = BitmapFactory.decodeFile(file,options);
        return receipt;
    }
}
