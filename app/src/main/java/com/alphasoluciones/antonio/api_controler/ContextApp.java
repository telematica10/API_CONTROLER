package com.alphasoluciones.antonio.api_controler;

import android.app.Application;
import android.content.Context;

public class ContextApp extends Application {
	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

	}
}