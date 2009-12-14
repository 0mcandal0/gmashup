package com.practica.gmashup;

import java.io.IOException;
import javax.servlet.http.*;

//Acceso al sistema de validaci�n de google
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class GMashUPServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();        
		User user = userService.getCurrentUser();        
		if (user != null) {            
			resp.setContentType("text/plain");            
			resp.getWriter().println("Bienvenido, " + user.getNickname());        
		} else {            
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));        
		}   
	}
}



