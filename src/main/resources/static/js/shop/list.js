/**
 * 
 */

$(document).ready(function(){
	
	var message = "Tem certeza que deseja remover esta empresa?";
	
	var parameters = location.search.substring(1).split("&");

	idShop = parameters[0].split("=")[1];
	
	if(idShop != null){
		
		if(confirm(message)){
			$.ajax({
				type: "DELETE",
				url: "/shops/" + idShop,
				success: function (data, textStatus, xhr) {
					if(xhr.status == 200){
						alert("Removido com Sucesso");
					}
				},
				fail: function (jqXHR, textStatus, errorThrown) {
					alert("Erro ao Remover");
				}
			});
		}
		
		location.search = null;
	}
	
	$.ajax({
		type: "GET",
		url: "/shops",
		success: function (data, textStatus, xhr) {
			if(xhr.status == 200){
				for(i = 0; i < data.length; i++){
					
					var row = "<tr>";
					row += "<td style='text-align: center;'>" + data[i].name +"</td>";
					row += "<td style='text-align: center;'>" + data[i].localization.address +"</td>";
					row += "<td style='text-align: center;'>" + data[i].contact.telephone +"</td>";
					row += "<td style='text-align: center;'>";
					row += "<a href='/product/save.html?id=" + data[i].id + "' title='Cadastrar Produtos'><i class='tiny material-icons'>add_shopping_cart</i></a>";
					row += "<a href='/product/list.html?id=" + data[i].id + "' title='Visualizar Produtos'><i class='tiny material-icons'>assignment</i></a>";
					row += "<a href='/shop/list.html?removeId=" + data[i].id + "' title='Produtos' ><i class='tiny material-icons'>delete</i></a>";
					row += "</td>"
					row += "</tr>";
					
					$("#shops").append(row);
				}
			}
		},
		fail: function (jqXHR, textStatus, errorThrown) {
			alert("Erro ao Salvar");
		}
	});
		

});
