package net.gshp.apiencuesta.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.gshp.apiencuesta.Encuesta;
import net.gshp.apiencuesta.R;
import net.gshp.apiencuesta.adapter.OpcionFotoSpinnerAdapter;
import net.gshp.apiencuesta.model.DAO.DAOEncuestas;
import net.gshp.apiencuesta.model.DTO.EAOpcionPregunta;
import net.gshp.apiencuesta.model.DTO.PreguntaRespuesta;
import net.gshp.apiencuesta.util.Util;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class NodoPregunta implements PropertyChangeListener,
		android.widget.CompoundButton.OnCheckedChangeListener {

	private List<NodoPregunta> listHijos;
	private NodoPregunta nodoPadre;
	private PreguntaRespuesta pregunta;
	private List<EAOpcionPregunta> opciones;

	private View asignedView;

	private Context context;

	private String bufferRespuesta;

	/**
	 * Constructor
	 */
	public NodoPregunta(PreguntaRespuesta pregunta, Context context) {
		this.pregunta = pregunta;
		listHijos = new ArrayList<NodoPregunta>();
		this.context = context;
		bufferRespuesta = pregunta.respuesta;
	}

	/**
	 * Agrega un hijo
	 */
	public boolean addHijo(NodoPregunta nodo) {
		if (nodo.getPregunta().parentId == 0 && getPregunta().idPregunta == 0) {
			return false;
		} else if (nodo.getPregunta().parentId == getPregunta().idPregunta) {
			listHijos.add(nodo);
			return true;
		} else if (getPregunta().parentId == nodo.getPregunta().idPregunta) {
			return nodo.addHijo(this);
		}
		return false;
	}

	/**
	 * Indica que sucedió un evento y tiene que ser cachado por los nodos que
	 * dependan de él
	 */
	private void handleEvent() {
		for (NodoPregunta nodoHijo : listHijos) {
			nodoHijo.propertyChange(new PropertyChangeEvent(this,
					"RespuestaPadre", null, getPregunta().respuesta));
		}
	}

	/**
	 * Regresa la visibilidad de la vista asignada
	 * */
	public boolean isVisible() {
		return this.asignedView.getVisibility() == View.VISIBLE;
	}

	/**
	 * Carga la respuesta a la vista
	 * */
	public void loadRespuesta() {
		if (bufferRespuesta == null) {
			return;
		}
		Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta);
		if (isVisible()) {
			String resp = bufferRespuesta;
			if (resp != null && resp.length() > 0) {
				Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
						+ " resp != null && resp.length() > 0");
				int tipo = (int) (getPregunta()).idTipoPregunta;
				switch (tipo) {
				case Encuesta.TYPE_SELECT_ONE_RADIO:
				case Encuesta.TYPE_SI_NO:
				case Encuesta.TYPE_BOOLEAN:
					RadioGroup rg = ((RadioGroup) asignedView
							.findViewById(R.id.radioGroupPregunta));
					for (int i = 0; i < ((ViewGroup) rg).getChildCount(); i++) {
						RadioButton radio = (RadioButton) rg.getChildAt(i);
						if (radio.getText().equals(resp)) {
							rg.check(radio.getId());
						}
					}
					break;
				case Encuesta.TYPE_SELECT_ONE_SPINNER:
					Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
							+ " is Spinner");
					Spinner spinner = (Spinner) asignedView
							.findViewById(R.id.spinnerPregunta);
					int position = 0;
					if (getNodoPadre() != null) {
						reLoadOptions();
						System.out.println(getPregunta().pregunta
								+ ", options.size:" + opciones.size());
					}
					for (EAOpcionPregunta opcion : opciones) {
						if (opcion.opcion.equals(resp)) {
							position = opciones.indexOf(opcion);
						}
					}
					spinner.setSelection(position);
					Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
							+ " ");
					break;
				case Encuesta.TYPE_SELECT_MULTIPLE:
					LinearLayout layout = ((LinearLayout) asignedView
							.findViewById(R.id.checkLayout));
					for (int i = 0; i < layout.getChildCount(); i++) {
						if (resp.contains("|"
								+ ((CheckBox) layout.getChildAt(i)).getText()
								+ "|")) {
							((CheckBox) layout.getChildAt(i)).setChecked(true);
						}
					}
					break;
				case Encuesta.TYPE_NUMERIC_REAL:
				case Encuesta.TYPE_NUMERIC_INTEGER:
				case Encuesta.TYPE_TEXT_AREA:
				case Encuesta.TYPE_TEXT_ROW:
				case Encuesta.TYPE_TEXT_MAIL:
				case Encuesta.TYPE_TEXT_TEL:
					((EditText) asignedView.findViewById(R.id.editTextPregunta))
							.setText(resp);
				case Encuesta.TYPE_ARCHIVO:
					break;
				case Encuesta.TYPE_FOTO:
					break;
				case Encuesta.TYPE_IMAGEN:
					break;
				default:
					break;
				}
			} else {
				setRespuesta(null);
			}
			for (NodoPregunta hijo : listHijos) {
				hijo.loadRespuesta();
			}
		}
	}

	/**
	 * Carga la respuesta a la vista
	 * */
	public void loadRespuesta(OnItemSelectedListener onItemSelectedListener,
			OnCheckedChangeListener onCheckedChangeListener,
			OnItemClickListener onItemClickListener,
			OnClickListener onClickListener,
			android.widget.CompoundButton.OnCheckedChangeListener checkListener) {
		if (bufferRespuesta == null) {
			int tipo = (int) getPregunta().idTipoPregunta;
			switch (tipo) {
			case Encuesta.TYPE_SELECT_ONE_SPINNER:
				setRespuesta(opciones.get(0).opcion);
				break;
			default:
				break;
			}
			return;
		}
		Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta);
		if (isVisible()) {
			String resp = bufferRespuesta;
			if (resp != null && resp.length() > 0) {
				Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
						+ " resp != null && resp.length() > 0");
				int tipo = (int) (getPregunta()).idTipoPregunta;
				switch (tipo) {
				case Encuesta.TYPE_SELECT_ONE_RADIO:
				case Encuesta.TYPE_BOOLEAN:
					RadioGroup rg = ((RadioGroup) asignedView
							.findViewById(R.id.radioGroupPregunta));
					for (int i = 0; i < ((ViewGroup) rg).getChildCount(); i++) {
						RadioButton radio = (RadioButton) rg.getChildAt(i);
						if (radio.getText().equals(resp)) {
							rg.check(radio.getId());
						}
					}
					break;
				case Encuesta.TYPE_SELECT_ONE_SPINNER:
					Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
							+ " is Spinner");
					Spinner spinner = (Spinner) asignedView
							.findViewById(R.id.spinnerPregunta);
					int position = 0;
					if (getNodoPadre() != null) {
						reLoadOptions();
						System.out.println(getPregunta().pregunta
								+ ", options.size:" + opciones.size());
					}
					for (EAOpcionPregunta opcion : opciones) {
						if (opcion.opcion.equals(resp)) {
							position = opciones.indexOf(opcion);
						}
					}
					spinner.setSelection(position);
					spinner.setOnItemSelectedListener(onItemSelectedListener);
					Log.i("INFO:", "Load respuesta: " + getPregunta().pregunta
							+ " ");
					break;
				case Encuesta.TYPE_SELECT_MULTIPLE:
					LinearLayout layout = ((LinearLayout) asignedView
							.findViewById(R.id.checkLayout));
					for (int i = 0; i < layout.getChildCount(); i++) {
						if (resp.contains("|"
								+ ((CheckBox) layout.getChildAt(i)).getText()
								+ "|")) {
							((CheckBox) layout.getChildAt(i)).setChecked(true);
						}
					}
					break;
				case Encuesta.TYPE_NUMERIC_REAL:
				case Encuesta.TYPE_NUMERIC_INTEGER:
				case Encuesta.TYPE_TEXT_AREA:
				case Encuesta.TYPE_TEXT_ROW:
				case Encuesta.TYPE_TEXT_MAIL:
				case Encuesta.TYPE_TEXT_TEL:
					((EditText) asignedView.findViewById(R.id.editTextPregunta))
							.setText(resp);
				case Encuesta.TYPE_ARCHIVO:
					break;
				case Encuesta.TYPE_FOTO:
					break;
				case Encuesta.TYPE_IMAGEN:
					break;
				default:
					break;
				}
			} else {
				setRespuesta(null);
			}
			for (NodoPregunta hijo : listHijos) {
				hijo.loadRespuesta(onItemSelectedListener,
						onCheckedChangeListener, onItemClickListener,
						onClickListener, checkListener);
			}
		}

	}

	/**
	 * visibility=false: Esconde la vista asignada y nulifica la respuesta
	 * visibility=true: Regresa la visibilidad
	 * */
	public void setVisible(boolean visibility) {
		if (this.asignedView == null)
			return;
		if (visibility) {
			asignedView.setVisibility(View.VISIBLE);
			setRespuesta(null);
			// if (getPregunta().idTipoPregunta ==
			// Encuesta.TYPE_SELECT_ONE_SPINNER) {
			// Spinner spinner = (Spinner) asignedView
			// .findViewById(R.id.spinnerPregunta);
			// spinner.setSelection(0);
			// }
		} else {
			System.out.println("Seting invisible pregunta: "
					+ getPregunta().idPregunta);
			asignedView.setVisibility(View.GONE);
			cleanView();
		}
	}

	/**
	 * Regresa la lista de opciones de la pregunta hay tres opciones: 1. Tiene
	 * opciones en la tabla EAOpcionPregunta 2. tiene una query asignada para ir
	 * por las opciones 3. tiene una query de dependencia(cambian sus opciones
	 * cuando cambia la respuesta del padre)
	 */
	public List<EAOpcionPregunta> getOpciones(long idPregunta,
			String queryOpciones, String queryDependencia, String respuestaPadre) {
		// System.out.println("getOpciones idPregunta: " + idPregunta);
		// System.out.println("getOpciones queryOpciones: " + queryOpciones);
		// System.out.println("getOpciones queryDependencia: " +
		// queryDependencia);
		// System.out.println("getOpciones respuestaPadre: " + respuestaPadre);

		DAOEncuestas dao = new DAOEncuestas();
		if (queryOpciones != null) {
			return dao.getOpcionesByQuery(queryOpciones);
		} else if (queryDependencia != null && respuestaPadre != null) {
			String[] param = { respuestaPadre };
			return dao.getOpcionesByQuery(queryDependencia, param);
		} else {
			return dao.getOpcionesByIDPregunta(idPregunta);
		}
	}

	/**
	 * Construye la vista asignada al nodo
	 * */
	public View constructView(OnItemSelectedListener onItemSelectedListener,
			OnCheckedChangeListener onCheckedChangeListener,
			OnItemClickListener onItemClickListener,
			OnClickListener onClickListener,
			android.widget.CompoundButton.OnCheckedChangeListener checkListener) {

		LayoutInflater inflater = LayoutInflater.from(context);
		int tipo = (int) (getPregunta()).idTipoPregunta;
		// System.out.println("tipo: " + tipo);

		switch (tipo) {
		case Encuesta.TYPE_SI_NO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup_si_no, null);
			setWebViewPregunta(asignedView, getPregunta());

			// if (getPregunta().RangoMinimo != null
			// && getPregunta().RangoMaximo != null) {
			//
			// opciones = new ArrayList<EAOpcionPregunta>();
			//
			// EAOpcionPregunta opcion = new EAOpcionPregunta();
			// opcion.opcion = getPregunta().RangoMinimo;
			// opciones.add(opcion);
			//
			// opcion = new EAOpcionPregunta();
			// opcion.opcion = getPregunta().RangoMaximo;
			// opciones.add(opcion);
			//
			// } else {
			// opciones = getOpciones(getPregunta().idPregunta,
			// getPregunta().queryOpciones,
			// getPregunta().queryOpcionesDependencia, null);
			// }
			// for (EAOpcionPregunta opcion : opciones) {
			// RadioButton rb = new RadioButton(context);
			// rb.setLayoutParams(((RadioButton) asignedView
			// .findViewById(R.id.radioSample)).getLayoutParams());
			// rb.setText(opcion.opcion);
			// rb.setTextColor(Color.BLACK);
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .addView(rb);
			// }
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_BOOLEAN:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();

				EAOpcionPregunta opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMinimo;
				opciones.add(opcion);

				opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMaximo;
				opciones.add(opcion);

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_RADIO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_SPINNER:
			asignedView = inflater.inflate(R.layout.ql_spinner, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null && getPregunta().RangoMinimo!=""
					&& getPregunta().RangoMaximo != null && getPregunta().RangoMaximo!="") {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				reLoadOptions();
			}
			ArrayList<String> opcionesNombres = new ArrayList<String>();
			for (EAOpcionPregunta opcion : opciones) {
				opcionesNombres.add(opcion.opcion);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, opcionesNombres);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setAdapter(adapter);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setTag(this);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setOnItemSelectedListener(onItemSelectedListener);
			break;
		case Encuesta.TYPE_SELECT_MULTIPLE:
			asignedView = inflater.inflate(R.layout.ql_checkbox, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {

				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				CheckBox cb = new CheckBox(context);
				cb.setLayoutParams(((CheckBox) asignedView
						.findViewById(R.id.checkBoxDefault)).getLayoutParams());
				cb.setText(opcion.opcion);
				cb.setTextColor(Color.BLACK);
				((LinearLayout) asignedView.findViewById(R.id.checkLayout))
						.addView(cb);
				cb.setOnCheckedChangeListener(checkListener);
			}
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.removeViewAt(0);
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.setTag(this);
			break;
		case Encuesta.TYPE_NUMERIC_REAL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_DECIMAL);

			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(8);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setFilters(FilterArray);

			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_NUMERIC_INTEGER:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER);

			InputFilter[] filterArrayI = new InputFilter[1];
			filterArrayI[0] = new InputFilter.LengthFilter(8);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setFilters(filterArrayI);

			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_IMAGEN:

			asignedView = inflater.inflate(R.layout.ql_spinner, null);
			setWebViewPregunta(asignedView, getPregunta());

			opciones = getOpciones(getPregunta().idPregunta,
					getPregunta().queryOpciones,
					getPregunta().queryOpcionesDependencia, null);
			OpcionFotoSpinnerAdapter adapterFoto = new OpcionFotoSpinnerAdapter(
					context, opciones);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setAdapter(adapterFoto);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setTag(this);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setOnItemSelectedListener(onItemSelectedListener);
			break;
		case Encuesta.TYPE_TEXT_AREA:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(100);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_ROW:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(1);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_MAIL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_TEL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_PHONE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_ARCHIVO:
			break;
		case Encuesta.TYPE_FOTO:
			asignedView = inflater.inflate(R.layout.ql_foto, null);
			((Button) asignedView.findViewById(R.id.buttonFoto))
					.setOnClickListener(onClickListener);
			((Button) asignedView.findViewById(R.id.buttonFoto)).setTag(this);
			break;
		default:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			break;
		}
		return asignedView;
	}

	/**
	 * Construye la vista asignada al nodo
	 * */
	public View constructViewAndLoadAnswer(
			OnItemSelectedListener onItemSelectedListener,
			OnCheckedChangeListener onCheckedChangeListener,
			OnItemClickListener onItemClickListener,
			OnClickListener onClickListener,
			android.widget.CompoundButton.OnCheckedChangeListener checkListener) {

		LayoutInflater inflater = LayoutInflater.from(context);
		int tipo = (int) (getPregunta()).idTipoPregunta;
		// System.out.println("tipo: " + tipo);

		switch (tipo) {
		case Encuesta.TYPE_SI_NO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup_si_no, null);
			setWebViewPregunta(asignedView, getPregunta());
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			if (bufferRespuesta != null) {
				if (bufferRespuesta.equals("Si")) {
					((RadioGroup) asignedView
							.findViewById(R.id.radioGroupPregunta))
							.check(R.id.RadioSi);
				} else {
					((RadioGroup) asignedView
							.findViewById(R.id.radioGroupPregunta))
							.check(R.id.RadioNo);
				}
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_BOOLEAN:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();

				EAOpcionPregunta opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMinimo;
				opciones.add(opcion);

				opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMaximo;
				opciones.add(opcion);

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_RADIO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_SPINNER:
			asignedView = inflater.inflate(R.layout.ql_spinner, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(
						getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia,
						(getNodoPadre() != null) ? getNodoPadre().getPregunta().respuesta
								: null);
			}
			ArrayList<String> opcionesNombres = new ArrayList<String>();
			int position = 0;
			int iteration = 0;
			for (EAOpcionPregunta opcion : opciones) {
				if (opcion.opcion.equals(bufferRespuesta)) {
					position = iteration;
				}
				opcionesNombres.add(opcion.opcion);
				iteration += 1;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, opcionesNombres);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setAdapter(adapter);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setTag(this);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setSelection(position, false);
			// getPregunta().respuesta=bufferRespuesta;
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setOnItemSelectedListener(onItemSelectedListener);
			break;
		case Encuesta.TYPE_SELECT_MULTIPLE:
			asignedView = inflater.inflate(R.layout.ql_checkbox, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {

				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				CheckBox cb = new CheckBox(context);
				cb.setLayoutParams(((CheckBox) asignedView
						.findViewById(R.id.checkBoxDefault)).getLayoutParams());
				cb.setText(opcion.opcion);
				cb.setTextColor(Color.BLACK);
				((LinearLayout) asignedView.findViewById(R.id.checkLayout))
						.addView(cb);
				cb.setOnCheckedChangeListener(checkListener);
			}
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.removeViewAt(0);
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.setTag(this);
			break;
		case Encuesta.TYPE_NUMERIC_REAL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_NUMERIC_INTEGER:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_IMAGEN:
			break;
		case Encuesta.TYPE_TEXT_AREA:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(100);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_ROW:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(1);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_MAIL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_TEL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_PHONE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_ARCHIVO:
			break;
		case Encuesta.TYPE_FOTO:
			asignedView = inflater.inflate(R.layout.ql_foto, null);
			((Button) asignedView.findViewById(R.id.buttonFoto))
					.setOnClickListener(onClickListener);
			((Button) asignedView.findViewById(R.id.buttonFoto)).setTag(this);
			break;
		default:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			break;
		}
		checkVisibility();
		return asignedView;
	}

	private void checkVisibility() {
		if (getNodoPadre() == null) {
			return;
		} else {
			String respuestaPadre = getNodoPadre().getPregunta().respuesta;
			if (getPregunta().operadorDependencia == null
					&& getPregunta().queryOpcionesDependencia == null) {
				setVisible(getNodoPadre().isVisible());
				return;
			}
			if (respuestaPadre == null) {
				setVisible(false);
				return;
			} else {
				// if (!isVisible())
				setVisible(true);
				if (getPregunta().operadorDependencia == null) {
					return;
				}
			}
			int tipo = (int) (getNodoPadre().getPregunta()).idTipoPregunta;
			switch (tipo) {

			case Encuesta.TYPE_SELECT_MULTIPLE:
				if (respuestaPadre.contains("|"
						+ getPregunta().valorDependencia1 + "|")) {
					setVisible(Util.evalua(getPregunta().valorDependencia1,
							getPregunta().valorDependencia1,
							getPregunta().operadorDependencia));
				} else {
					setVisible(Util.evalua(getPregunta().valorDependencia1, "",
							getPregunta().operadorDependencia));
				}

				break;
			case Encuesta.TYPE_NUMERIC_REAL:
			case Encuesta.TYPE_NUMERIC_INTEGER:
				if (getPregunta().operadorDependencia.equals("BETWEEN")) {
					setVisible(Util.evalua(
							Double.valueOf(getPregunta().valorDependencia1),
							Double.valueOf(respuestaPadre), ">")
							&& Util.evalua(Double
									.valueOf(getPregunta().valorDependencia2),
									Double.valueOf(respuestaPadre), "<"));

				} else {
					setVisible(Util.evalua(
							Double.valueOf(getPregunta().valorDependencia1),
							Double.valueOf(respuestaPadre),
							getPregunta().operadorDependencia));
				}
				break;
			case Encuesta.TYPE_SELECT_ONE_RADIO:
			case Encuesta.TYPE_BOOLEAN:
			case Encuesta.TYPE_SELECT_ONE_SPINNER:
			case Encuesta.TYPE_IMAGEN:
			case Encuesta.TYPE_TEXT_AREA:
			case Encuesta.TYPE_TEXT_ROW:
			case Encuesta.TYPE_TEXT_MAIL:
			case Encuesta.TYPE_TEXT_TEL:
			case Encuesta.TYPE_ARCHIVO:

				if (getPregunta().operadorDependencia.equals("BETWEEN")) {
					setVisible(Util.evalua(getPregunta().valorDependencia1,
							respuestaPadre, ">")
							&& Util.evalua(getPregunta().valorDependencia2,
									respuestaPadre, "<"));
				} else {
					setVisible(Util.evalua(getPregunta().valorDependencia1,
							respuestaPadre, getPregunta().operadorDependencia));
				}
				break;
			default:
				break;
			}
			if (!isVisible()) {
				getPregunta().respuesta = null;
				handleEvent();
			} else {
				if (getPregunta().queryOpcionesDependencia != null) {
					getPregunta().queryOpcionesDependencia = getPregunta().queryOpcionesDependencia
							+ respuestaPadre;
				}
			}
		}
	}

	/**
	 * Construye la vista asignada al nodo
	 * */
	public View constructView() {

		LayoutInflater inflater = LayoutInflater.from(context);
		int tipo = (int) (getPregunta()).idTipoPregunta;
		// System.out.println("tipo: " + tipo);

		switch (tipo) {
		case Encuesta.TYPE_SI_NO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup_si_no, null);
			setWebViewPregunta(asignedView, getPregunta());

			// if (getPregunta().RangoMinimo != null
			// && getPregunta().RangoMaximo != null) {
			//
			// opciones = new ArrayList<EAOpcionPregunta>();
			//
			// EAOpcionPregunta opcion = new EAOpcionPregunta();
			// opcion.opcion = getPregunta().RangoMinimo;
			// opciones.add(opcion);
			//
			// opcion = new EAOpcionPregunta();
			// opcion.opcion = getPregunta().RangoMaximo;
			// opciones.add(opcion);
			//
			// } else {
			// opciones = getOpciones(getPregunta().idPregunta,
			// getPregunta().queryOpciones,
			// getPregunta().queryOpcionesDependencia, null);
			// }
			// for (EAOpcionPregunta opcion : opciones) {
			// RadioButton rb = new RadioButton(context);
			// rb.setLayoutParams(((RadioButton) asignedView
			// .findViewById(R.id.radioSample)).getLayoutParams());
			// rb.setText(opcion.opcion);
			// rb.setTextColor(Color.BLACK);
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .addView(rb);
			// }
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_BOOLEAN:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();

				EAOpcionPregunta opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMinimo;
				opciones.add(opcion);

				opcion = new EAOpcionPregunta();
				opcion.opcion = getPregunta().RangoMaximo;
				opciones.add(opcion);

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_RADIO:
			asignedView = inflater.inflate(R.layout.ql_radiogroup, null);
			setWebViewPregunta(asignedView, getPregunta());

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(((RadioButton) asignedView
						.findViewById(R.id.radioSample)).getLayoutParams());
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeViewAt(0);
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.setTag(this);
			// ((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
			// .setOnCheckedChangeListener(onCheckedChangeListener);
			break;
		case Encuesta.TYPE_SELECT_ONE_SPINNER:
			asignedView = inflater.inflate(R.layout.ql_spinner, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			ArrayList<String> opcionesNombres = new ArrayList<String>();
			for (EAOpcionPregunta opcion : opciones) {
				opcionesNombres.add(opcion.opcion);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, opcionesNombres);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setAdapter(adapter);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setTag(this);
			// ((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
			// .setOnItemSelectedListener(onItemSelectedListener);
			break;
		case Encuesta.TYPE_SELECT_MULTIPLE:
			asignedView = inflater.inflate(R.layout.ql_checkbox, null);
			setWebViewPregunta(asignedView, getPregunta());
			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {

				opciones = getOpciones(getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia, null);
			}
			for (EAOpcionPregunta opcion : opciones) {
				CheckBox cb = new CheckBox(context);
				cb.setLayoutParams(((CheckBox) asignedView
						.findViewById(R.id.checkBoxDefault)).getLayoutParams());
				cb.setText(opcion.opcion);
				cb.setTextColor(Color.BLACK);
				((LinearLayout) asignedView.findViewById(R.id.checkLayout))
						.addView(cb);
				// cb.setOnCheckedChangeListener(checkListener);
			}
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.removeViewAt(0);
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.setTag(this);
			break;
		case Encuesta.TYPE_NUMERIC_REAL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(8);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setFilters(FilterArray);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_NUMERIC_INTEGER:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_NUMBER);
			InputFilter[] FilterArrayR = new InputFilter[1];
			FilterArrayR[0] = new InputFilter.LengthFilter(8);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setFilters(FilterArrayR);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_IMAGEN:
			break;
		case Encuesta.TYPE_TEXT_AREA:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(100);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_ROW:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setMaxLines(1);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_MAIL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_TEXT_TEL:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setInputType(InputType.TYPE_CLASS_PHONE);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setTag(this);
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.addTextChangedListener(new TextWatcherEncuesta());
			break;
		case Encuesta.TYPE_ARCHIVO:
			break;
		case Encuesta.TYPE_FOTO:
			asignedView = inflater.inflate(R.layout.ql_foto, null);
			// ((Button) asignedView.findViewById(R.id.buttonFoto))
			// .setOnClickListener(onClickListener);
			((Button) asignedView.findViewById(R.id.buttonFoto)).setTag(this);
			break;
		default:
			asignedView = inflater.inflate(R.layout.ql_textfield, null);
			setWebViewPregunta(asignedView, getPregunta());
			break;
		}
		return asignedView;
	}

	/**
	 * Borra y actualiza las opciones correspondientes a la vista cargada
	 * previamente
	 * */
	private void reLoadOptions() {
		int tipo = (int) getPregunta().idTipoPregunta;
		LayoutParams params;
		switch (tipo) {
		case Encuesta.TYPE_SELECT_ONE_RADIO:
		case Encuesta.TYPE_BOOLEAN:
			opciones = getOpciones(getPregunta().idPregunta,
					getPregunta().queryOpciones,
					getPregunta().queryOpcionesDependencia, getNodoPadre()
							.getPregunta().respuesta);
			params = ((RadioGroup) asignedView
					.findViewById(R.id.radioGroupPregunta)).getChildAt(0)
					.getLayoutParams();
			// LayoutParams params=((RadioButton) asignedView
			// .findViewById(R.id.radioSample)).getLayoutParams();
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.removeAllViews();
			for (EAOpcionPregunta opcion : opciones) {
				RadioButton rb = new RadioButton(context);
				rb.setLayoutParams(params);
				rb.setText(opcion.opcion);
				rb.setTextColor(Color.BLACK);
				((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
						.addView(rb);
			}
			break;
		case Encuesta.TYPE_SELECT_ONE_SPINNER:

			System.out
					.println("Respuesta del padre: "
							+ ((getNodoPadre() != null) ? getNodoPadre()
									.getPregunta().respuesta : null));

			if (getPregunta().RangoMinimo != null
					&& getPregunta().RangoMaximo != null) {

				opciones = new ArrayList<EAOpcionPregunta>();
				int rmin = Integer.valueOf(getPregunta().RangoMinimo);
				int rmax = Integer.valueOf(getPregunta().RangoMaximo);

				for (int i = rmin; i <= rmax; i++) {
					EAOpcionPregunta opcion = new EAOpcionPregunta();
					opcion.opcion = i + "";
					opciones.add(opcion);
				}

			} else {
				opciones = getOpciones(
						getPregunta().idPregunta,
						getPregunta().queryOpciones,
						getPregunta().queryOpcionesDependencia,
						(getNodoPadre() != null) ? getNodoPadre().getPregunta().respuesta
								: null);
			}
			ArrayList<String> opcionesNombres = new ArrayList<String>();
			System.out.println("opciones.size(): " + opciones.size());
			for (EAOpcionPregunta opcion : opciones) {
				opcionesNombres.add(opcion.opcion);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, opcionesNombres);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			((Spinner) asignedView.findViewById(R.id.spinnerPregunta))
					.setAdapter(adapter);
			break;
		case Encuesta.TYPE_SELECT_MULTIPLE:

			opciones = getOpciones(getPregunta().idPregunta,
					getPregunta().queryOpciones,
					getPregunta().queryOpcionesDependencia, getNodoPadre()
							.getPregunta().respuesta);
			params = ((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.getChildAt(0).getLayoutParams();
			((LinearLayout) asignedView.findViewById(R.id.checkLayout))
					.removeAllViews();

			for (EAOpcionPregunta opcion : opciones) {
				CheckBox cb = new CheckBox(context);
				cb.setLayoutParams(params);
				cb.setText(opcion.opcion);
				cb.setTextColor(Color.BLACK);
				((LinearLayout) asignedView.findViewById(R.id.checkLayout))
						.addView(cb);
				cb.setOnCheckedChangeListener(this);
			}
			break;
		case Encuesta.TYPE_NUMERIC_REAL:
			break;
		case Encuesta.TYPE_NUMERIC_INTEGER:
			break;
		case Encuesta.TYPE_IMAGEN:
			break;
		case Encuesta.TYPE_TEXT_AREA:
			break;
		case Encuesta.TYPE_TEXT_ROW:
			break;
		case Encuesta.TYPE_TEXT_MAIL:
			break;
		case Encuesta.TYPE_TEXT_TEL:
			break;
		case Encuesta.TYPE_ARCHIVO:
			break;
		case Encuesta.TYPE_FOTO:
			break;
		default:
			break;
		}
	}

	/**
	 * Carga el texto de la pregunta justificado y con formato
	 * */
	private void setWebViewPregunta(View view, PreguntaRespuesta pregunta) {
		((WebView) (view.findViewById(R.id.webViewPregunta))).loadData(
				Util.toStringJustified(pregunta.pregunta),
				"text/html; charset=utf-8", "utf-8");
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String respuestaPadre = (String) event.getNewValue();
		if (getPregunta().operadorDependencia == null
				&& getPregunta().queryOpcionesDependencia == null&&getPregunta().queryVisibility==null) {
			setVisible(getNodoPadre().isVisible());
			return;
		}
		if (respuestaPadre == null) {
			setVisible(false);
			return;
		} else {
			System.out.println("Pregunta"+getPregunta().idPregunta+" queryVisibility: "+getPregunta().queryVisibility);
			if (getPregunta().queryVisibility != null) {
				//TODO quitar esto es solo para coca, esta hardcodeado <--me lo compre
				String query=getPregunta().queryVisibility;
				if(getPregunta().idPregunta==148l&&getPregunta().idEncuesta==21l){
					String company=getNodoPadre().getNodoPadre().getPregunta().respuesta;
					query=query.concat(" and odsc_company=\""+company+"\"");
					System.out.println("###querytz: "+query);
				}
				System.out.println("###query: "+query);
				query=query.replace("@", respuestaPadre);
				setVisible(new DAOEncuestas()
						.evaluateQueryByRows(query));
				return;
			}
			setVisible(true);
			if (getPregunta().queryOpcionesDependencia != null) {
				reLoadOptions();
				return;
			}
		}
		int tipo = (int) (getNodoPadre().getPregunta()).idTipoPregunta;
		switch (tipo) {

		case Encuesta.TYPE_SELECT_MULTIPLE:
			if (respuestaPadre.contains("|" + getPregunta().valorDependencia1
					+ "|")) {
				setVisible(Util.evalua(getPregunta().valorDependencia1,
						getPregunta().valorDependencia1,
						getPregunta().operadorDependencia));
			} else {
				setVisible(Util.evalua(getPregunta().valorDependencia1, "",
						getPregunta().operadorDependencia));
			}

			break;
		case Encuesta.TYPE_NUMERIC_REAL:
		case Encuesta.TYPE_NUMERIC_INTEGER:
			if (getPregunta().operadorDependencia.equals("BETWEEN")) {
				setVisible(Util.evalua(
						Double.valueOf(getPregunta().valorDependencia1),
						Double.valueOf(respuestaPadre), ">")
						&& Util.evalua(
								Double.valueOf(getPregunta().valorDependencia2),
								Double.valueOf(respuestaPadre), "<"));

			} else {
				setVisible(Util.evalua(
						Double.valueOf(getPregunta().valorDependencia1),
						Double.valueOf(respuestaPadre),
						getPregunta().operadorDependencia));
			}
			break;
		case Encuesta.TYPE_SELECT_ONE_RADIO:
		case Encuesta.TYPE_SI_NO:
		case Encuesta.TYPE_BOOLEAN:
		case Encuesta.TYPE_SELECT_ONE_SPINNER:
		case Encuesta.TYPE_IMAGEN:
		case Encuesta.TYPE_TEXT_AREA:
		case Encuesta.TYPE_TEXT_ROW:
		case Encuesta.TYPE_TEXT_MAIL:
		case Encuesta.TYPE_TEXT_TEL:
		case Encuesta.TYPE_ARCHIVO:
			if (getPregunta().operadorDependencia.equals("BETWEEN")) {
				setVisible(Util.evalua(getPregunta().valorDependencia1,
						respuestaPadre, ">")
						&& Util.evalua(getPregunta().valorDependencia2,
								respuestaPadre, "<"));
			} else {
				setVisible(Util.evalua(getPregunta().valorDependencia1,
						respuestaPadre, getPregunta().operadorDependencia));
			}
			break;
		default:
			break;
		}
		if (!isVisible()) {
			getPregunta().respuesta = null;
			handleEvent();
		} else {
			if (getPregunta().queryOpcionesDependencia != null) {
				getPregunta().queryOpcionesDependencia = getPregunta().queryOpcionesDependencia
						+ respuestaPadre;
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		PreguntaRespuesta pregunta = ((NodoPregunta) ((LinearLayout) arg0
				.getParent()).getTag()).getPregunta();
		String respuesta = (pregunta.respuesta == null) ? ""
				: pregunta.respuesta;
		String text = arg0.getText() + "";
		if (arg1) {
			respuesta = respuesta.concat("|" + text + "|");
		} else {
			respuesta = respuesta.replace("|" + text + "|", "");
		}
		setRespuesta(respuesta);
		// System.out.println("Pregunta: " + pregunta.idPregunta + "Respuesta: "
		// + pregunta.respuesta + "");

	}

	/**
	 * Limpia los elementos de la vista seleccionados
	 */
	private void cleanView() {
		int tipo = (int) getPregunta().idTipoPregunta;
		switch (tipo) {

		case Encuesta.TYPE_SI_NO:
		case Encuesta.TYPE_SELECT_ONE_RADIO:
		case Encuesta.TYPE_BOOLEAN:
			((RadioGroup) asignedView.findViewById(R.id.radioGroupPregunta))
					.check(-1);
			break;
		case Encuesta.TYPE_SELECT_MULTIPLE:
			LinearLayout checkLayout = (LinearLayout) asignedView
					.findViewById(R.id.checkLayout);
			for (int i = 0; i < checkLayout.getChildCount(); i++) {
				((CheckBox) checkLayout.getChildAt(i)).setChecked(false);
			}
			break;
		case Encuesta.TYPE_SELECT_ONE_SPINNER:
			break;
		case Encuesta.TYPE_NUMERIC_REAL:
		case Encuesta.TYPE_NUMERIC_INTEGER:
		case Encuesta.TYPE_TEXT_AREA:
		case Encuesta.TYPE_TEXT_ROW:
		case Encuesta.TYPE_TEXT_MAIL:
		case Encuesta.TYPE_TEXT_TEL:
			((EditText) asignedView.findViewById(R.id.editTextPregunta))
					.setText(null);
			break;
		case Encuesta.TYPE_IMAGEN:
			break;
		case Encuesta.TYPE_ARCHIVO:
			break;
		default:
			break;
		}

	}

	class TextWatcherEncuesta implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			int tipo = (int) pregunta.idTipoPregunta;
			switch (tipo) {
			case Encuesta.TYPE_TEXT_AREA:
			case Encuesta.TYPE_TEXT_MAIL:
			case Encuesta.TYPE_TEXT_ROW:
			case Encuesta.TYPE_TEXT_TEL:
				setRespuesta(cs.toString());
				break;
			case Encuesta.TYPE_NUMERIC_INTEGER:
				try {
					if (cs.length() > 0) {
						int valor = Integer.parseInt(cs.toString());
						setRespuesta("" + cs.toString());
					} else {
						setRespuesta(null);

					}
				} catch (Exception e) {
					e.printStackTrace();
					setRespuesta(null);
				}
				break;
			case Encuesta.TYPE_NUMERIC_REAL:
				try {
					if (cs.length() > 0) {
						double valor = Double.parseDouble(cs.toString());
						setRespuesta("" + cs.toString());
					} else {
						setRespuesta(null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setRespuesta(null);
				}
				break;
			default:
				break;
			}
		}
	}

	public NodoPregunta getNodoPadre() {
		return nodoPadre;
	}

	public void setNodoPadre(NodoPregunta nodoPadre) {
		this.nodoPadre = nodoPadre;
	}

	public PreguntaRespuesta getPregunta() {
		return pregunta;
	}

	public void setPregunta(PreguntaRespuesta pregunta) {
		this.pregunta = pregunta;
	}

	public void setRespuesta(String respuesta) {
		getPregunta().respuesta = respuesta;
		handleEvent();
	}

	public void setAsignedView(View asignedView) {
		this.asignedView = asignedView;
	}

	@Override
	public String toString() {
		String s = "NodoPregunta " + getPregunta().idPregunta + ": [";
		;
		for (NodoPregunta nodo : listHijos) {
			s += (" " + nodo.getPregunta().idPregunta + " ");
		}
		s = s + "]";
		return s;
	}

	public View getAsignedView() {
		return asignedView;
	}

}
