package net.gshp.apicontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;

import net.gshp.apicontroller.model.EncuestaStruct;
import net.gshp.apicontroller.model.GrupoStruct;
import net.gshp.apicontroller.model.NodoPregunta;
import net.gshp.apicontroller.model.PreguntaArbol;
import net.gshp.apicontroller.model.SeccionStruct;
import net.gshp.apicontroller.model.DAO.DAOEncuestas;
import net.gshp.apicontroller.model.DTO.PreguntaRespuesta;
import net.gshp.apicontroller.util.ReziseFoto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Encuesta extends Activity  implements OnItemSelectedListener,
		OnCheckedChangeListener,
		android.widget.CompoundButton.OnCheckedChangeListener,
		OnItemClickListener, OnClickListener {

	public static final int ACTION_BACK = 1000;
	public static final int ACTION_NEXT = 1001;

	private List<PreguntaRespuesta> listPreguntaRespuesta;
	private DAOEncuestas dao;

	private long idEncuesta;
	private long idReporte;
	private int idCanal;
	private int idCliente;
	private int idPdv;
	private int idRtm;
	private int numeroEncuesta;

	private String campoExtra1;
	private String campoExtra2;

	private PreguntaArbol preguntaArbol;

	public static final int TYPE_SELECT_ONE_RADIO = 1;
	public static final int TYPE_SELECT_ONE_SPINNER = 2;
	public static final int TYPE_SELECT_MULTIPLE = 3;
	public static final int TYPE_NUMERIC_REAL = 4;
	public static final int TYPE_NUMERIC_INTEGER = 5;
	public static final int TYPE_IMAGEN = 6;
	public static final int TYPE_TEXT_AREA = 7;
	public static final int TYPE_TEXT_ROW = 8;
	public static final int TYPE_TEXT_MAIL = 9;
	public static final int TYPE_TEXT_TEL = 10;
	public static final int TYPE_ARCHIVO = 11;
	public static final int TYPE_FIRMA_DIGITAL = 12;
	public static final int TYPE_GPS = 13;
	public static final int TYPE_BATERIA = 14;
	public static final int TYPE_FOTO = 15;
	public static final int TYPE_BOOLEAN = 16;
	public static final int TYPE_SI_NO = 17;
	public static final int TIMESTAMP = 18;

	private LinearLayout scrollViewEncuestaLayout;
	private EncuestaStruct encuestaStruct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//android O fix bug orientation
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_encuesta);
		SharedPreferences sp = getSharedPreferences("APIEncuestaPreferences",
				Context.MODE_PRIVATE);
		if (sp.getBoolean("Landscape", false))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		if (getIntent().getExtras() != null) {
			idEncuesta = getIntent().getExtras().getLong("idEncuesta");
			idReporte = getIntent().getExtras().getLong("idReporte");
			idCanal = getIntent().getExtras().getInt("idCanal");
			idCliente = getIntent().getExtras().getInt("idCliente");
			idPdv = getIntent().getExtras().getInt("idPdv");
			idRtm = getIntent().getExtras().getInt("idRtm");
			numeroEncuesta = getIntent().getExtras().getInt("numeroEncuesta");

			campoExtra1 = getIntent().getExtras().getString("campoExtra1");
			campoExtra2 = getIntent().getExtras().getString("campoExtra2");

			// System.out.println("idEncuesta: " + idEncuesta + ", idReporte: "
			// + idReporte + ", idCanal: " + idCanal + ", idCliente: "
			// + idCliente + ", idPdv: " + idPdv + ", idRtm: " + idRtm
			// + ", numeroencuesta: " + numeroEncuesta);
		}
		dao = new DAOEncuestas();

		listPreguntaRespuesta = dao.selectPreguntaRespuesta(idEncuesta,
				idReporte, numeroEncuesta, campoExtra1, campoExtra2);
		scrollViewEncuestaLayout = (LinearLayout) findViewById(R.id.scrollViewEncuestaLayout);
		encuestaStruct = new EncuestaStruct(listPreguntaRespuesta);
		preguntaArbol = new PreguntaArbol(listPreguntaRespuesta, this);
		System.out.println("ARBOL: " + preguntaArbol);

		// Toast.makeText(this, "idEncuesta: " + idEncuesta, Toast.LENGTH_SHORT)
		// .show();

		if (listPreguntaRespuesta.size() > 0) {
			loadEncuesta();
			loadRespuestas();
			if (scrollViewEncuestaLayout.getChildCount() > 0) {
				// System.out.println("REQUESTFOCUS");

				if (scrollViewEncuestaLayout.getFocusedChild() != null) {
					scrollViewEncuestaLayout.getFocusedChild().clearFocus();
				}
				// ((ScrollView)scrollViewEncuestaLayout.getParent()).fullScroll(ScrollView.FOCUS_UP);

				((ScrollView) scrollViewEncuestaLayout.getParent())
						.postDelayed(new Runnable() {
							@Override
							public void run() {
								try {
									((ScrollView) scrollViewEncuestaLayout
											.getParent())
											.fullScroll(ScrollView.FOCUS_UP);
									((ScrollView) scrollViewEncuestaLayout
											.getParent()).smoothScrollTo(0, 0);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
						}, 800);
			}
		} else {
			finish();
		}
	}

	private void loadEncuesta() {
		for (Object o : encuestaStruct.getElementos()) {
			if (o.getClass().equals(String.class)) {
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.text_view_titulo, null);
				tv.setText((String) o);
				scrollViewEncuestaLayout.addView(tv);
			} else if (o.getClass().equals(PreguntaRespuesta.class)) {
				View arg1 = getPreguntaView(preguntaArbol
						.getNodo(listPreguntaRespuesta
								.indexOf((PreguntaRespuesta) o)));
				scrollViewEncuestaLayout.addView(arg1);
				//modificado para no mostrar las preguntas que dependen de otras
				PreguntaRespuesta pr=(PreguntaRespuesta) o;
				if (pr.parentId>0 && pr.operadorDependencia!=null && pr.operadorDependencia!="")
					arg1.setVisibility(View.GONE);
			} else if (o.getClass().equals(SeccionStruct.class)) {
				loadSeccion((SeccionStruct) o);
			} else {
				TextView tv = new TextView(this);
				tv.setText("OTRA COSA");
				scrollViewEncuestaLayout.addView(tv);
			}
		}
	}

	private void loadSeccion(SeccionStruct seccion) {
		for (Object o : seccion.getElementos()) {
			if (o.getClass().equals(String.class)) {
				TextView tv = new TextView(this);
				tv.setText((String) o);
				scrollViewEncuestaLayout.addView(tv);
			} else if (o.getClass().equals(PreguntaRespuesta.class)) {
				View arg1 = getPreguntaView(preguntaArbol
						.getNodo(listPreguntaRespuesta
								.indexOf((PreguntaRespuesta) o)));
				scrollViewEncuestaLayout.addView(arg1);
				//modificado para no mostrar las preguntas que dependen de otras
				PreguntaRespuesta pr=(PreguntaRespuesta) o;
				if (pr.parentId>0 && pr.operadorDependencia!=null && pr.operadorDependencia!="")
					arg1.setVisibility(View.GONE);
			} else if (o.getClass().equals(GrupoStruct.class)) {
				loadGrupo((GrupoStruct) o);
			}
		}
	}

	private void loadGrupo(GrupoStruct grupo) {
		for (Object o : grupo.getElementos()) {
			if (o.getClass().equals(String.class)) {
				TextView tv = new TextView(this);
				tv.setText((String) o);
				scrollViewEncuestaLayout.addView(tv);
			} else if (o.getClass().equals(PreguntaRespuesta.class)) {
				View arg1 = getPreguntaView(preguntaArbol
						.getNodo(listPreguntaRespuesta
								.indexOf((PreguntaRespuesta) o)));
				scrollViewEncuestaLayout.addView(arg1);
				//modificado para no mostrar las preguntas que dependen de otras
				PreguntaRespuesta pr=(PreguntaRespuesta) o;
				if (pr.parentId>0 && pr.operadorDependencia!=null && pr.operadorDependencia!="")
					arg1.setVisibility(View.GONE);
			}
		}
	}

	private void loadRespuestas() {
		// System.out.println("load respuestas");
		for (int i = 0; i < listPreguntaRespuesta.size(); i++) {
			preguntaArbol.getNodo(i).loadRespuesta();
		}
	}

	private View getPreguntaView(NodoPregunta nodo) {
		return nodo.constructView(this, this, this, this, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			preguntaArbol.getNodo(requestCode).setRespuesta(null);
		} else {
			if (pathActual != null) {
				File f = new File(pathActual);
				if (f.exists()) {
					ReziseFoto rz = new ReziseFoto();
					Bitmap bitmap = rz.getResizedBitmap(pathActual);
					try {
						FileOutputStream out = new FileOutputStream(pathActual);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					} catch (Exception e) {
						Toast.makeText(
								getApplicationContext(),
								"Error en cargar la imagen, intentelo de nuevo",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					System.out.println("Nullificando path");
					preguntaArbol.getNodo(requestCode).setRespuesta(null);
				}
			}
		}
		// for (PreguntaRespuesta pregunta : listPreguntaRespuesta) {
		// System.out.println("preguntarespuesta: " + pregunta);
		// }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == -1) {
			((NodoPregunta) group.getTag()).setRespuesta(null);
		} else {
			((NodoPregunta) group.getTag()).setRespuesta(((RadioButton) group
					.findViewById(checkedId)).getText() + "");
			System.out.println("RESP: "
					+ ((RadioButton) group.findViewById(checkedId)).getText()
					+ "");
		}
		// for (PreguntaRespuesta pregunta : listPreguntaRespuesta) {
		// System.out.println("pregunta: " + pregunta.idPregunta
		// + ", respuesta: " + pregunta.respuesta);
		// }
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		System.out
				.println("Spinner pregunta: "
						+ (((NodoPregunta) (((Spinner) arg0).getTag()))
								.getPregunta().pregunta));
		Log.i("INFO:", "Spinner ItemSelected: " + position);
		System.out.println("spinnerChilds: " + arg0.getCount());
		if (arg0.getCount() < 1 || arg1 == null) {
			return;
		}
		Log.i("INFO:", "Spinner ItemSelected: " + ((TextView) arg1).getText()
				+ ", position: " + position);
		((NodoPregunta) arg0.getTag()).setRespuesta(((TextView) arg1).getText()
				+ "");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		NodoPregunta nodo = ((NodoPregunta) ((LinearLayout) arg0.getParent())
				.getTag());
		PreguntaRespuesta pregunta = nodo.getPregunta();
		String respuesta = (pregunta.respuesta == null) ? ""
				: pregunta.respuesta;
		String text = arg0.getText() + "";
		if (arg1) {
			respuesta = respuesta.concat("|" + text + "|");
		} else {
			respuesta = respuesta.replace("|" + text + "|", "");
		}
		if (respuesta.length() > 1) {
			nodo.setRespuesta(respuesta);
		} else {
			nodo.setRespuesta(null);
		}
		// System.out.println("Pregunta: " + pregunta.idPregunta + "Respuesta: "
		// + nodo.getPregunta().respuesta + "");
		// for (PreguntaRespuesta preguntac : listPreguntaRespuesta) {
		// System.out.println("pregunta: " + preguntac.idPregunta
		// + ", respuesta: " + preguntac.respuesta);
		// }
	}

	String pathActual;

	@Override
	public void onClick(View v) {

		NodoPregunta nodo = (NodoPregunta) v.getTag();
		PreguntaRespuesta pregunta = nodo.getPregunta();
		int position = listPreguntaRespuesta.indexOf(pregunta);
		pathActual = Apicontroller.getInstance().getPATH_FOTO()
				+ System.currentTimeMillis() + ".jpg";
		File file = new File(pathActual);
		Uri outputFileUri = Uri.fromFile(file);
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		nodo.setRespuesta("" + pathActual);
		startActivityForResult(intent, position);
	}

	@Override
	public void onBackPressed() {
		// Intent intent = new Intent();
		// intent.putExtras(getIntent().getExtras());
		// intent.putExtra("ListaPreguntas",
		// (Serializable) listPreguntaRespuesta);
		// intent.putExtra("ACTION", ACTION_BACK);
		// intent.setAction("net.gshp.encuesta.broadcast");
		// sendBroadcast(intent);
		finish();
	}

	public void onClickGuardar(View view) {
		for (int i = 0; i < listPreguntaRespuesta.size(); i++) {
			NodoPregunta notdo = preguntaArbol.getNodo(i);
			if (!notdo.isVisible()) {
				notdo.getPregunta().respuesta = null;
			} else {
				if(!isData(notdo.getPregunta().respuesta) && notdo.getPregunta().obligatoria) {
					notdo.getAsignedView().setFocusable(true);
					notdo.getAsignedView().setFocusableInTouchMode(true);
					notdo.getAsignedView().requestFocus();
					if (notdo.getPregunta().idTipoPregunta == TYPE_FOTO) {
						Toast.makeText(
								this,
								"Foto obligatoria, por favor tome una fotografía.",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this,
								"Pregunta obligatoria, por favor contestela",
								Toast.LENGTH_SHORT).show();
					}
					return;
				} else {
					if (notdo.getPregunta().idTipoPregunta == (long) TYPE_NUMERIC_REAL
							|| notdo.getPregunta().idTipoPregunta == (long) TYPE_NUMERIC_INTEGER) {
						double resp = Double
								.valueOf(notdo.getPregunta().respuesta);
						if (notdo.getPregunta().RangoMinimo != null) {
							double rmin = Double
									.valueOf(notdo.getPregunta().RangoMinimo);
							;
							System.out.println("rmin: " + rmin);
							if (resp < rmin) {
								notdo.getAsignedView().setFocusable(true);
								notdo.getAsignedView().setFocusableInTouchMode(
										true);
								notdo.getAsignedView().requestFocus();
								Toast.makeText(
										this,
										"Verifique su respuesta!. El rango mínimo es: "
												+ rmin, Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (notdo.getPregunta().RangoMaximo != null) {
							double rmax = Double
									.valueOf(notdo.getPregunta().RangoMaximo);
							;
							System.out.println("rmax: " + rmax);
							if (resp > rmax) {
								notdo.getAsignedView().setFocusable(true);
								notdo.getAsignedView().setFocusableInTouchMode(
										true);
								notdo.getAsignedView().requestFocus();
								Toast.makeText(
										this,
										"Verifique su respuesta!. El rango máximo es: "
												+ rmax, Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
					}
				}
			}
		}

		if (dao.persistRespuestas(listPreguntaRespuesta, numeroEncuesta,
				(int) idReporte, campoExtra1, campoExtra2)) {
			Intent intent = new Intent();
			intent.putExtras(getIntent().getExtras());
			intent.putExtra("ListaPreguntas",
					(Serializable) listPreguntaRespuesta);
			intent.setAction("net.gshp.encuesta.broadcast");
			intent.putExtra("ACTION", ACTION_NEXT);
			//el bradcast sera accesible unicamente para receivers dentro del mismo proceso
			//que creo la encuesta (bug coca)
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			finish();
		} else {
			Toast.makeText(this, "Error al guardar!", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	/**
	 * Método que revisa si el campo de texto sobre un edittext tiene datos.
	 * @param info - cadena de texto que contiene la respuesta a una pregunta cualquiera de un edittext.
	 * @return boolean - true si tiene datos : false si no tiene datos.
	*/
	private boolean isData(String info) {
		if(info == null || info.trim().length() == 0) {
			return false;
		} else {
			return true;
		}
	}
}