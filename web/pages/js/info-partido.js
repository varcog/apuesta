var url = "../infoPartidoController";
var idPartido;
$(document).ready(cargar);

function cargar() {
    idPartido = $.get("idPartido");
    $.post(url, {evento: "cargar",idPartido:idPartido}, function (resp) {
        var json = $.parseJSON(resp);
        $(".tableroF1").text(json.partido.fecha);
        $(".tableroF2").text(json.partido.hora);
        
        var cuerpo = "<option value='"+json.partido.idEquipo1+"'>"+json.partido.nombre1+"</option>";
        cuerpo += "<option value='"+json.partido.idEquipo2+"'>"+json.partido.nombre2+"</option>";
        $("select[name=equipo]").html(cuerpo);
        $(".equipo1.info-nombre").text(json.partido.nombre1);
        $(".equipo1.info-bandera").addClass(json.partido.icono1);
        $(".equipo1.info-marcador").text(0);
        $(".equipo2.info-nombre").text(json.partido.nombre2);
        $(".equipo2.info-bandera").addClass(json.partido.icono2);
        $(".equipo2.info-marcador").text(0);
        $(".info-grupo").text(json.partido.nombreGrupo );
        $(".nombre-estadio").text("Lugar: " + json.partido.nombreEstadio);
        $(".imagen-estadio").attr("src", "../img/estadios/" + json.partido.fotoEstadio);
        
//        var cuerpo = "";
//        cuerpo += "<div class='info-partido cancha'>";
//        cuerpo += "  <div class='info-header'>";
//        cuerpo += "    <div class='info-marcador'>0</div>";
//        cuerpo += "    <div class='info-tablero'>";
//        cuerpo += "    <div>" + json.partido.fecha + "</div>"; //Fecha Partido
//        cuerpo += "    <div>" + json.partido.hora + "</div>"; //Hora Partido
//        cuerpo += "    </div>";
//        cuerpo += "    <div class='info-marcador'>0</div>";
//        cuerpo += "  </div>";
//        cuerpo += "  <div class='info-body'>";
//        cuerpo += "    <div class='info-equipo'>";
//        cuerpo += "      <div class='info-bandera flag flag-icon-background " + json.partido.icono1 + "'></div>";
//        cuerpo += "      <div class='info-nombre'>" + json.partido.nombre1 + "</div>";
//        cuerpo += "    </div>";
//        cuerpo += "    <div class='info-vs'>VS</div>";
//        cuerpo += "    <div class='info-equipo'>";
//        cuerpo += "      <div class='info-bandera flag flag-icon-background " + json.partido.icono2 + "'></div>";
//        cuerpo += "      <div class='info-nombre'>" + json.partido.nombre2 + "</div>";
//        cuerpo += "    </div>";
//        cuerpo += "  </div>";
//        cuerpo += "  <div class='info-footer'>";
//        cuerpo += "    <div class='info-grupo'>" + json.partido.nombreGrupo + "</div>";
//        cuerpo += "  </div>";
//        cuerpo += "</div>";
        
        
        
//        var cuerpo = "<div class='row'>";
//        cuerpo += "<div class='col-lg-5 col-xs-5' style='height: 50px; text-align: center;'><h4>"+json.partido.nombre1+"</h4></div>";
//        cuerpo += "<div class='col-lg-2 col-xs-2'>";
//        cuerpo += "<h3 style='text-align: center;'></h3>";
//        cuerpo += "</div>";
//        cuerpo += "<div class='col-lg-5 col-xs-5' style='height: 50px; text-align: center;'><h4>"+json.partido.nombre2+"</h4></div>";
//        cuerpo += "</div>";        
//        cuerpo += "<div class='row'>";
//        cuerpo += "<div class='col-lg-5 col-xs-5  img-thumbnail flag flag-icon-background "+json.partido.icono1+"' style='height: 120px;'></div>";
//        cuerpo += "<div class='col-lg-2 col-xs-2'>";
//        cuerpo += "<h3 style='text-align: center;'>VS</h3>";
//        cuerpo += "</div>";
//        cuerpo += "<div class='col-lg-5 col-xs-5  img-thumbnail flag flag-icon-background "+json.partido.icono2+"' style='height: 120px;'></div>";
//        cuerpo += "</div>";        
//        cuerpo += "<div class='row'>";
//        cuerpo += "<div class='col-md-4'></div>";
//        cuerpo += "<div class='col-md-4'><h2 style='text-align: center;'>"+json.partido.nombreEstadio+"</h2></div>";
//        cuerpo += "<div class='col-md-4'></div>";        
//        cuerpo += "</div>";        
//        cuerpo += "<div class='row'>";
//        cuerpo += "<div class='col-md-4'></div>";
//        cuerpo += "<div class='col-md-4'><img alt='' src='../img/estadios/"+json.partido.fotoEstadio+"' style='width:100%;'></div>";
//        cuerpo += "<div class='col-md-4'></div>";        
//        cuerpo += "</div>";                
//        cuerpo += "<div class='row'>";        
//        cuerpo += "<h1>Mi Historico de Apuestas en este partido</h1>";        
//        cuerpo += "</div>";                
//        cuerpo += "<div class='row'>";        
//        cuerpo += "<h1 style='text-align:center;'>Apuestas al ganador</h1>";    
//        cuerpo += "</div>";                
//         cuerpo += "<div class='row'>";
//        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";
//        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";
//        cuerpo += "<div class='col-md-4' style='cursor:pointer;'></div>";        
//        cuerpo += "</div>";
//        $("#cuerpo").html(cuerpo);        
    });
    ocultarCargando();
}

function popApostar(idPartido) {
    window.parent.cambiarMenu("pages/info-partido.html");    
}
function buscarRetador1(event) {
    if(event.keyCode===13) buscarRetador();
}
function buscarRetador() {    
    var usr = $("input[name=buscarRetador]").val();
    $.post(url, {evento: "buscarRetador",usr:usr}, function (obj) {
        var obj = $.parseJSON(obj);
        if(obj){
            $("#nombreRetado").text(obj.nombres+" "+obj.apellidos);
            $("#fotoRetado").attr("src","../"+obj.foto);
            $("input[name=idUsuarioRetado]").val(obj.id);
            $("#retar").css("display","");
        }else{
            
        }        
    });    
}
function apostarCon() {
    var idEquipo = $("select[name=equipo]").val();
    var id=$("input[name=idUsuarioRetado]").val();
    $.post(url, {evento: "apostarCon",id:id,idPartido:idPartido,idEquipo:idEquipo}, function (resp) {
        
    });
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
