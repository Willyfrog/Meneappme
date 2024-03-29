package org.willyfrog.meneappme;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TitularAdapter extends ArrayAdapter<Titular> {
	public static class ViewHolder {
		TextView titulo;
		TextView dominio;
		TextView autor;
		public TextView upvotes;
		public TextView downvotes;
		public TextView karma;

	}

	Activity contexto;
	List<Titular> datos;
	
	public TitularAdapter(Activity context, List<Titular> datos){
		super(context, R.layout.listitem_titular, datos);
		this.contexto = context;
		this.datos = datos;
	}

	public void setDatos(List<Titular> datos) {
		this.datos = datos;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		View item = convertView; //ya lo habiamos pasado?
		if (item == null){ //si no, generalo de nuevo
			LayoutInflater inflater = contexto.getLayoutInflater();
			item = inflater.inflate(R.layout.listitem_titular, null);
			holder = new ViewHolder();
			
			holder.titulo = (TextView) item.findViewById(R.id.LblTitulo);
			holder.dominio = (TextView) item.findViewById(R.id.LblDominio);
			holder.autor = (TextView) item.findViewById(R.id.LblAutor);
			holder.karma = (TextView) item.findViewById(R.id.LblKarma);
			holder.upvotes = (TextView) item.findViewById(R.id.LblUpVotes);
			holder.downvotes = (TextView) item.findViewById(R.id.LblDownVotes);
			
			item.setTag(holder);
		}
		else{
			holder = (ViewHolder) item.getTag();
		}
		
		TextView lblKarma = (TextView) item.findViewById(R.id.LblKarma);
		lblKarma.setText(datos.get(position).getPositivos().toString());
		
		TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
		lblTitulo.setText(datos.get(position).getTitulo());
		
		TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblDominio);
		lblSubtitulo.setText(datos.get(position).getDominio());
		
		TextView lblAutor = (TextView) item.findViewById(R.id.LblDominio);
		lblAutor.setText(datos.get(position).getAutor());
		
		TextView lblUpVotes = (TextView) item.findViewById(R.id.LblUpVotes);
		lblUpVotes.setText(datos.get(position).getPositivos().toString());
		
		TextView lblDownVotes = (TextView) item.findViewById(R.id.LblDownVotes);
		lblDownVotes.setText(datos.get(position).getNegativos().toString());
		
		return item;
	}

	
}
