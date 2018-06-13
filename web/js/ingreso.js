var url = "IngresoController";

$(document).ready(function () {
    formato_decimal_all();
    $.post(url, {evento: "obtenerIngreso"}, function (resp) {
        if (resp === "false")
            location.href = "index.html";
        else {
            $(".elmenu").remove();
            var json = $.parseJSON(resp);
            var cuerpo = "";
            var cant = 0;
            $.each(json.notificaciones, function (i, obj) {
                if (obj.estadoVisto === 0) {
                    cant++;
                    cuerpo += "<li class='bg-warning' title='" + obj.descripcion + "'><a href='#'><i class='fa fa-users text-aqua'></i>" + obj.descripcion + "</a></li>";
                }
                else {
                    cuerpo += "<li title='" + obj.descripcion + "'><a href='#'><i class='fa fa-users text-aqua'></i>" + obj.descripcion + "</a></li>";
                }
            });
            $("#navNovedades").html(cuerpo);
            $("#cantNot").text(cant);

            var html = "";
            $.each(json.menu, function (menu, subMenus) {
                html += "<li class='treeview'>";
                html += "<a>";
                // fa-asterisk
                // fa-certificate
                // fa-circle
                html += "<i class='fa fa-asterisk'></i>";
                html += "<span>" + menu + "</span>";
                html += "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>";
                html += "</a>";
                html += "<ul class='treeview-menu'>";
                $.each(subMenus, function (i, subMenu) {
                    html += "<li><a onclick='cambiarMenu(\"" + subMenu.url + "\")'><i class='fa fa-circle-o'></i> " + subMenu.nombre + "</a></li>";
                });
                html += "</ul>";
                html += "</li>";
            });
            $("#menu").append(html);
            $(".nombre_usuario").text((json.usuario || ""));
            $("input[name=idUsuario]").val(json.idUsuario);
            $(".usr_img").attr("src", json.foto);
            $(".cargo").text((json.perfil || ""));
            actualizarCredito(json.credito);
            openSocket();
        }
    });
});

function cambiarMenu(url) {
    $("#contentFrame").attr("src", url);
}

function cambiarFotoPerfil(ele) {
    $("input[name=file_foto_perfil]").click();
    var img = $("#img-foto-perfil").attr("src");
    $("input[name=old]").val(img);
}
function okCambiarFotoPerfil(ele) {
    var formData = new FormData($("#form-foto-peril")[0]);
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data, textStatus, jqXHR)
        {
            $(".usr_img").attr("src", data);
            $("input[name=file_foto_perfil]").val("");
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            $("input[name=file_foto_perfil]").val("");
        }
    });
}

function actualizarCredito(credito) {
    $(".credito_actual").autoNumeric("set", (credito || 0));
    $(".credito_actual").attr("data-original-title", "Credito = " + $(".credito_actual").text());
}

function actualizarCreditos(credito, id) {
    var id_act = parseInt($("input[name=idUsuario]").val());
    if (id_act === id) {
        $(".credito_actual").autoNumeric("set", (credito || 0));
        $(".credito_actual").attr("data-original-title", "Credito = " + $(".credito_actual").text());
    }
}

function agregarApuesta(json) {
    var html = "";
    html += "<div class='box box-success'>";
    html += "  <div class='box-header with-border'>";
    html += "    <h3 class='box-title'>" + json.titulo + "</h3>";
    html += "    <div class='box-tools pull-right'>";
    html += "      <button type='button' class='btn btn-box-tool' data-widget='remove'><i class='fa fa-times'></i></button>";
    html += "    </div>";
    html += "  </div>";
    html += "  <div class='box-body'>";
    html += "    <span class='info-box-text'>" + json.subtitulo + "</span>";
    if (json.vs)
        html += "    <span class='info-box-text'>" + json.vs + "</span>";
    html += "    <span class='info-box-text'>Porcentaje: <strong class='porcentaje'>" + json.porcentaje + "</strong></span>";
    html += "    <span class='info-box-text'>Monto: <input type='text' class='monto' name='monto' onkeyup='calcularGanancia(this);' data-idPartido='" + json.idPartido + "' data-idTipoApuesta='" + json.idTipoApuesta + "'/></span>";
    html += "    <span class='info-box-text'>Ganancia: <strong class='ganancia'>" + "" + "</strong></span>";
    html += "  </div>";
    html += "</div>";
    $("#apuPendientes").append(html);
    var sidebar = $(".control-sidebar");
    if (!sidebar.hasClass('control-sidebar-open')
            && !$('body').hasClass('control-sidebar-open')) {
        //Open the sidebar
        $("#panelLateral").click();
    }
    $("#tabPendientes").click();
}


function calcularGanancia(ele) {
    var $ele = $(ele).closest(".box-body");
    var monto = parseFloat($(ele).val());
    var porcentaje = parseFloat($ele.find(".porcentaje").text());
    var monto = monto * porcentaje;
    if (isNaN(monto))
        $ele.find(".ganancia").text("");
    else
        $ele.find(".ganancia").text(monto);
}

function apostar() {
}

function okApostar() {
    mostrarCargando();
    $.post(url, {evento: ""}, function (resp) {
        var json = $.parseJSON(resp);

        ocultarCargando();
    });
}