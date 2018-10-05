package net.gshp.apicontroller;

import java.util.List;

import net.gshp.apicontroller.adapter.ListTipoEncuestaAdapter;
import net.gshp.apicontroller.model.DAO.DAOEncuestas;
import net.gshp.apicontroller.model.DTO.EAEncuesta;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Esta es la Activity principal del modulo de encuestas. Se muestra una lista
 * de las Encuestas que se pueden o deben realizar de acuerdo a los parametros
 * recibidos en el bundle. Estos parametros deben ser: idReporte, idCanal,
 * idCliente, idPdv
 */
public class ListTipoEncuestas extends Activity implements OnItemClickListener {

	private ListView listView;
	private ListTipoEncuestaAdapter adapter;
	private List<EAEncuesta> listEncuesta;
	private DAOEncuestas dao;
	private long idReporte;
	private int idCanal;
	private int idCliente;
	private int idPdv;
	private int idRtm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tipo_encuestas);

		if (getIntent().getExtras() != null) {
			idReporte = getIntent().getExtras().getLong("idReporte");
			idCanal = getIntent().getExtras().getInt("idCanal");
			idCliente = getIntent().getExtras().getInt("idCliente");
			idPdv = getIntent().getExtras().getInt("idPdv");
			System.out.println(""+idPdv);
			idRtm = getIntent().getExtras().getInt("idRtm");
		}

		listView = (ListView) findViewById(R.id.listViewListTipoEncuesta);
		dao = new DAOEncuestas();
		listEncuesta = dao.selectTipoEncuestas(idReporte, idCanal, idCliente,
				idPdv, idRtm);
		adapter = new ListTipoEncuestaAdapter(this, listEncuesta);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		System.out.println("listencuesta size: "+listEncuesta.size());
		System.out.println("position: "+position);
		System.out.println("getIntent().getExtras())==null"+(getIntent().getExtras()==null));
		startActivity(new Intent(this, ListEncuestas.class).putExtra(
				"idEncuesta", listEncuesta.get(position).id)
				.putExtras(
				getIntent().getExtras())
				);
	}

}
