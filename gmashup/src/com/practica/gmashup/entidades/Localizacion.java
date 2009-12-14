package com.practica.gmashup.entidades;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Localizacion implements JSONAware {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key clave;
	
	@Persistent
	private User autor;
	@Persistent
	private String nombre;
	@Persistent
	private Double longitud;
	@Persistent
	private Double latitud;
	@Persistent
	private String comentario;
	@Persistent
	private Date fechaalta;

	// http://code.google.com/intl/es/appengine/docs/java/datastore/relationships.html#Owned_One_to_One_Relationships
	@Persistent
	private Key idimagen;


	// private Blob imagen;

	public Localizacion(User autor, String nombre, String comentario,
			Double longitud, Double latitud, Date fechaalta, Key imagen) {
		this.setAutor(autor);
		this.setNombre(nombre);
		this.setComentario(comentario);
		this.setLongitud(longitud);
		this.setLatitud(latitud);
		this.setFechaalta(fechaalta);
		this.setIdimagen(imagen);
	}

	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		sb.append("\"id\""); sb.append(":"); sb.append(this.clave.getId());
		sb.append(",");
		
		sb.append("\"nombre\"");	sb.append(":");sb.append("\"" + JSONObject.escape(this.nombre)+"\"" );sb.append(",");
		
		if(this.idimagen!=null)
		{
			sb.append("\"imagen\""); sb.append(":");sb.append(this.idimagen.getId());sb.append(",");
		}
		
		String fecha = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(this.fechaalta);
		
		
		sb.append("\"fecha\""); sb.append(":"); sb.append("\"" + fecha + "\"");sb.append(",");
		
		sb.append("\"longitud\""); sb.append(":"); sb.append(this.longitud);sb.append(",");
		
		sb.append("\"latitud\""); sb.append(":"); sb.append(this.latitud);sb.append(",");
		
		sb.append("\"comentario\""); sb.append(":");	sb.append("\"" + JSONObject.escape(this.comentario)+"\"");	sb.append(",");
		
		sb.append("\"autor\""); sb.append(":");	sb.append("\"" + JSONObject.escape(this.getAutor().getNickname()) + "\"" );
				
		sb.append("}");
				
		return sb.toString();
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setFechaalta(Date fechaalta) {
		this.fechaalta = fechaalta;
	}

	public Date getFechaalta() {
		return fechaalta;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}

	public User getAutor() {
		return autor;
	}

	public void setClave(Key clave) {
		this.clave = clave;
	}

	public Key getClave() {
		return clave;
	}

	public void setIdimagen(Key idimagen) {
		this.idimagen = idimagen;
	}

	public Key getIdimagen() {
		return idimagen;
	}
}
