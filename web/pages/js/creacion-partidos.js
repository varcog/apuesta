var url = "../creacionPartidosController";

$(document).ready(function(){
    init();    
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);        
        var cuerpo = "";
        $.each(json.Equipos,function(i,equipo){
            cuerpo+="<option value='"+equipo.id+"'>"+equipo.nombre+"</option>";
        });       
        $("select[name=equipo1]").html(cuerpo);
        $("select[name=equipo2]").html(cuerpo);
        cuerpo="";
        var fecha = "";
        $.each(json.Partidos,function(i,partido){
            if(fecha!==partido.fecha){
                fecha = partido.fecha;
                cuerpo+="<li class='time-label'>";
                cuerpo+="<span class='bg-red'>";
                cuerpo+=fecha;
                cuerpo+="</span>";
                cuerpo+="</li>";                
            }
            cuerpo+=armarPartidos(partido);
        });       
        $("#cuerpo").html(cuerpo);        
        ocultarCargando();
    });
}
function armarPartidos(partido) {
    var cuerpo = "<li>";
    cuerpo+="<i class='fa fa-envelope bg-blue'></i>";
    cuerpo+="<div class='timeline-item'></div>";
    cuerpo+="<span class='time'><i class='fa fa-clock-o'></i>"+partido.hora+"</span>";
    cuerpo+="<h3 class='timeline-header'><a href='#'>"+partido.nombre1+" 0</a> VS <a href='#'>"+partido.nombre2+" 0</a></h3>";
    cuerpo+="<div class='timeline-body'>";
    cuerpo+="";
    cuerpo+="</div>";
    cuerpo+="<div class='timeline-footer'>";
    cuerpo+="<a class='btn btn-primary btn-xs'>VER RESUMEN</a>";
    cuerpo+="</div>";
    cuerpo+="</li>";
    return cuerpo;
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
function popRegistrarPartido() {    
    $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});        
    //$("[data-mask]").inputmask();
    $('.calendario').calendario();
    $(".timepicker").bootstrapMaterialDatePicker({ date: false , format : 'HH:mm'});
    openModal('#popCreacionPartido');
}

function crearPartido(){
    var fecha = $("#fechaPartido").val();
    var hora = $("input[name=horaPartido]").val();
    var id1 = $("select[name=equipo1]").val();
    var id2 = $("select[name=equipo2]").val();
    if(id1==id2){
        $("select[name=equipo1]").val("");
        $("select[name=equipo2]").val("");
        return ;
    }
    if(fecha.length===0){
        $("#fechaPartido").focus();
        return;
    }
    if(hora.length===0){
        $("input[name=horaPartido]").focus();
        return;
    }
    $.post(url, {evento: "crearPartido",fecha:fecha,hora:hora,id1:id1,id2:id2}, function (resp) {
        var partido = $.parseJSON(resp);
        cerrarModal();        
    });    
}