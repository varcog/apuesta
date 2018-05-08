var url = "../AdministracionMenuController";
var tabla, tablaSubMenu;
$(document).ready(function () {
    $('#asignarSubMenuModal').on('shown.bs.modal', openedPopVerSubMenu);
    todos();
});

function todos() {
    mostrarCargando();
    $.post(url, {evento: "todos"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += menuFilaHtml(obj);
        });
        $("#cuerpo").html(html);

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            }
        });
        ocultarCargando();
    });
}

function menuFilaHtml(obj) {
    var tr = "<tr data-id='" + obj.id + "' class='menu_" + obj.id + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-edit text-warning' title='Editar' onclick='popModificarMenu(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-gear text-muted' title='Ver Sub Menu' onclick='popVerSubMenu(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-remove text-danger' title='Eliminar' onclick='popEliminarMenu(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "</tr>";
    return tr;
}

function popRegistrarMenu() {
    $("#mId").val(0);
    $("#mNombre").val("");
    $('#botonMenu').text("Crear Menu");
    openModal('#menuModal');
}

function guardarMenu() {
    if ($("#mNombre").val().trim().length === 0) {
        openAlert("Es necesario el nombre del menu");
        return;
    }
    var id = $("#mId").val();
    var nombre = $("#mNombre").val();
    mostrarCargando();
    $.post(url, {evento: "guardarMenu", id: id, nombre: nombre}, function (resp) {
        if (resp === "false") {
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var id_aux = id;
            var json = $.parseJSON(resp);
            if (id_aux > 0) {
                tabla.cell(".menu_" + id_aux + " > td:eq(0)").data((json.nombre || ""));
                tabla.rows().invalidate();
            } else {
                tabla.row.add($(menuFilaHtml(json))).draw(false);
            }
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

function popModificarMenu(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#mId").val(id);
    $("#mNombre").val(_tr.find("td:eq(0)").text());
    $('#botonMenu').text("Modificar Menu");
    openModal('#menuModal');
}

function popEliminarMenu(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#eliminarModalText").html("¿Esta seguro de eliminar el menu <strong>" + _tr.find("td:eq(0)").text() + "</strong>?")
            .data("id", id);
    $("#elminarBotonModal").off("click");
    $("#elminarBotonModal").click(eliminarMenu);
    openModal('#eliminarModal');
}

function eliminarMenu() {
    mostrarCargando();
    var id = $("#eliminarModalText").data("id");
    $.post(url, {evento: "eliminarMenu", id: id}, function (resp) {
        cerrarModal();
        if (resp === "false") {
            openAlert("Revise Dependencias.");
        } else {
            tabla.row(".menu_" + id).remove().draw(false);
            tabla.rows().invalidate();
//            tabla.row(".producto_" + id).remove().draw(false);
            openAlert("Eliminado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

////////////////////////////////////////////////////////////////////////////////
function popVerSubMenu(id, ele) {
    mostrarCargando();
    $.post(url, {evento: "todosSubMenu", idMenu: id}, function (resp) {
        $("#smIdMenu").val(id);
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += subMenuFilaHtml(obj);
        });
        $("#cuerpoSubMenu").html(html);
        $("#asignarSubMenuModalLabel").text($(ele).closest("tr").find("td:eq(0)").text());
        openModal('#asignarSubMenuModal');
        ocultarCargando();
    });
}

function openedPopVerSubMenu() {
    if (tablaSubMenu)
        tablaSubMenu.rows().invalidate();
    else {
        tablaSubMenu = $('#tablaSubMenu').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json",
                "scrollX": true
            }
        });
//        $("#tablaSubMenu").parent().css("overflow-y", "auto");
    }
}

function subMenuFilaHtml(obj) {
    var tr = "<tr data-id='" + obj.ID + "' class='sub_menu_" + obj.id + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td>" + (obj.url || "") + "</td>";
    tr += "<td id='sm_vis_" + obj.id + "' class='text-center'>" + htmlCheckSubMenu(obj.id, obj.visible) + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-edit text-warning' title='Editar' onclick='popModificarSubMenu(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-remove text-danger' title='Eliminar' onclick='popEliminarSubMenu(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "</tr>";
    return tr;
}

function popRegistrarSubMenu() {
    $("#smId").val(0);
    $("#smNombre").val("");
    $("#smUrl").val("");
    $('#botonSubMenu').text("Crear Sub Menu");
    openModal('#subMenuModal');
}

function guardarSubMenu() {
    if ($("#smNombre").val().trim().length === 0) {
        openAlert("Es necesario el Nombre del sub menu");
        return;
    }
    if ($("#smUrl").val().trim().length === 0) {
        openAlert("Es necesario la url del sub menu");
        return;
    }
    var id = $("#smId").val();
    var nombre = $("#smNombre").val();
    var url1 = $("#smUrl").val();
    var idMenu = $("#smIdMenu").val();
    mostrarCargando();
    $.post(url, {evento: "guardarSubMenu", id: id, nombre: nombre, url: url1, idMenu: idMenu}, function (resp) {
        if (resp === "false") {
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var id_aux = id;
            var json = $.parseJSON(resp);
            if (id_aux > 0) {
                tablaSubMenu.cell(".sub_menu_" + id_aux + " > td:eq(0)").data((json.nombre || ""));
                tablaSubMenu.cell(".sub_menu_" + id_aux + " > td:eq(1)").data((json.url || ""));
                tablaSubMenu.rows().invalidate();
            } else {
                tablaSubMenu.row.add($(subMenuFilaHtml(json))).draw(false);
                tablaSubMenu.rows().invalidate();
            }
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

function popModificarSubMenu(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#smId").val(id);
    $("#smNombre").val(_tr.find("td:eq(0)").text());
    $("#smUrl").val(_tr.find("td:eq(1)").text());
    $('#botonSubMenu').text("Modificar Sub Menu");
    openModal('#subMenuModal');
}

function popEliminarSubMenu(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#eliminarModalText").html("¿Esta seguro de eliminar el sub menu <strong>" + _tr.find("td:eq(0)").text() + "</strong>?")
            .data("id", id);
    $("#elminarBotonModal").off("click");
    $("#elminarBotonModal").click(eliminarSubMenu);
    openModal('#eliminarModal');
}

function eliminarSubMenu() {
    mostrarCargando();
    var id = $("#eliminarModalText").data("id");
    $.post(url, {evento: "eliminarSubMenu", id: id}, function (resp) {
        cerrarModal();
        if (resp === "false") {
            openAlert("Revise Dependencias.");
        } else {
            tablaSubMenu.row(".sub_menu_" + id).remove().draw(false);
            tablaSubMenu.rows().invalidate();
//            tabla.row(".producto_" + id).remove().draw(false);
            openAlert("Eliminado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}

function htmlCheckSubMenu(id, checked) {
    return "<input type='checkbox' onclick='cambiarEstadoSubMenu(" + id + ", this)' " + (checked ? " checked " : "") + "/>";
}

function cambiarEstadoSubMenu(idSubMenu, ele) {
    mostrarCargando();
    var visible = $(ele).prop("checked");
    $.post(url, {evento: "cambiarEstadoSubMenu", idSubMenu: idSubMenu, visible: visible}, function (resp) {
        if (resp === "false") {
            tablaSubMenu.cell($(ele).parent()[0]).data(htmlCheckSubMenu(idSubMenu, !visible));
        } else {
            tablaSubMenu.cell($(ele).parent()[0]).data(htmlCheckSubMenu(idSubMenu, visible));
        }
        tablaSubMenu.rows().invalidate();
        ocultarCargando();
    });
}
