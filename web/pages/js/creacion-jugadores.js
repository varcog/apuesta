var url = "../creacionJugadoresController";
var $btn;
$(document).ready(function(){
    init();
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        
        var cuerpo = "";        
        $.each(json,function(i,equipo){
            cuerpo+="<i class='fa fa-plus-circle' style='color: green; float: left; cursor:pointer; font-size: 26px; margin-left:15px;' title='Agregar' onclick='popRegistrarJugador("+equipo.id+",this);'></i><h2 class='page-header' style='cursor:pointer;' data-generados=false onclick='verJugadores("+equipo.id+",this);'><span style='margin-right: 5px;' class='flag-icon "+equipo.icono+"'></span>"+equipo.nombre+"</h2>";            
        });
        
        $("#cuerpo").html(cuerpo);
        ocultarCargando();
    });
}

function armarTargeta(obj) {            
    var tarjeta = "<div class='col-md-4 jugador' style='cursor:pointer;' onclick='editarJugador("+obj.id+",this);'>";
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
    tarjeta += "<h5 class='description-header'>"+obj.detalle+"</h5>";
    tarjeta += "<span class='description-text'>EQUIPO</span>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "<div class='col-sm-4 border-right'>";
    tarjeta += "<div class='description-block'>";
    tarjeta += "<h5 class='description-header'>"+obj.edad+"</h5>";
    tarjeta += "<span class='description-text'>EDAD</span>";
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
function armarTargetaInterna(obj) {            
    var tarjeta = "";
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
    tarjeta += "<h5 class='description-header'>"+obj.detalle+"</h5>";
    tarjeta += "<span class='description-text'>EQUIPO</span>";
    tarjeta += "</div>";
    tarjeta += "</div>";
    tarjeta += "<div class='col-sm-4 border-right'>";
    tarjeta += "<div class='description-block'>";
    tarjeta += "<h5 class='description-header'>"+obj.edad+"</h5>";
    tarjeta += "<span class='description-text'>EDAD</span>";
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

function popRegistrarPosicion() {
    $("input[name=nombrePosicion]").val("");
    openModal('#popCreacionPosiciones');
}
function guardarPosicion() {
    var nombrePosicion= $("input[name=nombrePosicion]").val();
    $.post(url, {evento: "guardarPosicion",nombrePosicion:nombrePosicion}, function (resp) {
        $("select[name=posicion]").append("<option value='"+resp+"'>"+nombrePosicion+"</option>");
        cerrarModal();
    });
}
function verJugadores(id,ele) {
    if($(ele).data("generado")){
        if($(".rowJugador_"+id).css("display")==="none") $(".rowJugador_"+id).css("display","");
        else $(".rowJugador_"+id).css("display","none");
    }else{
        var cont = 1;
        var cuerpo = "";
        $.post(url, {evento: "verJugadores",id:id}, function (resp) {
            var json = $.parseJSON(resp);                        
            $.each(json,function(i,jugador){        
                if(cont===1){
                    cuerpo+="<div class='row rowJugador_"+id+"'>";                    
                }
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

function editarJugador(idJugador, ele) {
    $tarjeta=ele;
    $.post(url, {evento: "editarJugador",idJugador:idJugador}, function (resp) {
        var obj = $.parseJSON(resp);
        var jugador = obj.jugador;
        var cuerpo = "";
        $.each(obj.posiciones,function(i,pos){
            cuerpo+="<option value='"+pos.id+"'>"+pos.nombre+"</option>";
        });
        $("select[name=posicion]").html(cuerpo);
        $("#btnEliminar").css("display","");        
        $("input[name=id]").val(jugador.id);
        $("input[name=idEquipo]").val(jugador.idEquipo);
        $("input[name=nombres]").val(jugador.nombres);
        $("input[name=apellidos]").val(jugador.apellidos);
        $("input[name=fechaNacimiento]").val(jugador.fechaNacimiento);
        $("select[name=posicion]").val(jugador.idPosicion);
        $("textarea[name=detalle]").val(jugador.detalle);
        $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});        
        //$("[data-mask]").inputmask();
        $('.calendario').calendario();
        $(".est_img").attr("src","../img/jugadores/"+jugador.foto);
        $("input[name=old]").val(jugador.foto);    
        $("#btnCrear").text("MODIFICAR");
        openModal('#popCreacionJugador');
    });
}

function popRegistrarJugador(idEquipo,btn) {
    $btn=btn;
    $.post(url, {evento: "popRegistrarJugador"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json,function(i,pos){
            cuerpo+="<option value='"+pos.id+"'>"+pos.nombre+"</option>";
        });
         $("select[name=posicion]").html(cuerpo);
        $("input[name=id]").val(0);    
        $("input[name=idEquipo]").val(idEquipo);    
        $("input[name=old]").val("");
        $("input[name=foto]").val("");
        $("input[name=nombres]").val("");
        $("input[name=apellidos]").val("");    
        $("#fechaNacimiento").val("");
        $("#fechaNacimiento").val("");       
        $("textarea[name=detalle]").val("");
        $(".est_img").attr("src","");
         $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});        
        //$("[data-mask]").inputmask();
        $('.calendario').calendario();
        $(".est_img").attr("src","");
        $("#btnCrear").text("CREAR");
        $tarjeta=null;
        $("#btnEliminar").css("display","none");
        openModal('#popCreacionJugador');
    });    
}

function cambiarFoto() {
    $("input[name=foto]").click();
}

function okCambiarFoto(ele) {
    var input = ele;
    var url = $(ele).val();
    var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
    if (input.files && input.files[0]&& (ext == "gif" || ext == "png" || ext == "jpeg" || ext == "jpg")) 
     {
        var reader = new FileReader();

        reader.onload = function (e) {
           $('.est_img').attr('src', e.target.result);
        }
       reader.readAsDataURL(input.files[0]);
    }
}
function guardarDatos() {
    mostrarCargando();
    var pos=$("select[name=posicion]").val();
    if(!parseInt(pos)>0){
        openAlert("Ingrese la Posicion del Jugador","Falta Info");
        ocultarCargando();
        return;
    }
    var fecha=$("input[name=fechaNacimiento]").val();
    if(fecha.length===0){
        openAlert("Ingrese la Fecha de Nacimiento","Falta Info");
        ocultarCargando();
        return;
    }
    var formData = new FormData($("#form")[0]);
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,        
        success: function (data, textStatus, jqXHR)
        {                 
            var jugador = $.parseJSON(data);
            if($tarjeta){
                var cuerpo = armarTargetaInterna(jugador);
                $($($tarjeta).html(cuerpo));
            }else{
                var cant= $(".rowJugador_"+jugador.idEquipo+":last").find(".jugador").length;
                var cant1= $(".rowJugador_"+jugador.idEquipo).length;
                if(cant1===0){
                    var cuerpo ="<div class='row rowJugador_"+jugador.idEquipo+"'>";
                    cuerpo+=armarTargeta(jugador);
                    cuerpo +="</div>";                    
                    $($btn).next().after(cuerpo);
                }else if(cant===3){
                    var cuerpo ="<div class='row rowJugador_"+jugador.idEquipo+"'>";
                    cuerpo+=armarTargeta(jugador);
                    cuerpo +="</div>";                    
                    $($btn).next().after(cuerpo);
                }else {
                    var cuerpo=armarTargeta(jugador);
                    $(".rowJugador_"+jugador.idEquipo+":last").append(cuerpo);
                }
            }
            cerrarModal();
            ocultarCargando();
            $("input[name=foto]").val("");                     
        },
        error: function (jqXHR, textStatus, errorThrown)
        {               
            ocultarCargando();
            $("input[name=foto]").val("");
        }         
    });
}


function eliminar() {
    var id = $("input[name=id]").val();
    $.post(url, {evento: "eliminar", id:id}, function (resp) {
        if(resp==="true"){
            $($tarjeta).remove();
            cerrarModal();
        }
    });
    
}