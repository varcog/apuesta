var url = "../fixtureController";

$(document).ready(cargar);

function cargar() {
    $.post(url, {evento: "cargar"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";        
        var grupo = "";        
        $.each(json,function(i,obj){                         
            if(grupo!==obj.grupo){   
                if(i>0) {
                    cuerpo+="</tbody>";
                    cuerpo+="</table>";
                    cuerpo+="</div>";
                    cuerpo+="</div>";                    
                    cuerpo+="</div>";                    
                }
                cuerpo+="<div class='col-xs-12'>";
                cuerpo+="<div class='box collapsed-box'>";
                cuerpo+="<div class='box-header'>";
                cuerpo+="<h3 class='box-title'>"+obj.grupo+"</h3>";
                cuerpo+="<div class='box-tools pull-right'>";
                cuerpo+="<button type='button' class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-plus'></i>";
                cuerpo+="</button>";                
                cuerpo+="</div>";
                cuerpo+="</div>";
                cuerpo+="<div class='box-body table-responsive no-padding' style='display:none;'>";                
                cuerpo+="<table class='table table-hover'>";
                cuerpo+="<tbody>";
                cuerpo+="<tr>";                
                cuerpo+="<th>Equipo 1</th>";                
                cuerpo+="<th>Equipo 2</th>";                
                cuerpo+="<th>Fecha</th>";                
                cuerpo+="<th>Apostar</th>";                
                cuerpo+="<th>Ver Partido</th>";                
                cuerpo+="</tr>";                
            }
            cuerpo+="<tr>";
            cuerpo+="<td><span class='flag-icon "+obj.icono1+"'></span>"+obj.eq1+" - "+obj.goles1+"</td>";
            cuerpo+="<td><span class='flag-icon "+obj.icono2+"'></span>"+obj.eq2+" - "+obj.goles2+"</td>";
            cuerpo+="<td>"+obj.fecha+"</td>";
            cuerpo+="<td><button class='btn btn-success' onclick='popApostar("+obj.idPartido+");'>Apostar</button></td>";
            cuerpo+="<td><button class='btn btn-success' onclick='verPartido("+obj.idPartido+",this);'>Ver</button></td>";
            cuerpo+="</tr>";
            grupo=obj.grupo;
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