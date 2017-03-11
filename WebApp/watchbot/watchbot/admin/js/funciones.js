/**
 * @copyright Plaza Centro Sur
 * @author Edoardo Alejandro Palazuelos Osorio (edoardo@nucliux.mx)
 * @version 1.0 (Julio 2016)
 * @since 1.0
 */

//Usuarios
function login(){
 	var usuario=$("#correo").val();
 	var password=$("#contrasena").val();

 	if(usuario=='' || password=='')
 		$("#mensaje").html('Ingrese todos los datos');
 	else{
 		$.ajax({
 			type: "POST",
 			url: "https://echo-mx.herokuapp.com/services/loginUnidad",
 			data: {
 				usuario: usuario,
 				password: password
 			},
 			success: function(result){
 				if(result.to==true){
 					var key='d5d54645d7948fe17bed97f18fd89acbcc598be9';
 					$.post("acceso.php",{
 						key: key
 					},function(e){
 						if(e==1)
 							window.location.href="inicio.php";
 						else
 							$("#mensaje").html('Error al iniciar sesión');
 					});
 				}
 				else
 					$("#mensaje").html('Usuario o contraseña incorrectos.');
 			},
 			error: function(){
 				$("#mensaje").html('No se pudo establecer conexión con el servidor.');
 			}
 		});
 	}
}

function emergencias(){
	$.ajax({
		type: "GET",
		url: "https://echo-mx.herokuapp.com/services/showAllEmergencies",
		success: function(result){
			for(var i=0; i<result.emergencias.length; i++){
				$("#tabla").DataTable().row.add([
					result.emergencias[i].idEmergencia,
					result.emergencias[i].fecha,
					result.emergencias[i].nombre,
					result.emergencias[i].tipo,
					result.emergencias[i].descripcion,
					result.emergencias[i].estado,
					result.emergencias[i].reporte
				]).draw();
			}
		}
	});
}

function emergenciasFiltro(tipo){
	var url;
	if (tipo == "todas"){
		url="https://echo-mx.herokuapp.com/services/showAllEmergencies";	
	}
	if (tipo == "pendientes") {
		url="https://echo-mx.herokuapp.com/services/showAllPending";
	}
	if (tipo == "concluidas") {
		url="https://echo-mx.herokuapp.com/services/showAllSolved";
	}
	

	$.ajax({
		type: "GET",
		url: url,
		success: function(result){
			for(var i=0; i<result.emergencias.length; i++){
				$("#tabla").DataTable().row.add([
					result.emergencias[i].idEmergencia,
					result.emergencias[i].fecha,
					result.emergencias[i].nombre,
					result.emergencias[i].tipo,
					result.emergencias[i].descripcion,
					result.emergencias[i].estado,
					result.emergencias[i].reporte
				]).draw();
			}
		}
	});
}

function unidades(){
	$.ajax({
		type: "GET",
		url: "https://echo-mx.herokuapp.com/services/showAllUnits",
		success: function(result){
			for(var i=0; i<result.unidades.length; i++){
				$("#tabla").DataTable().row.add([
					result.unidades[i].idUnidad,
					result.unidades[i].numeroUnidad,
					'(' + result.unidades[i].ubicacionX+','+ result.unidades[i].ubicacionY+')',
					result.unidades[i].usuario
				]).draw();
			}
		}
	});
}


function agregarUnidad(){
	var unidad = $("#unidad").val();
	var password = $("#password").val();
	var usuario = $("#usuario").val();

	$.ajax({
		type: "POST",
		url: "https://echo-mx.herokuapp.com/services/registerUnidad",
		data: {
			unidad: unidad,
			password: password,
			usuario: usuario
		},
		success: function(result){
			if(result.success==true)
				location.href="unidades.php";
			else
			{
				alert('Error al intentar crear registro.');
				location.href="unidades_crear.php";
			}
		},
		error: function(){
 			alert('No se pudo establecer conexión con el servidor.');
 		}
	});
}
