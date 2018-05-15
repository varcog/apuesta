var url = "../PrestamoCreditoController";
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
        $.each(json.prestamos, function (i, obj) {
            html += prestamosFilaHtml(obj);
        });
        $("#cuerpo").html(html);
        html = "";
        $.each(json.relacionadores, function (i, obj) {
            html += "<option value='" + obj.id + "'>" + (obj.nombre || "") + "</option>";
        });
        $("#c_relacionador").html(html);
        html = "";
        formato_decimal_all();

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            }
        });
        ocultarCargando();
    });
}

function prestamosFilaHtml(obj) {
//    fa-eye
    var tr = "<tr data-idUsuario='" + obj.idUsuario + "' class='pr_" + obj.idUsuario + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td class='text-right'>" + new BigNumber(obj.deuda || "0").toFormat() + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-eye text-warning' title='Ver Sub Menu' onclick='popVerDetalle(" + obj.idUsuario + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-money text-green' title='Pagar' onclick='popPagar(" + obj.idUsuario + ",this);'></i>";
    tr += "</td>";
    tr += "</tr>";
    return tr;
}

function popPrestar() {
    $("#c_id").val(0);
    $("#c_monto").val(0);
    openModal('#prestarModal');
}

function prestar() {
    var monto = parseFloat($("#c_monto").val());
    if (isNaN(monto) || monto <= 0) {
        openAlert("El Monto debe ser Mayor a 0.");
        return;
    }
    popConfirmarPrestar();
}

function popConfirmarPrestar() {
    $("#confirmarModalText").html("¿Esta seguro de Prestar Credito a <strong>" + $("#c_relacionador").find("option:selected").text() + "</strong> el monto de <strong>" + $("#c_monto")[0].value + "</strong>?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okPrestar);
    openModal('#confirmarModal');
}

function okPrestar() {
    mostrarCargando();
    var id = $("#c_id").val();
    var relacionador = $("#c_relacionador").val();
    var monto = parseFloat($("#c_monto").val());
    $.post(url, {evento: "prestar", id: id, monto: monto, relacionador: relacionador}, function (resp) {
        if (resp === "false") {
            cerrarModal();
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var json = $.parseJSON(resp);
            if (json === null) {
                // eliminar
                tabla.row('.pr_' + json.idUsuario).remove().draw(false);
            } else if ($('.pr_' + json.idUsuario).length > 0) {
                tabla.cell(".pr_" + json.idUsuario + " > td:eq(1)").data((json.deuda || "0"));
                tabla.rows().invalidate();
            } else {
                tabla.row.add($(prestamosFilaHtml(json))).draw(false);
            }
            cerrarModal();
            cerrarModal();
            openAlert("Guardado Correctamente.", "Información");
        }
        ocultarCargando();
    });
}
