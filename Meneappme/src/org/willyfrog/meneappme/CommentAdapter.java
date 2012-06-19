package org.willyfrog.meneappme;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<Comentario> {
	public static class ViewHolder {
		TextView texto;
		TextView autor;
		TextView number;
	}

	Activity contexto;
	List<Comentario> datos;
	
	public CommentAdapter(Activity context, List<Comentario> datos){
		super(context, R.layout.listitem_comment, datos);
		this.contexto = context;
		this.datos = datos;
	}

	public void setDatos(List<Comentario> datos) {
		this.datos = datos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		View item = convertView; //ya lo habiamos pasado?
		if (item == null){ //si no, generalo de nuevo
			LayoutInflater inflater = contexto.getLayoutInflater();
			item = inflater.inflate(R.layout.listitem_comment, null);
			holder = new ViewHolder();
			
			holder.texto = (TextView) item.findViewById(R.id.LblTexto);
			holder.autor = (TextView) item.findViewById(R.id.LblAutor);
			holder.number = (TextView) item.findViewById(R.id.LblNumber);
			
			item.setTag(holder);
		}
		else{
			holder = (ViewHolder) item.getTag();
		}
		
		TextView lblNumber = (TextView) item.findViewById(R.id.LblNumber);
		lblNumber.setText(datos.get(position).getNumber().toString());
		
		TextView lblTexto = (TextView) item.findViewById(R.id.LblTexto);
		lblTexto.setText(datos.get(position).getTexto());
		
		TextView lblAutor = (TextView) item.findViewById(R.id.LblAutorComment);
		lblAutor.setText(datos.get(position).getAutor());
		
		return item;
	}

	
}
