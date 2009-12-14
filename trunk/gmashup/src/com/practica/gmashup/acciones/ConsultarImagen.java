package com.practica.gmashup.acciones;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.practica.gmashup.bbdd.ImagenBBDD;
import com.practica.gmashup.entidades.Imagen;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class ConsultarImagen extends HttpServlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ConsultarImagen.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		Key idimagen = null;
		if(req.getParameter("idimagen")!=null)
		{
			idimagen= KeyFactory.createKey(Imagen.class.getSimpleName(), Long.parseLong(req.getParameter("idimagen")));
		}
		
		ImagenBBDD ibbdd = ImagenBBDD.get();
		Imagen i = ibbdd.obtenerImagen(idimagen);
		if (i != null) {
			res.getOutputStream().write(i.getImagen().getBytes());
			res.getOutputStream().flush();
		}
	}
}
