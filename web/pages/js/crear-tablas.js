var url = "../CrearTablasController";
var tabla, tablaSubMenu;
$(document).ready(function () {
    init();
});

function init() {
    mostrarCargando();
    $.post(url, {evento: "cargar"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<tr>";
            html += "<td>" + obj + "</td>";
            html += "<td><input type='checkbox'/></td>";

        });
        $("#cuerpo").html(html);
//        tabla = $('#tabla').DataTable({
//            "language": {
//                "url": "../plugins/datatables/i18n/spanish.json"
//            }
//        });
        ocultarCargando();
    });
}

function generarTablas() {
    mostrarCargando();
    var nombre = [];
    $("#cuerpo").find("input:checkbox:checked").each(function (i, ch) {
        nombre.push($(ch).closest("tr").find("td:eq(0)").text());
    });
    if (nombre.length === 0) {
        openAlert("Ninguna Tabla Seleccionada");
        ocultarCargando();
    }
    $.post(url, {evento: "crearTablasSeleccionadas", nombre: nombre}, function (resp) {
        openAlert("Tablas Generadas");
        ocultarCargando();
    });
}


////////////////////////////////////////////////////////////////////////////////
function htmlCheckUsuario(id, checked) {
    return "<input type='checkbox' onclick='cambiarEstadoUsuario(" + id + ", this)' " + (checked ? " checked " : "") + "/>";
}

function cambiarEstadoUsuario(idUsuario, ele) {
    mostrarCargando();
    var estado = $(ele).prop("checked");
    $.post(url, {evento: "cambiarEstadoUsuario", idUsuario: idUsuario, estado: estado}, function (resp) {
        if (resp === "false") {
            tabla.cell($(ele).parent()[0]).data(htmlCheckUsuario(idUsuario, !estado));
        } else {
            tabla.cell($(ele).parent()[0]).data(htmlCheckUsuario(idUsuario, estado));
        }
        tabla.rows().invalidate();
        ocultarCargando();
    });
}
