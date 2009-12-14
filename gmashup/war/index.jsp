<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/png" href="favicon.png" />

<link rel="stylesheet" type="text/css" media="screen" href="/ext-3.0.3/resources/css/ext-all.css" />

<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/xformfile.css" />

<script
	src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true&amp;key=ABQIAAAAZ38e0L4wMHGyFfmrUldsJxQyttk6-EgPckH3VJMJ9ANraOnrmBT5oQ-g9vYnqQpFRcGv3T6Efnyw3A"
	type="text/javascript"></script>

<script type="text/javascript" src="/ext-3.0.3/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/ext-3.0.3/ext-all.js"></script>
<script type="text/javascript" src="/ext-3.0.3/FileUpload.js"></script>

<script type="text/javascript" src="/js/formulario.js"></script>
<script type="text/javascript" src="js/labeled_marker.js"></script>
<script type="text/javascript" src="js/map_functions.js"></script>

<title>Visor de Lugares de Esparcimiento</title>
</head>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
%>
	<body onload="initMarkers()" onunload="GUnload()" class="sidebar-right">
		<div id="toolbar">
			<p>Hola, <%=user.getNickname()%>! (<a href="<%=userService.createLogoutURL(request.getRequestURI())%>">Salir</a>)</p>
			<div>Haga clic sobre el mapa para colocar un nuevo lugar. 
				Puede arrastrarlo sobre el mapa. 
				Sólo se pueden eliminar con el botón derecho los lugares sin guardar.
			</div>
			<div>
				<a href="/doc/ProyectoMashUp.pdf">Explicación en PDF</a>
				<img src="http://code.google.com/appengine/images/appengine-noborder-120x30.gif" alt="Con la tecnología de Google App Engine" />
			</div>
		</div>
		<div id="content">
			<div id="map-wrapper"><div id="mapa"></div></div>
			<div id="sidebar"><ul id="sidebar-list"></ul></div>
		</div>
	</body>
<%
	} else {
%>
	<body>
		<div></div>
		<div>Es necesario una cuenta de Google para <a href="<%=userService.createLoginURL(request.getRequestURI())%>">Acceder</a></div>
	</body>
<%
	}
%>
</html>
