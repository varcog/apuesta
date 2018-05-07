var url = "IngresoController";

$(document).ready(function () {
    $.post(url, {evento: "obtener_ingreso"}, function (resp) {
        if (resp === "false")
            location.href = "index.html";
        else {
            $(".elmenu").remove();
            var json = $.parseJSON(resp);
            var html = "";
            $.each(json.Menu, function (menu, submenus) {
                html += "<li class='treeview'>";
                html += "<a>";
                // fa-asterisk
                // fa-certificate
                // fa-circle
                html += "<i class='fa fa-asterisk'></i>";
                html += "<span>" + menu + "</span>";
                html += "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>";
                html += "</a>";
                html += "<ul class='treeview-menu'>";
                $.each(submenus, function (i, sub_menu) {
                    html += "<li><a onclick='cambiar_menu(\"" + sub_menu.url + "\")'><i class='fa fa-circle-o'></i> " + sub_menu.nombre + "</a></li>";
                });
                html += "</ul>";
                html += "</li>";
            });
            $("#menu").append(html);
            $(".nombre_usuario").text((json.Usuario || ""));
            $(".cargo").text((json.Perfil || ""));
        }
    });
});

function cambiar_menu(url) {
    $("#content_frame").attr("src", url);
}

