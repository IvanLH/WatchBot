<?php
	/**
	 * @copyright Plaza Centro Sur
	 * @author Edoardo Alejandro Palazuelos Osorio (edoardo@nucliux.mx)
	 * @version 1.0 (Julio 2016)
	 * @since 1.0
	 */

	$key=$_POST["key"];
	
	if($key=="d5d54645d7948fe17bed97f18fd89acbcc598be9")
	{
		session_start();

		$_SESSION["acceso"]=1;

		echo 1;
	}
	else
		echo 0;
?>