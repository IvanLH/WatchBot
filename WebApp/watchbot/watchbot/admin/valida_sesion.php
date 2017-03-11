<?php
	/**
	 * @copyright Plaza Centro Sur
	 * @author Edoardo Alejandro Palazuelos Osorio (edoardo@nucliux.mx)
	 * @version 1.0 (Julio 2016)
	 * @since 1.0
	 */

	function validaSesion()
	{
		session_start();

		if(!isset($_SESSION["acceso"])) 
		{
			header('Location: http://localhost/watchbot/admin/');
		}
	}
?>