var url = "../EntregaRelacionadorController";
var tabla, tablaSubMenu;
$(document).ready(function () {
    init();
});

function init() {
    mostrarCargando();
    formato_decimal_all();
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json.entregaRelacionador, function (i, obj) {
            html += entregaRelacionadorFilaHtml(obj);
        });
        $("#cuerpo").html(html);
        html = "";
        $.each(json.relacionadores, function (i, obj) {
            html += "<option value='" + obj.id + "'>" + (obj.nombre || "") + "</option>";
        });
        $("#c_relacionador").html(html);
        html = "";
        $.each(json.entregas, function (i, obj) {
            html += "<option value='" + obj.id + "'>" + (obj.nombre || "") + "</option>";
        });
        $("#c_entrega").html(html);

        formato_decimal_all();

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            }
        });
        ocultarCargando();
    });
}

function entregaRelacionadorFilaHtml(obj) {
    var tr = "<tr data-id='" + obj.id + "' class='er_" + obj.id + "'>";
    tr += "<td>" + (obj.fecha || "") + "</td>";
    tr += "<td class='text-right'>" + new BigNumber(obj.monto || "0").toFormat() + "</td>";
    tr += "<td>" + (obj.relacionador || "") + "</td>";
    tr += "<td>" + (obj.entrega || "") + "</td>";
    tr += "</tr>";
    return tr;
}

function popRegistrarEntregaRelacionador() {
    $("#c_id").val(0);
    $("#c_monto").val(0);
    $('#boton_perfil').text("Entregar Credito a Relacionador");
    openModal('#registroModal');
}

function guardarEntregaRelacionador() {
    var monto = parseFloat($("#c_monto").val());
    if (isNaN(monto) || monto <= 0) {
        openAlert("El Monto debe ser Mayor a 0.");
        return;
    }
    popConfirmarGuardar();
}

function popConfirmarGuardar() {
    $("#confirmarModalText").html("¿Esta seguro de Asignar a <strong>" + $("#c_relacionador").find("option:selected").text() + "</strong> el monto de <strong>" + $("#c_monto")[0].value + "</strong>?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okGuardarEntregaRelacionador);
    openModal('#confirmarModal');
}

function okGuardarEntregaRelacionador() {
    mostrarCargando();
    var id = $("#c_id").val();
    var relacionador = $("#c_relacionador").val();
    var entrega = $("#c_entrega").val();
    var monto = parseFloat($("#c_monto").val());
    $.post(url, {evento: "guardarEntregaRelacionador", id: id, monto: monto, relacionador: relacionador, entrega: entrega}, function (resp) {
        if (resp === "false") {
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var json = $.parseJSON(resp);
            tabla.row.add($(entregaRelacionadorFilaHtml(json))).draw(false);
            cerrarModal();
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}
