var url = "../AdministracionUsuarioController";
var tabla, tablaSubMenu;
$(document).ready(function () {
    init();
});

function init() {
    mostrarCargando();
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";        
        $.each(json.usuarios, function (i, obj) {
            html += usuarioFilaHtml(obj);            
        });
        $("#cuerpo").html(html);
        
        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            }
        });

        //////////////// Perfil         
        ocultarCargando();
    });
}

function usuarioFilaHtml(obj) {
    var color = "";
    switch(obj.estado){
        case 1 :
            color = "bg-warning";            
            break;
        case 0 :
            color = "bg-danger";
            break;
    }    
    var tr = "<tr data-id='" + obj.id + "' class='"+color+" usuario_" + obj.id + "'>";
    tr += "<td>" + (obj.ci || "") + "</td>";
    tr += "<td>" + (obj.nombres || "") +" "+ (obj.apellidos || "") + "</td>";
    tr += "<td>" + (obj.perfil || "") + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-edit text-warning' title='Editar' onclick='popModificarUsuario(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-gear text-muted' title='Cambiar Contraseña' onclick='popCambiarContrasena(" + obj.id + ",this);'></i>";
    tr += "</td>";    
    tr += "</tr>";
    return tr;
}

function popRegistrarUsuario() {
    $("#u_id").val(0);
    $("#u_accion").val(0);
    $("#u_usuario").val("").prop("disabled", false);
    $("#u_contrasena").val("").prop("disabled", false);
    $("#u_contrasena_rep").val("").prop("disabled", false);    
    $("#u_ci").val("").prop("disabled", false);
    $("#u_nombres").val("").prop("disabled", false);
    $("#u_apellidos").val("").prop("disabled", false);
    $("#u_fecha_nacimiento").val("").prop("disabled", false);
    $("#u_sexo").val("M").prop("disabled", false);
    $("#u_sexo").val("M").prop("disabled", false);
    $("#u_direccion").val("").prop("disabled", false);
    $("#u_celular").val("").prop("disabled", false);

    $(".usuario_creacion").css("display", "");
    $(".usuario_modificacion").css("display", "");

    $("#u_usuario").removeClass("bg-error");
    $("#u_contrasena").removeClass("bg-error");
    $("#u_contrasena_rep").removeClass("bg-error");
    $("#u_ci").removeClass("bg-error");
    $("#u_nombres").removeClass("bg-error");
    $("#u_apellidos").removeClass("bg-error");

    $('#boton_usuario').text("Crear Usuario");
    openModal('#usuarioModal');
    $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});    
    $(".celular").inputmask();    
        //$("[data-mask]").inputmask();
    $('.calendario').calendario();
}

function guardarUsuario() {
    $("#u_usuario").removeClass("bg-error");
    $("#u_contrasena").removeClass("bg-error");
    $("#u_contrasena_rep").removeClass("bg-error");
    $("#u_ci").removeClass("bg-error");
    $("#u_nombres").removeClass("bg-error");
    $("#u_apellidos").removeClass("bg-error");
    $("#u_direccion").removeClass("bg-error");
    $("#u_celular").removeClass("bg-error");
    var id = parseInt($("#u_id").val());
    var accion = parseInt($("#u_accion").val());
    var usuario = $("#u_usuario").val().trim();
    var contrasena = $("#u_contrasena").val().trim();
    var contrasena_rep = $("#u_contrasena_rep").val().trim();    
    var ci = $("#u_ci").val().trim();
    var nombres = $("#u_nombres").val().trim();
    var apellidos = $("#u_apellidos").val().trim();
    var fechaNacimiento = $("#u_fecha_nacimiento").val().trim();
    var sexo = $("#u_sexo").val();
    var direccion = $("#u_direccion").val();
    var celular = $("#u_celular").val();
    if (accion === 0 || accion === 2) {
        if (usuario.length === 0 && accion === 0) {
            $("#u_usuario").addClass("bg-error");
            openAlert("Es necesario el usuario");
            return;
        }
        if (contrasena.length === 0) {
            $("#u_contrasena").addClass("bg-error");
            openAlert("Es necesario la Contraseña");
            return;
        }        
        if (contrasena_rep.length === 0) {
            $("#u_contrasena_rep").addClass("bg-error");
            openAlert("Es necesario repetir la Contraseña");
            return;
        }
        if (contrasena !== contrasena_rep) {
            $("#u_contrasena").addClass("bg-error");
            $("#u_contrasena_rep").addClass("bg-error");
            openAlert("La contraseña no coincide");
            return;
        }
        if (ci.length === 0 && accion === 0) {
            $("#u_ci").addClass("bg-error");
            openAlert("Es necesario el CI");
            return;
        }
        if (direccion.length === 0 && accion === 0) {
            $("#u_direccion").addClass("bg-error");
            openAlert("Es necesario la direccion");
            return;
        }
        if (celular.length === 0 && accion === 0) {
            $("#u_celular").addClass("bg-error");
            openAlert("Es necesario el celular");
            return;
        }
    }
    if (nombres.length === 0) {
        $("#u_nombres").addClass("bg-error");
        openAlert("Es necesario el Nombre");
        return;
    }
    mostrarCargando();
    $.post(url, {
        evento: "guardarUsuario",
        id: id,
        usuario: usuario,
        contrasena: contrasena,        
        direccion:direccion,
        celular:celular,
        ci: ci,
        nombres: nombres,
        apellidos: apellidos,
        fechaNacimiento: fechaNacimiento,
        sexo: sexo,
        accion: accion
    }, function (resp) {
        if (resp === "false") {
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var id_aux = id;
            var json = $.parseJSON(resp);
            if (accion === 1) {
                tabla.cell(".usuario_" + id_aux + " > td:eq(1)").data((json.NOMBRES || "") + (json.APELLIDOS || ""));                
                tabla.rows().invalidate();
            } else if (accion === 0) {
                tabla.row.add($(usuarioFilaHtml(json))).draw(false);
            }
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");

        }
        ocultarCargando();
    });
}

function popModificarUsuario(id, ele) {
    mostrarCargando();
    $.post(url, {evento: "datosUsuario", id: id}, function (resp) {
        if (resp === "false") {
            openAlert("Intentelo de nuevo.");
            ocultarCargando();
            return;
        }
        var json = $.parseJSON(resp);
        $("#u_id").val(id);
        $("#u_accion").val(1);
        $("#u_usuario").val((json.usuario || "")).prop("disabled", true);
        $("#u_contrasena").val("").prop("disabled", true);
        $("#u_contrasena_rep").val("").prop("disabled", true);        
        $("#u_ci").val((json.ci || "")).prop("disabled", true);
        $("#u_nombres").val((json.nombres || "")).prop("disabled", false);
        $("#u_apellidos").val((json.apellidos || "")).prop("disabled", false);
        $("#u_fecha_nacimiento").val((json.fechaNacimiento || "")).prop("disabled", false);
        $("#u_sexo").val(json.sexo).prop("disabled", false);
        $("#u_direccion").val(json.direccion).prop("disabled", false);;
        $("#u_celular").val(json.telefono).prop("disabled", false);;        
        $(".usuario_creacion").css("display", "none");
        $(".usuario_modificacion").css("display", "");

        $("#u_usuario").removeClass("bg-error");
        $("#u_contrasena").removeClass("bg-error");
        $("#u_contrasena_rep").removeClass("bg-error");
        $("#u_ci").removeClass("bg-error");
        $("#u_nombres").removeClass("bg-error");
        $("#u_apellidos").removeClass("bg-error");
        $("#u_direccion").removeClass("bg-error");
        $("#u_celular").removeClass("bg-error");

        $('#boton_usuario').text("Modificar Usuario");
        ocultarCargando();
        openModal('#usuarioModal');
        $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});    
        //$("[data-mask]").inputmask();
        $('.calendario').calendario();
        $(".celular").inputmask();    
    });
}

function popEliminarUsuario(id, ele) {
    var _tr = $(ele).closest("tr");
    $("#eliminarModalText").html("¿Esta seguro de eliminar el Usuario con CI: <strong>" + _tr.find("td:eq(0)").text() + "</strong> y Nombre: <strong>" + _tr.find("td:eq(1)").text() + "</strong>?")
            .data("id", id);
    $("#elminarBotonModal").off("click");
    $("#elminarBotonModal").click(eliminarUsuario);
    openModal('#eliminarModal');
}

function eliminarUsuario() {
    mostrarCargando();
    var id = $("#eliminarModalText").data("id");
    $.post(url, {evento: "eliminarUsuario", id: id}, function (resp) {
        cerrarModal();
        if (resp === "false") {
            openAlert("Revise Dependencias.");
        } else {
            tabla.row(".usuario_" + id).remove().draw(false);
            tabla.rows().invalidate();
            openAlert("Eliminado Correctamente.", "Información");
        }
        ocultarCargando();
        openModal('#alertModal');
    });
}

function popCambiarContrasena(id, ele) {
    mostrarCargando();
    $.post(url, {evento: "datosUsuario", id: id}, function (resp) {
        var json = $.parseJSON(resp);
        $("#u_id").val(id);
        $("#u_accion").val(2);
        $("#u_usuario").val((json.usuario || "")).prop("disabled", true);
        $("#u_contrasena").val("").prop("disabled", false);
        $("#u_contrasena_rep").val("").prop("disabled", false);
        $("#u_ci").val((json.ci || "")).prop("disabled", true);
        $("#u_nombres").val((json.nombres || "")).prop("disabled", true);
        $("#u_apellidos").val((json.apellidos || "")).prop("disabled", true);
        $("#u_fecha_nacimiento").val((json.fechaNacimiento || "")).prop("disabled", true);
        $("#u_sexo").val(json.sexo).prop("disabled", true);
        $("#u_direccion").val(json.direccion).prop("disabled", true);
        $("#u_celular").val(json.telefono).prop("disabled", true);

        $(".usuario_creacion").css("display", "");
        $(".usuario_modificacion").css("display", "none");        

        $("#u_usuario").removeClass("bg-error");
        $("#u_contrasena").removeClass("bg-error");
        $("#u_contrasena_rep").removeClass("bg-error");
        $("#u_ci").removeClass("bg-error");
        $("#u_nombres").removeClass("bg-error");
        $("#u_apellidos").removeClass("bg-error");

        $('#boton_usuario').text("Cambiar Contraseña");
        ocultarCargando();
        openModal('#usuarioModal');
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