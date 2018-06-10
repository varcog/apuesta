var url = "../notificacionesController";

$(document).ready(init);

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json,function(i,obj){
            cuerpo += armar(obj);
        });
        $("#chat-box").html(cuerpo);
        $("#navNovedades").find("li").removeClass("bg-warning");
        $("#cantNot").text("0");
        ocultarCargando();
    });
}

function armar(obj) {
    var cuerpo = "<div class='item'>";
    cuerpo += "<img src='../"+obj.foto+"' alt='user image' class='online'>";
    cuerpo += "<p class='message'>";
    cuerpo += "<a href='#' class='name'>";
    cuerpo += "<small class='text-muted pull-right'><i class='fa fa-clock-o'></i> "+obj.fecha+"</small>";
    cuerpo += obj.nombre;
    cuerpo += "</a>";
    cuerpo += obj.descripcion;
    cuerpo += "</p>";    
    cuerpo += "</div>";
    cuerpo += "</div>";
    return cuerpo;
}