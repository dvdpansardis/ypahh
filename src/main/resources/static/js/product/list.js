/**
 * 
 */

(function() {

	var parameters = location.search.substring(1).split("&");

	var idShop = parameters[0].split("=")[1];

	$(document)
			.ready(
					function() {
						if (parameters.length > 1) {
							var idPrd = parameters[1].split("=");
							if (idPrd[0] == 'idProduct' && idPrd[1] != null) {
								var message = "Tem certeza que deseja remover este produto?";
								if (confirm(message)) {
									$.ajax({
										type : "DELETE",
										url : "/shops/" + idShop + "/products/"
												+ idPrd[1],
										success : function(data, textStatus,
												xhr) {
											if (xhr.status == 200) {
												alert("Removido com Sucesso");
												getTable();
											}
										},
										fail : function(jqXHR, textStatus,
												errorThrown) {
											alert("Erro ao Remover");
										}
									});
								}

								window.history.pushState({}, document.title,
										"/products/list.html?id=" + idShop);
							}
						} else {
							getTable();
						}
					});

	var getTable = function() {
		$.ajax({
			type : "GET",
			url : "/shops/" + idShop + "/products",
			success : function(data, textStatus, xhr) {
				if (xhr.status == 200) {
					loadTable(data);
				}
			},
			fail : function(jqXHR, textStatus, errorThrown) {
				alert("Erro ao Salvar");
			}
		});
	};

	var loadTable = function(data) {
		for (i = 0; i < data.length; i++) {

			var row = "<tr>";
			row += "<td style='text-align: center;'>" + data[i].code + "</td>";
			row += "<td style='text-align: center;'>" + data[i].name + "</td>";
			row += "<td style='text-align: center;'>" + data[i].description
					+ "</td>";
			if (data[i].images != null) {
				row += "<td style='text-align: center;'><img src='"
						+ data[i].images[0] + "' class='imgs'/></td>";
			} else {
				row += "<td style='text-align: center;'></td>";
			}
			row += "<td style='text-align: center;'>";
			row += "<a href='/product/save.html?id="
				+ idShop
				+ "&idProduct="
				+ data[i].code
				+ "' title='Editar Produto'><i class='tiny material-icons'>mode_edit</i></a>";
			row += "<a href='/product/list.html?id="
					+ idShop
					+ "&idProduct="
					+ data[i].code
					+ "' title='Remover Produto'><i class='tiny material-icons'>delete</i></a>";
			row += "</td>"
			row += "</tr>";

			$("#shops").append(row);
		}
	}
})();