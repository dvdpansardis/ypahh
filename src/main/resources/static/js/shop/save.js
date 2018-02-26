
function initAutocomplete() {
	autocomplete = new google.maps.places.Autocomplete(

		(document.getElementById('addressShop')), {
			types: ['geocode']
		});
};

$(document)
	.ready(
		function () {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCUmHkeCKoJyirlqfHTkexfz3q1kcZzv7c&libraries=places&callback=initAutocomplete';
			script.async = true;
			script.defer = true;

			document.body.appendChild(script);

		});

$('.timepicker').pickatime({
	default: 'now',
	fromnow: 0,
	twelvehour: false,
	donetext: 'OK',
	cleartext: 'Clear',
	canceltext: 'Cancel',
	autoclose: false,
	ampmclickable: true,
	aftershow: function () { }
});

$('#save').click(function () {

	var data = {
		name: $('#name').val(),
		description: $('#description').val(),
		localization: {
			address: $('#addressShop').val()
		},
		contact: {
			telephone: $('#telephone').val(),
			whatsup: $('#whatsup').val(),
			email: $('#email').val(),
			openTime: $('#openTime').val(),
			closeTime: $('#closeTime').val()
		}
	};

	$.ajax({
		type: "POST",
		url: "/shops",
		contentType: "application/json;charset=UTF-8",
		data: JSON.stringify(data),
		dataType: "json",
		complete: function (xhr, textStatus) {
			if(xhr.status == 201){
				alert("Salvo com Sucesso.");
			}
		},
		fail: function (jqXHR, textStatus, errorThrown) {
			alert("Erro ao Salvar");
		}
	});

});

