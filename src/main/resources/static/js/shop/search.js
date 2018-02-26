/**
 * 
 */
var search = (function (map) {

	var markers = [];

	var addUser = function () {
		var parameters = location.search.substring(1).split("&");

		latUser = parameters[0].split("=")[1];
		lonUser = parameters[1].split("=")[1];

		var myLatlng = new google.maps.LatLng(parseFloat(latUser),
			parseFloat(lonUser));

		var marker = new google.maps.Marker({
			position: myLatlng,
			label: 'Você'
		});

		marker.setMap(map);
	}

	var clearMarkers = function () {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(null);
		}
		markers.length = 0;
	}

	$('#search').click(function () {
		var parameters = location.search.substring(1)
			.split("&");

		latUser = parameters[0].split("=")[1];
		lonUser = parameters[1].split("=")[1];

		productName = $('#product').val();
		distancie = $('#distance').val() / 1000;

		clearMarkers();

		$.ajax({
			type: "GET",
			url: "/shops/nearWithProduct?productName="
				+ productName + "&lat=" + latUser
				+ "&lon=" + lonUser
				+ "&distanceKm=" + distancie,
			success: function (data, textStatus, xhr) {
				if (xhr.status == 200) {
					$("#shops tbody").remove();
					
					for (i = 0; i < data.length; i++) {
						shop = data[i];
						
						var row = "<tbody><tr>";
						row += "<td style='text-align: center;'>" + shop.name +"</td>";
						row += "<td style='text-align: center;'>" + shop.description +"</td>";
						row += "<td style='text-align: center;'>" + shop.products.length +"</td>";
						if(shop.products[0].images != null){
							row += "<td style='text-align: center;'><img src='" + shop.products[0].images[0] +"' class='imgs'/></td>";
						} else {
							row += "<td style='text-align: center;'></td>";
						}
						row += "</tr></tbody>";
						
						$("#shops").append(row);
						
						nameShop = shop.name;
						latShop = shop.localization.location.coordinates[0];
						lonShop = shop.localization.location.coordinates[1];

						var shopLatLon = new google.maps.LatLng(
							parseFloat(latShop),
							parseFloat(lonShop));

						var marker = new google.maps.Marker(
							{
								position: shopLatLon,
								map: map,
								label: nameShop
							});

						markers.push(marker);
					}
				}
			},
			fail: function (jqXHR, textStatus,
				errorThrown) {
				alert("Erro ao Salvar");
			}
		});
	});

	addUser();
});