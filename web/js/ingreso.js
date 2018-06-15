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
                } else {
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
            cargarHistorial(json.historialApuesta);
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
    html += "<div class='box box-success p_" + json.idPartido + " ta_" + json.idTipoApuesta + " ap_" + json.idApuestaPartido + "'>";
    html += "  <div class='box-header with-border'>";
    html += "    <h3 class='box-title'>" + json.titulo + "</h3>";
    html += "    <div class='box-tools pull-right'>";
    html += "      <button type='button' class='btn btn-box-tool' onclick='eliminarApuesta(this);'><i class='fa fa-times'></i></button>";
    html += "    </div>";
    html += "  </div>";
    html += "  <div class='box-body'>";
    html += "    <span class='info-box-text'>" + json.subtitulo + "</span>";
    if (json.vs)
        html += "    <span class='info-box-text'>" + json.vs + "</span>";
    html += "    <span class='info-box-text'>Porcentaje: <strong class='porcentaje'>" + json.porcentaje + "</strong></span>";
    html += "    <span class='info-box-text'>Monto: <input type='text' class='monto' name='monto' onkeyup='calcularGanancia(this);' data-idpartido='" + json.idPartido + "' data-idtipoapuesta='" + json.idTipoApuesta + "' data-idapuestapartido='" + json.idApuestaPartido + "' data-porcentaje='" + json.porcentaje + "'/></span>";
    html += "    <span class='info-box-text'>Ganancia: <strong class='ganancia'>" + "" + "</strong></span>";
    html += "  </div>";
    html += "</div>";
    $("#apuPendientes").append(html);
    formato_decimal2_s("#apuPendientes .monto");
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
    calcularTotalApuestas();
}

function recalcular() {
    $("#apuPendientes .monto").each(function (i, ele) {
        var $ele = $(ele).closest(".box-body");
        var monto = parseFloat($(ele).val());
        var porcentaje = parseFloat($ele.find(".porcentaje").text());
        var monto = monto * porcentaje;
        if (isNaN(monto))
            $ele.find(".ganancia").text("");
        else
            $ele.find(".ganancia").text(monto);
    });
    calcularTotalApuestas();
}

var listaApuesta;
function apostar() {
    listaApuesta = [];
    var $monto, monto, clon, div;
    $("#cuerpoApuesta").html("");
    $("#apuPendientes").find(".box").each(function (i, ele) {
        $monto = $(ele).find(".monto");
        monto = parseFloat($monto.val());
        if (monto > 0) {
            listaApuesta.push({
                idApuestaPartido: $monto.data("idapuestapartido"),
                idPartido: $monto.data("idpartido"),
                idTipoApuesta: $monto.data("idtipoapuesta"),
                porcentaje: $monto.data("porcentaje"),
                monto: monto
            });
            clon = $(ele).clone();
            clon.find(".monto").attr("disabled", true);
            div = $("<div class='col-md-3 col-sm-6 col-xs-12'></div>");
            div.append(clon);
            $("#cuerpoApuesta").append(div);
        }
    });
    var credito = parseFloat($(".credito_actual_principal").autoNumeric("get"));
    var total = parseFloat($("#apuTotal").text());
    if (credito < total) {
        openAlert("No tiene Credito Suficiente para realizar la apuesta", "Apostar");
    } else if (listaApuesta.length === 0) {
        openAlert("No tiene ninguna apuesta seleccionada o falta algun monto que rellenar", "Apostar");
    } else {
        openModal("#apuestaModal");
    }
}

function okApostar() {
    mostrarCargando();
    $.post(url, {evento: "okApostar", lista: listaApuesta, length: listaApuesta.length}, function (resp) {
        cerrarModal();
        if (resp === "LENGTH_0") {
            openAlert("los montos no son validos", "Apuesta");
        } else {
            try {
                var json = $.parseJSON(resp);
                if (json.resp === "CREDITO_INSUFICIENTE") {
                    openAlert("No tiene credito suficiente para realizar esta apuesta <strong>" + new BigNumber("" + json.credito).toFormat(2) + "</strong>", "Apuesta");
                } else if (json.resp) {
                    $("#apuPendientes").find(".box").remove();
                    $("#apuTotal").text("Total : 0");
                    actualizarCredito(json.credito);
                    openAlert("Apuesta Realizada", "Apuesta");
                } else {
                    $.each(json.eliminar, function (i, obj) {
                        $("#apuPendientes").find(".ap_" + obj).remove();
                    });
                    var $ele;
                    $.each(json.actualizar, function (i, obj) {
                        $ele = $("#apuPendientes").find(".p_" + obj.idPartido + ".ta" + obj.idTipoApuesta);
                        $ele.find(".porcentaje").text(obj.porcentaje);
                        $ele.find(".idapuestapartido").text(obj.idApuestaPartido);
                        var monto = parseFloat($ele.find(".monto").val());
                        var porcentaje = parseFloat($ele.find(".porcentaje").text());
                        var monto = monto * porcentaje;
                        if (isNaN(monto))
                            $ele.find(".ganancia").text("");
                        else
                            $ele.find(".ganancia").text(monto);
                    });
                    calcularTotalApuestas();
                    openAlert("Se a Actualizado la lista, revisela", "Apuesta");
                }
                // Actualizar
            } catch (e) {
                openAlert("Intentelo Nuevamente", "Apuesta");
            }
        }
        ocultarCargando();
    });
}

function eliminarApuesta(element) {
    var box = $(element).parents(".box").first();
    box.slideUp(500, function () {
        box.remove();
        calcularTotalApuestas();
    });
}

function calcularTotalApuestas() {
    var monto;
    var total = 0;
    $("#apuPendientes").find(".box").each(function (i, ele) {
        monto = parseFloat($(ele).find(".monto").val());
        if (monto > 0) {
            total += monto;
        }
    });
    $("#apuTotal").text("Total: " + total.toFixed(2));
}

function cargarHistorial(json) {
    var html = "";
    $.each(json, function (i, h) {
        html += "<div class='box box-default'>";
        html += "  <div class='box-header with-border'>";
        html += "    <h3 class='box-title'>" + h.titulo + "</h3>";
        html += "    <div class='box-tools pull-right'>";
        html += "      <button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>";
        html += "    </div>";
        html += "  </div>";
        html += "  <div class='box-body'>";
        html += "    <span class='info-box-text'>" + h.tipo + "</span>";
        html += "    <span class='info-box-text'>" + h.vs + "</span>";
        if (h.porcentaje)
            html += "    <span class='info-box-text'>Porcentaje: <strong class='porcentaje'>" + h.porcentaje + "</strong></span>";
        html += "    <span class='info-box-text'>Monto: <strong class='monto'>" + h.monto + "</strong></span>";
        html += "  </div>";
        html += "</div>";
    });
    $("#control-sidebar-historial-tab").html(html);
}

function onCargarHistorial() {
    mostrarCargando();
    $("#control-sidebar-historial-tab").html("");
    $.post(url, {evento: "historialApuestas"}, function (resp) {
        var json = $.parseJSON(resp);
        cargarHistorial(json);
        ocultarCargando();
    });
}

function onCargarPendienteAmigo() {
    mostrarCargando();
    $("#apuPendientesReto").html("");
    $("#apuPendientesAprobacion").html("");
    $.post(url, {evento: "historialPendienteAmigo"}, function (resp) {
        var json = $.parseJSON(resp);
        cargarPendienteAprobar(json.aprobacion);
        cargarPendienteReto(json.reto);
        ocultarCargando();
    });
}

function cargarPendienteAprobar(json) {
    var html = "";
    $.each(json, function (i, h) {
        html += "<div class='box box-default'>";
        html += "  <div class='box-header with-border'>";
        html += "    <h3 class='box-title'>" + h.titulo + "</h3>";
        html += "    <div class='box-tools pull-right'>";
        html += "      <button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>";
        html += "    </div>";
        html += "  </div>";
        html += "  <div class='box-body'>";
        html += "    <span class='info-box-text'>" + h.tipo + "</span>";
        html += "    <span class='info-box-text'>" + h.vs + "</span>";
        html += "    <span class='info-box-text'>Monto: <strong class='monto'>" + h.monto + "</strong></span>";
        html += "    <div class='text-center pie'>";
        html += "       <button class='btn btn-danger' onclick='rechazarApuestaAmigo(" + h.idApuestaAmigo + ", this)'>Eliminar</button>";
        html += "    </div>";
        html += "  </div>";
        html += "</div>";
    });
    $("#apuPendientesAprobacion").html(html);
}

function cargarPendienteReto(json) {
    var html = "";
    $.each(json, function (i, h) {
        html += "<div class='box box-default'>";
        html += "  <div class='box-header with-border'>";
        html += "    <h3 class='box-title'>" + h.titulo + "</h3>";
        html += "    <div class='box-tools pull-right'>";
        html += "      <button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>";
        html += "    </div>";
        html += "  </div>";
        html += "  <div class='box-body'>";
        html += "    <span class='info-box-text'>" + h.tipo + "</span>";
        html += "    <span class='info-box-text'>" + h.vs + "</span>";
        html += "    <span class='info-box-text'>Monto: <strong class='monto'>" + h.monto + "</strong></span>";
        html += "    <div class='text-center pie'>";
        html += "       <button class='btn btn-success' onclick='aprobarApuestaAmigo(" + h.idApuestaAmigo + ", this)'>Aceptar</button>";
        html += "       <button class='btn btn-danger' onclick='rechazarApuestaAmigo(" + h.idApuestaAmigo + ", this)'>Rechazar</button>";
        html += "    </div>";
        html += "  </div>";
        html += "</div>";
    });
    $("#apuPendientesReto").html(html);
}

var idAA;
var $eleAA;
function aprobarApuestaAmigo(idApuestaAmigo, ele) {
    idAA = idApuestaAmigo;
    $eleAA = ele;
    var msj = "<h5>¿Esta Seguro de Aceptar la siguiente Apuesta?</h5>";
    $("#confirmarAABotonModal").find(".modal-body").html("");
    $("#confirmarAABotonModal").find(".modal-body").append(msj);
    var clon = $(ele).closest(".box").clone();
    clon.find(".pie").remove();
    clon.find(".btn-box-tool").remove();
    clon.find(".box-body").css("display", "block");
    clon.removeClass(".collapsed-box");
    $("#confirmarAABotonModal").find(".modal-body").append(clon);
    $("#confirmarAABotonModal").off("click");
    $("#confirmarAABotonModal").text($(ele).text());
    $("#confirmarAABotonModal").click(okAprobarApuestaAmigo);
    openModal("#confirmarAAModal");
}

function okAprobarApuestaAmigo() {
    mostrarCargando();
    cerrarModal();
    $.post(url, {evento: "okAprobarApuestaAmigo"}, function (resp) {
        if (resp === "PARTIDO_PASADO") {
            openAlert("Ya No se puede Apostar en este Partido", "Apuesta");
        } else {
            try {
                var json = $.parseJSON(resp);
                if (json.resp === "CREDITO_INSUFICIENTE") {
                    openAlert("No tiene credito suficiente para realizar esta apuesta <strong>" + new BigNumber("" + json.credito).toFormat(2) + "</strong>", "Apuesta");
                } else if (json.resp) {
                    actualizarCredito(json.credito);
                    openAlert("La Apuesta ha sido Aceptada", "Apuesta con Amigo");
                } else {
                    openAlert("Intentelo Nuevamente", "Apuesta");
                }
            } catch (e) {
                openAlert("Intentelo Nuevamente", "Apuesta");
            }
        }
        ocultarCargando();
    });
}

function rechazarApuestaAmigo(idApuestaAmigo, ele) {
    idAA = idApuestaAmigo;
    $eleAA = ele;
    var msj = "<h5>¿Esta Seguro de " + $(ele).text() + " la siguiente Apuesta?</h5>";
    $("#confirmarAAModal").find(".modal-body").html("");
    $("#confirmarAAModal").find(".modal-body").append(msj);
    var clon = $(ele).closest(".box").clone();
    clon.find(".pie").remove();
    clon.find(".btn-box-tool").remove();
    clon.find(".box-body").css("display", "block");
    clon.removeClass(".collapsed-box");
    $("#confirmarAAModal").find(".modal-body").append(clon);
    $("#confirmarAABotonModal").text($(ele).text());
    $("#confirmarAABotonModal").off("click");
    $("#confirmarAABotonModal").click(okRechazarApuestaAmigo);
    openModal("#confirmarAAModal");
}

function okRechazarApuestaAmigo() {
    mostrarCargando();
    cerrarModal();
    $.post(url, {evento: "okRechazarApuestaAmigo", idApuestaAmigo: idAA}, function (resp) {
        if (resp === "true") {
            $($eleAA).closest(".box").remove();
            openAlert("La apuesta a sido rechazada");
        } else {
            openAlert("Intentelo mas tarde");
        }
        ocultarCargando();
    });
}
