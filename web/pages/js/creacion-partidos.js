var url = "../creacionPartidosController";

$(document).ready(function () {
    init();
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json.Equipos, function (i, equipo) {
            cuerpo += "<option value='" + equipo.id + "'>" + equipo.nombre + "</option>";
        });
        $("select[name=equipo1]").html(cuerpo);
        $("select[name=equipo2]").html(cuerpo);
        cuerpo = "";
        $.each(json.Grupos, function (i, grupo) {
            cuerpo += "<option value='" + grupo.id + "'>" + grupo.nombre + "</option>";
        });
        $("select[name=grupo]").html(cuerpo);
        cuerpo = "";
        var fecha = "";
        $.each(json.Partidos, function (i, partido) {
            if (fecha !== partido.fecha) {
                fecha = partido.fecha;
                var res = fecha.split("/");
                var cod = "";
                $.each(res, function (i, obj) {
                    cod += obj;
                });
                cuerpo += "<li class='time-label'>";
                cuerpo += "<span class='bg-red fec_" + cod + " fecha'>";
                cuerpo += fecha;
                cuerpo += "</span>";
                cuerpo += "</li>";
            }
            cuerpo += armarPartidos(partido);
        });
        $("#cuerpo").html(cuerpo);
        cuerpo = "";
        $.each(json.Estadios, function (i, estadio) {
            cuerpo += "<option value='" + estadio.id + "'>" + estadio.nombre + "</option>";
        });
        $("select[name=estadio]").html(cuerpo);
        ocultarCargando();
    });
}
function armarPartidos(partido) {
    var cuerpo = "<li>";
    cuerpo += "<i class='fa fa-futbol-o bg-blue'></i>";
    cuerpo += "<div class='timeline-item'>";
    cuerpo += "<span class='time'><i class='fa fa-clock-o'></i>" + partido.hora + "</span>";
    cuerpo += "<h3 class='timeline-header'><a class='eq1' data-idequipo='" + partido.idEquipo1 + "'>" + partido.nombre1 + "<span style='margin-left:5px;' class='flag-icon " + partido.icono1 + "'></span></a> VS <a class='eq2' data-idequipo='" + partido.idEquipo2 + "'>" + partido.nombre2 + "<span style='margin-left:5px;' class='flag-icon " + partido.icono2 + "'></span></a></h3>";
    cuerpo += "<div class='timeline-body'>";
    cuerpo += "<h2>" + partido.nombreGrupo + "</h2>";
    cuerpo += "<h4>" + partido.nombreEstadio + "</h4>";
    cuerpo += "<img alt='' src='../img/estadios/" + partido.fotoEstadio + "' style='width:240px;'/>";
    cuerpo += "</div>";
    cuerpo += "<div class='timeline-footer'>";
    cuerpo += "<a class='btn btn-danger btn-xs' style='margin-left:5px;' onclick='eliminar(" + partido.id + ",this);'>Eliminar</a>";
    cuerpo += "<a class='btn btn-warning btn-xs' style='margin-left:5px;' onclick='verApuestas(" + partido.id + ",this);'>Ver porcentaje de Apuestas</a>";
    cuerpo += "<a class='btn btn-success btn-xs' style='margin-left:5px;' onclick='relatarPartido(" + partido.id + ",this);'>Relatar Partido</a>";
    if (partido.estado === 0)
        cuerpo += "<a class='btn btn-info btn-xs' style='margin-left:5px;' onclick='cerrarPartido(" + partido.id + ",this);'>Cerrar Partido y Pagar Paguesta</a>";
    cuerpo += "</div>";
    cuerpo += "</div>";
    cuerpo += "</li>";
    return cuerpo;
}

function relatarPartido(idPartido, btn) {
    window.parent.cambiarMenu("pages/relatarPartido.html?idPartido=" + idPartido);
}

function buscar(e) {
    var b = $("input[name=buscador]").val();
    if (b.length > 0) {
        b = b.toUpperCase();
        $(".pais").css("display", "none");
        $(".nombre:contains('" + b + "')").parent().parent().parent().css("display", "");
    } else {
        $(".pais").css("display", "");
    }
}
function popRegistrarPartido() {
    $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
    //$("[data-mask]").inputmask();
    $('.calendario').calendario();
    $(".timepicker").bootstrapMaterialDatePicker({date: false, format: 'HH:mm'});
    openModal('#popCreacionPartido');
}

function crearPartido() {
    var fecha = $("#fechaPartido").val();
    var hora = $("input[name=horaPartido]").val();
    var id1 = $("select[name=equipo1]").val();
    var id2 = $("select[name=equipo2]").val();
    var idEstadio = $("select[name=estadio]").val();
    var idGrupo = $("select[name=grupo]").val();
    if (id1 == id2) {
        $("select[name=equipo1]").val("");
        $("select[name=equipo2]").val("");
        return;
    }
    if (fecha.length === 0) {
        $("#fechaPartido").focus();
        return;
    }
    if (hora.length === 0) {
        $("input[name=horaPartido]").focus();
        return;
    }
    if (idEstadio <= 0) {
        return;
    }
    if (idGrupo <= 0) {
        return;
    }
    mostrarCargando();
    $.post(url, {evento: "crearPartido", idGrupo: idGrupo, idEstadio: idEstadio, fecha: fecha, hora: hora, id1: id1, id2: id2}, function (resp) {
        var partido = $.parseJSON(resp);
        addPartido(partido);
        ocultarCargando();
        cerrarModal();
    });
}
function addPartido(partido) {
    var res = partido.fecha.split("/");
    var cod = "";
    $.each(res, function (i, obj) {
        cod += obj;
    });
    if ($(".fec_" + cod).length > 0) {
        $(".fec_" + cod).parent().after(armarPartidos(partido));
    } else {
        var fecha = partido.fecha;
        var cuerpo = "";
        var res = fecha.split("/");
        var cod = "";
        $.each(res, function (i, obj) {
            cod += obj;
        });
        cuerpo += "<li class='time-label'>";
        cuerpo += "<span class='bg-red fec_" + cod + " fecha'>";
        cuerpo += fecha;
        cuerpo += "</span>";
        cuerpo += "</li>";
        cuerpo += armarPartidos(partido);
        $("#cuerpo").append(cuerpo);
    }
}

function eliminar(id, ele) {
    $.post(url, {evento: "eliminar", id: id}, function (resp) {
        if (resp === "true") {
            $(ele).parent().parent().parent().remove();
        }
    });
}

function verApuestas(id, ele) {
    var eq1 = $(ele).parent().parent().find(".eq1").text();
    var eq2 = $(ele).parent().parent().find(".eq2").text();
    $("#cuerpoApuestas").parent().find(".eq1").text(eq1);
    $("#cuerpoApuestas").parent().find(".eq2").text(eq2);
    $("#cuerpoApuestasPartido").find(".eq1").text(eq1);
    $("#cuerpoApuestasPartido").find(".eq2").text(eq2);
    openModal("#popPorcentajeApuestas");
    $.post(url, {evento: "verApuestas", id: id}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json.goles, function (i, obj) {
            cuerpo += "<tr>";
            cuerpo += "<td>" + obj.equipo1 + "</td>";
            cuerpo += "<td>" + obj.equipo2 + "</td>";
            cuerpo += "<td><input type='text' value='" + obj.porcentaje + "' onchange='cambiarPorcApuesta(" + id + "," + obj.id + ",this);'/></td>";
            cuerpo += "</tr>";
        });
        $.each(json.partido, function (i, obj) {
            switch (obj.id) {
                case 1:
                    $("input[name=apuEq1]").val(obj.porcentaje);
                    $("input[name=apuEq1]").data("idPartido", id);
                    $("input[name=apuEq1]").data("idTipoApuesta", 1);
                    break;
                case 2:
                    $("input[name=apuEmp]").val(obj.porcentaje);
                    $("input[name=apuEmp]").data("idPartido", id);
                    $("input[name=apuEmp]").data("idTipoApuesta", 2);
                    break;
                case 3:
                    $("input[name=apuEq2]").val(obj.porcentaje);
                    $("input[name=apuEq2]").data("idPartido", id);
                    $("input[name=apuEq2]").data("idTipoApuesta", 3);
                    break;
            }
        });
        $("#cuerpoApuestas").html(cuerpo);
    });
}

function cambiarPorcApuesta(idPartido, idTipoApuesta, ele) {
    var monto = $(ele).val();
    $.post(url, {evento: "cambiarPorcApuesta", idPartido: idPartido, idTipoApuesta: idTipoApuesta, monto: monto}, function (resp) {
        var json = $.parseJSON(resp);

    });
}
function cambiarPorcApuesta2(ele) {
    var idPartido = $(ele).data("idPartido");
    var idTipoApuesta = $(ele).data("idTipoApuesta");
    var monto = $(ele).val();
    $.post(url, {evento: "cambiarPorcApuesta", idPartido: idPartido, idTipoApuesta: idTipoApuesta, monto: monto}, function (resp) {
        var json = $.parseJSON(resp);

    });
}

var cpIdPartido;
var elecp;
function cerrarPartido(idPartido, ele) {
    mostrarCargando();
    cpIdPartido = idPartido;
    elecp = ele;
    $.post(url, {evento: "cerrarPartido", idPartido: idPartido}, function (resp) {
        var json = $.parseJSON(resp);
        if (json.estado === 0) {
            $("#confirmarAABotonModal").css("display", "");
        } else {
            $("#confirmarAABotonModal").css("display", "none");
        }
        var html = "<h5>¿Esta Seguro de Cerrar y pagar las apuestas del siguiente Partido?</h5>";
        var cont = $(ele).closest("li");
        var eq1 = cont.find(".eq1");
        var eq2 = cont.find(".eq2");
        html += "<div>" + eq1.text() + ":" + json.resultado[eq1.data("idequipo")].goles + "</div>";
        html += "<div>" + eq2.text() + ":" + json.resultado[eq2.data("idequipo")].goles + "</div>";
        $("#confirmarAAModal").find(".modal-body").html(html);
        $("#confirmarAABotonModal").off("click");
        $("#confirmarAABotonModal").click(okCerrarPartido);
        openModal("#confirmarAAModal");
        ocultarCargando();
    });
}

function okCerrarPartido() {
    mostrarCargando();
    cerrarModal();
    $.post(url, {evento: "okCerrarPartido", idPartido: cpIdPartido}, function (resp) {
        if (resp === "true") {
            $(elecp).remove();
            openAlert("Apuestas Pagadas");
        } else {
            openAlert("Intentelo de nuevo");
        }
        ocultarCargando();
    });
}