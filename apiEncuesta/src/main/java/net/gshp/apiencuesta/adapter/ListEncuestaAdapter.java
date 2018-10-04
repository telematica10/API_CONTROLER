package net.gshp.apiencuesta.adapter;

import java.util.List;

import com.gosharp.apis.db.DBAPI;
import com.gosharp.apis.db.DBObjectController;

import net.gshp.apiencuesta.R;
import net.gshp.apiencuesta.model.DTO.EAEncuesta;
import net.gshp.apiencuesta.model.DTO.EARespuesta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListEncuestaAdapter extends BaseAdapter{
	
	LayoutInflater inflater;
	List<EARespuesta> listRespuesta;
	EAEncuesta encuesta;
	
	public ListEncuestaAdapter(Context context, List<EARespuesta> listRespuesta) {
		this.listRespuesta=listRespuesta;
		inflater=LayoutInflater.from(context);
		DBObjectController controller=DBAPI.getInstance().getAbstractController(new EAEncuesta(), "EAEncuesta"); 
		if(listRespuesta.size()>0)
		encuesta = ((EAEncuesta)controller.selectById((int)listRespuesta.get(0).idEncuesta));
		System.out.println("listRespuesta.size(): "+listRespuesta.size());
		System.out.println("encuesta==null"+(encuesta==null));
	}

	@Override
	public int getCount() {
		return listRespuesta.size();
	}

	@Override
	public Object getItem(int arg0) {
		return encuesta;
	}

	@Override
	public long getItemId(int arg0) {
		return listRespuesta.get(arg0).numeroEncuesta;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView=inflater.inflate(R.layout.layout_tipo_encuesta_row, parent, false);
		}
		((TextView)convertView.findViewById(R.id.textViewTitleListEncuesta)).setText("#"+getItemId(position)+" "+(((EAEncuesta)getItem(position)).nombre));
		((TextView)convertView.findViewById(R.id.textViewDescripcionEncuesta)).setText(""+(((EAEncuesta)getItem(position)).descripcion));
		return convertView;
	}

}
