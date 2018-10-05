package com.alphasoluciones.antonio.api_controler;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gosharp.apis.db.DBAPI;

import net.gshp.apicontroller.Apicontroller;
import net.gshp.apicontroller.Encuesta;
import net.gshp.apicontroller.model.DAO.DAOEncuestas;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private DBAPI dbApi;
	private DAOEncuestas daoEncuestas;
	public static String pathFoto =
		Environment.getExternalStorageDirectory().getPath() + "/API_CONTROLER/";
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//android O fix bug orientation
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.id_button);
		button.setOnClickListener(this);
		createTablesEA();
	}

	public void createTablesEA() {
		dbApi = DBAPI.getInstance();
		dbApi.loadPropertiesFromFile(getApplicationContext().getResources());
		//dbApi.loadDatabaseFromXML(getApplicationContext().getResources());
		dbApi.createDB(getApplicationContext());
		daoEncuestas = new DAOEncuestas();
		Apicontroller.getInstance().setPATH_FOTO(pathFoto);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_button:
				// inicializamos la encuesta de atm
				showEncuesta(1, Encuesta.class);
				break;
		}
	}

	private void showEncuesta(long from, Class<?> clazz) {
		//Log.i("IdReporte", "" + dtoBundle.getIdReporte());
		Log.i("idEncuesta", "" + from);
		startActivity(new Intent(getApplicationContext(), clazz)
			.putExtra("idReporte", (long)1)//(long) dtoBundle.getIdReporte())
			.putExtra("idEncuesta", from)
			.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
}
