<?php
	include "valida_sesion.php";

	validaSesion();
?>

<!DOCTYPE html>
<!--
	CECADEP V1.0
	Hecho por Charle
    Última actualización: Marzo 2017
    Soporte: toledo.cervantes@outlook.com
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
		<div class="menu menu-push">
			<img class="menu__logo" alt="Logo" src="../img/Watchbot2.png">
		</div>

		<div class="menu-opciones">
			<a class="menu__link" href="inicio.php">INICIO</a>
			<a class="menu__link" href="emergencias.php">EMERGENCIAS</a>
			<a class="menu__link active" href="unidades.php">UNIDADES</a>
			<a class="menu__link" href="logout.php">SALIR</a>
		</div>

		<!--Contenido-->
		
		<div class="content__login">
			<div class="seccion__titulo">
				<h1>Agregar Unidades</h1>
			</div>
			

			<form class="contacto" enctype="multipart/form-data">

				<p>UNIDAD</p>
				<input class="form_contacto-admin" type="text" id="unidad" required/>

				<p>PASSWORD</p>
				<input class="form_contacto-admin" type="text" id="password" required/>

				<p>USUARIO</p>
				<input class="form_contacto-admin" type="text" id="usuario" required/>

				<div class="contacto__footer">
					<input class="form_contacto-enviar-admin" type="button" value="Agregar" onclick="agregarUnidad()">
				</div>
			</form>

		</div>

		<!--JavaScript-->
		<script src="../js/jquery-3.1.1.min.js"></script>
		<script src="js/funciones.js"></script>	
	</body>
</html>