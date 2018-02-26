/**
 * 
 */
(function() {
	var idShop;
	var idProduct = null;
	var picture;
	$(document).ready(function() {

		var parameters = location.search.substring(1).split("&");

		idShop = parameters[0].split("=")[1];
		if(parameters.length > 1){
			idProduct = parameters[1].split("=")[1];
			getProduct();
		}

	});
	
	var getProduct = function() {
		$.ajax({
			type : "GET",
			url : "/shops/" + idShop + "/products/" + idProduct,
			success : function(data, textStatus, xhr) {
				if (xhr.status == 200) {
					loadForm(data);
				}
			},
			fail : function(jqXHR, textStatus, errorThrown) {
				alert("Erro ao Salvar");
			}
		});
	};
	
	var loadForm = function(data) {
		prod = data.products[0];
	
		$('#code').val(prod.code);
		$('#name').val(prod.name);
		$('#description').val(prod.description);
		if(prod.images != null){
			$('#imgProd').attr('src', prod.images[0]);
		}
	}

	$('#save').click(function() {

		if(idProduct != null){
			var data =  {
				code : $('#code').val(),
				name : $('#name').val(),
				images: [picture],
				description : $('#description').val()
			} ;
			
			$.ajax({
				type : "PUT",
				url : "/shops/" + idShop + "/products/" + idProduct,
				contentType : "application/json;charset=UTF-8",
				data : JSON.stringify(data),
				dataType : "json",
				complete : function(xhr, textStatus) {
					if (xhr.status == 200) {
						alert("Salvo com Sucesso.");
					}
				},
				fail : function(jqXHR, textStatus, errorThrown) {
					alert("Erro ao Salvar");
				}
			});
		} else {
			var data = [ {
				code : $('#code').val(),
				name : $('#name').val(),
				images: [picture],
				description : $('#description').val()
			} ];
			
			$.ajax({
				type : "POST",
				url : "/shops/" + idShop + "/products",
				contentType : "application/json;charset=UTF-8",
				data : JSON.stringify(data),
				dataType : "json",
				complete : function(xhr, textStatus) {
					if (xhr.status == 201) {
						alert("Salvo com Sucesso.");
					}
				},
				fail : function(jqXHR, textStatus, errorThrown) {
					alert("Erro ao Salvar");
				}
			});
		}
	});
	
	var readURL = function(input) {
	  if (input.files && input.files[0]) {
	    var reader = new FileReader();

	    reader.onload = function(e) {
	      $('#imgProd').attr('src', e.target.result);
	      picture = reader.result;
	    }

	    reader.readAsDataURL(input.files[0]);
	  }
	};

	
	$("#fileUploader").change(function() {
	  readURL(this);
	});
})();