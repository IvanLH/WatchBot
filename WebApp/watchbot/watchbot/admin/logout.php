<?php
	/**
	 * @copyright Plaza Centro Sur
	 * @author Edoardo Alejandro Palazuelos Osorio (edoardo@nucliux.mx)
	 * @version 1.0 (Julio 2016)
	 * @since 1.0
	 */

	session_start();
	
	session_destroy();

	header("Location: http://localhost/watchbot/admin/");
?>
