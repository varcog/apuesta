var url = "../relatarPartidoController";
var $idPArtido;
var ultimaHora;
var cont = 0;
$(document).ready(cargar);

function cargar() {
    $idPArtido = $.get("idPartido");
    $.post(url, {evento: "cargarRelato", idPartido:$idPArtido}, function (resp) {
        var json = $.parseJSON(resp);
        $("#equipo1").html("<span style='margin-right: 5px;' class='flag-icon "+json.partido.icono1+"'></span>"+json.partido.nombre1);
        $("#equipo2").html("<span style='margin-right: 5px;' class='flag-icon "+json.partido.icono2+"'></span>"+json.partido.nombre2);
        var cuerpo = "";
        $.each(json.relato,function(i,obj){
            cuerpo += armarEvento(obj);
        });
        $("#cuerpo").html(cuerpo);        
        ocultarCargando();
    });
}

function traerFaltantes() {
    $.post(url, {evento: "traerFaltantes", ultimaHora:ultimaHora, idPartido:$idPArtido}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json.relato,function(i,obj){
            cuerpo += armarEvento(obj);
        });
        $("#cuerpo").prepend(cuerpo);        
    });
}

function armarEvento(obj) {
    if(cont===0) ultimaHora=obj.hora;
    
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
    return cuerpo;

}

(function($) {  
    $.get = function(key)   {  
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
