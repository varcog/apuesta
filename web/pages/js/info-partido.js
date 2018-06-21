var url = "../infoPartidoController";
var idPartido;
$(document).ready(cargar);

function cargar() {
    $("input[name=montoRetar]").solo_numeros();
    idPartido = $.get("idPartido");
    $.post(url, {evento: "cargar", idPartido: idPartido}, function (resp) {
        var json = $.parseJSON(resp);
        $(".tableroF1").text(json.partido.fecha);
        $(".tableroF2").text(json.partido.hora);

        var cuerpo = "<option value='" + json.partido.idEquipo1 + "'>" + json.partido.nombre1 + "</option>";
        cuerpo += "<option value='" + json.partido.idEquipo2 + "'>" + json.partido.nombre2 + "</option>";
        $("select[name=equipo]").html(cuerpo);
        $(".equipo1.info-nombre").text(json.partido.nombre1);
        $(".equipo1.info-bandera").addClass(json.partido.icono1);
        $(".equipo1.info-marcador").text(0).data("id", json.partido.idEquipo1);
        $(".equipo2.info-nombre").text(json.partido.nombre2);
        $(".equipo2.info-bandera").addClass(json.partido.icono2);
        $(".equipo2.info-marcador").text(0).data("id", json.partido.idEquipo2);
        $(".info-grupo").text(json.partido.nombreGrupo);
        $(".nombre-estadio").text("Lugar: " + json.partido.nombreEstadio);
        $(".imagen-estadio").attr("src", "../img/estadios/" + json.partido.fotoEstadio);
        // ----------- Cargar Apuestas
        // Resultado Final
        $.each(json.apuestas.partido, function (i, obj) {
            switch (obj.id) {
                case 1:
                    $(".apuEquipo1").data("idtipoapuesta", 1);
                    $(".apuEquipo1").data("idapuestapartido", obj.idApuestaPartido);
                    $(".apuEquipo1").data("idpartido", idPartido);
                    $(".apuEquipo1").data("titulo", json.partido.nombre1);
                    $(".apuEquipo1").data("subtitulo", "Resuldato Final");
                    $(".apuEquipo1").data("vs", json.partido.nombre1 + " vs " + json.partido.nombre2);
                    $(".apuEquipo1").find(".flag-icon").addClass(json.partido.icono1);
                    $(".apuEquipo1").find(".nombre").text(json.partido.nombre1);
                    $(".apuEquipo1").find(".porcentaje").text(obj.porcentaje);
                    break;
                case 2:
                    $(".apuEmpate").data("idtipoapuesta", 2);
                    $(".apuEmpate").data("idapuestapartido", obj.idApuestaPartido);
                    $(".apuEmpate").data("idpartido", idPartido);
                    $(".apuEmpate").data("titulo", "Empate");
                    $(".apuEmpate").data("subtitulo", "Resuldato Final");
                    $(".apuEmpate").data("vs", json.partido.nombre1 + " vs " + json.partido.nombre2);
                    $(".apuEmpate").find(".nombre").text("Empate");
                    $(".apuEmpate").find(".porcentaje").text(obj.porcentaje);
                    break;
                case 3:
                    $(".apuEquipo2").data("idtipoapuesta", 3);
                    $(".apuEquipo2").data("idapuestapartido", obj.idApuestaPartido);
                    $(".apuEquipo2").data("idpartido", idPartido);
                    $(".apuEquipo2").data("titulo", json.partido.nombre2);
                    $(".apuEquipo2").data("subtitulo", "Resuldato Final");
                    $(".apuEquipo2").data("vs", json.partido.nombre1 + " vs " + json.partido.nombre2);
                    $(".apuEquipo2").find(".flag-icon").addClass(json.partido.icono2);
                    $(".apuEquipo2").find(".nombre").text(json.partido.nombre2);
                    $(".apuEquipo2").find(".porcentaje").text(obj.porcentaje);
                    break;
            }
        });

        cuerpo = "";
        var titulo;
        var subtitulo = "Goles en el Partido";
        $.each(json.apuestas.goles, function (i, obj) {
            if (obj.porcentaje && obj.porcentaje > 0) {
                titulo = json.partido.nombre1 + " " + obj.equipo1 + " - " + obj.equipo2 + " " + json.partido.nombre2;
                cuerpo += "<div class='col-md-4 col-sm-6 col-xs-12' style='cursor: pointer;'>";
                cuerpo += "  <div class='info-box' onclick='apostar(this);' data-idpartido='" + idPartido + "' data-idtipoapuesta='" + obj.id + "' data-titulo='" + titulo + "' data-subtitulo='" + subtitulo + "' data-idapuestapartido='" + obj.idApuestaPartido + "'>";
                cuerpo += "	<span class='info-box-icon bg-green' style='height: 90px;width: 70px;font-size: 30px;'><i class='porcentaje'>" + obj.porcentaje + "</i></span>";
                cuerpo += "	<div class='info-box-content' style='margin-left: 70px; padding: 5px 5px;'>";
                cuerpo += "	  <div style='display: flex;'>";
                cuerpo += "		<div style='width: 50%;text-align: center;'>";
                cuerpo += "			<span class='info-box-number' style=''>" + obj.equipo1 + "</span>";
                cuerpo += "			<span class='info-box-text'>" + json.partido.nombre1 + "</span>";
                cuerpo += "		</div>";
                cuerpo += "		<div style='width: 50%;text-align: center;'>";
                cuerpo += "			<span class='info-box-number'>" + obj.equipo2 + "</span>";
                cuerpo += "			<span class='info-box-text'>" + json.partido.nombre2 + "</span>";
                cuerpo += "		</div>";
                cuerpo += "	  </div>";
                cuerpo += "	  <div class='progress' style='margin: 5px -5px 5px -5px;'>";
                cuerpo += "		<div class='progress-bar bg-green' style='width: 100%;'></div>";
                cuerpo += "	  </div>";
                cuerpo += "	  <span class='progress-description'></span>"; // Cantidad de Apuestas
                cuerpo += "	</div>";
                cuerpo += "  </div>";
                cuerpo += "</div>";
            }
        });
        $("#cuerpoApuGoles").html(cuerpo);
        cargarRelato();
    });
    ocultarCargando();
}

function popApostar(idPartido) {
    window.parent.cambiarMenu("pages/info-partido.html");
}
function buscarRetador1(event) {
    if (event.keyCode === 13)
        buscarRetador();
}
function buscarRetador() {
    var usr = $("input[name=buscarRetador]").val();
    $.post(url, {evento: "buscarRetador", usr: usr}, function (obj) {
        var obj = $.parseJSON(obj);
        if (obj) {
            $("#nombreRetado").text(obj.nombres + " " + obj.apellidos);
            $("#fotoRetado").attr("src", "../" + obj.foto);
            $("input[name=idUsuarioRetado]").val(obj.id);
            $("#retar").css("display", "");
        } else {

        }
    });
}
function apostarCon() {
    mostrarCargando();
    var idEquipo = $("select[name=equipo]").val();
    var id = $("input[name=idUsuarioRetado]").val();
    var monto = $("input[name=montoRetar]").val();
    $.post(url, {evento: "apostarCon", id: id, idPartido: idPartido, idEquipo: idEquipo, monto: monto}, function (resp) {
        if (resp === "PARTIDO_PASADO") {
            openAlert("Ya No se puede Apostar en este Partido", "Apuesta");
        } else {
            try {
                var json = $.parseJSON(resp);
                if (json.resp === "CREDITO_INSUFICIENTE") {
                    openAlert("No tiene credito suficiente para realizar esta apuesta <strong>" + new BigNumber("" + json.credito).toFormat(2) + "</strong>", "Apuesta");
                } else if (json.resp) {
                    window.parent.actualizarCredito(json.credito);
                    $("#nombreRetado").text("");
                    $("#fotoRetado").removeAttr("src");
                    $("input[name=idUsuarioRetado]").val(0);
                    $("#retar").css("display", "none");
                    openAlert("Apuesta En espera de Aprobacion ", "Apuesta con Amigo");
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
(function ($) {
    $.get = function (key) {
        key = key.replace(/[\[]/, '\\[');
        key = key.replace(/[\]]/, '\\]');
        var pattern = "[\\?&]" + key + "=([^&#]*)";
        var regex = new RegExp(pattern);
        var url = unescape(window.location.href);
        var results = regex.exec(url);
        if (results === null) {
            return null;
        } else {
            return results[1];
        }
    }
})(jQuery);

function apostar(ele) {
    var $ele = $(ele);
    var json = {
        titulo: $ele.data("titulo"),
        vs: $ele.data("vs"),
        subtitulo: $ele.data("subtitulo"),
        porcentaje: $ele.find(".porcentaje").text(),
        idTipoApuesta: $ele.data("idtipoapuesta"),
        idApuestaPartido: $ele.data("idapuestapartido"),
        idPartido: idPartido
    };
    window.parent.agregarApuesta(json);
}

//<editor-fold defaultstate="collapsed" desc="Relato">
var urlRelato = "../relatarPartidoController";
var $idPArtido;
var ultimaHora;
var cont = 0;
function cargarRelato() {
    $idPArtido = $.get("idPartido");
    $.post(urlRelato, {evento: "cargarRelato", idPartido:$idPArtido}, function (resp) {
        var json = $.parseJSON(resp);        
        var cuerpo = "";
        $.each(json.relato,function(i,obj){
            cuerpo += armarEventoRelato(obj);
        });
        $("#cuerpo-relato").html(cuerpo);    
        setTimeout(traerFaltantesrelato, 10000);
        ocultarCargando();
    });
}

function traerFaltantesrelato() {
    cont=0;
    $.post(urlRelato, {evento: "traerFaltantes", ultimaHora:ultimaHora, idPartido:$idPArtido}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json,function(i,obj){
            cuerpo += armarEventoRelato(obj);
        });
        $("#cuerpo-relato").prepend(cuerpo);       
        setTimeout(traerFaltantesrelato, 10000);
    });
}

function armarEventoRelato(obj) {
    if(cont===0) ultimaHora=obj.fecha;
    
    cont++;
    var cuerpo = "<li>";
    switch(obj.idEvento){
        case 1 : cuerpo+="<i class='fa fa-futbol-o bg-green'></i>"; break;
        case 2 : cuerpo+="<i class='fa fa-line-chart bg-green'></i>"; break;
        case 3 : cuerpo+="<i class='fa fa-long-arrow-right bg-green'></i>"; break;
        case 4 : cuerpo+="<i class='fa fa-area-chart bg-green'></i>"; break;
        case 5 : cuerpo+="<i class='fa fa-square bg-yellow'></i>"; break;
        case 6 : cuerpo+="<i class='fa fa-square bg-red'></i>"; break;
        case 7 : cuerpo+="<i class='fa fa-plus bg-red'></i>"; break;
    }
    if(obj.idEvento>8){
        cuerpo+="<i class='fa fa-tv bg-red'></i>";
    }
    
    cuerpo+="<div class='timeline-item'>";
    cuerpo+="<span class='time'><i class='fa fa-clock-o'></i>"+obj.hora+"</span>";
    cuerpo+="<h3 class='timeline-header'>"+obj.evento+" para "+obj.equipo+"<span style='margin-left:5px;' class='flag-icon "+obj.iconoEquipo+"'></span></h3>";
    cuerpo+="<div class='timeline-body'>";    
    cuerpo+="<h4>"+(obj.nombres||"")+" "+(obj.apellidos||"")+"</h4>";        
    cuerpo+="<img alt='' src='../img/jugadores/"+obj.foto+"' style='width:240px;'/>";
    cuerpo+="</div>";  
    cuerpo+="<div class='timeline-footer'>";    
    cuerpo+="<a class='btn btn-danger btn-xs' style='margin-left:5px;' onclick='eliminar("+obj.id+",this);'>Eliminar</a>";    
    cuerpo+="</div>";
    cuerpo+="</div>";
    cuerpo+="</li>";
    
    
    $(".equipo1.info-marcador").text(obj.resultado[$(".equipo1.info-marcador").data("id")].goles);
    $(".equipo2.info-marcador").text(obj.resultado[$(".equipo2.info-marcador").data("id")].goles);
    
    return cuerpo;

}
//</editor-fold>