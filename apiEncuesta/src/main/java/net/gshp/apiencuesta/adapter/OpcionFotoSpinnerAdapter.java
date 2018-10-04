package net.gshp.apiencuesta.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import net.gshp.apiencuesta.R;
import net.gshp.apiencuesta.model.DTO.EAOpcionPregunta;
import android.R.raw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class OpcionFotoSpinnerAdapter extends BaseAdapter implements
		SpinnerAdapter {

	LayoutInflater inflater;
	List<EAOpcionPregunta> listOpciones;
	Drawable defaultDrawable;
	boolean[] imageLoaded;

	public OpcionFotoSpinnerAdapter(Context context,
			List<EAOpcionPregunta> listOpciones) {
		inflater = LayoutInflater.from(context);
		this.listOpciones = listOpciones;
		defaultDrawable = context.getResources().getDrawable(
				android.R.drawable.picture_frame);
	}

	@Override
	public int getCount() {
		return listOpciones.size();
	}

	@Override
	public Object getItem(int position) {
		return listOpciones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolderImage holder;
		if (convertView == null) {
			holder = new ViewHolderImage();
			convertView = inflater.inflate(R.layout.layout_foto_row, parent,
					false);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageViewImage);
			holder.textView = (TextView) convertView
					.findViewById(R.id.textViewImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderImage) (convertView.getTag());
		}
		setImageViewImage(holder.imageView,
				((EAOpcionPregunta) getItem(position)).image);
		((TextView) convertView.findViewById(R.id.textViewImage))
				.setText(((EAOpcionPregunta) getItem(position)).opcion);
		return convertView;
	}

	private void setImageViewImage(ImageView imageView, String image) {
		System.out.println("converting: " + image);
		if (image == null) {
			imageView.setImageDrawable(defaultDrawable);
			return;
		}
		byte[] buf = (image).getBytes();

		Bitmap d;
		try {
			d = base64ToBitmap(image);
			imageView.setImageBitmap(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap base64ToBitmap(String ImageBase64) throws Exception {
		byte[] data2 = android.util.Base64.decode(ImageBase64,
				android.util.Base64.NO_WRAP);
		InputStream is = new ByteArrayInputStream(data2);
		Bitmap d = BitmapFactory.decodeStream(is);
		return d;
	}

	static class ViewHolderImage {
		public ImageView imageView;
		public TextView textView;
	}

}
