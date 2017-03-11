$(".responsive-options").click(function(){
	if($('.menu__opciones-responsive').hasClass('menu-activo')) {
		$(".menu__opciones-responsive").removeClass('menu-activo');
		$(".menu__opciones-responsive").css('opacity','0');
    	$(".menu__opciones-responsive").css('pointer-events','none');
	}
	else {
		$(".menu__opciones-responsive").addClass('menu-activo');
		$(".menu__opciones-responsive").css('opacity','1');
    	$(".menu__opciones-responsive").css('pointer-events','auto');	
	}
});

$(document).ready(function(){
	var ancho = $(window).width();
	if(ancho <= 600){
		$('.trigger').html('<img src="img/comprar_flecha_responsive.png">');
	}

	$(window).resize(function(){
		var ancho = $(window).width();
		if(ancho <= 600){
			$('.trigger').html('<img src="img/comprar_flecha_responsive.png">');
		}
	});
});