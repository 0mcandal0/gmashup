package com.practica.gmashup.acciones;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.practica.gmashup.bbdd.ImagenBBDD;
import com.practica.gmashup.entidades.Imagen;

@SuppressWarnings("serial")
public class EnviarImagen extends HttpServlet {
	private static final Logger log = Logger.getLogger(EnviarImagen.class
			.getName());

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		JSONObject json = new JSONObject();
		res.setContentType("text/html");
		
		if (user != null) {
			byte[] imagen = null;
			ServletFileUpload upload = new ServletFileUpload();
			upload.setSizeMax(80*1024);
			try {
				FileItemIterator iterator = upload.getItemIterator(req);
				while (iterator.hasNext()) {
					
					FileItemStream item = iterator.next();
					InputStream stream = item.openStream();
					if (!item.isFormField()) {
						try {
							imagen = IOUtils.toByteArray(stream);
						}
						catch(Exception e)
						{
							log.severe("EnviarImagen: "+ e.toString());
							json.put("success",false);
							json.put("error", "Error al cargar el archivo");
						}
						finally {
							IOUtils.closeQuietly(stream);
						}
					}
				}
				ImagenBBDD i = ImagenBBDD.get();
				Imagen o = i.insertarImagen(imagen);

				if(o!=null)
				{	
					log.info("Imagen insertada por el usuario "	+ user.getNickname());			
					json.put("success", true);
					json.put("id",o.getClave().getId());
				}
				else
				{
					json.put("success", false);
					json.put("error","Error al insertar el archivo en base de datos");
				}
			} 
			catch (FileUploadException e) {
				log.severe("EnviarImagen: " + e.toString());
				json.put("success",false);
				json.put("error", "El archivo supera los 80k");
			}
		}
		else
		{
			json.put("success", false);
			json.put("error","Debe acceder como un usuario válido para poder insertar imágenes");
		}
		res.getWriter().write(json.toJSONString());
	}
}