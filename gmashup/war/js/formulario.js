/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */


Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = "side";
    
    //Objeto que va a contener la ventana flotante con los dos formularios, el datos y el de imagen
    var win;

//Formulario que va a alojar los datos (sin imagen)
    var formDatos =   new Ext.form.FormPanel({
          width:410,
          height:180,
          frame:true,
           	          	  items:[
   					          new Ext.form.TextField({id:"nombre", fieldLabel:"Nombre", width:275, allowBlank:false, blankText:"Introduce el nombre del lugar"}),
   					          new Ext.form.TextArea ({id:"comentario", fieldLabel:"Comentario", width:275, height:100}),
   					          new Ext.form.TextField({id:"latitud", hidden:true}),
   					          new Ext.form.TextField({id:"longitud", hidden:true}),
   					          new Ext.form.TextField({id:"idimagen", hidden:true})
   					       ]
   				       ,
          buttons: [
					{
						text:"Borrar", 
						handler: function(){
//Aquí se llama a la función borrarmarca de map_functions.js						
							borrarmarca();
						}
					},
                    {
                    	text:"Cancelar", 
                    	handler: function(){
                    		formDatos.getForm().reset();
                    		win.hide();
                    	}
                    },
                    {
                   	 text:"Guardar",
                   	 handler: function(){
                   	 	if (formDatos.getForm().isValid()) {
                   	 		formDatos.getForm().submit({
                   	 			url:'/enviarlocalizacion',
//Si el envío es correcto se cierra el formulario, se borra la marca estándar y se crea una extendida                   	 			
                   	 			success: function(formDatos, o){
                   	 				Ext.Msg.alert('GMashUp', 'Guardada correctamente.');
                   	 				id = Ext.util.JSON.decode(o.response.responseText).id;
//	Aquí se llama a la función borrarmarca de map_functions.js						
        							borrarmarca();
    								createMarkerID(id);
                				}, 
                				failure:function(formDatos, o){
                					Ext.Msg.alert('GMashUp', 'ERROR\n' +  Ext.util.JSON.decode(o.response.responseText).error);
                				},
                				waitTitle:'GMashUp',
                				waitMsg:'Guardando...'
                   	 		});	
                   	 	}
                   	 	else {
                   	 		Ext.Msg.alert('GMashUp', 'Revise los datos');
                   	 	}
                    	}
                    }
                 ]
       });
    
// Formulario para el envío de la imagen  
    var formImagen=new Ext.FormPanel({
    	fileUpload: true,
    	width:410,
    	frame: false,
    	height: 200,
        items:  [{
    			xtype: 'fileuploadfield',
    			id: 'form-file',
    			name: 'imagen',
    			anchor: '95%',
    			buttonText: 'Cargar Imagen...',
    			buttonOnly: true,
    			listeners: {
    	            'fileselected': function(fb, v) {
    					if (formImagen.getForm().isValid()) {
    						formImagen.getForm().submit({
    							url:'/enviarimagen',
    							waitMsg:'Enviando...',
    							waitTitle:'GMashUp',
//Si el envío es correcto se carga la imagen el un cuadro del formulario y en el formulario de datos se actualiza el id de imagen    							
                   	 			success: function(formImagen, o){
                   	 				id = Ext.util.JSON.decode(o.response.responseText).id;
                   	 				Ext.DomHelper.overwrite('srcimagen', {
                   	 		    				tag: 'img', src: '/consultarimagen?idimagen=' + id, 
                   	 		    				style:'display:block;margin-left:auto;margin-right:auto;visibility:hidden;width:150px;height:150px'
                   	 						}, true).show(true).frame();
                   	 				Ext.DomQuery.selectNode('input[id="idimagen"]').value=id;
                   	 				
                				}, 
                				failure:function(formImagen, o){
                					Ext.Msg.alert('GMashUp', 'ERROR\n' +  Ext.util.JSON.decode(o.response.responseText).error);
                					Ext.DomQuery.selectNode('input[id="idimagen"]').value=0;
                					Ext.DomQuery.selectNode('div[id="srcimagen"]').innerHTML='';
                				}
    						});
    					}
   	                }
                }
    		},
    		{
    		xtype:'box',
    		anchor:'',
    		isFormField:false,
    		autoEl:{ tag:'div', style:'margin-left: auto;margin-right: auto', id:'srcimagen'}
    		}]
    
    });

//Crea la ventana componiendo los dos formulario, el de datos y el de imagen    
    win = new Ext.Window({
        title: 'Formulario entrada de datos',
        id: 'ventanadatos',
        height: 420,
        width: 427,
        closeAction :'hide',
        resizable: false,
        closable: false,
        plain:true,
        
        items :  [formDatos, formImagen ]
	});

});
