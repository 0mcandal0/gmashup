package com.practica.gmashup.entidades;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Imagen {
	@PrimaryKey    
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)    
    private Key clave;	

	@Persistent
	private User usuario;
	
	@Persistent
	private Date fechaalta;
	
	@Persistent
	private Blob imagen;
		
	public Imagen(Key clave)
	{
		this.setClave(clave);
	}
	
	public Imagen(User usuario, Date fechaalta, byte[] imagen) {
		this.setUsuario(usuario);
		this.setFechaalta(fechaalta);
		this.setImagen(new Blob(imagen));
	}

	public void setImagen(Blob imagen) {
		this.imagen = imagen;
	}

	public Blob getImagen() {
		return imagen;
	}

	public void setFechaalta(Date fechaalta) {
		this.fechaalta = fechaalta;
	}

	public Date getFechaalta() {
		return fechaalta;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setClave(Key clave) {
		this.clave = clave;
	}

	public Key getClave() {
		return clave;
	}
}
