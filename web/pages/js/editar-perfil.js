var url = "../editarPerfilController";

$(document).ready(function () {
    init();
});

function init() {
    formato_decimal_all();
    $.post(url, {evento: "init"}, function (resp) {
        var json = $.parseJSON(resp);
        var usuario = json.perfil;
        $(".usr_img").attr("src", "../" + usuario.foto);
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
        //---- Balance
        cargarBalance(usuario.balance);
        /// Prestamo
        cargarPrestamos(usuario.prestamos);
        // transacciones
        cargarTransacciones(usuario.transacciones);
        // usuarios para traspasos
        var html = "";
        $.each(json.usuarios, function (i, obj) {
            html += "<option value=" + obj.id + ">" + obj.usuario + "</option>";
        });
        $("#tUsuario").html(html);
        ocultarCargando();
    });
}

function cargarBalance(balance) {
    var total = new BigNumber("0");
    var aux = new BigNumber("" + balance.prestamo);
    total = total.plus(aux);
    $("#bTotalPrestamo").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.compra);
    total = total.plus(aux);
    $("#bTotalCompras").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.recibe);
    total = total.plus(aux);
    $("#bTotalRecibido").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.da);
    total = total.minus(aux);
    $("#bTotalEnviado").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.apuesta);
    total = total.minus(aux);
    $("#bTotalApostado").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.ganancia);
    total = total.plus(aux);
    $("#bTotalGanado").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.retiro);
    total = total.minus(aux);
    $("#bTotalRetirado").text(aux.toFormat(2));
    aux = new BigNumber("" + balance.pagoPrestamo);
    total = total.minus(aux);
    $("#bTotalPagoPrestamo").text(aux.toFormat(2));
    $("#bTotalSaldo").text(total.toFormat(2)).data("monto", parseFloat(total.toFixed(2)));
    if (balance.prestamo === 0 && balance.pagoPrestamo === 0) {
        $("#bTotalPagoPrestamo").closest(".list-group-item").remove();
        $("#bTotalPrestamo").closest(".list-group-item").remove();
    }
}

function cargarPrestamos(prestamos) {
    if (prestamos) {
        var total = new BigNumber("0");
        var aux = new BigNumber("" + prestamos.haber);
        total = total.plus(aux);
        $("#pTotalPagos").text(aux.toFormat(2));
        aux = new BigNumber("" + prestamos.debe);
        total = total.minus(aux);
        $("#pTotalPrestamos").text(aux.toFormat(2));
        if (total.isNegative) {
            total = total.negated();
        }
        $("#pTotalDeuda").text(total.toFormat(2));
    } else {
        $("#pPrestamos").remove();
    }
}

function cargarTransacciones(transacciones) {
    var html = "";
    $.each(transacciones, function (i, obj) {
        html += "<strong><i class='fa fa-book margin-r-5'></i>" + obj.transaccion + "</strong>";
        html += "<p class='text-muted'>" + obj.detalle + "</p>";
        html += "<hr>";
    });
    $("#transacciones").html(html);
    formato_decimal_all();
}

function cambiarFoto() {
    $("input[name=foto]").click();
}

function okCambiarFoto(ele) {
    var input = ele;
    var url = $(ele).val();
    var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
    if (input.files && input.files[0] && (ext == "gif" || ext == "png" || ext == "jpeg" || ext == "jpg"))
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
            if (data.length > 0) {
                $(window.parent.document).find(".usr_img").attr("src", data);
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

function modalPagarPrestamo() {
    var credito = $("#bTotalSaldo").data("monto");
    if (credito > 0) {
        $("#transaccionModalLabel").text("Pagar Prestamo con crédito");
        $("#botonTransaccion").text("Pagar Prestamo");
        $("#botonTransaccion").off("click");
        $("#botonTransaccion").click(pagarPrestamo);
        $("#tUsuario").parent().css("display", "none");
        $("#tMonto").val(0);
        $("#tSaldoDisponible").html("Credito Disponiple: <strong>" + credito + "</strong>");
        openModal("#transaccionModal");
    } else {
        openAlert("No tiene Credito Suficiente para pagar la deuda del Prestamo", "Credito insuficiente");
    }
}

function pagarPrestamo() {
    var monto = $("#tMonto").val();
    if (monto <= 0) {
        openAlert("El monto a pagar debe ser mayor a 0", "Pagar Prestamo");
        return;
    }
    var credito = $("#bTotalSaldo").data("monto");
    if (monto > credito) {
        openAlert("El monto a pagar no debe ser mayor al crédito disponible (" + new BigNumber("" + credito).toFormat(2) + ")", "Crédito Insuficiente");
        return;
    }
    $("#confirmarModalLabel").html("Confirmar Pago de Prestamo");
    $("#confirmarModalText").html("¿Esta seguro de pagar <strong>" + new BigNumber("" + monto).toFormat(2) + "</strong> de su deuda del Prestamo con su crédito?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okPagarPrestamo);
    openModal('#confirmarModal');
}

function okPagarPrestamo() {
    mostrarCargando();
    var monto = $("#tMonto").val();
    $.post(url, {evento: "okPagarPrestamo", monto: monto}, function (resp) {
        var json = $.parseJSON(resp);
        if (json.resp === "MONTO_0") {
            openAlert("El monto a pagar debe ser mayor a 0", "Pagar Prestamo");
            return;
        }
        if (json.resp === "CREDITO_INSUFICIENTE") {
            openAlert("El monto a pagar no debe ser mayor al crédito disponible (" + new BigNumber("" + json.credito).toFormat(2) + ")", "Crédito Insuficiente");
            return;
        }
        cerrarModal();
        cerrarModal();
        //---- Balance
        cargarBalance(json.balance);
        /// Prestamo
        cargarPrestamos(json.prestamos);
        // transacciones
        cargarTransacciones(json.transacciones);
        // Actualizar Saldo Principal
        window.parent.actualizarCredito(json.credito);
        ocultarCargando();
    });
}

function modalTraspasoCredito() {
    var credito = $("#bTotalSaldo").data("monto");
    if (credito > 0) {
        $("#transaccionModalLabel").text("Traspaso de Crédito");
        $("#botonTransaccion").text("Registrar Traspaso");
        $("#botonTransaccion").off("click");
        $("#botonTransaccion").click(TraspasoCredito);
        $("#tUsuario").parent().css("display", "");
        $("#tMonto").val(0);
        $("#tSaldoDisponible").html("Credito Disponiple: <strong>" + credito + "</strong>");
        openModal("#transaccionModal");
    } else {
        openAlert("No tiene Credito Suficiente para realizar el traspaso", "Credito Insuficiente");
    }
}

function TraspasoCredito() {
    var monto = $("#tMonto").val();
    if (monto <= 0) {
        openAlert("El monto debe ser mayor a 0", "Traspaso de Crédito");
        return;
    }
    var credito = $("#bTotalSaldo").data("monto");
    if (monto > credito) {
        openAlert("El monto no debe ser mayor al crédito disponible (" + new BigNumber("" + credito).toFormat(2) + ")", "Crédito Insuficiente");
        return;
    }
    $("#confirmarModalLabel").html("Confirmar Traspaso de Crédito");
    $("#confirmarModalText").html("¿Esta seguro de registrar el Traspaso de Crédito de <strong>" + new BigNumber("" + monto).toFormat(2) + "</strong> a " + $("#tUsuario").find("option:selected").text() + "?");
    $("#confirmarBotonModal").off("click");
    $("#confirmarBotonModal").click(okTraspasoCredito);
    openModal('#confirmarModal');
}

function okTraspasoCredito() {
    mostrarCargando();
    var monto = $("#tMonto").val();
    $.post(url, {evento: "okTraspasoCredito", monto: monto, idUsuarioRecibe : $("#tUsuario").val()}, function (resp) {
        var json = $.parseJSON(resp);
        if (json.resp === "MONTO_0") {
            openAlert("El monto debe ser mayor a 0", "Traspaso de Crédito");
            return;
        }
        if (json.resp === "CREDITO_INSUFICIENTE") {
            openAlert("El monto no debe ser mayor al crédito disponible (" + new BigNumber("" + json.credito).toFormat(2) + ")", "Crédito Insuficiente");
            return;
        }
        cerrarModal();
        cerrarModal();
        //---- Balance
        cargarBalance(json.balance);
        // transacciones
        cargarTransacciones(json.transacciones);
        // Actualizar Saldo Principal
        window.parent.actualizarCredito(json.credito);
        ocultarCargando();
    });
}

function modalApostar() {
    window.parent.cambiarMenu("pages/fixture.html");
}