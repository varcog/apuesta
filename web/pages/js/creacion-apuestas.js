var url = "../creacionApuestasController";
var $ele;
$(document).ready(cargar());

function cargar() {
    $.post(url, {evento: "cargar"}, function (resp) {
        var json = $.parseJSON(resp);
        var cuerpo = "";
        $.each(json,function(i,obj){
            cuerpo += armarFila(obj);            
        });
        $("#cuerpo").html(cuerpo);
        ocultarCargando();
    });
}

function armarFila(obj) {
    var cuerpo = "";        
    cuerpo += "<tr onclick='popEditarApuestas("+obj.id+",this);'>";
    cuerpo += "<td>"+obj.id+"</td>";
    if(obj.tipo===1) cuerpo += "<td>Equipo</td>";
    else cuerpo += "<td>Gol</td>";
    cuerpo += "<td>"+obj.equipo1+"</td>";
    cuerpo += "<td>"+obj.equipo2+"</td>";
    cuerpo += "</tr>";
    return cuerpo;
}
function armarFilaInterna(obj) {
    var cuerpo = "";            
    cuerpo += "<td>"+obj.id+"</td>";
    if(obj.tipo===1) cuerpo += "<td>Equipo</td>";
    else cuerpo += "<td>Gol</td>";
    cuerpo += "<td>"+obj.equipo1+"</td>";
    cuerpo += "<td>"+obj.equipo2+"</td>";    
    return cuerpo;
}
function popCrearApuestas() {
    $("#btnCrear").css("display","");
    $("#btnEliminar").css("display","none");
    $("select[name=tipoApuesta]").val(1);
    $("input[name=id]").val(0);
    $("input[name=equipo1]").val(0);
    $("input[name=equipo2]").val(0);
    openModal("#popCreacionTipoApuesta");
}
function popEditarApuestas(id,ele) {
    $ele=ele;
    $.post(url, {evento: "popEditarApuestas",id:id}, function (resp) {
        var obj = $.parseJSON(resp);
        $("#btnCrear").css("display","");
        $("#btnEliminar").css("display","");
        $("select[name=tipoApuesta]").val(obj.tipo);
        $("input[name=id]").val(obj.id);
        $("input[name=equipo1]").val(obj.equipo1);
        $("input[name=equipo2]").val(obj.equipo2);
        openModal("#popCreacionTipoApuesta");
    });    
}
function guardarDatos() {
    mostrarCargando();
    var id = $("input[name=id]").val();
    var tipoApuesta = $("select[name=tipoApuesta]").val();
    var equipo1 = $("input[name=equipo1]").val();
    var equipo2 = $("input[name=equipo2]").val();
    $.post(url, {evento: "guardarDatos",id:id,tipoApuesta:tipoApuesta,equipo1:equipo1,equipo2:equipo2}, function (resp) {
        var obj = $.parseJSON(resp);         
        if(id==0){
            $("#cuerpo").append(armarFila(obj));
        }else{
            $($ele).html(armarFilaInterna(obj));
        }
        ocultarCargando();
    });
}
function eliminar() {
    mostrarCargando();
    var id = $("input[name=id]").val();
    $.post(url, {evento: "eliminar",id:id}, function (resp) {
        if(resp==="true") $($ele).remove();
        ocultarCargando();
    });
}