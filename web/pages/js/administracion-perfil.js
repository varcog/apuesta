var url = "../AdministracionPerfilController";
var tabla, tablaSubMenu;
$(document).ready(function () {
    init();
});

function init() {
    mostrarCargando();
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json.perfiles, function (i, obj) {
            html += perfilFilaHtml(obj);
        });
        $("#cuerpo").html(html);

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            }
        });

        //////////////// MENU 
        html = "";
        $.each(json.menus, function (menu, subMenus) {
            if (subMenus.length > 0) {
                html += "<li><span class='text-bold'>" + menu + "</span>";
                html += "<ul>";
                $.each(subMenus, function (i, sm) {
                    html += "<li>";
                    html += "<input type='checkbox' id='sm_" + sm.id + "' class='sm_as' onclick='asignarDesasignarSubMenu(" + sm.id + ", this)'/>";
                    html += "<label for='sm_" + sm.id + "' class='text-normal'>" + sm.nombre + "</label>";
                    html += "</li>";
                });
            }
            html += "</ul>";
            html += "</li>";
        });
        $("#lista_menu_as").html(html);
        ocultarCargando();
    });
}

function perfilFilaHtml(obj) {
    var tr = "<tr data-id='" + obj.id + "' class='perfil_" + obj.id + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-edit text-warning' title='Editar' onclick='popModificarPerfil(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-gear text-muted' title='Ver Sub Menu' onclick='popPermisoPerfil(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-remove text-danger' title='Eliminar' onclick='popEliminarPerfil(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "</tr>";
    return tr;
}

function popRegistrarPerfil() {
    $("#c_id").val(0);
    $("#c_nombre").val("");
    $('#boton_perfil').text("Crear Perfil");
    openModal('#perfilModal');
}

function guardarPerfil() {
    if ($("#c_nombre").val().trim().length === 0) {
        openAlert("Es necesario el Nombre del Perfil");
        return;
    }
    var id = $("#c_id").val();
    var nombre = $("#c_nombre").val();
    mostrarCargando();
    $.post(url, {evento: "guardarPerfil", id: id, nombre: nombre}, function (resp) {
        if (resp === "false") {
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var id_aux = id;
            var json = $.parseJSON(resp);
            if (id_aux > 0) {
                tabla.cell(".perfil_" + id_aux + " > td:eq(0)").data((json.nombre || ""));
                tabla.rows().invalidate();
            } else {
                tabla.row.add($(perfilFilaHtml(json))).draw(false);
            }
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

function popModificarPerfil(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#c_id").val(id);
    $("#c_nombre").val(_tr.find("td:eq(0)").text());
    $('#boton_perfil').text("Modificar Perfil");
    openModal('#perfilModal');
}

function popEliminarPerfil(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#eliminarModalText").html("¿Esta seguro de eliminar el Perfil <strong>" + _tr.find("td:eq(0)").text() + "</strong>?")
            .data("id", id);
    $("#elminarBotonModal").off("click");
    $("#elminarBotonModal").click(eliminarPerfil);
    openModal('#eliminarModal');
}

function eliminarPerfil() {
    mostrarCargando();
    var id = $("#eliminarModalText").data("id");
    $.post(url, {evento: "eliminarPerfil", id: id}, function (resp) {
        cerrarModal();
        if (resp === "false") {
            openAlert("Revise Dependencias.");
        } else {
            tabla.row(".perfil_" + id).remove().draw(false);
            tabla.rows().invalidate();
            openAlert("Eliminado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

////////////////////////////////////////////////////////////////////////////////
function popPermisoPerfil(id, ele) {
    mostrarCargando();
    $.post(url, {evento: "todosSubMenuAsignados", idPerfil: id}, function (resp) {
        $("#as_id_perfil").val(id);
        $(".sm_as").prop("checked", false);
        var json = $.parseJSON(resp);
        $.each(json, function (i, obj) {
            $("#sm_" + obj.idSubMenu).prop("checked", true);
        });
        $("#asignarPermisoModalLabel").text($(ele).closest("tr").find("td:eq(0)").text());
        openModal('#asignarPermisoModal');
        ocultarCargando();
    });
}

function asignarDesasignarSubMenu(idSubMenu, ele) {
    mostrarCargando();
    var idPerfil = $("#as_id_perfil").val();
    var asignar = $(ele).prop("checked");
    $.post(url, {evento: "asignarDesasignarSubMenu", idSubMenu: idSubMenu, idPerfil: idPerfil, asignar: asignar}, function (resp) {
        if (resp === "false") {
            $(ele).prop("checked", !asignar);
        } else {
            $(ele).prop("checked", asignar);
        }
        ocultarCargando();
    });
}
