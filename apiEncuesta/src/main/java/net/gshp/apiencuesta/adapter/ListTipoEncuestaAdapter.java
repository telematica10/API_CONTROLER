package net.gshp.apiencuesta.adapter;

import java.util.List;

import net.gshp.apiencuesta.R;
import net.gshp.apiencuesta.model.DTO.EAEncuesta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListTipoEncuestaAdapter extends BaseAdapter {

	private List<EAEncuesta> listencuesta;
	private LayoutInflater inflater;

	public ListTipoEncuestaAdapter(Context context, List<EAEncuesta> listencuesta) {
		this.listencuesta = listencuesta;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listencuesta.size();
	}

	@Override
	public Object getItem(int position) {
		return listencuesta.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((EAEncuesta) listencuesta.get(position)).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView=inflater.inflate(R.layout.layout_tipo_encuesta_row, parent, false);
		}
		((TextView)convertView.findViewById(R.id.textViewTitleListEncuesta)).setText(""+(((EAEncuesta)getItem(position)).nombre));
		((TextView)convertView.findViewById(R.id.textViewDescripcionEncuesta)).setText(""+(((EAEncuesta)getItem(position)).descripcion));
		return convertView;
	}

}
