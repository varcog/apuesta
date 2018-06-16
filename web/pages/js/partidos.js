var url = "../partidosController";

$(document).ready(cargar);

function cargar() {
    $.post(url, {evento: "cargar"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";        
        var fecha = "";        
        var hoy = new Date();
        var dia = hoy.getDate();
        if(dia<10) dia="0"+dia;
        var mes = hoy.getMonth()+1;
        if(mes<10) mes = "0"+mes;
        var ano = hoy.getFullYear();
        var fa = dia+"/"+mes+"/"+ano;
        $.each(json,function(i,obj){                         
            if(fecha!==obj.soloFecha){   
                if(i>0) {
                    cuerpo+="</tbody>";
                    cuerpo+="</table>";
                    cuerpo+="</div>";
                    cuerpo+="</div>";                    
                    cuerpo+="</div>";                    
                }
                cuerpo+="<div class='col-xs-12'>";
                if(fa===obj.soloFecha) cuerpo+="<div class='box'>";
                else cuerpo+="<div class='box collapsed-box'>";
                cuerpo+="<div class='box-header'>";
                cuerpo+="<h3 class='box-title'>"+obj.soloFecha+"</h3>";
                cuerpo+="<div class='box-tools pull-right'>";                
                if(fa===obj.soloFecha) cuerpo+="<button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i>";
                else cuerpo+="<button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-plus'></i>";
                cuerpo+="</button>";                
                cuerpo+="</div>";
                cuerpo+="</div>";
                if(fa===obj.soloFecha) cuerpo+="<div class='box-body table-responsive no-padding' style='display:block;'>";                
                else cuerpo+="<div class='box-body table-responsive no-padding' style='display:none;'>";                
                cuerpo+="<table class='table table-hover'>";
                cuerpo+="<tbody>";                 
            }
            if(obj.estado===0) cuerpo+="<tr class='bg-success' onclick='popApostar("+obj.idPartido+");'>";
            else cuerpo+="<tr class='bg-danger'>";
            cuerpo+="<td class='text-left'><span class='flag-icon "+obj.icono1+"'></span>"+obj.eq1+"</td>";
            cuerpo+="<td class='text-center'>"+obj.soloHora+"</td>";
            cuerpo+="<td class='text-right'>"+obj.eq2+"<span class='flag-icon "+obj.icono2+"'></span></td>";
            cuerpo+="</tr>";
            fecha=obj.soloFecha;
        });
        $("#cuerpo").html(cuerpo);        
    });
    ocultarCargando();
}

function popApostar(idPartido) {
    window.parent.cambiarMenu("pages/info-partido.html?idPartido="+idPartido);    
}

function verPartido(idPartido,btn) {    
    window.parent.cambiarMenu("pages/verPartido.html?idPartido="+idPartido);    
}