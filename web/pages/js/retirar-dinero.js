var url = "../RetirarDineroController";
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
        $.each(json.apostadores, function (i, obj) {
            html += apostadoresFilaHtml(obj);
        });
        $("#cuerpo").html(html);

        tabla = $('#tabla').DataTable({
            "language": {
                "url": "../plugins/datatables/i18n/spanish.json"
            },
            columnDefs: [
                {
                    targets: 3,
                    render: $.fn.dataTable.render.number('.', ',', 2)
                }
            ]
        });
        ocultarCargando();
    });
}

function apostadoresFilaHtml(obj) {
//    fa-eye
    var tr = "<tr data-idUsuario='" + obj.id + "' class='pr_" + obj.id + "'>";
    tr += "<td>" + (obj.nombre || "") + "</td>";
    tr += "<td>" + (obj.ci || "") + "</td>";
    tr += "<td>" + (obj.usuario || "") + "</td>";
    tr += "<td class='text-right' data-monto=''>" + (obj.credito || "0") + "</td>";
    tr += "<td class='text-center'>";
    tr += "<i class='fa fa-money text-green' title='Pagar' onclick='modalRetirarDinero(" + obj.id + ",this);'></i>";
    tr += "</td>";
    tr += "</tr>";
    return tr;
}

var $trSel;
var idUsuarioSel;
function modalRetirarDinero(idUsuario, ele) {
    $trSel = $(ele).closest("tr");
    idUsuarioSel = idUsuario;
    $("#pagarModalLabel").text("Retirar Dinero de " + $trSel.find("td:eq(0)").text());
    $("#tMonto").val(0);
    $("#tCredito").html("Crédito Disponible: <strong>" + $trSel.find("td:eq(3)").text() + "</strong>");
    openModal("#pagarModal");
}

function retirarDinero() {
    var monto = parseFloat($("#tMonto").val() || "0");
    if (monto <= 0) {
        openAlert("El monto a retirar debe ser mayor a 0", "Retirar Dinero");
        return;
    }
    var saldo = parseFloat(tabla.cell($trSel.find("td:eq(3)")).data());
    if (monto > saldo) {
        openAlert("El monto a retirar no debe ser mayor al Crédito Disponible (" + $trSel.find("td:eq(3)").text() + ")", "Retirar Dinero");
        return;
    }
    $("#confirmarModalLabel").html("Confirmar Retiro de Dinero");
    $("#confirmarModalText").html("¿Esta seguro de Retirar <strong>" + new BigNumber("" + monto).toFormat(2) + "</strong> de dinero del Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong>?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okRetirarDinero);
    openModal('#confirmarModal');
}

function okRetirarDinero() {
    mostrarCargando();
    var monto = $("#tMonto").val();
    $.post(url, {evento: "okRetirarDinero", monto: monto, idUsuario: idUsuarioSel}, function (resp) {
        if (resp === "false") {
            cerrarModal();
            openAlert("No se Guardo, Intentelo de nuevo.");
        } else {
            var json = $.parseJSON(resp);
            if (json.resp === "MONTO_0") {
                openAlert("El monto a retirar debe ser mayor a 0", "Retirar Dinero");
                return;
            }
            if (json.resp === "CREDITO_INSUFICIENTE") {
                openAlert("No se Realizo el Retiro por que el Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong> no tiene cretido suficiente para retirar ese monto, Cuenta con credito " + new BigNumber("" + json.credito).toFormat(2) + "", "Retirar Dinero");
                return;
            }
            var json = $.parseJSON(resp);
            var msj = "Monto Retirado <strong>" + new BigNumber("" + json.montoDevuelto).toFormat(2) + "</strong> del Usuario <strong>" + $trSel.find("td:eq(0)").text() + "</strong>";
            if (json.credito > 0) {
                tabla.cell(".pr_" + idUsuarioSel + " > td:eq(3)").data((json.credito || 0));
                tabla.rows().invalidate();
            } else {
                // eliminar saldo
                tabla.row('.pr_' + idUsuarioSel).remove().draw(false);
            }
            // verificar si es el mismo usuario actualizar el credito
            window.parent.actualizarCreditos(json.credito, idUsuarioSel);
            cerrarModal();
            cerrarModal();
            openAlert(msj, "Información");
            ocultarCargando();
        }

    });
}

