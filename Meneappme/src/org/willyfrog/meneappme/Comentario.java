package org.willyfrog.meneappme;

public class Comentario {
	String autor;
	String texto;
	String number;
	
	public Comentario() {
		this("","");
	}
	
	public Comentario(String autor, String texto) {
		this.autor = autor;
		this.texto = texto;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
	
}
