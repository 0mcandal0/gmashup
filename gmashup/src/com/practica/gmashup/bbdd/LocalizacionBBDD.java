package com.practica.gmashup.bbdd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.practica.gmashup.entidades.Localizacion;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

public class LocalizacionBBDD {
	private static final Logger log = Logger.getLogger(LocalizacionBBDD.class.getName());

	private static final LocalizacionBBDD instancia = new LocalizacionBBDD();
	
	private LocalizacionBBDD() {}
	
	public static LocalizacionBBDD get() {
		return instancia;
	}
	
	@SuppressWarnings("unchecked")
	public List<Localizacion> obtenerLocalizacion(User nombreautor) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Localizacion.class);
		if(nombreautor!=null)
		{
			query.setFilter("autor == nombreautor");
			query.declareParameters("User nombreautor");
		}

		List<Localizacion> resultado = new ArrayList<Localizacion>();
		try {
			resultado = (List<Localizacion>) query.execute(nombreautor);
		} catch (Exception e) {
			log.warning("listadoLocalizacionesAutor: " + e.toString());
		}finally {
			query.closeAll();
		}
		return resultado;
	}
	
	public List<Localizacion> listarLocalizaciones() {
		return obtenerLocalizacion((User)null);
	}

	public Localizacion obtenerLocalizacion(Key id) {
		Localizacion resultado = null;
		if (id != null) {
			log.info("listarLocalizacion: " + id.getId());
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				resultado = pm.getObjectById(Localizacion.class, id);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				resultado = null;
			}
		}
		return resultado;
	}
	
	public Localizacion insertarLocalizacion(User nombreautor, String nombrelocalizacion,
					String comentario, Double longitud, Double latitud, Key idimagen)
	{
		log.info("InsertarLocalizacion: " + nombreautor.getNickname() + ", " + nombrelocalizacion + ", " + comentario);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Localizacion l=null;
		if(nombrelocalizacion != null)
		{
			l = new Localizacion(nombreautor,nombrelocalizacion,comentario,longitud,latitud, new Date(), idimagen);
			try {
				pm.makePersistent(l);
			} 
			catch(Exception e)
			{
				log.severe("Error:" + e.toString());						
			}
			finally {
				pm.close();
			}
		}
		return l;
	}
}