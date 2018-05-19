var url = "../editarPerfilController";

$(document).ready(function () {
    init();
});

function init() {
    formato_decimal_all();
    $.post(url, {evento: "init"}, function (resp) {
        var usuario = $.parseJSON(resp);
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
        var total = new BigNumber("0");
        var aux = new BigNumber("" + usuario.balance.prestamo);
        total = total.plus(aux);
        $("#bTotalPrestamo").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.compra);
        total = total.plus(aux);
        $("#bTotalCompras").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.recibe);
        total = total.plus(aux);
        $("#bTotalRecibido").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.da);
        total = total.minus(aux);
        $("#bTotalEnviado").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.apuesta);
        total = total.minus(aux);
        $("#bTotalApostado").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.ganancia);
        total = total.plus(aux);
        $("#bTotalGanado").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.retiro);
        total = total.minus(aux);
        $("#bTotalRetirado").text(aux.toFormat(2));
        aux = new BigNumber("" + usuario.balance.pagoPrestamo);
        total = total.minus(aux);
        $("#bTotalPagoPrestamo").text(aux.toFormat(2));
        $("#bTotalSaldo").text(total.toFormat(2));
        if (usuario.balance.prestamo === 0 && usuario.balance.pagoPrestamo === 0) {
            $("#bTotalPagoPrestamo").closest(".list-group-item").remove();
            $("#bTotalPrestamo").closest(".list-group-item").remove();
        }
        if (usuario.prestamos) {
            total = new BigNumber("0");
            aux = new BigNumber("" + usuario.prestamos.haber);
            total = total.plus(aux);
            $("#pTotalPagos").text(aux.toFormat(2));
            aux = new BigNumber("" + usuario.prestamos.debe);
            total = total.minus(aux);
            $("#pTotalPrestamos").text(aux.toFormat(2));
            if (total.isNegative) {
                total = total.negated();
            }
            $("#pTotalDeuda").text(total.toFormat(2));
        } else {
            $("#pPrestamos").remove();
        }
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