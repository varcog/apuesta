var url = "../creacionEquiposController";

$(document).ready(function(){
    init();
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var cont = 1;
        var cuerpo = "";
        $.each(json,function(i,equipo){
            if(cont===1){
                cuerpo+="<div class='row'>";
            }
            cuerpo+=armarTargeta(equipo);
            if(cont===4){
                cuerpo+="</div>";
                cont=0;
            }
            cont++;
        });
        if(cont!==1){
            cuerpo+="</div>";
        }
        $("#cuerpo").html(cuerpo);        
        ocultarCargando();
    });
}

function armarTargeta(obj) {
    var tarjeta = "<div class='col-md-3 col-sm-6 col-xs-12 pais' style='cursor:pointer;'>";
        tarjeta += "<div class='info-box bg-green'>";
        tarjeta += "<span class='info-box-icon'><span class='flag-icon "+obj.icono+"'></span></span>";
        tarjeta += "<div class='info-box-content'>";
        tarjeta += "<span class='info-box-text nombre'>"+obj.nombre+"</span>";
        tarjeta += "<span class='info-box-number'>0</span>";
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