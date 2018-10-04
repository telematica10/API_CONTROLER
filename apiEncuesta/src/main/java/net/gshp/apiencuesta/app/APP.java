package net.gshp.apiencuesta.app;

import android.app.Application;
import android.content.Context;

public class APP extends Application{
	
	public static Context context;
	
	@Override
	public void onCreate() {
		System.out.println("API ENCUESTA APP create");
		super.onCreate();
		context=getApplicationContext();
	}
	
}
