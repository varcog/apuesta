var url = "../creacionJugadoresController";

$(document).ready(function(){
    init();
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        
        var cuerpo = "";        
        $.each(json,function(i,equipo){
            cuerpo+="<h2 class='page-header' style='cursor:pointer;' data-generados=false onclick='verJugadores("+equipo.id+",this);'><span style='margin-right: 5px;' class='flag-icon "+equipo.icono+"'></span>"+equipo.nombre+"</h2>";            
        });
        
        $("#cuerpo").html(cuerpo);
        ocultarCargando();
    });
}

function armarTargeta(obj) {            
    var tarjeta = "<div class='col-md-4'>";
    tarjeta += "<div class='box box-widget widget-user'>";
    tarjeta += "<div class='widget-user-header bg-black' style='background: url('../dist/img/photo1.png') center center;'>";
    tarjeta += "<h3 class='widget-user-username'>"+obj.nombres+" "+obj.apellidos+"</h3>";
    tarjeta += "<h5 class='widget-user-desc'>"+obj.posicion+"</h5>";
    tarjeta += "</div>";
    tarjeta += "<div class='widget-user-image'>";
    tarjeta += "<img class='img-circle' src='../img/jugadores/"+obj.foto+"' alt='Jugador'>";
    tarjeta += "</div>";
    tarjeta += "<div class='box-footer'>";
    tarjeta += "<div class='row'>";
    tarjeta += "<div class='col-sm-4 border-right'>";
    tarjeta += "<div class='description-block'>";
    tarjeta += "<h5 class='description-header'>19</h5>";
    tarjeta += "<span class='description-text'>EDAD</span>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "<div class='col-sm-4 border-right'>";
    tarjeta += "<div class='description-block'>";
    tarjeta += "<h5 class='description-header'>PSG</h5>";
    tarjeta += "<span class='description-text'>Equipo</span>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "<div class='col-sm-4'>";
    tarjeta += "<div class='description-block'>";
    tarjeta += "<h5 class='description-header'>0</h5>";
    tarjeta += "<span class='description-text'>GOLES</span>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "</div>";
        
    return tarjeta;
}
function buscar(e) {    
    var b = $("input[name=buscador]").val();
    if(b.length>0){
        b=b.toUpperCase();
        $(".pais").css("display","none");
        $(".nombre:contains('"+b+"')").parent().parent().parent().css("display","");
    }else{
        $(".pais").css("display","");
    }    
}

function verJugadores(id,ele) {
    if($(ele).data("generado")){
        if($(".id_"+id).css("display")==="none") $(".id_"+id).css("display","");
        else $(".id_"+id).css("display","none");
    }else{
        var cont = 1;
        var cuerpo = "";
        $.post(url, {evento: "verJugadores",id:id}, function (resp) {
            var json = $.parseJSON(resp);
            cuerpo+="<i class='fa fa-plus-circle' style='color: green; float: right; cursor:pointer; font-size: 26px;' title='Agregar' onclick='popRegistrarJugador("+id+",this);'></i>";
            cuerpo+="<div class='row id_"+id+"'>";
            $.each(json,function(i,jugador){                
                cuerpo+=armarTargeta(jugador);
                if(cont===3){
                    cuerpo+="</div>";
                    cont=0;
                }    
                cont++;
            });
            if(cont!==1){
                cuerpo+="</div>";
            }
            $(ele).after(cuerpo);
            $(ele).data("generado",true);
        });    
    }    
}

function popRegistrarJugador() {
    
}