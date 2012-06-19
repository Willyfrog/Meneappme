package org.willyfrog.meneappme;

import java.net.MalformedURLException;
import java.net.URL;


public class Titular {

	private String titulo;
	private String autor;
	private URL url;
	private String dominio;
	private String positivos;
	private String negativos;
	private String karma;
	private String numComentarios;
	private String descripcion;
	private URL feedComentarios;
	private String urlComentarios;
	

	public Titular(){
		this("Vacio","Anonimo","");
	}
	
	public Titular(String titulo, String autor, String dominio){
		this(titulo, autor, dominio, "-", "-", "-", "-", null);
	}
	
	public Titular(String titulo, String autor, String url, String karma, String positivos, String negativos, String numComents, String comentarios){
		this.titulo = titulo;
		this.autor = autor;
		setUrl(url);
		this.positivos = positivos;
		this.negativos = negativos;
		this.numComentarios = numComents;
		setFeedComentarios(comentarios);				
	}
	
	public String getUrlComentarios() {
		return urlComentarios;
	}

	public void setUrlComentarios(String urlComentarios) {
		this.urlComentarios = urlComentarios;
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
	public String getPositivos() {
		return positivos;
	}
	public String getNegativos() {
		return negativos;
	}
	public String getNumComentarios() {
		return numComentarios;
	}
	public URL getFeedComentarios() {
		return feedComentarios;
	}
	
	public String getKarma() {
		return karma;
	}

	public void setKarma(String karma) {
		this.karma = karma;
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

	public void setPositivos(String positivos) {
		this.positivos = positivos;
	}

	public void setNegativos(String negativos) {
		this.negativos = negativos;
	}

	public void setNumComentarios(String numComentarios) {
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
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	
}
