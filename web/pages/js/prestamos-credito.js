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

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            },
            columnDefs: [
                {
                    targets: 1,
                    render: $.fn.dataTable.render.number('.', ',', 2)
                }
            ]
        });
        ocultarCargando();
    });
}

function prestamosFilaHtml(obj) {
//    fa-eye
    var tr = "<tr data-idUsuario='" + obj.idUsuario + "' class='pr_" + obj.idUsuario + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td class='text-right' data-monto=''>" + (obj.deuda || "0") + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-eye text-warning' title='Ver Sub Menu' onclick='popVerDetalle(" + obj.idUsuario + ",this);'></i>";
    tr += "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-money text-green' title='Pagar' onclick='modalPagarPrestamo(" + obj.idUsuario + ",this);'></i>";
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
                tabla.row('.pr_' + relacionador).remove().draw(false);
            } else if (tabla.$('.pr_' + json.idUsuario).length > 0) {
                tabla.cell(".pr_" + json.idUsuario + " > td:eq(1)").data((json.deuda || 0));
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

var $trSel;
var idUsuarioSel;
function modalPagarPrestamo(idUsuario, ele) {
    $trSel = $(ele).closest("tr");
    idUsuarioSel = idUsuario;
    $("#pagarModalLabel").text("Pagar Prestamo de " + $trSel.find("td:eq(0)").text());
    $("#tMonto").val(0);
    $("#tPrestamo").html("Deuda Prestamo: <strong>" + $trSel.find("td:eq(1)").text() + "</strong>");
    openModal("#pagarModal");
}

function pagarPrestamo() {
    var monto = parseFloat($("#tMonto").val() || "0");
    if (monto <= 0) {
        openAlert("El monto a pagar debe ser mayor a 0", "Pagar Prestamo");
        return;
    }
    var saldo = parseFloat(tabla.cell($trSel.find("td:eq(1)")).data());
    if (monto > saldo) {
        openAlert("El monto a pagar no debe ser mayor a la Deuda (" + $trSel.find("td:eq(1)").text() + ")", "Pago Prestamo");
        return;
    }
    $("#confirmarModalLabel").html("Confirmar Pago de Prestamo");
    $("#confirmarModalText").html("¿Esta seguro de pagar <strong>" + new BigNumber("" + monto).toFormat(2) + "</strong> de la deuda del Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong>?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okPagarPrestamo);
    openModal('#confirmarModal');
}

function okPagarPrestamo() {
    mostrarCargando();
    var monto = $("#tMonto").val();
    $.post(url, {evento: "okPagarPrestamo", monto: monto, idUsuario: idUsuarioSel}, function (resp) {
        if (resp === "false") {
            cerrarModal();
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var json = $.parseJSON(resp);
            if (json.resp === "MONTO_0") {
                openAlert("El monto a pagar debe ser mayor a 0", "Pagar Prestamo");
                return;
            }
            if (json.resp === "DEUDA_0") {
                openAlert("No se Realizo el Pago por que el Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong> no tiene ninguna deuda", "Pagar Prestamo");
                return;
            }
            var json = $.parseJSON(resp);
            var msj = "Monto Pagado <strong>" + new BigNumber("" + json.montoUsado).toFormat(2) + "</strong> del Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong>";
            if (json.prestatario === null || json.prestatario === undefined) {
                // eliminar deuda
                tabla.row('.pr_' + idUsuarioSel).remove().draw(false);
            } else if (tabla.$('.pr_' + json.prestatario.idUsuario).length > 0) {
                tabla.cell(".pr_" + json.prestatario.idUsuario + " > td:eq(1)").data((json.prestatario.deuda || 0));
                tabla.rows().invalidate();
            } else {
                tabla.row.add($(prestamosFilaHtml(json.prestatario))).draw(false);
            }
            cerrarModal();
            cerrarModal();
            openAlert(msj, "Información");
            ocultarCargando();
        }

    });
}
