var url = "../infoPartidoController";

$(document).ready(cargar);

function cargar() {
    var idPartido = $.get("idPartido");
    $.post(url, {evento: "cargar",idPartido:idPartido}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "<div class='row'>";
        cuerpo += "<div class='col-lg-5 col-xs-5' style='height: 50px; text-align: center;'><h4>"+json.partido.nombre1+"</h4></div>";
        cuerpo += "<div class='col-lg-2 col-xs-2'>";
        cuerpo += "<h3 style='text-align: center;'></h3>";
        cuerpo += "</div>";
        cuerpo += "<div class='col-lg-5 col-xs-5' style='height: 50px; text-align: center;'><h4>"+json.partido.nombre2+"</h4></div>";
        cuerpo += "</div>";        
        cuerpo += "<div class='row'>";
        cuerpo += "<div class='col-lg-5 col-xs-5  img-thumbnail flag flag-icon-background "+json.partido.icono1+"' style='height: 120px;'></div>";
        cuerpo += "<div class='col-lg-2 col-xs-2'>";
        cuerpo += "<h3 style='text-align: center;'>VS</h3>";
        cuerpo += "</div>";
        cuerpo += "<div class='col-lg-5 col-xs-5  img-thumbnail flag flag-icon-background "+json.partido.icono2+"' style='height: 120px;'></div>";
        cuerpo += "</div>";        
        cuerpo += "<div class='row'>";
        cuerpo += "<div class='col-md-4'></div>";
        cuerpo += "<div class='col-md-4'><h2 style='text-align: center;'>"+json.partido.nombreEstadio+"</h2></div>";
        cuerpo += "<div class='col-md-4'></div>";        
        cuerpo += "</div>";        
        cuerpo += "<div class='row'>";
        cuerpo += "<div class='col-md-4'></div>";
        cuerpo += "<div class='col-md-4'><img alt='' src='../img/estadios/"+json.partido.fotoEstadio+"' style='width:100%;'></div>";
        cuerpo += "<div class='col-md-4'></div>";        
        cuerpo += "</div>";                
        cuerpo += "<div class='row'>";        
        cuerpo += "<h1>Mi Historico de Apuestas en este partido</h1>";        
        cuerpo += "</div>";                
        cuerpo += "<div class='row'>";        
        cuerpo += "<h1 style='text-align:center;'>Apuestas al ganador</h1>";    
        cuerpo += "</div>";                
         cuerpo += "<div class='row'>";
        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";
        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";
        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";        
        cuerpo += "</div>";
        $("#cuerpo").html(cuerpo);        
    });
    ocultarCargando();
}

function popApostar(idPartido) {
    window.parent.cambiarMenu("pages/info-partido.html");    
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