var map, manager;
var markers;
var centerLatitude = 42.882957307120485, centerLongitude = -8.54273110628128, startZoom = 16;
var highlightCircle;
var currentMarker;

//Función que se llama por el evento mouseover del marcador extendido
//Si el marcador tiene asociado a la imagen se consulta al servlet consultarimagen
//marcador es un marcador extendido
function createMarkerHandler(marcador) {
    return function() {
		var res='<div id="ventanapopup">';
		//Imagen
		if(marcador.imagen!=null)
		{
    		res+='<div id="imagen">';
			res+='<img src="/consultarimagen?idimagen='+marcador.imagen+'"/ alt="">';
			res+='</div>';
		}
		if(marcador.id!=null)
		{
			res+='<div>Nombre: ' + marcador.nombre + '</div>';
			res+='<div>Comentario: ' + marcador.comentario + '</div>';
			res+='<div>Autor: ' + marcador.autor + '</div>';
	 	}        	
		res+='</div>';
		marcador.openInfoWindowHtml(res);
        return false;
    };
}

//Creación de un marcador extendido individual
//Se utiliza en formulario.js el momento de guardar la localización y convertir
//el marcador de estándar a extendido
//id es el id de localización
function createMarkerID(id)
{
//Truco para recoger datos JSON utilizando el Api de Google Maps
	GDownloadUrl("/consultarlocalizacion?id="+id, 
		function(d,r) {
			if(r==200) {
//Evaluación JSON
				var marker = eval( '(' + d + ')' );
				manager.addMarker(createMarker(marker[0]),1);
			}
			else
				alert('Error al consultar localizaciones');
		}
	);
}

//Función de creación de un marcador extendido
//pointData es un objeto JSON con datos de localización
function createMarker(pointData) {
	var latlng = new GLatLng(pointData.latitud, pointData.longitud);
	var icon = new GIcon();

//Si el objeto tiene imagen, esta se coloca como imagen el marcador
	if(pointData.imagen!=null)
		icon.image='/consultarimagen?idimagen='+pointData.imagen;
	else
		icon.image = '/img/bar.png';
		
	icon.iconSize = new GSize(27, 24);
	icon.iconAnchor = new GPoint(27, 24);
	icon.infoWindowAnchor = new GPoint(25, 7);
	
	opts = {
		"icon": icon,
		"clickable": true,
		"id":pointData.id,
		"nombre":pointData.nombre,
		"comentario":pointData.comentario,
		"autor":pointData.autor,
		"fecha":pointData.fecha,
		"imagen":pointData.imagen,
		"labelOffset": new GSize(-16, -16)
	};
	
	var marker = new LabeledMarker(latlng, opts);
	var handler = createMarkerHandler(marker);
	
//Creación del evento de mouseover sobre el marcador extendido	
	GEvent.addListener(marker, "mouseover", handler);

//Esto es para añadir al listado lateral de localizaciones	
	var listItem = document.createElement('li');
	listItem.innerHTML = '<div class="label">'+pointData.nombre+'</div><a href="' + pointData.nombre + '">' + pointData.nombre + '</a>';
	listItem.getElementsByTagName('a')[0].onclick = handler;

	document.getElementById('sidebar-list').appendChild(listItem);
	return marker;
}

function windowHeight() {
	// Standard browsers (Mozilla, Safari, etc.)
	if (self.innerHeight)
		return self.innerHeight;
	// IE 6
	if (document.documentElement && document.documentElement.clientHeight)
		return document.documentElement.clientHeight;
	// IE 5
	if (document.body)
		return document.body.clientHeight;
	// Just in case. 
	return 0;
}

function handleResize() {
	var height = windowHeight() - document.getElementById('toolbar').offsetHeight - 30;
	document.getElementById('mapa').style.height = height + 'px';
	document.getElementById('sidebar').style.height = height + 'px';
}

//Para resaltar el marcador activo, se utiliza un círculo centrado en él
function highlightCurrentMarker(){
    var markerPoint = currentMarker.getPoint();

    var polyPoints = Array();

    if (highlightCircle) {
      map.removeOverlay(highlightCircle);
    }

    var mapNormalProj = G_NORMAL_MAP.getProjection();
    var mapZoom = map.getZoom();
    var clickedPixel = mapNormalProj.fromLatLngToPixel(markerPoint, mapZoom);

    var polySmallRadius = 20;

    var polyNumSides = 20;
    var polySideLength = 18;

    for (var a = 0; a<(polyNumSides+1); a++) {
	    var aRad = polySideLength*a*(Math.PI/180);
	    var polyRadius = polySmallRadius; 
     	var pixelX = clickedPixel.x + polyRadius * Math.cos(aRad);
	    var pixelY = clickedPixel.y + polyRadius * Math.sin(aRad);
	    var polyPixel = new GPoint(pixelX,pixelY);
	    var polyPoint = mapNormalProj.fromPixelToLatLng(polyPixel,mapZoom);
	    polyPoints.push(polyPoint);
    }
    // Using GPolygon(points,  strokeColor?,  strokeWeight?,  strokeOpacity?,  fillColor?,  fillOpacity?)
    highlightCircle = new GPolygon(polyPoints,"#000000",2,0.0,"#FF0000",.5);
    map.addOverlay(highlightCircle);
 }

//Función para abrir el formulario en el momento de hacer un clic sobre un marcador
//El formulario se abre al lado del marcador 
function abrirformulario(marker) {
	var v =Ext.getCmp('ventanadatos');
	v.hide(); //Primero cerrar por si estaba abierto

	//Código para calcular la posición del marcador en pixels no en latlng
	var TlcLatLng = map.fromContainerPixelToLatLng(new GPoint(0,0),true);
	var TlcDivPixel = map.fromLatLngToDivPixel(TlcLatLng);
	var pointDivPixel = map.fromLatLngToDivPixel(marker.getPoint());
	
	var p=new GPoint(pointDivPixel.x-TlcDivPixel.x,pointDivPixel.y-TlcDivPixel.y);
	
	//Posicionamiento y mostrado
	v.x= p.x+32;
	v.y= p.y+32;
	v.show();

	//Rellenado de los valores de longitud y latitud en el formulario
	Ext.DomQuery.selectNode('input[id="longitud"]').value=marker.getPoint().lng();
	Ext.DomQuery.selectNode('input[id="latitud"]').value=marker.getPoint().lat();
}

//Función para borrar la marca estándar que está actualmente activa
//Primero se borra la marca del mapa y después el círculo de selección
//Por si acaso se cierra la ventana de formulario
function borrarmarca() {
	map.removeOverlay(currentMarker);
	currentMarker=null;
	map.removeOverlay(highlightCircle);
	highlightCircle=null;
	Ext.getCmp('ventanadatos').hide();
}

//Función inicial de carga de Google Maps
function Ginit() {
	if (!GBrowserIsCompatible()) {
		  alert('Su navegador no es compatible con Google Maps.');
	}
	handleResize();
	map = new GMap(document.getElementById("mapa"));
	map.addControl(new GSmallMapControl());
	map.setCenter(new GLatLng(centerLatitude, centerLongitude), startZoom);
	map.getInfoWindow().show(); 
	map.addControl(new GMapTypeControl());
	
 
	GEvent.addListener(map, "zoomend", function() {
		  highlightCurrentMarker();	
	        });

//Con el botón derecho se borra una marca estándar	
	GEvent.addListener(map , "singlerightclick", function(pixel,tile){
		borrarmarca();
	});
	

//Al hacer clic sobre el mapa se crea un marcador estándar	
	GEvent.addListener(map, "click", function(overlay, latlng) {
		if (latlng) {
			
//Crea el marcador estándar			
			var marker = new GMarker(latlng, {draggable:true, title:'Arrastrar para cambiar de lugar. Clic derecho para borrar.'});
			currentMarker = marker;
			highlightCurrentMarker(); //Dibuja el círculo de selección
			abrirformulario(marker); //Abre el formulario
			
			GEvent.addListener(marker, "click", function(punto) {
				currentMarker = marker;
				highlightCurrentMarker();
				marker.setLatLng(punto);
				abrirformulario(marker);
			});


			GEvent.addListener(marker, "dragstart", function() {
				Ext.getCmp('ventanadatos').hide();
	            map.closeInfoWindow();
			});
			GEvent.addListener(marker, "dragend", function(punto) {
				currentMarker = marker;
				highlightCurrentMarker();
				marker.setLatLng(punto);
				abrirformulario(marker);
		    });
			map.addOverlay(marker);
        }
	});
	
	manager = new GMarkerManager(map);
	
	// This is a sorting trick, don't worry too much about it.
	markers.sort(function(a, b) { return (a.nombre > b.nombre) ? +1 : -1; }); 
	batch = [];
	for(var i=0;i< markers.length;i++) {
		batch.push(createMarker(markers[i]));
	}
	manager.addMarkers(batch,1);
	manager.refresh();
	
}

//Función inicial de carga JSON
//Utiliza la función del Api de Google Maps para la carga de los datos
function initMarkers() {
	GDownloadUrl("/consultarlocalizacion", 
		function(d,r) {
			if(r==200) {
				//d tiene el resultado devuelto por el servlet en formato JSON
				//la función eval de javascript lo convierte en un objeto manejable por javascript
				markers = eval( '(' + d + ')' );
				Ginit();
			}
			else
				alert('Error al consultar localizaciones');
		}
	);
}

window.onresize = handleResize;