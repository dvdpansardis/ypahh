/**
 * 
 */
(function(search){
	var brazil = {
		lat : -23.183532,
		lng : -45.885562
	};

	var map = new google.maps.Map(document.getElementById('map'), {
		center : brazil,
		scrollwheel : true,
		zoom : 19
	});
	
	search(map);	
})(search)
