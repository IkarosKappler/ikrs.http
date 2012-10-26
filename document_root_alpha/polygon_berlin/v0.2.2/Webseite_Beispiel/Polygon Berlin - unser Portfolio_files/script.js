$(function() {
	
	$('.masonry').masonry({
  		itemSelector: '.box',
  		columnWidth: 20,
  		isAnimated: true,
  		animationOptions: {
    		duration: 400
  		}
	});

	$('.gallery.grid a').fancybox({
		'padding': 10,
		'transitionIn': 'elastic',
		'transitionOut': 'elastic',
		'cyclic': true,
		'overlayOpacity': 0.9,
		'overlayColor': '#fff'
	});
	
	$('#socialshareprivacy').socialSharePrivacy({
		/*
		services : {
			twitter : {
				'status' : 'off'
			},
			gplus : {
				'status' : 'off'
			}
		}
		*/
	});
	
	var segmentsNum = 12;
	var maxDistance = 40;
	var minDistance = 600;
	var elems = $('.follow');
	  
	$('body').bind('mousemove', function(e) {

		elems.each(function(index) {
			$this = $(this);
			// Position des Targets
			var targetCoords = $this.offset();
			var targetWidth = $this.width();
			var targetHeight = $this.height();
			// Abstand X und Y
			var distX = targetCoords.left + targetWidth / 2 - e.pageX;
			var distY = targetCoords.top + targetHeight / 2 - e.pageY;
			// Abstand per Pythagoras
			var distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
			var imgLeft = 0;
			if(distance > maxDistance && distance < minDistance) {
				// Winkel Zwischen Target und Mouse
				var angleRad = Math.atan2(distX, distY * -1);
				// Winkel umrechnen in Werte zwischen 0 und 1
				var angleSegment = (angleRad + Math.PI) / Math.PI / 2;
				// Winkel durch Runden in ganzzahlige Werte umrechnen
				angleSegment = Math.round(angleSegment * segmentsNum);
				if (angleSegment == segmentsNum) angleSegment = 0;
				// Werte in Versatz des Bildes nach links umrechnen
				imgLeft = ((angleSegment + 1) * targetWidth) * (-1);
			}
			// Debug
			//$(this).text(imgLeft);
			// Versatz dem HG-Bild zuweisen
			$this.css('background-position', imgLeft+'px 0');
		});

	});

	
});
