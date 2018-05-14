var url = "../editarPerfilController";

$(document).ready(function(){
    init();
});

function init() {
    $.post(url, {evento: "init"}, function (resp) {
        var usuario = $.parseJSON(resp);
        $(".usr_img").attr("src","../"+usuario.foto);
        $(".profile-ci").text(usuario.ci);
        $(".profile-perfil").text(usuario.perfil);
        $("input[name=nombres]").val(usuario.nombres);
        $("input[name=apellidos]").val(usuario.apellidos);
        $("input[name=fecNac]").val(usuario.fechaNacimiento);
        $("input[name=telefono]").val(usuario.telefono);
        $("select[name=sexo]").val(usuario.sexo);
        $("textarea[name=direccion]").val(usuario.direccion);
        $(".calendario").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});    
        $(".celular").inputmask();    
            //$("[data-mask]").inputmask();
        $('.calendario').calendario();
        ocultarCargando();
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
           $('.usr_img').attr('src', e.target.result);
        }
       reader.readAsDataURL(input.files[0]);
    }
}
function guardarDatos() {
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
            if(data.length>0){
                $(window.parent.document).find(".usr_img").attr("src",data);
            }
            openAlert("Guardado Correctamente.", "Información");
            $("input[name=foto]").val("");                     
        },
        error: function (jqXHR, textStatus, errorThrown)
        {   
            openAlert("ERROR AL GUARDAR.", "ERROR");
            $("input[name=foto]").val("");
        }         
    });
}