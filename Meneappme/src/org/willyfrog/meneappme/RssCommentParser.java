package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.Html;
import android.util.Log;

public class RssCommentParser {
	private InputStream feedUrl;
	private Comentario comentario;
	
	public RssCommentParser(){
		this.feedUrl = null;
	}
	
	public RssCommentParser(InputStream url){
			this.feedUrl = url;
		
	}
	
	public void setFeedUrl(InputStream feedUrl) {
		this.feedUrl = feedUrl;
	}

	/**
	 * Lee el input stream asignado y devuelve una lista de titulares
	 * @return
	 */
	public List<Comentario> parse(){
		final List<Comentario> comentarios = new ArrayList<Comentario>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder constructor = factory.newDocumentBuilder();
			
			//Se crea el dom
			Document dom = constructor.parse(feedUrl);
			Element root = dom.getDocumentElement();
			NodeList lista = root.getElementsByTagName("item");
			Log.d("DomParser", "Leemos el doc. Elementos: " + lista.getLength());
			
			for (int i=0; i<lista.getLength(); i++){
				Comentario comentario = new Comentario();
				Node nodo = lista.item(i);
				NodeList infoList = nodo.getChildNodes();
				
				for (int j=0; j<infoList.getLength(); j++){
					Node info = infoList.item(j);
					String eti = info.getNodeName();
					
					if (eti.equals("meneame:user")){
						Log.d("DomParser", "add autor");
						comentario.setAutor(obtenerTexto(info));
					}else if(eti.equals("meneame:order")){
						//Integer karma = Integer.getInteger(obtenerTexto(info));
						Log.d("DomParser", info.toString());
						//Log.d("DomParser", "add karma:" + karma );
						comentario.setNumber(obtenerTexto(info));
					}else if(eti.equals("description")){
						String descripcion = Html.fromHtml(obtenerTexto(info)).toString();
						Log.d("DomParser", "add negativos: " + descripcion);
						comentario.setTexto(descripcion);
					}
					else{
						Log.d("DomParser", "Sin manejar: " + info.getNodeName());
					}
				}
				comentarios.add(comentario);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("DomParser", "Error al parsear el feed: " + e.getMessage());
		}
		
		return comentarios;
	}
	
	//copiado de sgoliver.net convierte el valor de un nodo a texto
	private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();
 
        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }
 
        return texto.toString();
    }
	
}
