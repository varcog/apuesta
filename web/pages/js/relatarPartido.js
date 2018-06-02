var url = "../relatarPartidoController";
var $idPArtido;
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
        cuerpo = "";
        $.each(json.eventos,function(i,obj){
            cuerpo += "<option value='"+obj.id+"'>"+obj.evento+"</option>";
        });
        $("select[name=evento]").html(cuerpo);
        cargarJugadores(json.partido.idEquipo1,1);
        cargarJugadores(json.partido.idEquipo2,2);
        ocultarCargando();
    });
}
function cargarJugadores(idEquipo,eq) {
    $.post(url, {evento: "cargarJugadores",idEquipo:idEquipo}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json,function(i,obj){
            cuerpo+="<option value='"+obj.id+"' data-idEquipo='"+idEquipo+"'>"+(obj.nombres||"")+" "+(obj.apellidos||"")+"</option>";
        });
        $("#jugadores"+eq).html(cuerpo);        
    });
}

function armarEvento(obj) {
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
function eliminar(id,ele) {
    $.post(url, {evento: "eliminar", id:id}, function (resp) {
        if(resp==="true"){
            $(ele).parent().parent().parent().remove();
        }
    });    
}
function crearEvento() {
    
    var idJugador= 0;
    $("select[name=evento]").css("background","");
    $("#jugadores1").css("background","");
    $("#jugadores2").css("background","");
    var idEvento = $("select[name=evento]").val();
    if(idEvento>0){
        idJugador = $("#jugadores1").val();
        if(idJugador>0){
            
        }else{
            idJugador = $("#jugadores2").val();
            if(idJugador>0){
                
            }else{
                $("#jugadores1").css("background","#DD4B39");
                $("#jugadores2").css("background","#DD4B39");
                return;
            }
        }
    }else{
        $("select[name=evento]").css("background","#DD4B39");
        return;
    }
    $.post(url, {evento: "crearEvento", idPartido:$idPArtido, idJugador:idJugador, idEvento:idEvento}, function (resp) {
        var obj = $.parseJSON(resp);
        var cuerpo = armarEvento(obj);
        $("#cuerpo").prepend(cuerpo);
        quitarJugadores();
        $("select[name=evento]").val(0);
        $(".fa-minus").click();
    });
}

function buscar1(e) {
    var b = $("input[name=buscador1]").val();
    if(b.length>0){
        $("#jugadores1 option").css("display","none");        
        $("#jugadores1 option:contains('"+b+"')").css("display","");
    }else{
        $("#jugadores1 option").css("display","");
    }
}
function buscar2(e) {
    var b = $("input[name=buscador2]").val();
    if(b.length>0){
        $("#jugadores2 option").css("display","none");        
        $("#jugadores2 option:contains('"+b+"')").css("display","");
    }else{
        $("#jugadores2 option").css("display","");
    }
}

function quitarJugadores() {
    $("#jugadores1").val(0);
    $("#jugadores2").val(0);
}
function quitar1(){
    $("#jugadores1").val(0);
}
function quitar2(){
    $("#jugadores2").val(0);
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
