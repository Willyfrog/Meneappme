package org.willyfrog.meneappme;

import java.net.MalformedURLException;
import java.net.URL;


public class Titular {

	private String titulo;
	private String autor;
	private URL url;
	private String dominio;
	private Integer positivos;
	private Integer negativos;
	private Integer numComentarios;
	private URL feedComentarios;
	
	public Titular(){
		this("Vacio","Anonimo","");
	}
	
	public Titular(String titulo, String autor, String dominio){
		this(titulo, autor, dominio, 0, 0, 0, null);
	}
	
	public Titular(String titulo, String autor, String url, Integer positivos, Integer negativos, Integer numComents, String comentarios){
		this.titulo = titulo;
		this.autor = autor;
		setUrl(url);
		this.positivos = positivos;
		this.negativos = negativos;
		this.numComentarios = numComents;
		setFeedComentarios(comentarios);				
	}
	
	public String getTitulo() {
		return titulo;
	}
	public String getAutor() {
		return autor;
	}
	public URL getUrl() {
		return url;
	}
	public String getDominio() {
		return dominio;
	}
	public Integer getPositivos() {
		return positivos;
	}
	public Integer getNegativos() {
		return negativos;
	}
	public Integer getNumComentarios() {
		return numComentarios;
	}
	public URL getFeedComentarios() {
		return feedComentarios;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public void setUrl(String url) {
		if (url!=null && !url.isEmpty()){
			try{
				this.url = new URL(url);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
			if (this.url != null)
				this.dominio = this.url.getHost();
			else
				this.dominio = "Desconocido";
		}
		else{
			this.url = null;
			this.dominio = "desconocido";
		}

	}

	public void setPositivos(Integer positivos) {
		this.positivos = positivos;
	}

	public void setNegativos(Integer negativos) {
		this.negativos = negativos;
	}

	public void setNumComentarios(Integer numComentarios) {
		this.numComentarios = numComentarios;
	}

	public void setFeedComentarios(String feedComentarios) {
		if (feedComentarios!=null && !feedComentarios.isEmpty()){
			try{
				this.feedComentarios = new URL (feedComentarios);
			}
			catch (MalformedURLException e) {
				throw new RuntimeException(e); //TODO: convertir a error legible por el usuario
			}
		}
		else{
			feedComentarios = null;
		}
	}
	
	
	
}
