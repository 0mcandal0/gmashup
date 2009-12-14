package com.practica.gmashup.acciones;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.practica.gmashup.bbdd.LocalizacionBBDD;
import com.practica.gmashup.entidades.Localizacion;

@SuppressWarnings("serial")
public class ConsultarLocalizacion extends HttpServlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger
			.getLogger(ConsultarLocalizacion.class.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		Key id = null;
		List<Localizacion> localizaciones = new ArrayList<Localizacion>();
		LocalizacionBBDD lbbdd = LocalizacionBBDD.get();
		
		if(req.getParameter("id")!=null)
		{
			id= KeyFactory.createKey(Localizacion.class.getSimpleName(), Long.parseLong(req.getParameter("id")));
			localizaciones.add(lbbdd.obtenerLocalizacion(id));		
		}
		else
			localizaciones = lbbdd.listarLocalizaciones();
			
		JSONArray localizacionJSON = new JSONArray();
		for(Localizacion lo: localizaciones)
		{
			localizacionJSON.add(lo);
		}
		res.setContentType("text/html");
		res.setHeader("Cache-Control", "no-cache"); //Necesario para que el navegador no cache el resultado
		res.getWriter().write(localizacionJSON.toJSONString());

	}
}
