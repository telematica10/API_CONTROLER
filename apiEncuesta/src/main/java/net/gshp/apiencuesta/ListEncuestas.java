package net.gshp.apiencuesta;

import java.util.List;

import net.gshp.apiencuesta.adapter.ListEncuestaAdapter;
import net.gshp.apiencuesta.model.DAO.DAOEncuestas;
import net.gshp.apiencuesta.model.DTO.EAEncuesta;
import net.gshp.apiencuesta.model.DTO.EARespuesta;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gosharp.apis.db.DBAPI;
import com.gosharp.apis.db.DBObjectController;

public class ListEncuestas extends Activity implements OnItemClickListener{

	private long idEncuesta;
	private long idReporte;
	private int idCanal;
	private int idCliente;
	private int idPdv;
	private int idRtm;

	private int numeroSiguienteEncuesta;
	private int repeticiones;

	private List<EARespuesta> listRespuesta;
	private DAOEncuestas dao;

	private ListEncuestaAdapter adapter;

	private ListView listViewEncuestas;
	private TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_encuestas);
		if (getIntent().getExtras() != null) {
			idEncuesta = getIntent().getExtras().getLong("idEncuesta");
			idReporte = getIntent().getExtras().getLong("idReporte");
			idCanal = getIntent().getExtras().getInt("idCanal");
			idCliente = getIntent().getExtras().getInt("idCliente");
			idPdv = getIntent().getExtras().getInt("idPdv");
			idRtm = getIntent().getExtras().getInt("idRtm");
			System.out.println("idEncuesta: " + idEncuesta + ", idReporte: "
					+ idReporte + ", idCanal: " + idCanal + ", idCliente: "
					+ idCliente + ", idPdv: " + idPdv + ", idRtm: " + idRtm);
		}

//		buttonGenerar.findViewById(R.id.buttonGenerar);
		listViewEncuestas = (ListView) findViewById(R.id.listViewEncuestas);
		textView1=(TextView)findViewById(R.id.textView1);
		dao = new DAOEncuestas();
		
		listViewEncuestas.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		listRespuesta = dao.selectEncuestasbyNumero(idEncuesta, idReporte);

		if (listRespuesta.size() > 0) {
			numeroSiguienteEncuesta = (listRespuesta
					.get(listRespuesta.size() - 1).numeroEncuesta) + 1;
		} else {
			numeroSiguienteEncuesta = 0;
		}
		DBObjectController controller=DBAPI.getInstance().getAbstractController(new EAEncuesta(), "EAEncuesta"); 
		repeticiones = ((EAEncuesta)controller.selectById((int)idEncuesta)).repeticiones;
		adapter = new ListEncuestaAdapter(this, listRespuesta);
		listViewEncuestas.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		startActivity(new Intent(this, net.gshp.apiencuesta.Encuesta.class).putExtra(
				"numeroEncuesta",
				listRespuesta.get(arg2).numeroEncuesta)
				.putExtras(getIntent().getExtras()));
	}

	public void onClickGenerar(View view) {
		if (repeticiones == 0 ||repeticiones > listRespuesta.size()) {
			startActivity(new Intent(this, net.gshp.apiencuesta.Encuesta.class).putExtra(
					"numeroEncuesta", numeroSiguienteEncuesta).putExtras(
					getIntent().getExtras()));
		} else {
			Toast.makeText(this, "No puede generar una nueva encuesta",
					Toast.LENGTH_SHORT).show();
		}
	}
}
