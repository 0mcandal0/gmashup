package com.practica.gmashup.bbdd;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.practica.gmashup.entidades.Imagen;

public class ImagenBBDD {
	private static final Logger log = Logger.getLogger(ImagenBBDD.class
			.getName());

	private static final ImagenBBDD instancia = new ImagenBBDD();

	private ImagenBBDD() {
	}

	public static ImagenBBDD get() {
		return instancia;
	}

	public Imagen insertarImagen(byte[] imagen) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		log.info("insertarImagen: " + user.getNickname() + ", " + imagen.length + " bytes");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Imagen i=null;
		i = new Imagen(user, new Date(), imagen);
		try {
			pm.makePersistent(i);
			
		} catch (Exception e) {
			log.severe("insertarImagen: " + e.toString());
		} finally {
			pm.close();
		}
		return i;
	}

	public Imagen obtenerImagen(Key id) {
		Imagen resultado = null;
		if (id != null) {
			log.info("obtenerImagen: " + id.getId());
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				resultado = pm.getObjectById(Imagen.class, id);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resultado = null;
			}
		}
		return resultado;

	}
}
