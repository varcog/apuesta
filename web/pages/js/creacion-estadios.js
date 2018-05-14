var url = "../creacionEstadiosController";
var $tarjeta;
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
                cuerpo+="<div class='row rowEstadio'>";
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
    var tarjeta = "<div class='col-md-3 col-sm-6 col-xs-12 estadio' style='cursor:pointer;' onclick='popRegistrarEstadio("+obj.id+",this);'>";
        tarjeta += "<div class='info-box bg-green'>";
        tarjeta += "<span class='info-box-icon'><img class='flag-icon foto' src='../img/estadios/"+obj.foto+"' alt=''/></span>";
        tarjeta += "<div class='info-box-content'>";
        tarjeta += "<span class='info-box-text nombre'>"+obj.nombre+"</span>";        
        tarjeta += "<span data-desc='"+obj.descripcion+"' class='desc'>"+obj.capacidad+"</span>";
        tarjeta += "</div>";
        tarjeta += "</div>";
        tarjeta += "</div>";
    return tarjeta;
}
function armarTargetaInterna(obj) {
    var tarjeta = "";
        tarjeta += "<div class='info-box bg-green'>";
        tarjeta += "<span class='info-box-icon'><img class='flag-icon foto' src='../img/estadios/"+obj.foto+"' alt=''/></span>";
        tarjeta += "<div class='info-box-content'>";
        tarjeta += "<span class='info-box-text nombre'>"+obj.nombre+"</span>";        
        tarjeta += "<span data-desc='"+obj.descripcion+"' class='desc'>"+obj.capacidad+"</span>";
        tarjeta += "</div>";
        tarjeta += "</div>";        
    return tarjeta;
}
function buscar(e) {    
    var b = $("input[name=buscador]").val();
    if(b.length>0){
        b=b.toUpperCase();
        $(".estadio").css("display","none");
        $(".nombre:contains('"+b+"')").parent().parent().parent().css("display","");
    }else{
        $(".estadio").css("display","");
    }    
}

function popRegistrarEstadion() {        
    $("input[name=id]").val(0);
    $("input[name=nombre]").val("");    
    $("input[name=old]").val("");    
    $("input[name=capacidad]").val("0");    
    $("textarea[name=descripcion]").val("");    
    $(".est_img").attr("src","");
    $("#btnCrear").text("CREAR");
    $tarjeta=null;
    $("#btnEliminar").css("display","none");
    openModal('#popCreacionEstadio');
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
            var estadio = $.parseJSON(data);
            if($tarjeta){
                var cuerpo = armarTargetaInterna(estadio);
                $($($tarjeta).html(cuerpo));
            }else{
                var cant= $(".rowEstadio:last").find(".estadio").length;
                var cant1= $(".rowEstadio").length;
                if(cant==4 || cant1==0){
                    var cuerpo ="<div class='row rowEstadio'>";
                    cuerpo+=armarTargeta(estadio);
                    cuerpo +="</div>";
                    $("#cuerpo").append(cuerpo);
                }else{
                    var cuerpo=armarTargeta(estadio);
                    $(".rowEstadio:last").append(cuerpo);
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

function popRegistrarEstadio(id,ele) {
    $tarjeta=ele;
    $("#btnEliminar").css("display","");
    var nombre = $(ele).find(".nombre").text();
    var desc = $(ele).find(".desc").data("desc");
    var capacidad = $(ele).find(".desc").text();    
    var foto = $(ele).find(".foto").attr("src");
    $("input[name=id]").val(id);
    $("input[name=nombre]").val(nombre);
    $("input[name=capacidad]").val(capacidad);    
    $("textarea[name=descripcion]").val(desc);
    $(".est_img").attr("src",foto);
    $("input[name=old]").val(foto);    
    $("#btnCrear").text("MODIFICAR");
    openModal('#popCreacionEstadio');
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