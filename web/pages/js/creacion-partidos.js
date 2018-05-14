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
                var res = fecha.split("/");
                var cod = "";
                $.each(res,function(i,obj){
                    cod+=obj;
                });
                cuerpo+="<li class='time-label'>";
                cuerpo+="<span class='bg-red fec_"+cod+" fecha'>";
                cuerpo+=fecha;
                cuerpo+="</span>";
                cuerpo+="</li>";                
            }
            cuerpo+=armarPartidos(partido);
        });
        $("#cuerpo").html(cuerpo);
        cuerpo="";
        $.each(json.Estadios,function(i,estadio){
            cuerpo+="<option value='"+estadio.id+"'>"+estadio.nombre+"</option>";
        });
        $("select[name=estadio]").html(cuerpo);
        ocultarCargando();
    });
}
function armarPartidos(partido) {
    var cuerpo = "<li>";
    cuerpo+="<i class='fa fa-futbol-o bg-blue'></i>";    
    cuerpo+="<div class='timeline-item'>";
    cuerpo+="<span class='time'><i class='fa fa-clock-o'></i>"+partido.hora+"</span>";
    cuerpo+="<h3 class='timeline-header'><a href='#'>"+partido.nombre1+"<span style='margin-left:5px;' class='flag-icon "+partido.icono1+"'></span></a> VS <a href='#'>"+partido.nombre2+"<span style='margin-left:5px;' class='flag-icon "+partido.icono2+"'></span></a></h3>";
    cuerpo+="<div class='timeline-body'>";
    cuerpo+="<h2>"+partido.nombreEstadio+"</h2>";    
    cuerpo+="<img alt='' src='../img/estadios/"+partido.fotoEstadio+"' style='width:240px;'/>";
    cuerpo+="</div>";
    cuerpo+="<div class='timeline-footer'>";    
    cuerpo+="<a class='btn btn-danger btn-xs' style='margin-left:5px;' onclick='eliminar("+partido.id+",this);'>Eliminar</a>";
    cuerpo+="</div>";
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
    var idEstadio = $("select[name=estadio]").val();
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
    if(idEstadio<=0){
        return ;
    }
    mostrarCargando();
    $.post(url, {evento: "crearPartido",idEstadio:idEstadio,fecha:fecha,hora:hora,id1:id1,id2:id2}, function (resp) {
        var partido = $.parseJSON(resp);
        addPartido(partido);
        ocultarCargando();
        cerrarModal();        
    });    
}
function addPartido(partido) {
    var res = partido.fecha.split("/");
    var cod = "";
    $.each(res,function(i,obj){
        cod+=obj;
    });
    if($(".fec_"+cod).length>0){                
        $(".fec_"+cod).parent().after(armarPartidos(partido));
    }else{
        var fecha = partido.fecha;
        var cuerpo = "";
        var res = fecha.split("/");
        var cod = "";
        $.each(res,function(i,obj){
            cod+=obj;
        });
        cuerpo+="<li class='time-label'>";
        cuerpo+="<span class='bg-red fec_"+cod+" fecha'>";
        cuerpo+=fecha;
        cuerpo+="</span>";
        cuerpo+="</li>";
        cuerpo+=armarPartidos(partido);
        $("#cuerpo").append(cuerpo);
    }
}
function modificar(id) {
    
}

function eliminar(id,ele) {
    $.post(url, {evento: "eliminar",id:id}, function (resp) {
        if(resp==="true"){
            $(ele).parent().parent().parent().remove();
        }
    });
}