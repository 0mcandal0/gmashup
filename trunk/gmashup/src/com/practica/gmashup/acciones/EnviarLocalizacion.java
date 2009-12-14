package com.practica.gmashup.acciones;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.practica.gmashup.bbdd.LocalizacionBBDD;
import com.practica.gmashup.entidades.Imagen;
import com.practica.gmashup.entidades.Localizacion;

@SuppressWarnings("serial")
public class EnviarLocalizacion extends HttpServlet {
	private static final Logger log = Logger.getLogger(EnviarLocalizacion.class
			.getName());

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		JSONObject json = new JSONObject();
		String jsonResponse = null;
		res.setContentType("text/html");

		if (user != null) {

			String nombre = null;
			String comentario = null;
			Key idimagen = null;
			Double longitud = 0.0;
			Double latitud = 0.0;
			try {
				nombre = req.getParameter("nombre");
				comentario = req.getParameter("comentario");
				longitud = Double.parseDouble(req.getParameter("longitud"));
				latitud = Double.parseDouble(req.getParameter("latitud"));
	
				if (req.getParameter("idimagen") != null && !req.getParameter("idimagen").isEmpty() ) {
					idimagen = KeyFactory.createKey(Imagen.class.getSimpleName(),
							Long.parseLong(req.getParameter("idimagen")));
				}
	
				LocalizacionBBDD lbbdd = LocalizacionBBDD.get();

				Localizacion l = lbbdd.insertarLocalizacion(user, nombre,
						comentario, longitud, latitud, idimagen);
				if (l != null) {
					log.info("Localización insertada por el usuario "
							+ user.getNickname() + ": " + comentario + (idimagen!=null?"Id Imagen: "+req.getParameter("idimagen"):""));
					json.put("success", true);
					json.put("id", l.getClave().getId());
					jsonResponse = json.toJSONString();

				} else {
					json.put("success", false);
					json.put("error", "No se insertó el lugar");
				}
			} catch (Exception ex) {
				json.put("success", false);
				json.put("error", "Error en el proceso de inserción");
			}
		} else {
			json.put("success", false);
			json.put("error", "Debe ser un usuario registrado");
		}
		jsonResponse = json.toJSONString();
		res.getWriter().write(jsonResponse);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}