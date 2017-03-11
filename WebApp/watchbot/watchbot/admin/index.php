<!DOCTYPE html>
<!--
	CECADEP V1.0
	Hecho por Nucliux (epalazue)
    Última actualización: Julio 2016
    Soporte: soporte@nucliux.mx
-->


<html lang="es">
	<head>
		<!--Básico-->
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

		<!--Metadatos-->
		<title>Watchbot </title>
		<meta name="application-name" content="Watchbot">
		<meta name="copyright" content="Watchbot">
		<meta name="author" content="carlos toledo">
		<meta name="robots" content="index,follow">

		<!--Favicons-->

		<!--Precarga-->
		<link rel="subresource" href="../css/fonts/EuphemiaUCAS-Bold.ttf">
		<link rel="subresource" href="../css/fonts/EuphemiaUCAS-Italic.ttf">
		<link rel="subresource" href="../css/fonts/EuphemiaUCAS.ttf">

		<!--CSS-->
		<!-- <link rel="stylesheet" type="text/css" href="../css/general.css"> -->
		<link rel="stylesheet" type="text/css" href="css/admin.css">
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.12/r-2.1.0/datatables.min.css"/>

		<!--JavaScript MAIN-->

	</head>

	<body>

		<!--Menú-->
		<!-- <div class="menu menu-push">
			<img class="menu__logo" alt="Logo" src="../img/menu_logo.png">
		</div>
 -->
		<!--Contenido-->
		<div class="content__login">

			<div class="seccion__titulo">
				<img src="../img/Watchbot2.png" alt="watchbot" id="watchbot">
				<h1 id="maquinas"></h1>
			</div>

			<p id="mensaje"></p>

			<form class="contacto">
				<p>Correo</p>
				<input class="form_contacto-admin" type="email" id="correo" required>
				<p>Contraseña</p>
				<input class="form_contacto-admin" type="password" id="contrasena" required>

				<input class="form_contacto-enviar-admin" type="button" value="Ingresar" onclick="login()">				
			</form>
		</div>

		<!--JavaScript-->
		<script src="../js/jquery-3.1.1.min.js"></script>
		<script src="js/funciones.js"></script>
		<script type="text/javascript">
			function maquina(contenedor,texto,intervalo){
			   	// Calculamos la longitud del texto
			   	longitud = texto.length;
			   	// Obtenemos referencia del div donde se va a alojar el texto.
			   	cnt = document.getElementById(contenedor);
			   	var i=0;
			   	// Creamos el timer
			   	timer = setInterval(function(){
			      	// Vamos añadiendo letra por letra y la _ al final.
			      	cnt.innerHTML = cnt.innerHTML.substr(0,cnt.innerHTML.length-1) + texto.charAt(i) + "_";
			      	// Si hemos llegado al final del texto..
			      	if(i >= longitud){
			         	// Salimos del Timer y quitamos la barra baja (_)
			        	clearInterval(timer);
			         	cnt.innerHTML = cnt.innerHTML.substr(0,longitud);
			         	return true;
			      	} 
			      	else {
			         	// En caso contrario.. seguimos
			         	i++;
			      	}},intervalo);
			};

			var texto = "Bienvenido al Sistema 'Watchbot'.";
			// 100 es el intervalo de minisegundos en el que se escribirá cada letra.
			maquina("maquinas",texto,100);
		</script>
	</body>
</html>