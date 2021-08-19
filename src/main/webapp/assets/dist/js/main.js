const fill = (list) => {
    let table = "";

    if(list.length > 0){
        for(let i = 0; i < list.length; i++) {
            table += `
			<tr>
				<td>${ i + 1 }</td>
				<td>${list[i].nameGame}</td>
				<td>${list[i].datePremiere}</td>
				<td><img src="data:image/jpeg;base64,${list[i].imgGame}"></td>
				<td>${list[i].status ? "Activo" : "Inactivo"}</td>
				<td>
					<button type="button" class="btn btn-info">Visualizar</button>
					<button type="button" class="btn btn-primary">Editar</button>
					<button type="button" class="btn btn-danger">Borrar</button>
				</td>
			</tr>
			`;
        }
    }else{
        table = `
		<tr class="text-center">
			<td colspan="5">No hay registros</td>
		</tr>
		`;
    }
    $(`#container > tbody`).html(table);
};

const findAll = () => {
    const contextPath = window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));

    $.ajax({
        type: 'GET',
        url: contextPath + '/readGames',
        data: { }
    }).done(function(res){
        fill(res.listGames);
    });
};

findAll();

function create(){

    $.ajax({
        type: 'POST',
        url: contextPath + '/createGames',
        data: {
            action: '/createGames'
        }
    }).done(function(res){
        document.getElementById("lbl_name").value;
        document.getElementById("lbl_imgGame").value;
        document.getElementById("lbl_datePremiere").value;
        document.getElementById("lbl_Category_idCategory").value;
    });
};